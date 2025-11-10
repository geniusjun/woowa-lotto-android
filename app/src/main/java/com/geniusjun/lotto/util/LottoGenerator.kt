package com.geniusjun.lotto.util

import com.geniusjun.lotto.model.LottoPick

fun generateLottoNumbers(): LottoPick {
    val numbers = (1..45)
        .shuffled()
        .take(6)
        .sorted()

    val bonus = (1..45)
        .filterNot { it in numbers }
        .random()

    return LottoPick(
        numbers = numbers,
        bonus = bonus
    )
}
