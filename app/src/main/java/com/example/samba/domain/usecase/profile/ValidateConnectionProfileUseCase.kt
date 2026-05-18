package com.example.samba.domain.usecase.profile

import javax.inject.Inject

class ValidateConnectionProfileUseCase @Inject constructor() {

    operator fun invoke(
        profileName: String,
        host: String,
        shareName: String,
        username: String
    ): ValidationResult {
        return when {
            profileName.isBlank() -> {
                ValidationResult.Invalid("Profile name is required.")
            }

            host.isBlank() -> {
                ValidationResult.Invalid("Host is required.")
            }

            host.contains(" ") -> {
                ValidationResult.Invalid("Host cannot contain spaces.")
            }

            host.startsWith("smb://", ignoreCase = true) -> {
                ValidationResult.Invalid("Enter only the host or IP address, without smb://.")
            }

            shareName.isBlank() -> {
                ValidationResult.Invalid("Share name is required.")
            }

            shareName.contains("/") || shareName.contains("\\") -> {
                ValidationResult.Invalid("Share name should not include slashes.")
            }

            username.isBlank() -> {
                ValidationResult.Invalid("Username is required.")
            }

            else -> ValidationResult.Valid
        }
    }
}

sealed class ValidationResult {
    data object Valid : ValidationResult()
    data class Invalid(val message: String): ValidationResult()
}
