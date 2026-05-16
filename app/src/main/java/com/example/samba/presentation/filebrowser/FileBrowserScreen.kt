package com.example.samba.presentation.filebrowser

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.samba.domain.model.SmbFileItem
import com.example.samba.model.SmbConnectionProfile
import com.example.samba.presentation.components.SambaPrimaryButton

@Composable
fun FileBrowserRoute(
    connectionProfile: SmbConnectionProfile,
    password: String,
    viewModel: FileBrowserViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val observedState by viewModel.uiState.observeAsState()
    val state = observedState ?: FileBrowserUiState()

    BackHandler {
        val handled = viewModel.goBackFolder()

        if (!handled) onBackClick()
    }

    LaunchedEffect(connectionProfile, password) {
        viewModel.initialize(connectionProfile, password)
    }

    FileBrowserScreen(
        state = state,
        shareName = connectionProfile.shareName,
        onBackClick = {
            val handled = viewModel.goBackFolder()
            if (!handled) {
                onBackClick()
            }
        },
        onRefreshClick = viewModel::refresh,
        onFileClick = { item ->
            if (item.isDirectory) {
                viewModel.openFolder(item)
            }
        }
    )
}

@Composable
fun FileBrowserScreen(
    state: FileBrowserUiState,
    shareName: String,
    onBackClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onFileClick: (SmbFileItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
    ) {
        FileBrowserTopBar(
            shareName = shareName,
            currentPath = state.currentPath,
            onBackClick = onBackClick,
            onRefreshClick = onRefreshClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            state.isLoading -> {
                LoadingState()
            }

            state.errorMessage != null -> {
                ErrorState(
                    message = state.errorMessage,
                    onRetryClick = onRefreshClick
                )
            }

            state.isEmpty -> {
                EmptyState()
            }
            else -> {
                FileList(
                    files = state.files,
                    onFileClick = onFileClick
                )
            }
        }
    }
}

@Composable
private fun FileBrowserTopBar(
    shareName: String,
    currentPath: String,
    onBackClick: () -> Unit,
    onRefreshClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = shareName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = if (currentPath.isBlank()) "/" else "/$currentPath",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(onClick = onRefreshClick) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun FileList(
    files: List<SmbFileItem>,
    onFileClick: (SmbFileItem) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(files) { item ->
            FileItemRow(
                item = item,
                onClick = {
                    onFileClick(item)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileItemRow(
    item: SmbFileItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(
                imageVector = if (item.isDirectory) {
                    Icons.Default.Folder
                } else {
                    Icons.Default.Description
                },
                contentDescription = null,
                tint = if (item.isDirectory) {
                    Color(0xFF38BDF8)
                } else {
                    Color(0xFF94A3B8)
                }
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (item.isDirectory) {
                        "Folder"
                    } else {
                        formatFileSize(item.size)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatFileSize(size: Long?): String {
    if (size == null) return "Unknown size"

    return when {
        size < 1024 -> "$size B"
        size < 1024 * 1024 -> "${size / 1024} KB"
        else -> "${size / (1024 * 1024)} MB"
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "This folder is empty.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ErrorState(
    message: String,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        SambaPrimaryButton(
            text = "Retry",
            onClick = onRetryClick
        )
    }
}
