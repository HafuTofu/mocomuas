package com.example.uas_mocom.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas_mocom.data.MatchEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(matches: List<MatchEntity>, onClearHistory: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Header with clear action
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(PrimaryBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.History,
                            contentDescription = null,
                            tint = TextWhite,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Match History",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        "${matches.size} Matches Played",
                        fontSize = 14.sp,
                        color = TextGray,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(0.dp, 12.dp)
                    )
                    if (matches.isNotEmpty()) {
                        TextButton(onClick = onClearHistory, contentPadding = PaddingValues(0.dp)) {
                            Text(
                                "Clear", 
                                color = AccentRed,
                                fontSize = 14.sp, 
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (matches.isEmpty()) {
            Text(
                "No matches yet. Finish a game to see it here.",
                color = TextGray,
                fontSize = 14.sp
            )
        } else {
            matches.forEach { match ->
                HistoryMatchCard(match)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun HistoryMatchCard(match: MatchEntity) {
    val homeWins = match.homeScore > match.guestScore
    val guestWins = match.guestScore > match.homeScore
    val isDraw = match.homeScore == match.guestScore
    val formattedDate = formatDate(match.timestamp)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Date
            Text(
                formattedDate,
                fontSize = 11.sp,
                color = TextGray,
                letterSpacing = 0.5.sp
            )
            
            Spacer(modifier = Modifier.height(5.dp))
            
            // Match Result
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home Team with result tag
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = when {
                            isDraw -> ""
                            homeWins -> "WIN"
                            else -> "LOSE"
                        },
                        color = when {
                            isDraw -> Color.Transparent
                            homeWins -> PrimaryBlue
                            else -> AccentRed
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (homeWins) {
                            Icon(
                                Icons.Default.EmojiEvents,
                                contentDescription = "Winner",
                                tint = AccentYellow,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            match.homeTeam,
                            fontSize = 14.sp,
                            fontWeight = if (homeWins) FontWeight.Bold else FontWeight.Medium,
                            color = if (homeWins) TextWhite else TextGray,
                            maxLines = 1
                        )
                    }
                }

                // Score with optional draw tag
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isDraw) "DRAW" else " ",
                        color = if (isDraw) TextGray else Color.Transparent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            match.homeScore.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (homeWins) PrimaryBlue else TextWhite
                        )
                        Text(
                            " : ",
                            fontSize = 20.sp,
                            color = TextGray
                        )
                        Text(
                            match.guestScore.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (guestWins) PrimaryBlue else TextWhite
                        )
                    }
                }

                // Guest Team with result tag
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = when {
                            isDraw -> ""
                            guestWins -> "WIN"
                            else -> "LOSE"
                        },
                        color = when {
                            isDraw -> Color.Transparent
                            guestWins -> PrimaryBlue
                            else -> AccentRed
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            match.guestTeam,
                            fontSize = 14.sp,
                            fontWeight = if (guestWins) FontWeight.Bold else FontWeight.Medium,
                            color = if (guestWins) TextWhite else TextGray,
                            maxLines = 1
                        )
                        if (guestWins) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                Icons.Default.EmojiEvents,
                                contentDescription = "Winner",
                                tint = AccentYellow,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(Date(timestamp))
}
