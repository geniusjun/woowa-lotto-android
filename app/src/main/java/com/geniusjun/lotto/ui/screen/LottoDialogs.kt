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

    if (dialogState.showFortune) {
        FortuneDialog(
            fortuneText = "예상치 못한 행운이 찾아올 것입니다 ✨",
            fortuneTag = "행운",
            onDismiss = onCloseAll
        )
    }

    if (dialogState.showFortuneAlready) {
        FortuneAlreadySeenDialog(onDismiss = onCloseAll)
    }

    if (dialogState.showNoMoney) {
        NoMoneyDialog(onDismiss = onCloseAll)
    }
}

@Composable
fun LottoResultDialog(
    drawResult: LottoDrawResponse,
    onDismiss: () -> Unit
) {
    val matchedSet = remember(drawResult.matchedNumbers) { drawResult.matchedNumbers.toSet() }

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
                
                // 당첨 번호 (일치하는 번호는 색깔 표시)
                WinningNumbersSection(
                    numbers = drawResult.winningNumbers,
                    matchedSet = matchedSet
                )
                
                // 내가 구매한 번호
                MyNumbersSection(
                    myNumbers = drawResult.myNumbers,
                    bonusNumber = drawResult.bonusNumber,
                    matchedSet = matchedSet,
                    bonusMatched = drawResult.bonusMatched
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
    matchedSet: Set<Int>
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(text = "이번 주 당첨 번호", color = Color.Gray, fontSize = 13.sp)
        NumberBallsGrid(
            numbers = numbers,
            chunkSize = 4,
            isMatched = { num -> num in matchedSet },
            matchedColor = LottoColors.WinningNumberMatched,
            defaultColor = LottoColors.WinningNumberDefault,
            matchedTextColor = Color.White,
            defaultTextColor = Color.Black
        )
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
        
        NumberBallsGrid(
            numbers = myNumbers,
            chunkSize = 3,
            isMatched = { num -> num in matchedSet },
            matchedColor = LottoColors.MyNumberMatched,
            defaultColor = LottoColors.MyNumberDefault,
            matchedTextColor = Color.White,
            defaultTextColor = Color.White
        )
        
        BonusNumberRow(
            number = bonusNumber,
            isMatched = bonusMatched
        )
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
private fun BonusNumberRow(
    number: Int,
    isMatched: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "보너스", color = Color.Gray, fontSize = 12.sp)
        NumberBall(
            number = number,
            background = if (isMatched) LottoColors.BonusMatched else LottoColors.BonusDefault,
            contentColor = Color.White
        )
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
    fortuneTag: String,
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
                AssistChip(
                    onClick = { /* no-op */ },
                    label = { Text(fortuneTag) }
                )
                HorizontalDivider()
                // 너가 쓰던 문구 그대로 유지
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

@Composable
private fun FortuneAlreadySeenDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("확인")
            }
        },
        title = { Text("오늘의 운세는 이미 확인했어요") },
        text = {
            Text("하루에 한 번만 운세를 볼 수 있어요.\n내일 다시 시도해 주세요.")
        },
        shape = RoundedCornerShape(20.dp)
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
