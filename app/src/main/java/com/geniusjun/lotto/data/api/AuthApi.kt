package com.geniusjun.lotto.data.api

import com.geniusjun.lotto.data.model.ApiResponse
import com.geniusjun.lotto.data.model.GoogleLoginRequest
import com.geniusjun.lotto.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/google")
    suspend fun login(@Body request: GoogleLoginRequest): ApiResponse<LoginResponse>
    
    @DELETE("api/auth/logout")
    suspend fun logout(): ApiResponse<String>
}


