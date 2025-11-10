package com.geniusjun.lotto.model

data class LottoUiState(
    val balance: Int = 0,
    val thisWeekNumbers: List<Int> = emptyList()
)