package com.example.samba.presentation.filebrowser

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.samba.domain.model.SmbFileItem
import com.example.samba.model.SmbConnectionProfile
import com.example.samba.presentation.components.SambaPrimaryButton
import com.example.samba.presentation.components.SambaTextField

@Composable
fun FileBrowserRoute(
    connectionProfile: SmbConnectionProfile,
    password: String,
    readOnlyMode: Boolean,
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
        readOnlyMode = readOnlyMode,
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
        },
        onCreateFolder = viewModel::createFolder,
        onCreateFile = viewModel::createFile,
        onDeleteItem = viewModel::deleteItem,
        onActionMessageShown = viewModel::clearActionMessage
    )
}

@Composable
fun FileBrowserScreen(
    state: FileBrowserUiState,
    shareName: String,
    readOnlyMode: Boolean,
    onBackClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onFileClick: (SmbFileItem) -> Unit,
    onCreateFolder: (String) -> Unit,
    onCreateFile: (String) -> Unit,
    onDeleteItem: (SmbFileItem) -> Unit,
    onActionMessageShown: () -> Unit
) {
    var dialogState by remember {
        mutableStateOf<FileBrowserDialogState>(FileBrowserDialogState.None)
    }

    var visibleActionMessage by remember {
        mutableStateOf<FileBrowserActionMessage?>(null)
    }

    LaunchedEffect(state.actionMessage) {
        state.actionMessage?.let { message ->
            visibleActionMessage = message
            kotlinx.coroutines.delay(2500)
            visibleActionMessage = null
            onActionMessageShown()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp)
        ) {
            FileBrowserTopBar(
                shareName = shareName,
                currentPath = state.currentPath,
                onBackClick = onBackClick,
                onRefreshClick = onRefreshClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (readOnlyMode) {
                ReadOnlyBanner()
                Spacer(modifier = Modifier.height(12.dp))
            }

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
                        onFileClick = onFileClick,
                        showActions = !readOnlyMode,
                        onItemMenuClick = { item ->
                            dialogState = FileBrowserDialogState.FileActions(item)
                        }
                    )
                }
            }
        }

        if (!readOnlyMode) {
            FloatingActionButton(
                onClick = {
                    dialogState = FileBrowserDialogState.CreateMenu
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .padding(24.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create"
                )
            }
        }

        visibleActionMessage?.let { message ->
            SambaSnackBar(
                message = message,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 96.dp
                    )
            )
        }
    }

    when (val dialog = dialogState) {
        FileBrowserDialogState.None -> Unit

        FileBrowserDialogState.CreateMenu -> {
            CreateMenuBottomSheet(
                onDismiss = {
                    dialogState = FileBrowserDialogState.None
                },
                onNewFolderClick = {
                    dialogState = FileBrowserDialogState.NewFolder
                },
                onNewFileClick = {
                    dialogState = FileBrowserDialogState.NewFile
                }
            )
        }

        FileBrowserDialogState.NewFolder -> {
            NameInputDialog(
                title = "New folder",
                label = "Folder name",
                confirmText = "Create",
                onDismiss = {
                    dialogState = FileBrowserDialogState.None
                },
                onConfirm = { folderName ->
                    onCreateFolder(folderName)
                    dialogState = FileBrowserDialogState.None
                }
            )
        }

        FileBrowserDialogState.NewFile -> {
            NameInputDialog(
                title = "New file",
                label = "File name",
                confirmText = "Create",
                onDismiss = {
                    dialogState = FileBrowserDialogState.None
                },
                onConfirm = { fileName ->
                    onCreateFile(fileName)
                    dialogState = FileBrowserDialogState.None
                }
            )
        }

        is FileBrowserDialogState.FileActions -> {
            FileActionsDialog(
                item = dialog.item,
                onDismiss = {
                    dialogState = FileBrowserDialogState.None
                },
                onDeleteClick = {
                    dialogState = FileBrowserDialogState.ConfirmDelete(dialog.item)
                }
            )
        }

        is FileBrowserDialogState.ConfirmDelete -> {
            DeleteConfirmationDialog(
                item = dialog.item,
                onDismiss = {
                    dialogState = FileBrowserDialogState.None
                },
                onConfirm = {
                    onDeleteItem(dialog.item)
                    dialogState = FileBrowserDialogState.None
                }
            )
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    item: SmbFileItem,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val message = if (item.isDirectory) {
        "Only empty folders can be deleted in this version. This action cannot be undone."
    } else {
        "This action cannot be undone."
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF10172A),
        title = {
            Text(
                text = "Delete \"${item.name}\"?",
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(
                    text = "Delete",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun NameInputDialog(
    title: String,
    label: String,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var value by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF10172A),
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            SambaTextField(
                value = value,
                label = label,
                onValueChange = {
                    value = it
                }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val cleanValue = value.trim()

                    if (cleanValue.isNotBlank()) {
                        onConfirm(cleanValue)
                    }
                }
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun FileActionsDialog(
    item: SmbFileItem,
    onDismiss: () -> Unit,
    onDeleteClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF10172A),
        title = {
            Text(
                text = item.name,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(
                    onClick = onDeleteClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Delete",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
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
    onFileClick: (SmbFileItem) -> Unit,
    showActions: Boolean,
    onItemMenuClick: (SmbFileItem) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        items(files) { item ->
            FileItemRow(
                item = item,
                showActions = showActions,
                onClick = {
                    onFileClick(item)
                },
                onMenuClick = {
                    onItemMenuClick(item)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileItemRow(
    item: SmbFileItem,
    showActions: Boolean,
    onClick: () -> Unit,
    onMenuClick: () -> Unit
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

            if (showActions) {
                IconButton(
                    onClick = onMenuClick
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "File actions",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateMenuBottomSheet(
    onDismiss: () -> Unit,
    onNewFolderClick: () -> Unit,
    onNewFileClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF10172A),
        contentColor = Color(0xFFF8FAFC),
        dragHandle = null,
        shape = RoundedCornerShape(
            topStart = 28.dp,
            topEnd = 28.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 24.dp,
                    bottom = 32.dp
                ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Create new",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFFF8FAFC),
                fontWeight = FontWeight.SemiBold
            )

            CreateActionRow(
                icon = Icons.Default.CreateNewFolder,
                title = "New folder",
                onClick = onNewFolderClick
            )

            CreateActionRow(
                icon = Icons.Default.NoteAdd,
                title = "New file",
                onClick = onNewFileClick
            )

            TextButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF818CF8)
                )
            ) {
                Text("Cancel")
            }
        }
    }
}

@Composable
private fun CreateActionRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1E293B)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF38BDF8)
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFFF8FAFC),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun SambaSnackBar(
    message: FileBrowserActionMessage,
    modifier: Modifier = Modifier
) {
    val icon = when (message.type) {
        FileBrowserActionMessageType.Success -> Icons.Default.CheckCircle
        FileBrowserActionMessageType.Error -> Icons.Default.Error
    }

    val iconColor = when(message.type) {
        FileBrowserActionMessageType.Success -> Color(0xFF22C55E)
        FileBrowserActionMessageType.Error -> Color(0xFFEF4444)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF10172A)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFF1E293B)
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 14.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = message.message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFF8FAFC),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ReadOnlyBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF10172A)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFF1E293B)
        )
    ) {
        Text(
            text = "Read-only mode is enabled. Create and delete actions are disabled.",
            modifier = Modifier.padding(14.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private sealed class FileBrowserDialogState {
    object None : FileBrowserDialogState()
    object CreateMenu : FileBrowserDialogState()
    object NewFolder : FileBrowserDialogState()
    object NewFile : FileBrowserDialogState()
    data class FileActions(val item: SmbFileItem) : FileBrowserDialogState()
    data class ConfirmDelete(val item: SmbFileItem) : FileBrowserDialogState()
}
