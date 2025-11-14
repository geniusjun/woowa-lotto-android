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
    
    private val _uiState = MutableStateFlow(AuthUiState(isLoggedIn = false))
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    fun getSignInIntent(): Intent {
        return signInManager.getSignInIntent()
    }
    
    fun handleSignInResult(intent: Intent?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            signInManager.handleSignInResult(intent)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoggedIn = true,
                        isLoading = false,
                        error = null
                    )
                    Log.d(TAG, "로그인 성공")
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoggedIn = false,
                        isLoading = false,
                        error = error.message
                    )
                    Log.e(TAG, "로그인 실패: ${error.message}")
                }
        }
    }
    
    /**
     * AuthScreen이 표시될 때 상태를 초기화합니다.
     * 이전 세션의 로그인 상태가 남아있을 수 있으므로 명시적으로 리셋합니다.
     */
    fun resetState() {
        _uiState.value = AuthUiState(isLoggedIn = false)
    }
    
    companion object {
        private const val TAG = "AuthViewModel"
    }
}

data class AuthUiState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

