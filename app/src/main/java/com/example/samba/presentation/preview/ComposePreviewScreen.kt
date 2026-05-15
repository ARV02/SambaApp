package com.example.samba.presentation.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.samba.presentation.components.SambaConnectionCard
import com.example.samba.presentation.components.SambaPrimaryButton
import com.example.samba.presentation.components.SambaTextField
import com.example.samba.presentation.theme.SambaAppTheme

@Composable
fun ComposePreviewScreen() {
    val host = remember { mutableStateOf("192.168.1.14") }

    SambaAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "SambaApp",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            SambaConnectionCard(
                title = "MacBook SMB",
                host = "192.168.1.42",
                shareName = "samba-test"
            )

            SambaTextField(
                value = host.value,
                label = "Host or IP address",
                onValueChange = { host.value = it }

            )

            SambaPrimaryButton(
                text = "Connect",
                onClick = {}
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ComposePreviewScreenPreview() {
    ComposePreviewScreen()
}
