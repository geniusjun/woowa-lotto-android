package com.geniusjun.lotto.ui.screen

import androidx.compose.runtime.*
import com.geniusjun.lotto.model.LottoPick
import com.geniusjun.lotto.model.LottoUiState
import com.geniusjun.lotto.util.generateLottoNumbers

@Composable
fun LottoFortuneApp() {
    // 다이얼로그 열림 상태
    var showLottoDialog by remember { mutableStateOf(false) }
    var showFortuneDialog by remember { mutableStateOf(false) }
    var showNoMoneyDialog by remember { mutableStateOf(false) }

    // 내가 방금 산 로또
    var myLatestPick by remember { mutableStateOf<LottoPick?>(null) }

    // 초기 데이터 (변하지 않는 쪽)
    val uiState = remember {
        LottoUiState(
            balance = 60_000,
            thisWeekNumbers = listOf(7, 12, 23, 31, 38, 42)
        )
    }

    // 실제로 변하는 잔액
    var currentBalance by remember { mutableIntStateOf(uiState.balance) }

    // 한 장 가격
    val ticketPrice = 1_000

    LottoFortuneScreen(
        uiState = uiState.copy(balance = currentBalance),
        onClickBuy = {
            if (currentBalance >= ticketPrice) {
                // 돈 있으면 → 사기
                val pick = generateLottoNumbers()
                myLatestPick = pick
                currentBalance -= ticketPrice
                showLottoDialog = true
            } else {
                // 돈 없으면 → 잔액 부족
                showNoMoneyDialog = true
            }
        },
        onClickFortune = {
            showFortuneDialog = true
        }
    )

    // 다이얼로그 모아놓은 곳
    LottoDialogs(
        showLotto = showLottoDialog,
        showFortune = showFortuneDialog,
        showNoMoney = showNoMoneyDialog,
        thisWeekNumbers = uiState.thisWeekNumbers,
        myPick = myLatestPick,
        onCloseLotto = { showLottoDialog = false },
        onCloseFortune = { showFortuneDialog = false },
        onCloseNoMoney = { showNoMoneyDialog = false }
    )
}
