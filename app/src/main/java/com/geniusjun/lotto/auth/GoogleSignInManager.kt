@file:Suppress("DEPRECATION")
package com.geniusjun.lotto.auth

import android.content.Context
import android.util.Log
import com.geniusjun.lotto.BuildConfig
import com.geniusjun.lotto.data.repository.AuthRepository
import com.geniusjun.lotto.data.network.RetrofitClient
import com.geniusjun.lotto.data.network.TokenProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class GoogleSignInManager(
    private val context: Context,
    private val tokenProvider: TokenProvider
) {
    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()
        
        GoogleSignIn.getClient(context, gso)
    }
    
    private val authRepository: AuthRepository by lazy {
        val authApi = RetrofitClient.createAuthApi(tokenProvider)
        AuthRepository(authApi, tokenProvider)
    }
    
    fun getSignInIntent() = googleSignInClient.signInIntent
    
    suspend fun handleSignInResult(data: android.content.Intent?): Result<LoginResult> {
        return try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken ?: return Result.failure(Exception("ID token is null"))
            
            // 백엔드 API 호출 및 토큰 저장
            val loginData = authRepository.login(idToken).getOrElse { error ->
                return Result.failure(error)
            }
            
            Result.success(
                LoginResult(
                    memberId = loginData.memberId,
                    nickname = loginData.nickname,
                    accessToken = loginData.accessToken
                )
            )
        } catch (e: ApiException) {
            Log.e(TAG, "Google Sign-In API 에러: ${e.message}", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "로그인 API 에러: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    /**
     * 로그아웃 처리
     * 1. 백엔드 API 호출 (Redis에서 refresh 토큰 삭제)
     * 2. 로컬 토큰 삭제
     * 3. Google Sign-In 로그아웃
     */
    suspend fun logout(): Result<Unit> {
        return try {
            // 이미 로그아웃된 상태면 로컬 정리만 수행
            if (!tokenProvider.getAccessToken().isNullOrEmpty()) {
                performLogout()
            } else {
                clearLocalAuth()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            // 에러 발생 시에도 로컬 정리 (보안상 중요)
            clearLocalAuth()
            Result.failure(e)
        }
    }
    
    /**
     * 실제 로그아웃 수행 (토큰이 있는 경우)
     */
    private suspend fun performLogout() {
        // 백엔드 API 호출 (Redis에서 refresh 토큰 삭제)
        authRepository.logout()
        
        // 로컬 토큰 삭제 (API 성공/실패와 관계없이 항상 삭제)
        clearLocalAuth()
    }
    
    /**
     * 로컬 인증 정보 정리
     */
    private fun clearLocalAuth() {
        tokenProvider.clearTokens()
        googleSignInClient.signOut()
    }
    
    companion object {
        private const val TAG = "GoogleSignInManager"
    }
    
    fun isSignedIn(): Boolean {
        // 토큰이 없으면 로그인하지 않은 것으로 간주
        // (GoogleSignIn.getLastSignedInAccount는 로그아웃 직후에도 null이 아닐 수 있음)
        val hasToken = tokenProvider.getAccessToken() != null
        val hasGoogleAccount = GoogleSignIn.getLastSignedInAccount(context) != null
        
        // 토큰이 있어야만 로그인 상태로 간주
        return hasToken && hasGoogleAccount
    }
    
}

data class LoginResult(
    val memberId: Long,
    val nickname: String,
    val accessToken: String
)

