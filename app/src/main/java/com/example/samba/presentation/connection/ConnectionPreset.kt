package com.example.samba.presentation.connection

enum class ConnectionPreset(
    val displayName: String,
    val defaultProfileName: String,
    val defaultShareName: String
) {
    Fedora(
        displayName = "Fedora",
        defaultProfileName = "Fedora Lab",
        defaultShareName = "samba-test"
    ),
    FreeBSD(
        displayName = "FreeBSD",
        defaultProfileName = "FreeBSD Lab",
        defaultShareName = "samba-test"
    ),
    Solaris(
        displayName = "Solaris",
        defaultProfileName = "Solaris Lab",
        defaultShareName = "samba-test"
    ),
    Custom(
        displayName = "Custom",
        defaultProfileName = "",
        defaultShareName = ""
    )
}
