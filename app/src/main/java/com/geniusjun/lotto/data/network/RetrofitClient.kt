package com.geniusjun.lotto.data.network

import com.geniusjun.lotto.BuildConfig
import com.geniusjun.lotto.data.api.AuthApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"
    
    private fun createOkHttpClient(tokenProvider: TokenProvider): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor { tokenProvider.getAccessToken() })
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    fun createRetrofit(tokenProvider: TokenProvider): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient(tokenProvider))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * TokenProvider를 사용해 AuthApi 인스턴스를 생성한다.
     * 인증 관련 API 호출에서 사용된다.
     */
    fun createAuthApi(tokenProvider: TokenProvider): AuthApi {
        return createRetrofit(tokenProvider).create(AuthApi::class.java)
    }

    /**
     * 제네릭 API 생성 함수.
     * LottoApi, UserApi 등 새로운 API 인터페이스를 만들 때 사용한다.
     */
    inline fun <reified T> createApi(tokenProvider: TokenProvider): T {
        return createRetrofit(tokenProvider).create(T::class.java)
    }

}


