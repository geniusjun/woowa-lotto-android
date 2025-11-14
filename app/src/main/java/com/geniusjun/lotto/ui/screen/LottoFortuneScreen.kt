package com.geniusjun.lotto.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Arrangement
import com.geniusjun.lotto.model.LottoUiState
import com.geniusjun.lotto.ui.components.AppCard
import com.geniusjun.lotto.ui.theme.FortuneBg
import com.geniusjun.lotto.ui.theme.FortuneText
import com.geniusjun.lotto.ui.theme.MintBackground
import com.geniusjun.lotto.ui.theme.MintPrimary

@Composable
fun LottoFortuneScreen(
    uiState: LottoUiState,
    onClickBuy: () -> Unit,
    onClickFortune: () -> Unit,
    onLogout: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MintBackground
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LogoutButton(
                onLogout = onLogout,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TitleSection()
                BalanceCard(uiState.balance)
                LottoNumbersCard(uiState.thisWeekNumbers)
                TipBanner("매일 하루에 한 번 보너스 금액이 지급됩니다")
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
private fun LogoutButton(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onLogout,
        modifier = modifier
    ) {
        Text(
            text = "로그아웃",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun TitleSection() {
    Column {
        Text(
            text = "🧪 로또 운세실험실",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A6E63)
        )
        Text(
            text = "운을 데이터로 실험합니다",
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

@Composable
fun LottoNumbersCard(numbers: List<Int>) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Text(text = "이번 주 로또 번호", color = Color.Gray, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(12.dp))

        LottoNumberRows(numbers)
    }
}
@Composable
private fun LottoNumberRows(numbers: List<Int>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        numbers.chunked(3).forEach { row ->
            LottoNumberRow(row)
        }
    }
}
@Composable
private fun LottoNumberRow(row: List<Int>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            12.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        row.forEach { num ->
            LottoNumberBall(number = num)
        }
    }
}

@Composable
private fun LottoNumberBall(number: Int) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .background(MintPrimary, RoundedCornerShape(999.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = number.toString(), color = Color.White, fontSize = 16.sp)
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
        Button(
            onClick = onBuyLotto,
            colors = ButtonDefaults.buttonColors(
                containerColor = MintPrimary,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .weight(1f)
                .height(100.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "🎟 랜덤 로또 구매",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "(₩1,000)",
                    fontSize = 13.sp,
                )
            }
        }

        Button(
            onClick = onShowFortune,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF6D486),
                contentColor = Color(0xFF6B4D15)
            ),
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .weight(1f)
                .height(100.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "🔮 오늘의 운세",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

