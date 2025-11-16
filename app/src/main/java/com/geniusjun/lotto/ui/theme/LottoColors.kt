package com.geniusjun.lotto.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * 로또 앱에서 사용하는 색상 상수
 */
object LottoColors {
    // 당첨 번호 관련
    val WinningNumberMatched = Color(0xFFF6A94E)  // 주황색 (일치하는 번호)
    val WinningNumberDefault = Color(0xFFE0E0E0)  // 회색 (일치하지 않는 번호)
    
    // 내 번호 관련
    val MyNumberMatched = Color(0xFFF6A94E)       // 주황색 (일치하는 번호)
    val MyNumberDefault = Color(0xFF27C1A3)       // 민트색 (일치하지 않는 번호)
    
    // 보너스 번호
    val BonusMatched = Color(0xFFDB5A5A)          // 빨간색 (일치한 보너스)
    val BonusDefault = Color(0xFF27C1A3)          // 민트색 (일치하지 않는 보너스)
    
    // 보상
    val Reward = Color(0xFFFF6B35)                // 오렌지색
}

