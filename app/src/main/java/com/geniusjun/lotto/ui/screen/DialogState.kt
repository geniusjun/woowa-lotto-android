package com.geniusjun.lotto.ui.screen

import com.geniusjun.lotto.data.model.LottoDrawResponse
import com.geniusjun.lotto.data.model.MemberRankResult

/**
 * 다이얼로그 표시 상태를 관리하는 데이터 클래스
 */
data class DialogState(
    val showLotto: Boolean = false,
    val showFortune: Boolean = false,
    val showNoMoney: Boolean = false,
    val showRanking: Boolean = false,
    val lottoDrawResult: LottoDrawResponse? = null,
    val fortuneText: String? = null,
    val top3Ranks: List<MemberRankResult> = emptyList(),
    val myRank: MemberRankResult? = null
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
    
    fun showRankingDialog(
        top3: List<MemberRankResult>,
        myRank: MemberRankResult?
    ) = copy(
        showRanking = true,
        top3Ranks = top3,
        myRank = myRank
    )
    
    fun closeAll() = copy(
        showLotto = false,
        showFortune = false,
        showNoMoney = false,
        showRanking = false
    )
}

