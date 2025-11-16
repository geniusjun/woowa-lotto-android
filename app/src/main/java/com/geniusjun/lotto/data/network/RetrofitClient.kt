package com.geniusjun.lotto.data.network

import com.geniusjun.lotto.BuildConfig
import com.geniusjun.lotto.data.api.AuthApi
import com.geniusjun.lotto.data.repository.AuthRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = BuildConfig.BASE_URL
    
    private fun createOkHttpClient(
        tokenProvider: TokenProvider,
        authRepository: AuthRepository
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(
                AuthInterceptor(
                    tokenProvider = { tokenProvider.getAccessToken() },
                    tokenRefresher = {
                        authRepository.refreshAccessToken().map { Result.success<Unit>(Unit) }
                            .getOrElse { error -> Result.failure<Unit>(error) }
                    }
                )
            )
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    fun createRetrofit(
        tokenProvider: TokenProvider,
        authRepository: AuthRepository
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient(tokenProvider, authRepository))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * TokenProvider를 사용해 AuthApi 인스턴스를 생성한다.
     * 인증/로그인 전용 API라 순환 참조를 피하기 위해
     * 별도의 Retrofit 클라이언트를 사용한다.
     */
    fun createAuthApi(tokenProvider: TokenProvider): AuthApi {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(
                tokenProvider = { tokenProvider.getAccessToken() },
                tokenRefresher = { Result.success(Unit) } // AuthApi 호출 시에는 refresh 안 함
            ))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    /**
     * 공통 Retrofit 인스턴스로 모든 API 인터페이스를 생성하는 메서드.
     * 새 API 추가 시 이 메서드만 호출하면 됩니다.
     */
    inline fun <reified T> createApi(
        tokenProvider: TokenProvider,
        authRepository: AuthRepository
    ): T {
        return createRetrofit(tokenProvider, authRepository).create(T::class.java)
    }
}


