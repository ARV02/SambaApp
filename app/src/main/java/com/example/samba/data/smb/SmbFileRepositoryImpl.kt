package com.example.samba.data.smb

import com.example.samba.model.SmbConnectionProfile
import com.example.samba.domain.model.SmbFileItem
import com.example.samba.domain.model.SmbFileResult
import com.example.samba.domain.model.SmbFileResult.Error
import com.example.samba.domain.repository.SmbFileRepository
import com.hierynomus.msdtyp.AccessMask
import com.hierynomus.msfscc.FileAttributes
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation
import com.hierynomus.mssmb2.SMB2CreateDisposition
import com.hierynomus.mssmb2.SMB2CreateOptions
import com.hierynomus.mssmb2.SMB2ShareAccess
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
        password: String,
        path: String
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
                        val normalizedPath = path.trim('/')
                        val files = share.list(normalizedPath)
                            .filterNot { file ->
                                file.fileName == "." || file.fileName == ".."
                            }
                            .map { file ->
                                val childPath = if (normalizedPath.isBlank()) {
                                    file.fileName
                                } else {
                                    "$normalizedPath/${file.fileName}"
                                }
                                file.toSmbFileItem(path = childPath)
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

    private fun FileIdBothDirectoryInformation.toSmbFileItem(path: String): SmbFileItem {
        return SmbFileItem(
            name = fileName,
            path = path,
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

    override suspend fun createFolder(
        connectionProfile: SmbConnectionProfile,
        password: String,
        currentPath: String,
        folderName: String
    ): SmbFileResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                withDiskShare(connectionProfile, password) { share ->
                    val folderPath = buildRemotePath(currentPath, folderName)

                    if (share.folderExists(folderPath) || share.fileExists(folderPath)) {
                        return@withDiskShare SmbFileResult.Error(
                            message = "A file or folder with this name already exists."
                        )
                    }

                    share.mkdir(folderPath)
                    SmbFileResult.Success(Unit)
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
                    message = "Unable to create folder.",
                    cause = exception
                )
            }
        }
    }

    override suspend fun createFile(
        connectionProfile: SmbConnectionProfile,
        password: String,
        currentPath: String,
        fileName: String
    ): SmbFileResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                withDiskShare(connectionProfile, password) { share ->
                    val filePath = buildRemotePath(currentPath, fileName)

                    if (share.fileExists(filePath) || share.folderExists(filePath)) {
                        return@withDiskShare SmbFileResult.Error(
                            message = "A file or folder with this name already exists."
                        )
                    }

                    share.openFile(
                        filePath,
                        setOf(AccessMask.GENERIC_WRITE),
                        setOf(FileAttributes.FILE_ATTRIBUTE_NORMAL),
                        SMB2ShareAccess.ALL,
                        SMB2CreateDisposition.FILE_CREATE,
                        setOf(SMB2CreateOptions.FILE_NON_DIRECTORY_FILE)
                    ).use {
                        // Creates an empty remote file.
                    }

                    SmbFileResult.Success(Unit)
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
                    message = "Unable to create file.",
                    cause = exception
                )
            }
        }
    }

    override suspend fun deleteFile(
        connectionProfile: SmbConnectionProfile,
        password: String,
        filePath: String
    ): SmbFileResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                withDiskShare(connectionProfile, password) { share ->
                    if (!share.fileExists(filePath)) {
                        return@withDiskShare Error(
                            message = "File not found"
                        )
                    }

                    share.rm(filePath)
                    SmbFileResult.Success(Unit)
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
                    message = "Unable to delete file.",
                    cause = exception
                )
            }
        }
    }

    override suspend fun deleteEmptyFolder(
        connectionProfile: SmbConnectionProfile,
        password: String,
        folderPath: String
    ): SmbFileResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                withDiskShare(connectionProfile, password) { share ->
                    if (!share.folderExists(folderPath)) {
                        return@withDiskShare Error(
                            message = "Folder not found."
                        )
                    }

                    share.rmdir(folderPath, false)
                    SmbFileResult.Success(Unit)
                }
            } catch (exception: SMBApiException) {
                Error(
                    message = "Unable to delete folder. Make sure it is empty.",
                    cause = exception
                )
            } catch (exception: IOException) {
                Error(
                    message = "Unable to connect to the server. Check the host and network.",
                    cause = exception
                )
            } catch (exception: Exception) {
                Error(
                    message = "Unable to delete folder.",
                    cause = exception
                )
            }
        }
    }

    private fun buildRemotePath(
        currentPath: String,
        name: String
    ): String {
        val cleanPath = currentPath.trim('/')
        val cleanName = name.trim('/')

        return if (cleanPath.isBlank()) cleanName else "$cleanPath/$cleanName"
    }

    private inline fun <T> withDiskShare(
        connectionProfile: SmbConnectionProfile,
        password: String,
        operation: (DiskShare) -> SmbFileResult<T>
    ): SmbFileResult<T> {
        val client = SMBClient(SmbConfig.createDefaultConfig())

        client.connect(connectionProfile.host).use { connection ->
            val authenticationContext = AuthenticationContext(
                connectionProfile.username,
                password.toCharArray(),
                ""
            )

            val session = connection.authenticate(authenticationContext)

            val share = session.connectShare(connectionProfile.shareName)

            if (share !is DiskShare) {
                return Error(
                    message = "Shared folder not found. Check the share name."
                )
            }

            share.use { diskShare ->
                return operation(diskShare)
            }
        }
    }
}
