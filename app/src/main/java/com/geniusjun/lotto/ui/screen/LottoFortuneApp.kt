package com.geniusjun.lotto.ui.screen

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.geniusjun.lotto.auth.GoogleSignInManager
import com.geniusjun.lotto.data.api.LottoApi
import com.geniusjun.lotto.data.network.RetrofitClient
import com.geniusjun.lotto.data.network.TokenProvider
import com.geniusjun.lotto.data.repository.AuthRepository
import com.geniusjun.lotto.data.repository.LottoRepository
import com.geniusjun.lotto.model.LottoPick
import com.geniusjun.lotto.model.LottoUiState
import com.geniusjun.lotto.util.generateLottoNumbers
import com.geniusjun.lotto.util.loadLastFortuneDate
import com.geniusjun.lotto.util.saveLastFortuneDate
import com.geniusjun.lotto.util.todayString

@Composable
fun LottoFortuneApp(
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val tokenProvider = remember { TokenProvider(context) }

    // 의존성 초기화
    val signInManager = remember {
        GoogleSignInManager(context, tokenProvider)
    }
    
    val lottoRepository = remember {
        // AuthRepository 먼저 생성 (토큰 갱신용)
        val authApi = RetrofitClient.createAuthApi(tokenProvider)
        val authRepository = AuthRepository(authApi, tokenProvider)
        
        // 다른 API는 AuthRepository를 포함하여 생성 (자동 토큰 갱신 가능)
        val lottoApi = RetrofitClient.createApi<LottoApi>(tokenProvider, authRepository)
        LottoRepository(lottoApi)
    }
    
    // ViewModel 초기화
    val lottoViewModel: LottoViewModel = viewModel {
        LottoViewModel(lottoRepository)
    }
    
    val winningNumbers by lottoViewModel.winningNumbers.collectAsState()
    val bonusNumber by lottoViewModel.bonusNumber.collectAsState()

    // 로그인 완료 후 당첨 번호 로드
    LaunchedEffect(Unit) {
        val accessToken = tokenProvider.getAccessToken()
        if (accessToken != null) {
            lottoViewModel.loadWinningNumbers()
        }
    }

    // 다이얼로그 열림 상태
    var showLottoDialog by remember { mutableStateOf(false) }
    var showFortuneDialog by remember { mutableStateOf(false) }
    var showNoMoneyDialog by remember { mutableStateOf(false) }
    var showFortuneAlreadyDialog by remember { mutableStateOf(false) }

    // 내가 방금 산 로또
    var myLatestPick by remember { mutableStateOf<LottoPick?>(null) }

    // 하드코딩된 값들 (추후 API로 대체 예정)
    val balance = 50_000
    val ticketPrice = 1_000
    var currentBalance by remember { mutableIntStateOf(balance) }

    // 오늘 날짜 문자열
    val today = remember { todayString() }
    var lastFortuneDate by remember { mutableStateOf(loadLastFortuneDate(context)) }
    
    // 로그아웃 ViewModel
    val logoutViewModel: LogoutViewModel = viewModel {
        LogoutViewModel(signInManager)
    }

    // UI 상태 구성 (API에서 받은 당첨 번호 사용)
    val uiState = remember(winningNumbers, bonusNumber, currentBalance) {
        LottoUiState(
            balance = currentBalance,
            winningNumbers = winningNumbers,
            bonusNumber = bonusNumber
        )
    }

    LottoFortuneScreen(
        uiState = uiState,
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
            if (lastFortuneDate == today) {
                showFortuneAlreadyDialog = true
            } else {
                showFortuneDialog = true
                lastFortuneDate = today
                saveLastFortuneDate(context, today)
            }
        },
        onLogout = {
            logoutViewModel.logout(onScreenTransition = onLogout)
        }
    )

    // 다이얼로그 모음
    LottoDialogs(
        showLotto = showLottoDialog,
        showFortune = showFortuneDialog,
        showFortuneAlready = showFortuneAlreadyDialog,
        showNoMoney = showNoMoneyDialog,
        thisWeekNumbers = uiState.winningNumbers,
        myPick = myLatestPick,
        onCloseLotto = { showLottoDialog = false },
        onCloseFortune = { showFortuneDialog = false },
        onCloseFortuneAlready = { showFortuneAlreadyDialog = false },
        onCloseNoMoney = { showNoMoneyDialog = false }
    )
}
