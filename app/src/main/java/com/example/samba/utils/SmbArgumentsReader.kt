package com.example.samba.utils

import android.os.Build
import android.os.Bundle
import com.example.samba.model.SmbConnectionProfile
import com.example.samba.utils.Constants.CONNECTION_PROFILE
import com.example.samba.utils.Constants.PASSWORD

object SmbArgumentsReader {

    @JvmStatic
    fun getConnectionProfile(arguments: Bundle?): SmbConnectionProfile? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(
                CONNECTION_PROFILE,
                SmbConnectionProfile::class.java
            )
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(CONNECTION_PROFILE)
        }
    }

    @JvmStatic
    fun getPassword(arguments: Bundle?): String? {
        return arguments?.getString(PASSWORD)
    }
}
