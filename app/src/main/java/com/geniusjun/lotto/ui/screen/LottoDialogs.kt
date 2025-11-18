package com.geniusjun.lotto.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geniusjun.lotto.data.model.LottoDrawResponse
import com.geniusjun.lotto.data.model.MemberRankResult
import com.geniusjun.lotto.ui.theme.LottoColors
import com.geniusjun.lotto.ui.theme.MintPrimary

@Composable
fun LottoDialogs(
    dialogState: DialogState,
    onCloseAll: () -> Unit
) {
    dialogState.lottoDrawResult?.let { result ->
        if (dialogState.showLotto) {
            LottoResultDialog(
                drawResult = result,
                onDismiss = onCloseAll
            )
        }
    }

    dialogState.fortuneText?.let { fortune ->
        if (dialogState.showFortune) {
            FortuneDialog(
                fortuneText = fortune,
                onDismiss = onCloseAll
            )
        }
    }

    if (dialogState.showNoMoney) {
        NoMoneyDialog(onDismiss = onCloseAll)
    }

    if (dialogState.showRanking) {
        RankingDialog(
            top3 = dialogState.top3Ranks,
            myRank = dialogState.myRank,
            onDismiss = onCloseAll
        )
    }
}

@Composable
fun LottoResultDialog(
    drawResult: LottoDrawResponse,
    onDismiss: () -> Unit
) {
    val matchedSet = remember(drawResult.matchedNumbers) { drawResult.matchedNumbers.toSet() }
    val bonusNumber = drawResult.bonusNumber
    val isBonusMatched = drawResult.bonusMatched

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { LottoResultTitle() },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // 등수 및 보상 표시
                RankAndRewardSection(
                    rank = drawResult.rank,
                    reward = drawResult.reward
                )
                
                HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                
                // 당첨 번호 (6개 + 보너스 1개)
                WinningNumbersSection(
                    numbers = drawResult.winningNumbers,
                    bonusNumber = bonusNumber,
                    matchedSet = matchedSet,
                    isBonusMatched = isBonusMatched
                )
                
                // 내가 구매한 번호 (6개만)
                MyNumbersSection(
                    myNumbers = drawResult.myNumbers,
                    bonusNumber = bonusNumber,
                    matchedSet = matchedSet,
                    bonusMatched = isBonusMatched
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("닫기") }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White
    )
}

@Composable
private fun LottoResultTitle() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("🎟 로또 추첨 결과", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }
}

@Composable
private fun RankAndRewardSection(
    rank: String,
    reward: Long
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = rank,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MintPrimary
        )
        
        if (reward > 0) {
            Text(
                text = "보상: ₩ ${String.format("%,d", reward)}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = LottoColors.Reward
            )
        } else {
            Text(
                text = "다음 기회에...",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun WinningNumbersSection(
    numbers: List<Int>,
    bonusNumber: Int,
    matchedSet: Set<Int>,
    isBonusMatched: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(text = "이번 주 당첨 번호", color = Color.Gray, fontSize = 13.sp)
        
        // 당첨 번호 6개
        NumberBallsGrid(
            numbers = numbers,
            chunkSize = 4,
            isMatched = { num -> num in matchedSet },
            matchedColor = LottoColors.WinningNumberMatched,
            defaultColor = LottoColors.WinningNumberDefault,
            matchedTextColor = Color.White,
            defaultTextColor = Color.Black
        )
        
        // 보너스 번호 (일치 시 빨간색)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "보너스", color = Color.Gray, fontSize = 12.sp)
            NumberBall(
                number = bonusNumber,
                background = if (isBonusMatched) LottoColors.BonusMatched else LottoColors.WinningNumberDefault,
                contentColor = if (isBonusMatched) Color.White else Color.Black
            )
        }
    }
}

