package com.geniusjun.lotto.data.repository

import android.util.Log
import com.geniusjun.lotto.data.api.LottoApi
import com.geniusjun.lotto.data.model.ApiResponse
import com.geniusjun.lotto.data.model.BalanceResponse
import com.geniusjun.lotto.data.model.FortuneResponse
import com.geniusjun.lotto.data.model.LottoDrawResponse
import com.geniusjun.lotto.data.model.WinningNumbersResponse

/**
 * API 호출을 담당하는 Repository
 */
class LottoRepository(
    private val lottoApi: LottoApi
) {
    /**
     * 최신 당첨 번호 조회
     */
    suspend fun getLatestWinningNumbers(): Result<WinningNumbersResponse> {
        return handleApiCall(
            apiCall = { lottoApi.getLatestWinningNumbers() },
            errorMessage = "당첨 번호 조회 실패"
        )
    }
    
    /**
     * 랜덤 로또 구매
     */
    suspend fun drawLotto(): Result<LottoDrawResponse> {
        return handleApiCall(
            apiCall = { lottoApi.drawLotto() },
            errorMessage = "로또 구매 실패"
        )
    }
    
    /**
     * 회원 잔액 조회
     */
    suspend fun getBalance(): Result<BalanceResponse> {
        return handleApiCall(
            apiCall = { lottoApi.getBalance() },
            errorMessage = "잔액 조회 실패"
        )
    }
    
    /**
     * 오늘의 운세 조회
     */
    suspend fun getTodayFortune(): Result<FortuneResponse> {
        return handleApiCall(
            apiCall = { lottoApi.getTodayFortune() },
            errorMessage = "운세 조회 실패"
        )
    }
    
    /**
     * API 호출 공통 처리
     */
    private inline fun <T> handleApiCall(
        apiCall: () -> ApiResponse<T>,
        errorMessage: String
    ): Result<T> {
        return try {
            val response = apiCall()
            
            if (response.success && response.data != null) {
                Result.success(response.data!!)
            } else {
                val message = response.message ?: response.error ?: errorMessage
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Log.e(TAG, "$errorMessage: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    companion object {
        private const val TAG = "LottoRepository"
    }
}

