package com.geniusjun.lotto.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.geniusjun.lotto.auth.GoogleSignInManager
import com.geniusjun.lotto.data.network.TokenProvider
import com.geniusjun.lotto.ui.theme.LottoTheme

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current

    // 의존성 초기화
    val googleSignInManager = remember {
        GoogleSignInManager(context, TokenProvider(context))
    }

    val viewModel: AuthViewModel = viewModel(
        key = "auth_screen",
        factory = AuthViewModelFactory(googleSignInManager)
    )

    val uiState by viewModel.uiState.collectAsState()

    // Google Sign-In 결과 처리
    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.handleSignInResult(result.data)
    }

    // 화면 표시 시 ViewModel 상태 초기화
    LaunchedEffect(Unit) {
        viewModel.resetState()
    }

    // 로그인 성공 감지 (false → true 변경 시에만 처리)
    HandleLoginSuccess(
        isLoggedIn = uiState.isLoggedIn,
        onLoginSuccess = onLoginSuccess
    )

    // UI 렌더링
    LottoTheme {
        OnboardingScreen(
            onGoogleSignInClick = {
                signInLauncher.launch(viewModel.getSignInIntent())
            }
        )
    }
}

/**
 * 로그인 성공을 감지하고 콜백을 호출합니다.
 * resetState()로 인해 초기값은 항상 false이므로, false → true 변경 시에만 처리합니다.
 */
@Composable
private fun HandleLoginSuccess(
    isLoggedIn: Boolean,
    onLoginSuccess: () -> Unit
) {
    var hasHandledInitialState by remember { mutableStateOf(false) }
    var previousLoggedInState by remember { mutableStateOf(false) }
    
    LaunchedEffect(isLoggedIn) {
        // 초기 상태 처리: resetState() 완료 후 현재 상태 저장
        if (!hasHandledInitialState) {
            previousLoggedInState = isLoggedIn
            hasHandledInitialState = true
            return@LaunchedEffect
        }
        
        // 초기화 완료 후, false → true 변경 시에만 로그인 성공 처리
        if (isLoggedIn && !previousLoggedInState) {
            onLoginSuccess()
        }
        previousLoggedInState = isLoggedIn
    }
}

/**
 * AuthViewModel 생성을 위한 Factory
 */
class AuthViewModelFactory(
    private val signInManager: GoogleSignInManager
) : androidx.lifecycle.ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(signInManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}




