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

    // 내가 방금 산 로또
    var myLatestPick by remember { mutableStateOf<LottoPick?>(null) }

    // 화면에 보여줄 기본 상태 (지금은 하드코딩)
    val uiState = remember {
        LottoUiState(
            balance = 60_000,
            thisWeekNumbers = listOf(7, 12, 23, 31, 38, 42)
        )
    }

    // 실제 화면
    LottoFortuneScreen(
        uiState = uiState,
        onClickBuy = {
            // 여기서 번호 뽑고
            myLatestPick = generateLottoNumbers()
            // 다이얼로그 열기
            showLottoDialog = true
        },
        onClickFortune = {
            showFortuneDialog = true
        }
    )

    // 다이얼로그는 아래에서 한 번에
        LottoDialogs(
        showLotto = showLottoDialog,
        showFortune = showFortuneDialog,
        thisWeekNumbers = uiState.thisWeekNumbers,
        myPick = myLatestPick,
        onCloseLotto = { showLottoDialog = false },
        onCloseFortune = { showFortuneDialog = false }
    )
}
