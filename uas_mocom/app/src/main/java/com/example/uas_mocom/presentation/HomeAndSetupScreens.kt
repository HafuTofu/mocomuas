package com.example.uas_mocom.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.SportsBasketball
import androidx.compose.material.icons.rounded.SportsSoccer
import androidx.compose.material.icons.rounded.SportsTennis
import androidx.compose.material.icons.rounded.SportsVolleyball
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas_mocom.model.SportType

@Composable
fun HomeScreen(onNavigateToSetup: (SportType) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp).verticalScroll(rememberScrollState())) {
        Row(modifier = Modifier.fillMaxWidth().padding(top = 48.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(
                    PrimaryBlue
                ), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Scoreboard, contentDescription = null, tint = TextWhite, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("Scoreboard", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            }
            IconButton(onClick = {}) { Icon(Icons.Default.Settings, contentDescription = null, tint = TextGray) }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Text("WELCOME BACK", fontSize = 12.sp, color = TextGray, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Start New Game", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Text("View all sports", fontSize = 12.sp, color = PrimaryBlue, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp).clickable { })
        }
        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            HomeSportCard(
                "Basketball",
                "5v5, 3v3",
                Icons.Rounded.SportsBasketball,
                Brush.radialGradient(
                    colors = listOf(Color(0xFFEA580C), Color(0xFF000000)),
                    center = Offset(100f, 100f),
                    radius = 300f
                ),
                Modifier.weight(1f)
            ) {
                onNavigateToSetup(
                    SportType.BASKETBALL
                )
            }
            HomeSportCard(
                "Volleyball",
                "Indoor, Beach",
                Icons.Rounded.SportsVolleyball,
                Brush.radialGradient(
                    colors = listOf(Color(0xFFCA8A04), Color(0xFF000000)),
                    center = Offset(100f, 100f),
                    radius = 300f
                ),
                Modifier.weight(1f)
            ) {
                onNavigateToSetup(
                    SportType.VOLLEYBALL
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            HomeSportCard(
                "Soccer",
                "11v11, Futsal",
                Icons.Rounded.SportsSoccer,
                Brush.radialGradient(
                    colors = listOf(Color(0xFF166534), Color(0xFF000000)),
                    center = Offset(100f, 100f),
                    radius = 300f
                ),
                Modifier.weight(1f)
            ) {
                onNavigateToSetup(
                    SportType.SOCCER
                )
            }
            HomeSportCard(
                "Badminton",
                "Singles, Doubles",
                Icons.Rounded.SportsTennis,
                Brush.radialGradient(
                    colors = listOf(Color(0xFF0D9488), Color(0xFF000000)),
                    center = Offset(100f, 100f),
                    radius = 300f
                ),
                Modifier.weight(1f)
            ) {
                onNavigateToSetup(
                    SportType.BADMINTON
                )
            }
        }
        Spacer(modifier = Modifier.height(120.dp))
    }
}

@Composable
fun SetupScreen(initialSport: SportType, onBack: () -> Unit, onStartMatch: (SportType, String, String) -> Unit) {
    var selectedSport by remember { mutableStateOf(initialSport) }
    var homeName by remember { mutableStateOf("Home") }
    var guestName by remember { mutableStateOf("Guest") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            IconButton(onClick = onBack) { Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite) }
            Text("New Game Setup", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextWhite, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.size(48.dp))
        }
        Text("Select Sport", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(vertical = 16.dp)) {
            items(SportType.entries) { sport ->
                SportCard(
                    sport,
                    selectedSport == sport
                ) { selectedSport = sport }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TeamInput("Home Team", Icons.Default.Home, homeName) { homeName = it }
        Spacer(modifier = Modifier.height(16.dp))
        TeamInput("Guest Team", Icons.Default.AirplanemodeActive, guestName) { guestName = it }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { onStartMatch(selectedSport, homeName, guestName) }, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue), shape = RoundedCornerShape(12.dp)) {
            Icon(Icons.Default.PlayArrow, contentDescription = null); Spacer(modifier = Modifier.width(8.dp))
            Text("Start Match", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}