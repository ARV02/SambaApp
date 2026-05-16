package com.example.samba.presentation.connection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.example.samba.R
import com.example.samba.presentation.theme.SambaAppTheme
import com.example.samba.utils.SmbBundleFactory
import androidx.core.graphics.toColorInt
import com.example.samba.presentation.filebrowser.FileBrowserComposeFragment

class ConnectionComposeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        requireActivity().window.statusBarColor = "#070B1A".toColorInt()
        requireActivity().window.navigationBarColor = "#070B1A".toColorInt()
        WindowCompat.getInsetsController(
            requireActivity().window,
            requireActivity().window.decorView
        ).isAppearanceLightStatusBars = false

        WindowCompat.getInsetsController(
            requireActivity().window,
            requireActivity().window.decorView
        ).isAppearanceLightNavigationBars = false

        return ComposeView(requireContext()).apply {
            setContent {
                SambaAppTheme {
                    ConnectionRoute(
                        onConnectionReady = { connectionProfile, password ->
                            val files = FileBrowserComposeFragment()
                            files.arguments = SmbBundleFactory.createConnectionBundle(
                                connectionProfile,
                                password
                            )
                            requireActivity()
                                .supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.container3, files)
                                .addToBackStack("connection_to_file_browser")
                                .commit()
                        },
                        onBackClick = {
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }
                    )
                }
            }
        }
    }
}