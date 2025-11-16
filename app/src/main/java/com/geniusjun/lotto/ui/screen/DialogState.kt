package com.geniusjun.lotto.ui.screen

import com.geniusjun.lotto.data.model.LottoDrawResponse

/**
 * 다이얼로그 표시 상태를 관리하는 데이터 클래스
 */
data class DialogState(
    val showLotto: Boolean = false,
    val showFortune: Boolean = false,
    val showNoMoney: Boolean = false,
    val lottoDrawResult: LottoDrawResponse? = null,
    val fortuneText: String? = null
) {
    fun showLottoDialog(result: LottoDrawResponse) = copy(
        showLotto = true,
        lottoDrawResult = result
    )
    
    fun showFortuneDialog(fortune: String) = copy(
        showFortune = true,
        fortuneText = fortune
    )
    
    fun showNoMoneyDialog() = copy(showNoMoney = true)
    
    fun closeAll() = copy(
        showLotto = false,
        showFortune = false,
        showNoMoney = false
    )
}

