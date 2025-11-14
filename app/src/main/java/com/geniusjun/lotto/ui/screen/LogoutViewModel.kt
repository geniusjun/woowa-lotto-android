package com.geniusjun.lotto.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geniusjun.lotto.auth.GoogleSignInManager
import kotlinx.coroutines.launch

class LogoutViewModel(
    private val signInManager: GoogleSignInManager
) : ViewModel() {
    
    private var isLoggingOut = false
    
    /**
     * 로그아웃 처리
     * 1. 즉시 화면 전환 (사용자 경험 개선)
     * 2. 백그라운드에서 API 호출 및 토큰 삭제
     */
    fun logout(onScreenTransition: () -> Unit) {
        if (isLoggingOut) return
        
        isLoggingOut = true
        
        // 즉시 화면 전환
        onScreenTransition()
        
        // 백그라운드에서 로그아웃 처리
        viewModelScope.launch {
            try {
                signInManager.logout()
            } finally {
                isLoggingOut = false
            }
        }
    }
}