@Composable
private fun MyNumbersSection(
    myNumbers: List<Int>,
    bonusNumber: Int,
    matchedSet: Set<Int>,
    bonusMatched: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(text = "내가 구매한 번호", color = Color.Gray, fontSize = 13.sp)
        
        // 사용자가 구매한 번호 6개 (보너스 번호 일치 시 빨간색)
        NumberBallsGridWithBonus(
            numbers = myNumbers,
            chunkSize = 3,
            matchedSet = matchedSet,
            bonusNumber = bonusNumber,
            bonusMatched = bonusMatched
        )
        
        // 보너스 번호 일치 여부 표시
        if (bonusMatched) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "✨ 보너스 번호 일치!",
                    fontSize = 12.sp,
                    color = LottoColors.Reward,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun NumberBallsGrid(
    numbers: List<Int>,
    chunkSize: Int,
    isMatched: (Int) -> Boolean,
    matchedColor: Color,
    defaultColor: Color,
    matchedTextColor: Color,
    defaultTextColor: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        numbers.chunked(chunkSize).forEach { row ->
            CenteredNumberRow {
                row.forEach { num ->
                    val matched = isMatched(num)
                    NumberBall(
                        number = num,
                        background = if (matched) matchedColor else defaultColor,
                        contentColor = if (matched) matchedTextColor else defaultTextColor
                    )
                }
            }
        }
    }
}

@Composable
private fun NumberBallsGridWithBonus(
    numbers: List<Int>,
    chunkSize: Int,
    matchedSet: Set<Int>,
    bonusNumber: Int,
    bonusMatched: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        numbers.chunked(chunkSize).forEach { row ->
            CenteredNumberRow {
                row.forEach { num ->
                    val isRegularMatched = num in matchedSet
                    val isBonusMatched = bonusMatched && num == bonusNumber
                    val matched = isRegularMatched || isBonusMatched
                    
                    val backgroundColor = when {
                        isBonusMatched -> LottoColors.BonusMatched
                        isRegularMatched -> LottoColors.MyNumberMatched
                        else -> LottoColors.MyNumberDefault
                    }
                    
                    NumberBall(
                        number = num,
                        background = backgroundColor,
                        contentColor = Color.White
                    )
                }
            }
        }
    }
}

// 공 가운데 정렬 공통 Row
@Composable
private fun CenteredNumberRow(
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

// 공 하나
@Composable
private fun NumberBall(
    number: Int,
    background: Color,
    contentColor: Color
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(background, RoundedCornerShape(999.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = number.toString(), color = contentColor, fontSize = 15.sp)
    }
}


@Composable
fun FortuneDialog(
    fortuneText: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text(text = "🔮 오늘의 운세", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = fortuneText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF166E5F)
                )
                HorizontalDivider()
                Text(
                    text = "오늘의 운세는 하루에 한번씩 업데이트 됩니다!",
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
private fun NoMoneyDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("확인")
            }
        },
        title = { Text("잔액이 부족합니다") },
        text = { Text("로또를 구매하려면 최소 1,000원이 필요합니다.") },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun RankingDialog(
    top3: List<MemberRankResult>,
    myRank: MemberRankResult?,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { RankingTitle() },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                Top3Section(top3)
                
                if (myRank != null) {
                    HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                    MyRankSection(myRank)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("닫기") }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White
    )
}

@Composable
private fun RankingTitle() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("🏆 랭킹", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }
}

@Composable
private fun Top3Section(top3: List<MemberRankResult>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "TOP 3",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        
        if (top3.isEmpty()) {
            Text(
                text = "랭킹 데이터가 없습니다.",
                fontSize = 13.sp,
                color = Color.Gray
            )
        } else {
            top3.forEachIndexed { index, rank ->
                RankItem(
                    rank = rank,
                    position = index + 1
                )
            }
        }
    }
}

@Composable
private fun RankItem(
    rank: MemberRankResult,
    position: Int
) {
    val (backgroundColor, textColor) = when (position) {
        1 -> Color(0xFFFFD700) to Color(0xFF6B4D15) // 금색
        2 -> Color(0xFFC0C0C0) to Color(0xFF4A4A4A) // 은색
        3 -> Color(0xFFCD7F32) to Color.White       // 동색
        else -> Color(0xFFE0E0E0) to Color.Black
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "${position}등",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Text(
                    text = rank.nickname,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )
            }
            Text(
                text = "₩ ${String.format("%,d", rank.balance)}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

@Composable
private fun MyRankSection(myRank: MemberRankResult) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "나의 등수는 ${myRank.rank}등",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MintPrimary
        )
        Text(
            text = "보유 금액: ₩ ${String.format("%,d", myRank.balance)}",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}
