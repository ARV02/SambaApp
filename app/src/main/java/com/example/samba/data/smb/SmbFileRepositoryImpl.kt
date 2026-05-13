package com.example.samba.data.smb

import com.example.samba.model.SmbConnectionProfile
import com.example.samba.domain.model.SmbFileItem
import com.example.samba.domain.model.SmbFileResult
import com.example.samba.domain.model.SmbFileResult.Error
import com.example.samba.domain.repository.SmbFileRepository
import com.hierynomus.msfscc.FileAttributes
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation
import com.hierynomus.mssmb2.SMBApiException
import com.hierynomus.smbj.SMBClient
import com.hierynomus.smbj.SmbConfig
import com.hierynomus.smbj.auth.AuthenticationContext
import com.hierynomus.smbj.share.DiskShare
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class SmbFileRepositoryImpl : SmbFileRepository {

    override suspend fun listFiles(
        connectionProfile: SmbConnectionProfile,
        password: String
    ): SmbFileResult<List<SmbFileItem>> {
        return withContext(Dispatchers.IO) {
            try {
                val client = SMBClient(SmbConfig.createDefaultConfig())

                client.connect(connectionProfile.host).use { connection ->
                    val authenticationContext = AuthenticationContext(
                        connectionProfile.username,
                        password.toCharArray(),
                        ""
                    )

                    val session = connection.authenticate(authenticationContext)

                    (session.connectShare(connectionProfile.shareName) as DiskShare).use { share ->
                        val files = share.list(null)
                            .filterNot { file ->
                                file.fileName == "." || file.fileName == ".."
                            }
                            .map { file ->
                                file.toSmbFileItem()
                            }
                        SmbFileResult.Success(files)
                    }
                }
            } catch (exception: SMBApiException) {
                Error(
                    message = exception.toUserMessage(),
                    cause = exception
                )
            } catch (exception: IOException) {
                Error(
                    message = "Unable to connect to the server. Check the host and network.",
                    cause = exception
                )
            } catch (exception: Exception) {
                Error(
                    message = "Unexpected error while listing files.",
                    cause = exception
                )
            }
        }
    }

    private fun FileIdBothDirectoryInformation.toSmbFileItem(): SmbFileItem {
        return SmbFileItem(
            name = fileName,
            path = fileName,
            isDirectory = isDirectory(),
            size = endOfFile
        )
    }

    private fun FileIdBothDirectoryInformation.isDirectory(): Boolean {
        return fileAttributes and FileAttributes.FILE_ATTRIBUTE_DIRECTORY.value != 0L
    }

    private fun SMBApiException.toUserMessage(): String {
        val status = status.toString()
        return when {
            status.contains("STATUS_LOGON_FAILURE") ->
                "Invalid username or password."
            status.contains("STATUS_ACCESS_DENIED") ->
                "Access denied. Check your SMB permissions."
            status.contains("STATUS_BAD_NETWORK_NAME") ->
                "Shared folder not found. Check the share name."
            else ->
                "SMB error: $status"
        }
    }
}
