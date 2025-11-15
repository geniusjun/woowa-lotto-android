package com.geniusjun.lotto.data.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 모든 API 요청에 Authorization 헤더를 자동으로 추가하고,
 * 401 에러 발생 시 refresh 토큰으로 access 토큰을 자동 갱신하는 인터셉터
 */
class AuthInterceptor(
    private val tokenProvider: () -> String?,
    private val tokenRefresher: suspend () -> Result<Unit>
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = proceedWithToken(chain, request)
        
        // 401 에러이고 refresh API가 아니면 토큰 갱신 시도
        if (response.code == 401 && !isRefreshEndpoint(request)) {
            return retryWithRefreshedToken(chain, request, response)
        }
        
        return response
    }
    
    private fun proceedWithToken(chain: Interceptor.Chain, request: okhttp3.Request): Response {
        val token = tokenProvider() ?: return chain.proceed(request)
        
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", "Bearer ${token.trim()}")
            .build()
        
        return chain.proceed(authenticatedRequest)
    }
    
    private fun retryWithRefreshedToken(
        chain: Interceptor.Chain,
        originalRequest: okhttp3.Request,
        originalResponse: Response
    ): Response {
        if (!refreshToken()) {
            return originalResponse
        }
        
        val newToken = tokenProvider() ?: return originalResponse
        
        val retryRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer ${newToken.trim()}")
            .build()
        
        return chain.proceed(retryRequest)
    }
    
    private fun refreshToken(): Boolean {
        return try {
            val result = kotlinx.coroutines.runBlocking {
                tokenRefresher()
            }
            result.isSuccess
        } catch (e: Exception) {
            Log.e(TAG, "토큰 갱신 중 오류 발생: ${e.message}")
            false
        }
    }
    
    private fun isRefreshEndpoint(request: okhttp3.Request): Boolean {
        return request.url.encodedPath.contains("/api/auth/refresh")
    }
    
    companion object {
        private const val TAG = "AuthInterceptor"
    }
}


