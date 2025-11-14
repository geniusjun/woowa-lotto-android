package com.geniusjun.lotto.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geniusjun.lotto.data.repository.LottoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 로또 관련 UI 상태를 관리하는 ViewModel
 */
class LottoViewModel(
    private val lottoRepository: LottoRepository
) : ViewModel() {
    
    private val _winningNumbers = MutableStateFlow<List<Int>>(emptyList())
    val winningNumbers: StateFlow<List<Int>> = _winningNumbers.asStateFlow()
    
    private val _bonusNumber = MutableStateFlow<Int>(0)
    val bonusNumber: StateFlow<Int> = _bonusNumber.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private var hasLoaded = false
    
    /**
     * 당첨 번호 로드
     * 로그인 완료 후에만 호출되도록 보장
     */
    fun loadWinningNumbers() {
        if (hasLoaded) return
        
        hasLoaded = true
        viewModelScope.launch {
            lottoRepository.getLatestWinningNumbers()
                .onSuccess { winningNumbers ->
                    _winningNumbers.value = winningNumbers.mainNumbers
                    _bonusNumber.value = winningNumbers.bonusNumber
                    _error.value = null
                }
                .onFailure { error ->
                    Log.e(TAG, "당첨 번호 로드 실패: ${error.message}")
                    _error.value = error.message
                    hasLoaded = false // 실패 시 재시도 가능하도록
                }
        }
    }
    
    companion object {
        private const val TAG = "LottoViewModel"
    }
}

