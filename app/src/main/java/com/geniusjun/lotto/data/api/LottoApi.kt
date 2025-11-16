package com.geniusjun.lotto.data.api

import com.geniusjun.lotto.data.model.ApiResponse
import com.geniusjun.lotto.data.model.BalanceResponse
import com.geniusjun.lotto.data.model.FortuneResponse
import com.geniusjun.lotto.data.model.LottoDrawResponse
import com.geniusjun.lotto.data.model.WinningNumbersResponse
import retrofit2.http.GET
import retrofit2.http.POST

interface LottoApi {
    /**
     * 최신 당첨 번호 조회
     */
    @GET("api/winning/latest")
    suspend fun getLatestWinningNumbers(): ApiResponse<WinningNumbersResponse>
    
    /**
     * 랜덤 로또 구매
     */
    @POST("api/lotto/draw")
    suspend fun drawLotto(): ApiResponse<LottoDrawResponse>
    
    /**
     * 회원 잔액 조회
     */
    @GET("api/member/balance")
    suspend fun getBalance(): ApiResponse<BalanceResponse>
    
    /**
     * 오늘의 운세 조회
     */
    @GET("api/fortune/today")
    suspend fun getTodayFortune(): ApiResponse<FortuneResponse>
}

