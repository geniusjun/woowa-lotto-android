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
import com.geniusjun.lotto.model.LottoPick

@Composable
fun LottoDialogs(
    showLotto: Boolean,
    showFortune: Boolean,
    showFortuneAlready: Boolean,
    showNoMoney: Boolean,
    thisWeekNumbers: List<Int>,
    myPick: LottoPick?,
    onCloseLotto: () -> Unit,
    onCloseFortune: () -> Unit,
    onCloseFortuneAlready: () -> Unit,
    onCloseNoMoney: () -> Unit
) {
    if (showLotto && myPick != null) {
        LottoResultDialog(
            thisWeekNumbers = thisWeekNumbers,
            myPick = myPick,
            onDismiss = onCloseLotto
        )
    }

    if (showFortune) {
        FortuneDialog(
            fortuneText = "예상치 못한 행운이 찾아올 것입니다 ✨",
            fortuneTag = "행운",
            onDismiss = onCloseFortune
        )
    }

    if (showFortuneAlready) {
        FortuneAlreadySeenDialog(onDismiss = onCloseFortuneAlready)
    }

    if (showNoMoney) {
        NoMoneyDialog(onDismiss = onCloseNoMoney)
    }
}

@Composable
fun LottoResultDialog(
    thisWeekNumbers: List<Int>,
    myPick: LottoPick,
    onDismiss: () -> Unit
) {
    // 이번 주 번호를 set으로 만들어서 일치 여부만 빠르게 보게끔 설계
    val winningSet = remember(thisWeekNumbers) { thisWeekNumbers.toSet() }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { LottoResultTitle() },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                WinningNumbersSection(thisWeekNumbers)
                MyNumbersSection(
                    myPick = myPick,
                    winningSet = winningSet
                )
                LottoResultLegend()
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
    Text("🎟 로또 추첨 결과", fontWeight = FontWeight.Bold)
}

@Composable
private fun WinningNumbersSection(numbers: List<Int>) {
    Text(text = "이번 주 당첨 번호", color = Color.Gray, fontSize = 13.sp)

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        numbers.chunked(4).forEach { row ->
            CenteredNumberRow {
                row.forEach { num ->
                    NumberBall(
                        number = num,
                        background = Color(0xFFE0E0E0),
                        contentColor = Color.Black
                    )
                }
            }
        }
    }
}

// 내가 구매한 번호(일반 6개 + 보너스)
@Composable
private fun MyNumbersSection(
    myPick: LottoPick,
    winningSet: Set<Int>
) {
    Text(text = "내가 구매한 번호", color = Color.Gray, fontSize = 13.sp)

    // 1) 일반 번호 6개
    PurchasedNumbers(
        numbers = myPick.numbers,
        winningSet = winningSet
    )

    // 2) 보너스 따로
    BonusNumber(
        bonus = myPick.bonus,
        isMatched = myPick.bonus in winningSet
    )
}

@Composable
private fun PurchasedNumbers(
    numbers: List<Int>,
    winningSet: Set<Int>
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        numbers.chunked(3).forEach { row ->
            CenteredNumberRow {
                row.forEach { num ->
                    val matched = num in winningSet
                    NumberBall(
                        number = num,
                        background = if (matched) Color(0xFFF6A94E) else Color(0xFF27C1A3),
                        contentColor = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun BonusNumber(
    bonus: Int,
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
            number = bonus,
            background = if (isMatched) Color(0xFFDB5A5A) else Color(0xFF27C1A3),
            contentColor = Color.White
        )
    }
}

@Composable
private fun LottoResultLegend() {
    Text(
        text = "주황색으로 표시된 번호가 일치한 번호입니다.\n보너스 번호는 별도로 표시됩니다.",
        fontSize = 11.sp,
        color = Color.Gray
    )
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
