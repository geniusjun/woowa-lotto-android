package com.geniusjun.lotto.model

data class LottoUiState(
    val balance: Int,
    val winningNumbers: List<Int>,
    val bonusNumber: Int = 0
)
