package com.geniusjun.lotto.model

/**
 * 로또 한 장에 대한 데이터
 * numbers: 기본 번호 6개
 * bonus: 보너스 번호 1개
 */
data class LottoPick(
    val numbers: List<Int>,
    val bonus: Int
)
