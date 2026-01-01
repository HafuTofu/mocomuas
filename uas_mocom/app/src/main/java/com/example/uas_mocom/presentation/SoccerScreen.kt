package com.example.uas_mocom.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas_mocom.viewmodel.SoccerViewModel

@Composable
fun SoccerScreen(viewModel: SoccerViewModel, onBack: () -> Unit) {
    val homeName by viewModel.homeName.collectAsState()
    val guestName by viewModel.guestName.collectAsState()
    val homeScore by viewModel.homeScore.collectAsState()
    val guestScore by viewModel.guestScore.collectAsState()
    val timeDisplay by viewModel.timeDisplay.collectAsState()
    val isTimerRunning by viewModel.isTimerRunning.collectAsState()

    val homeYellow by viewModel.homeYellow.collectAsState()
    val homeRed by viewModel.homeRed.collectAsState()
    val guestYellow by viewModel.guestYellow.collectAsState()
    val guestRed by viewModel.guestRed.collectAsState()
    val homeCorners by viewModel.homeCorners.collectAsState()
    val guestCorners by viewModel.guestCorners.collectAsState()

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        TopBar("SOCCER", onBack)
        GameTimerBoard(timeDisplay)

        // Period Selection (Visual only in this example, can be connected to VM if needed)
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.Center) {
            listOf("1st", "2nd", "ET").forEach { SuggestionChip(onClick = {}, label = { Text(it) }, modifier = Modifier.padding(horizontal = 4.dp)) }
        }

        // Teams
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            SoccerTeamCard(homeName, homeScore.toString(), 0, homeYellow, homeRed, true, Modifier.weight(1f),
                onScoreChange = { viewModel.updateHomeScore(it) },
                onYellowChange = { viewModel.updateCard(true, false, it) }, onRedChange = { viewModel.updateCard(true, true, it) }, onFoulChange = {})
            Spacer(modifier = Modifier.width(16.dp))
            SoccerTeamCard(guestName, guestScore.toString(), 0, guestYellow, guestRed, false, Modifier.weight(1f),
                onScoreChange = { viewModel.updateGuestScore(it) },
                onYellowChange = { viewModel.updateCard(false, false, it) }, onRedChange = { viewModel.updateCard(false, true, it) }, onFoulChange = {})
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stats
        Column(modifier = Modifier.padding(16.dp)) {
            SoccerStatRow("CORNERS", homeCorners, guestCorners,
                onHomeChange = { viewModel.updateCorners(true, it) }, onGuestChange = { viewModel.updateCorners(false, it) })
        }

        BottomControls(
            onReset = { viewModel.resetTimer(0) },
            onToggleTimer = { viewModel.toggleTimer() },
            isTimerRunning = isTimerRunning
        )
    }
}

@Composable
fun SoccerTeamCard(
    label: String, score: String, fouls: Int, yellow: Int, red: Int, isHome: Boolean, modifier: Modifier,
    onScoreChange: (Int) -> Unit, onYellowChange: (Int) -> Unit, onRedChange: (Int) -> Unit, onFoulChange: (Int) -> Unit
) {
    Column(modifier = modifier.fillMaxHeight().clip(RoundedCornerShape(16.dp)).background(
        CardSurface
    ).padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(if (isHome) PrimaryBlue else SuccessGreen), contentAlignment = Alignment.Center) { Text(label.take(1), fontWeight = FontWeight.Bold, color = TextWhite) }
        Text(label, fontSize = 12.sp, color = TextGray, modifier = Modifier.padding(top = 8.dp), maxLines = 1)
        Spacer(modifier = Modifier.weight(1f))
        Text(score, fontSize = 64.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth().height(48.dp).clip(RoundedCornerShape(8.dp)).background(
                PrimaryBlue
            ),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { onScoreChange(-1) }) { Icon(Icons.Default.Remove, null, tint = TextWhite) }
            Text("GOAL", fontWeight = FontWeight.Bold, color = TextWhite)
            IconButton(onClick = { onScoreChange(1) }) { Icon(Icons.Default.Add, null, tint = TextWhite) }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Style, null, tint = AccentYellow)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onYellowChange(-1) }, modifier = Modifier.size(20.dp)) { Icon(Icons.Default.Remove, null, tint = TextGray, modifier = Modifier.size(12.dp)) }
                Text("$yellow", fontSize = 12.sp, color = TextWhite, modifier = Modifier.padding(horizontal = 4.dp))
                IconButton(onClick = { onYellowChange(1) }, modifier = Modifier.size(20.dp)) { Icon(Icons.Default.Add, null, tint = TextWhite, modifier = Modifier.size(12.dp)) }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Style, null, tint = AccentRed)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onRedChange(-1) }, modifier = Modifier.size(20.dp)) { Icon(Icons.Default.Remove, null, tint = TextGray, modifier = Modifier.size(12.dp)) }
                Text("$red", fontSize = 12.sp, color = TextWhite, modifier = Modifier.padding(horizontal = 4.dp))
                IconButton(onClick = { onRedChange(1) }, modifier = Modifier.size(20.dp)) { Icon(Icons.Default.Add, null, tint = TextWhite, modifier = Modifier.size(12.dp)) }
            }
        }
    }
}

@Composable
fun SoccerStatRow(title: String, homeVal: Int, guestVal: Int, onHomeChange: (Int) -> Unit, onGuestChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().border(1.dp, CardSurface, RoundedCornerShape(8.dp)).padding(8.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onHomeChange(-1) }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Remove, null, tint = TextGray, modifier = Modifier.size(14.dp)) }
            Text("$homeVal", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite, modifier = Modifier.padding(horizontal = 8.dp))
            IconButton(onClick = { onHomeChange(1) }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Add, null, tint = TextWhite, modifier = Modifier.size(14.dp)) }
        }
        Text(title, fontSize = 12.sp, color = TextGray, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onGuestChange(-1) }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Remove, null, tint = TextGray, modifier = Modifier.size(14.dp)) }
            Text("$guestVal", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite, modifier = Modifier.padding(horizontal = 8.dp))
            IconButton(onClick = { onGuestChange(1) }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Add, null, tint = TextWhite, modifier = Modifier.size(14.dp)) }
        }
    }
}