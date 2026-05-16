package com.example.samba.presentation.filebrowser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.example.samba.presentation.theme.SambaAppTheme
import com.example.samba.utils.SmbArgumentsReader

class FileBrowserComposeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val connectionProfile = SmbArgumentsReader.getConnectionProfile(arguments)

        val password = SmbArgumentsReader.getPassword(arguments)

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                SambaAppTheme {
                    if (connectionProfile != null && password != null) {
                        FileBrowserRoute(
                            connectionProfile = connectionProfile,
                            password = password,
                            onBackClick = {
                               parentFragmentManager.popBackStack()
                            }
                        )
                    } else {
                        ErrorState(
                            message = "Missing connection data.",
                            onRetryClick = {}
                        )
                    }
                }
            }
        }
    }
}
