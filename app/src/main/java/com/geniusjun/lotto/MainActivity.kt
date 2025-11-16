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
    val tokenProvider = remember { TokenProvider(context) }
    val signInManager = remember {
        GoogleSignInManager(context, tokenProvider)
    }
    
    // 로그인 상태 관리 (초기값은 항상 false - 로그인 화면부터 시작)
    var isLoggedIn by remember { mutableStateOf(false) }
    
    // 앱 시작 시 로그인 상태 확인
    LaunchedEffect(Unit) {
        isLoggedIn = signInManager.isSignedIn()
    }
    
    // 화면 전환 핸들러
    val handleLogout: () -> Unit = {
        isLoggedIn = false
    }
    
    val handleLoginSuccess: () -> Unit = {
        isLoggedIn = true
    }
    
    // 화면 표시
    if (isLoggedIn) {
        LottoFortuneApp(onLogout = handleLogout)
    } else {
        AuthScreen(onLoginSuccess = handleLoginSuccess)
    }
}

