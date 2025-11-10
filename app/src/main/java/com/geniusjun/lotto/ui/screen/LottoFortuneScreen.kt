package com.geniusjun.lotto.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geniusjun.lotto.model.LottoUiState
import com.geniusjun.lotto.ui.components.AppCard
import com.geniusjun.lotto.ui.theme.FortuneBg
import com.geniusjun.lotto.ui.theme.FortuneText
import com.geniusjun.lotto.ui.theme.MintBackground
import com.geniusjun.lotto.ui.theme.MintPrimary

@Composable
fun LottoFortuneApp() {
    // 화면에서 쓸 상태
    var showLottoDialog by remember { mutableStateOf(false) }
    var showFortuneDialog by remember { mutableStateOf(false) }

    // 임시 데이터
    val uiState = remember {
        LottoUiState(
            balance = 60_000,
            thisWeekNumbers = listOf(7, 12, 23, 31, 38, 42)
        )
    }

    // 화면 그리기
    LottoFortuneScreen(
        uiState = uiState,
        onClickBuy = { showLottoDialog = true },
        onClickFortune = { showFortuneDialog = true }
    )

    // 다이얼로그 분리
    if (showLottoDialog) {
        LottoResultDialog(
            thisWeekNumbers = uiState.thisWeekNumbers,
            onDismiss = { showLottoDialog = false }
        )
    }

    if (showFortuneDialog) {
        FortuneDialog(
            fortuneText = "예상치 못한 행운이 찾아올 것입니다 ✨",
            fortuneTag = "행운",
            onDismiss = { showFortuneDialog = false }
        )
    }
}

@Composable
fun LottoFortuneScreen(
    uiState: LottoUiState,
    onClickBuy: () -> Unit,
    onClickFortune: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MintBackground
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TitleSection(
                    title = "🧪 로또 운세실험실",
                    subtitle = "운을 데이터로 실험합니다"
                )
                BalanceCard(uiState.balance)
                LottoNumbersCard(uiState.thisWeekNumbers)
                TipBanner("매일 하루에 한 번 보너스 금액이 지급됩니다")
                Spacer(modifier = Modifier.weight(1f))
            }

            BottomButtons(
                onBuyLotto = onClickBuy,
                onShowFortune = onClickFortune,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
            )
        }
    }
}

@Composable
fun TitleSection(title: String, subtitle: String) {
    Column {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A6E63)
        )
        Text(
            text = subtitle,
            fontSize = 14.sp,
            color = Color(0xFF4F7E78)
        )
    }
}

@Composable
fun BalanceCard(balance: Int) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Text(text = "현재 보유 금액", color = Color.Gray, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "₩ %,d".format(balance),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MintPrimary
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LottoNumbersCard(numbers: List<Int>) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Text(text = "이번 주 로또 번호", color = Color.Gray, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(12.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            numbers.forEach { num ->
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(MintPrimary, RoundedCornerShape(999.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = num.toString(), color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun TipBanner(text: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FortuneBg),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "💡 $text", color = FortuneText)
        }
    }
}

@Composable
fun BottomButtons(
    onBuyLotto: () -> Unit,
    onShowFortune: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 왼쪽 버튼
        Button(
            onClick = onBuyLotto,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF27C1A3),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .weight(1f)              // ← 양쪽 균등
                .height(100.dp)           // ← 키운 높이
        ) {
            Text(text = "🎟 랜덤 로또 구매 (₩1,000)")
        }

        // 오른쪽 버튼
        Button(
            onClick = onShowFortune,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF6D486),
                contentColor = Color(0xFF6B4D15)
            ),
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .weight(1f)
                .height(100.dp)
        ) {
            Text(text = "🔮 오늘의 운세")
        }
    }
}


@Composable
fun LottoResultDialog(
    thisWeekNumbers: List<Int>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            Text(text = "🎟 로또 추첨 결과", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text(text = "이번 주 당첨 번호", color = Color.Gray, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    thisWeekNumbers.forEach { num ->
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .background(Color(0xFFE0E0E0), RoundedCornerShape(999.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = num.toString(), fontSize = 15.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "매주 새로운 당첨 번호로 업데이트됩니다.",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("닫기")
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White
    )
}

@Composable
fun FortuneDialog(
    fortuneText: String,
    fortuneTag: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            Text(text = "🔮 오늘의 운세", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = fortuneText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF166E5F)
                )
                AssistChip(
                    onClick = { },
                    label = { Text(fortuneTag) }
                )
                Divider()
                Text(
                    text = "오늘은 이미 운세를 보셨습니다.\n내일 다시 확인해보세요!",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("닫기")
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White
    )
}
