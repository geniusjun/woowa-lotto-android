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
import com.geniusjun.lotto.model.LottoUiState

@Composable
fun LottoFortuneApp(
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val tokenProvider = remember { TokenProvider(context) }
    val signInManager = remember { GoogleSignInManager(context, tokenProvider) }
    
    // Repository 및 ViewModel 초기화
    val lottoRepository = remember {
        val authApi = RetrofitClient.createAuthApi(tokenProvider)
        val authRepository = AuthRepository(authApi, tokenProvider)
        val lottoApi = RetrofitClient.createApi<LottoApi>(tokenProvider, authRepository)
        LottoRepository(lottoApi)
    }
    
    val lottoViewModel: LottoViewModel = viewModel {
        LottoViewModel(lottoRepository)
    }
    val logoutViewModel: LogoutViewModel = viewModel {
        LogoutViewModel(signInManager)
    }
    
    // 상태 수집
    val winningNumbers by lottoViewModel.winningNumbers.collectAsState()
    val bonusNumber by lottoViewModel.bonusNumber.collectAsState()
    val balance by lottoViewModel.balance.collectAsState()
    
    // 다이얼로그 상태
    var dialogState by remember { mutableStateOf(DialogState()) }

    // 로그인 후 초기 데이터 로드
    LaunchedEffect(Unit) {
        if (tokenProvider.getAccessToken() != null) {
            lottoViewModel.loadInitialData()
        }
    }

    // UI 상태
    val uiState = remember(winningNumbers, bonusNumber, balance) {
        LottoUiState(
            balance = balance,
            winningNumbers = winningNumbers,
            bonusNumber = bonusNumber
        )
    }

    // 이벤트 핸들러
    val handleBuyLotto = {
        lottoViewModel.drawLotto(
            onSuccess = { result -> dialogState = dialogState.showLottoDialog(result) },
            onFailure = { dialogState = dialogState.showNoMoneyDialog() }
        )
    }
    
    val handleShowFortune = {
        lottoViewModel.loadFortune(
            onSuccess = { fortune ->
                dialogState = dialogState.showFortuneDialog(fortune)
            },
            onFailure = {
                // 에러 표시
            }
        )
    }
    
    val handleShowRanking = {
        lottoViewModel.loadRanking(
            onSuccess = { top3, myRank ->
                dialogState = dialogState.showRankingDialog(top3, myRank)
            },
            onFailure = {
                // 에러 표시
            }
        )
    }
    
    val handleCloseDialogs = {
        dialogState = dialogState.closeAll()
    }

    // 화면 구성
    LottoFortuneScreen(
        uiState = uiState,
        onClickBuy = handleBuyLotto,
        onClickFortune = handleShowFortune,
        onClickRanking = handleShowRanking,
        onLogout = { logoutViewModel.logout(onScreenTransition = onLogout) }
    )

    // 다이얼로그
    LottoDialogs(
        dialogState = dialogState,
        onCloseAll = handleCloseDialogs
    )
}
