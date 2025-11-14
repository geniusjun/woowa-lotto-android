package com.geniusjun.lotto.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geniusjun.lotto.ui.theme.MintBackground
import com.geniusjun.lotto.ui.theme.MintPrimary

@Composable
fun OnboardingScreen(
    onGoogleSignInClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 80.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // App Header
            AppHeader()
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Welcome Card
            WelcomeCard()
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Google Sign-In Button
            GoogleSignInButton(onClick = onGoogleSignInClick)
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Features List
            FeaturesList()
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Legal Disclaimer
            LegalDisclaimer()
        }
    }
}

@Composable
private fun AppHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // App Icon
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MintPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🧪",
                fontSize = 56.sp
            )
        }
        
        // App Title
        Text(
            text = "로또 운세실험실",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MintPrimary
        )
        
        // Tagline
        Text(
            text = "운을 데이터로 실험합니다",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun WelcomeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MintBackground.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "✨ 환영합니다!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MintPrimary
            )
            Text(
                text = "매일 행운을 실험하고",
                fontSize = 15.sp,
                color = Color.Black,
                lineHeight = 22.sp
            )
            Text(
                text = "나만의 운세를 확인해보세요",
                fontSize = 15.sp,
                color = Color.Black,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun GoogleSignInButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        border = BorderStroke(
            width = 1.5.dp,
            color = Color.Gray.copy(alpha = 0.3f)
        ),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Google G Logo (simplified colorful representation)
            Box(
                modifier = Modifier
                    .size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                // Multi-color G logo approximation
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = CircleShape,
                    color = Color.White
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "G",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4285F4)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Google로 시작하기",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun FeaturesList() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        FeatureItem(
            icon = "🎁",
            text = "매일 10,000원 보너스 지급"
        )
        FeatureItem(
            icon = "🎟",
            text = "하루 1,000원으로 로또 구매"
        )
        FeatureItem(
            icon = "🔮",
            text = "매일 새로운 운세 확인"
        )
    }
}

@Composable
private fun FeatureItem(icon: String, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = icon,
            fontSize = 28.sp,
            modifier = Modifier.padding(end = 16.dp)
        )
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

@Composable
private fun LegalDisclaimer() {
    Text(
        text = "로그인하면 서비스 이용약관 및 개인정보 처리방침에 동의하게 됩니다",
        fontSize = 11.sp,
        color = Color.Gray.copy(alpha = 0.7f),
        modifier = Modifier.padding(horizontal = 16.dp),
        lineHeight = 16.sp
    )
}

