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

    val googleSignInManager = remember {
        GoogleSignInManager(
            context,
            TokenProvider(context)
        )
    }

    val viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(googleSignInManager)
    )

    LaunchedEffect(Unit) {
        googleSignInManager.signOut()
    }

    val uiState by viewModel.uiState.collectAsState()

    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.handleSignInResult(result.data)
    }

    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            onLoginSuccess()
        }
    }

    LottoTheme {
        OnboardingScreen(
            onGoogleSignInClick = {
                val signInIntent = viewModel.getSignInIntent()
                signInLauncher.launch(signInIntent)
            }
        )
    }
}
class AuthViewModelFactory(
    private val signInManager: GoogleSignInManager
) : androidx.lifecycle.ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(signInManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}




