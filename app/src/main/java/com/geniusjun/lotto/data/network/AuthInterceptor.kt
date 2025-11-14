package com.geniusjun.lotto.data.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * 모든 API 요청에 Authorization 헤더를 자동으로 추가하는 인터셉터
 */
class AuthInterceptor(
    private val tokenProvider: () -> String?
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = tokenProvider()
        
        return if (token != null) {
            val cleanToken = token.trim()
            val newRequest = request.newBuilder()
                .header("Authorization", "Bearer $cleanToken")
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(request)
        }
    }
}


