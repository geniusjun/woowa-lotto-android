package com.geniusjun.lotto.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geniusjun.lotto.data.model.LottoDrawResponse
import com.geniusjun.lotto.data.repository.LottoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI 상태를 관리하는 ViewModel
 */
class LottoViewModel(
    private val lottoRepository: LottoRepository
) : ViewModel() {
    
    private val _winningNumbers = MutableStateFlow<List<Int>>(emptyList())
    val winningNumbers: StateFlow<List<Int>> = _winningNumbers.asStateFlow()
    
    private val _bonusNumber = MutableStateFlow<Int>(0)
    val bonusNumber: StateFlow<Int> = _bonusNumber.asStateFlow()
    
    private val _balance = MutableStateFlow<Long>(0)
    val balance: StateFlow<Long> = _balance.asStateFlow()
    
    private val _fortune = MutableStateFlow<String?>(null)
    val fortune: StateFlow<String?> = _fortune.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private var hasLoadedInitialData = false
    
    /**
     * 로그인 후 초기 데이터 로드 (당첨 번호 + 잔액)
     */
    fun loadInitialData() {
        if (hasLoadedInitialData) return
        
        hasLoadedInitialData = true
        viewModelScope.launch {
            // 당첨 번호 로드
            lottoRepository.getLatestWinningNumbers()
                .onSuccess { response ->
                    _winningNumbers.value = response.mainNumbers
                    _bonusNumber.value = response.bonusNumber
                }
                .onFailure { error ->
                    Log.e(TAG, "당첨 번호 로드 실패: ${error.message}")
                    _error.value = error.message
                    hasLoadedInitialData = false
                    return@launch
                }
            
            // 잔액 로드
            lottoRepository.getBalance()
                .onSuccess { response ->
                    _balance.value = response.balance
                    _error.value = null
                }
                .onFailure { error ->
                    Log.e(TAG, "잔액 로드 실패: ${error.message}")
                    _error.value = error.message
                    hasLoadedInitialData = false
                }
        }
    }
    
    /**
     * 랜덤 로또 구매
     */
    fun drawLotto(
        onSuccess: (LottoDrawResponse) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            lottoRepository.drawLotto()
                .onSuccess { drawResponse ->
                    _winningNumbers.value = drawResponse.winningNumbers
                    _bonusNumber.value = drawResponse.bonusNumber
                    _balance.value = drawResponse.balance
                    _error.value = null
                    onSuccess(drawResponse)
                }
                .onFailure { error ->
                    Log.e(TAG, "로또 구매 실패: ${error.message}")
                    _error.value = error.message
                    onFailure(error.message ?: "로또 구매에 실패했습니다.")
                }
        }
    }
    
    /**
     * 오늘의 운세 조회
     */
    fun loadFortune(
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            lottoRepository.getTodayFortune()
                .onSuccess { response ->
                    _fortune.value = response.fortune
                    _error.value = null
                    onSuccess(response.fortune)
                }
                .onFailure { error ->
                    Log.e(TAG, "운세 조회 실패: ${error.message}")
                    _error.value = error.message
                    onFailure(error.message ?: "운세 조회에 실패했습니다.")
                }
        }
    }
    
    companion object {
        private const val TAG = "LottoViewModel"
    }
}

