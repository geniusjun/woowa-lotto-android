package com.geniusjun.lotto.data.model

data class LottoDrawResponse(
    val myNumbers: List<Int>,
    val winningNumbers: List<Int>,
    val matchedNumbers: List<Int>,
    val bonusNumber: Int,
    val bonusMatched: Boolean,
    val rank: String,
    val reward: Long,
    val balance: Long
)

