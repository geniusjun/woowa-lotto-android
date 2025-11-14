@file:Suppress("DEPRECATION")
package com.geniusjun.lotto.auth

import android.content.Context
import android.util.Log
import com.geniusjun.lotto.BuildConfig
import com.geniusjun.lotto.data.api.AuthApi
import com.geniusjun.lotto.data.model.GoogleLoginRequest
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
    
    private val authApi: AuthApi by lazy {
        RetrofitClient.createAuthApi(tokenProvider)
    }
    
    fun getSignInIntent() = googleSignInClient.signInIntent
    
    suspend fun handleSignInResult(data: android.content.Intent?): Result<LoginResult> {
        return try {
            Log.d(TAG, "🔵 [1/4] Google Sign-In 결과 처리 시작")
            
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken ?: return Result.failure(Exception("ID token is null"))
            
            Log.d(TAG, "✅ [2/4] Google ID Token 획득 성공")
            Log.d(TAG, "   ID Token (앞 20자): ${idToken.take(20)}...")
            Log.d(TAG, "   Email: ${account.email}")
            
            // Call backend API
            Log.d(TAG, "🔄 [3/4] 백엔드 API 호출 중...")
            val response = authApi.login(GoogleLoginRequest(idToken))
            
            if (response.success && response.data != null) {
                val loginData = response.data!!
                tokenProvider.saveTokens(loginData.accessToken, loginData.refreshToken)
                
                Log.d(TAG, "✅ [4/4] 로그인 성공!")
                Log.d(TAG, "   Member ID: ${loginData.memberId}")
                Log.d(TAG, "   Nickname: ${loginData.nickname}")
                Log.d(TAG, "   Access Token (앞 20자): ${loginData.accessToken.take(20)}...")
                Log.d(TAG, "   Refresh Token (앞 20자): ${loginData.refreshToken.take(20)}...")
                Log.d(TAG, "   Access Token 저장 완료: ${tokenProvider.getAccessToken() != null}")
                Log.d(TAG, "   🔐 Full Access Token: ${loginData.accessToken}")
                
                Result.success(
                    LoginResult(
                        memberId = loginData.memberId,
                        nickname = loginData.nickname,
                        accessToken = loginData.accessToken
                    )
                )
            } else {
                Log.e(TAG, "❌ 로그인 실패: ${response.message}")
                Result.failure(Exception(response.message ?: "Login failed"))
            }
        } catch (e: ApiException) {
            Log.e(TAG, "❌ Google Sign-In API 에러: ${e.message}", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "❌ 로그인 처리 중 에러: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    companion object {
        private const val TAG = "GoogleSignInManager"
    }
    
    fun isSignedIn(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(context) != null && 
               tokenProvider.getAccessToken() != null
    }
    
    fun signOut() {
        googleSignInClient.signOut()
        tokenProvider.clearTokens()
    }
}

data class LoginResult(
    val memberId: Long,
    val nickname: String,
    val accessToken: String
)

