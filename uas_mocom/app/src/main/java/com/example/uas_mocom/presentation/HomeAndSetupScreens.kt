package com.example.uas_mocom.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas_mocom.data.MatchEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(matches: List<MatchEntity>, onViewHistory: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(PrimaryBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Scoreboard,
                    contentDescription = null,
                    tint = TextWhite,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Scoreboard",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Recent Matches Section
        Text(
            "RECENT MATCHES",
            fontSize = 12.sp,
            color = TextGray,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (matches.isEmpty()) {
            Text(
                "No matches yet. Start a game to see results here.",
                color = TextGray,
                fontSize = 14.sp
            )
        } else {
            matches.take(5).forEach { match ->
                MatchCard(match)
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (matches.size > 5) {
                TextButton(
                    onClick = onViewHistory,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "View More",
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun MatchCard(match: MatchEntity) {
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
            
            Spacer(modifier = Modifier.height(10.dp))
            
            // Match Row
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

@Composable
fun SetupScreen(onBack: () -> Unit, onStartMatch: (String, String) -> Unit) {
    var homeName by remember { mutableStateOf("Home") }
    var guestName by remember { mutableStateOf("Guest") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            IconButton(onClick = onBack) { Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite) }
            Text("New Game Setup", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextWhite, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.size(48.dp))
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        TeamInput("Home Team", Icons.Default.Home, homeName) { homeName = it }
        Spacer(modifier = Modifier.height(16.dp))
        TeamInput("Guest Team", Icons.Default.AirplanemodeActive, guestName) { guestName = it }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { onStartMatch(homeName, guestName) }, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue), shape = RoundedCornerShape(12.dp)) {
            Icon(Icons.Default.PlayArrow, contentDescription = null); Spacer(modifier = Modifier.width(8.dp))
            Text("Start Match", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(Date(timestamp))
}