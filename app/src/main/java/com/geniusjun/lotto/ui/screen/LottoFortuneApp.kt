package com.geniusjun.lotto.ui.screen

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.geniusjun.lotto.model.LottoPick
import com.geniusjun.lotto.model.LottoUiState
import com.geniusjun.lotto.util.generateLottoNumbers
import com.geniusjun.lotto.util.loadLastFortuneDate
import com.geniusjun.lotto.util.saveLastFortuneDate
import java.time.LocalDate

@Composable
fun LottoFortuneApp() {
    val context = LocalContext.current

    // 다이얼로그 열림 상태
    var showLottoDialog by remember { mutableStateOf(false) }
    var showFortuneDialog by remember { mutableStateOf(false) }
    var showNoMoneyDialog by remember { mutableStateOf(false) }
    var showFortuneAlreadyDialog by remember { mutableStateOf(false) }

    // 내가 방금 산 로또
    var myLatestPick by remember { mutableStateOf<LottoPick?>(null) }

    // 초기 데이터 (변하지 않는 쪽)
    val uiState = remember {
        LottoUiState(
            balance = 50_000,
            thisWeekNumbers = listOf(7, 12, 23, 31, 38, 42)
        )
    }

    // 실제로 변하는 잔액
    var currentBalance by remember { mutableIntStateOf(uiState.balance) }

    // 오늘 날짜 문자열 (예: "2025-11-10")
    val today = remember { LocalDate.now().toString() }

    // 로컬 저장된 마지막 운세 본 날짜 불러오기
    var lastFortuneDate by remember { mutableStateOf(loadLastFortuneDate(context)) }

    // 한 장 가격
    val ticketPrice = 1_000

    LottoFortuneScreen(
        uiState = uiState.copy(balance = currentBalance),
        onClickBuy = {
            if (currentBalance >= ticketPrice) {
                val pick = generateLottoNumbers()
                myLatestPick = pick
                currentBalance -= ticketPrice
                showLottoDialog = true
            } else {
                showNoMoneyDialog = true
            }
        },
        onClickFortune = {
            // 1일 1회 제한 로직
            if (lastFortuneDate == today) {
                // 이미 오늘 봤다
                showFortuneAlreadyDialog = true
            } else {
                // 오늘 처음 본다 → 운세 보기 + 날짜 저장
                showFortuneDialog = true
                lastFortuneDate = today
                saveLastFortuneDate(context, today)
            }
        }
    )

    // 다이얼로그 모음
    LottoDialogs(
        showLotto = showLottoDialog,
        showFortune = showFortuneDialog,
        showFortuneAlready = showFortuneAlreadyDialog,
        showNoMoney = showNoMoneyDialog,
        thisWeekNumbers = uiState.thisWeekNumbers,
        myPick = myLatestPick,
        onCloseLotto = { showLottoDialog = false },
        onCloseFortune = { showFortuneDialog = false },
        onCloseFortuneAlready = { showFortuneAlreadyDialog = false },
        onCloseNoMoney = { showNoMoneyDialog = false }
    )
}
