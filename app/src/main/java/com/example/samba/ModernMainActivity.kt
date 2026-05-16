package com.example.samba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowCompat
import com.example.samba.presentation.main.SambaAppRoot
import com.example.samba.presentation.theme.SambaAppTheme

class ModernMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.statusBarColor = "#070B1A".toColorInt()
        window.navigationBarColor = "#070B1A".toColorInt()

        WindowCompat.getInsetsController(
            window,
            window.decorView
        ).isAppearanceLightStatusBars = false

        WindowCompat.getInsetsController(
            window,
            window.decorView
        ).isAppearanceLightNavigationBars = false

        setContent {
            SambaAppTheme {
                SambaAppRoot()
            }
        }
    }
}
