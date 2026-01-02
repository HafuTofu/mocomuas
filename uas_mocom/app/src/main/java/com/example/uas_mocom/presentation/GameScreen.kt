package com.example.uas_mocom.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas_mocom.viewmodel.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel, onBackToHome: () -> Unit) {
    val homeName by viewModel.homeName.collectAsState()
    val guestName by viewModel.guestName.collectAsState()
    val homeScore by viewModel.homeScore.collectAsState()
    val guestScore by viewModel.guestScore.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        TopBar("SCORE", onBackToHome)

        // Score Cards - Stacked vertically
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TeamScoreCard(
                name = homeName,
                score = homeScore,
                isHome = true,
                onScoreChange = { viewModel.updateHomeScore(it) },
                modifier = Modifier.weight(1f)
            )
            TeamScoreCard(
                name = guestName,
                score = guestScore,
                isHome = false,
                onScoreChange = { viewModel.updateGuestScore(it) },
                modifier = Modifier.weight(1f)
            )
        }

        GameControls(
            onReset = { viewModel.resetGame() },
            onEndMatch = {
                viewModel.saveCurrentMatch()
                viewModel.resetGame()
                onBackToHome()
            }
        )
    }
}

@Composable
private fun TeamScoreCard(
    name: String,
    score: Int,
    isHome: Boolean,
    onScoreChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = if (isHome) PrimaryBlue else AccentRed

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Team info - TOP
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    name.uppercase(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite,
                    letterSpacing = 1.sp,
                    maxLines = 1
                )
            }

            // Score - MIDDLE
            Text(
                score.toString(),
                fontSize = 80.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            // Score buttons - BOTTOM
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledIconButton(
                    onClick = { onScoreChange(-1) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.size(64.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = DarkBackground
                    )
                ) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = "Decrease",
                        tint = TextGray,
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                Button(
                    onClick = { onScoreChange(1) },
                    modifier = Modifier.size(64.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                    contentPadding = PaddingValues(0.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(
                        "+",
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = TextWhite
                    )
                }
            }
        }
    }
}

@Composable
private fun GameControls(
    onReset: () -> Unit,
    onEndMatch: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onReset,
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                brush = androidx.compose.ui.graphics.SolidColor(CardSurface)
            )
        ) {
            Text(
                "RESET",
                color = TextGray,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                letterSpacing = 1.sp
            )
        }
        
        Button(
            onClick = onEndMatch,
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text(
                "END MATCH",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                letterSpacing = 1.sp
            )
        }
    }
}
