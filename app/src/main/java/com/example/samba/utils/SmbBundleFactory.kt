package com.example.samba.utils

import android.os.Bundle
import com.example.samba.model.SmbConnectionProfile
import com.example.samba.utils.Constants.CONNECTION_PROFILE
import com.example.samba.utils.Constants.PASSWORD

object SmbBundleFactory {

    @JvmStatic
    fun createConnectionBundle(
        connectionProfile: SmbConnectionProfile,
        password: String
    ): Bundle {
        return Bundle().apply {
            putParcelable(CONNECTION_PROFILE, connectionProfile)
            putString(PASSWORD, password)
        }
    }
}
