package com.geniusjun.lotto.data.api

import com.geniusjun.lotto.data.model.ApiResponse
import com.geniusjun.lotto.data.model.WinningNumbersResponse
import retrofit2.http.GET

interface LottoApi {
    /**
     * 최신 당첨 번호 조회
     */
    @GET("api/winning/latest")
    suspend fun getLatestWinningNumbers(): ApiResponse<WinningNumbersResponse>
}

