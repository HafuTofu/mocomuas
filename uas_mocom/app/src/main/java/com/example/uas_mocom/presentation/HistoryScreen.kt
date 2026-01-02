package com.example.uas_mocom.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas_mocom.data.MatchEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(matches: List<MatchEntity>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        // Header
        Text(
            "Match History",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )
        
        Text(
            "${matches.size} matches played",
            fontSize = 14.sp,
            color = TextGray
        )

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
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Match Result
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home Team
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
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
                
                // Score
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
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
                        color = if (guestWins) AccentRed else TextWhite
                    )
                }
                
                // Guest Team
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.weight(1f)
                ) {
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

private fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(Date(timestamp))
}
