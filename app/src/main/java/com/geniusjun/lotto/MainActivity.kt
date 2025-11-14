package com.geniusjun.lotto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.geniusjun.lotto.auth.GoogleSignInManager
import com.geniusjun.lotto.data.network.TokenProvider
import com.geniusjun.lotto.ui.screen.AuthScreen
import com.geniusjun.lotto.ui.screen.LottoFortuneApp
import com.geniusjun.lotto.ui.theme.LottoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LottoTheme {
                AppContent()
            }
        }
    }
}

@Composable
private fun AppContent() {
    val context = LocalContext.current
    val signInManager = remember {
        GoogleSignInManager(context, TokenProvider(context))
    }
    
    var isLoggedIn by remember { mutableStateOf(signInManager.isSignedIn()) }
    
    if (isLoggedIn) {
        LottoFortuneApp()
    } else {
        AuthScreen(
            onLoginSuccess = {
                isLoggedIn = true
            }
        )
    }
}

