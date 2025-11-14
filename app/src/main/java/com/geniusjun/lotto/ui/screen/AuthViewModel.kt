package com.geniusjun.lotto.ui.screen

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geniusjun.lotto.auth.GoogleSignInManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val signInManager: GoogleSignInManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    fun getSignInIntent(): Intent {
        return signInManager.getSignInIntent()
    }
    
    fun handleSignInResult(intent: Intent?) {
        viewModelScope.launch {
            Log.d(TAG, "🔵 로그인 결과 처리 시작")
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            signInManager.handleSignInResult(intent)
                .onSuccess { result ->
                    Log.d(TAG, "✅ 로그인 성공 - ViewModel")
                    Log.d(TAG, "   Member ID: ${result.memberId}")
                    Log.d(TAG, "   Nickname: ${result.nickname}")
                    _uiState.value = _uiState.value.copy(
                        isLoggedIn = true,
                        isLoading = false,
                        error = null
                    )
                }
                .onFailure { error ->
                    Log.e(TAG, "❌ 로그인 실패 - ViewModel: ${error.message}", error)
                    _uiState.value = _uiState.value.copy(
                        isLoggedIn = false,
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }
    
    companion object {
        private const val TAG = "AuthViewModel"
    }
    
    fun checkLoginStatus() {
        _uiState.value = _uiState.value.copy(
            isLoggedIn = signInManager.isSignedIn()
        )
    }
}

data class AuthUiState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

