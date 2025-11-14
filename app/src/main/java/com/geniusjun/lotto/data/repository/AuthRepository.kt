package com.geniusjun.lotto.data.repository

import android.util.Log
import com.geniusjun.lotto.data.api.AuthApi
import com.geniusjun.lotto.data.model.GoogleLoginRequest
import com.geniusjun.lotto.data.model.LoginResponse
import com.geniusjun.lotto.data.network.TokenProvider

class AuthRepository(
    private val authApi: AuthApi,
    private val tokenProvider: TokenProvider
) {
    suspend fun login(idToken: String): Result<LoginResponse> {
        return try {
            val response = authApi.login(GoogleLoginRequest(idToken))
            
            if (response.success && response.data != null) {
                val loginData = response.data!!
                tokenProvider.saveTokens(loginData.accessToken, loginData.refreshToken)
                Log.d(TAG, "로그인 성공")
                Result.success(loginData)
            } else {
                Log.e(TAG, "로그인 실패: ${response.message}")
                Result.failure(Exception(response.message ?: "Login failed"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "로그인 API 에러: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    /**
     * 로그아웃 API 호출
     * Redis에서 refresh 토큰을 삭제합니다.
     * 주의: 로컬 토큰은 GoogleSignInManager에서 삭제합니다.
     */
    suspend fun logout(): Result<Unit> {
        return try {
            val response = authApi.logout()
            
            if (response.success) {
                Result.success(Unit)
            } else {
                Log.e(TAG, "로그아웃 API 에러: ${response.message}")
                Result.failure(Exception(response.message ?: "Logout failed"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "로그아웃 API 에러: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    companion object {
        private const val TAG = "AuthRepository"
    }
}


