package com.geniusjun.lotto.model

data class LottoUiState(
    val balance: Long,
    val winningNumbers: List<Int>,
    val bonusNumber: Int = 0
)
