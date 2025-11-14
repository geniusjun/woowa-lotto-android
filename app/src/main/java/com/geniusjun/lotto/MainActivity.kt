package com.geniusjun.lotto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.geniusjun.lotto.ui.screen.LottoFortuneApp
import com.geniusjun.lotto.ui.screen.OnboardingScreen
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
    var isLoggedIn by remember { mutableStateOf(false) }
    
    if (isLoggedIn) {
        LottoFortuneApp()
    } else {
        OnboardingScreen(
            onGoogleSignInClick = {
                // TODO: 구글 로그인 구현 필요
                isLoggedIn = true
            }
        )
    }
}

