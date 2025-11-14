package com.geniusjun.lotto.data.repository

import android.util.Log
import com.geniusjun.lotto.data.api.LottoApi
import com.geniusjun.lotto.data.model.WinningNumbersResponse

/**
 * 로또 관련 API 호출을 담당하는 Repository
 */
class LottoRepository(
    private val lottoApi: LottoApi
) {
    /**
     * 최신 당첨 번호 조회
     */
    suspend fun getLatestWinningNumbers(): Result<WinningNumbersResponse> {
        return try {
            val response = lottoApi.getLatestWinningNumbers()
            
            if (response.success && response.data != null) {
                Result.success(response.data!!)
            } else {
                Result.failure(Exception(response.message ?: response.error ?: "Failed to get winning numbers"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "당첨 번호 조회 API 에러: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    companion object {
        private const val TAG = "LottoRepository"
    }
}

