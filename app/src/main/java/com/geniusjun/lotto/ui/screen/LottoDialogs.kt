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
import com.geniusjun.lotto.model.LottoPick

@Composable
fun LottoDialogs(
    showLotto: Boolean,
    showFortune: Boolean,
    thisWeekNumbers: List<Int>,
    myPick: LottoPick?,
    onCloseLotto: () -> Unit,
    onCloseFortune: () -> Unit
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
}

@Composable
fun LottoResultDialog(
    thisWeekNumbers: List<Int>,
    myPick: LottoPick,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            Text(text = "🎟 로또 추첨 결과", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

                Text(text = "이번 주 당첨 번호", color = Color.Gray, fontSize = 13.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    thisWeekNumbers.forEach { num ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFE0E0E0), RoundedCornerShape(999.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = num.toString())
                        }
                    }
                }

                Text(text = "내가 구매한 번호", color = Color.Gray, fontSize = 13.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    myPick.numbers.forEach { num ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFF27C1A3), RoundedCornerShape(999.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = num.toString(), color = Color.White)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFF6D486), RoundedCornerShape(999.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = myPick.bonus.toString(), color = Color(0xFF6B4D15))
                    }
                }

                Text(
                    text = "다음 단계에서 일치 번호를 강조합니다.",
                    fontSize = 11.sp,
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
                    onClick = { /* no-op */ },
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
