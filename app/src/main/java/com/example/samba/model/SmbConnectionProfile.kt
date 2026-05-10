package com.example.samba.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SmbConnectionProfile(
    val name: String,
    val host: String,
    val shareName: String,
    val username: String
) : Parcelable
