package com.example.uas_mocom.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
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
import com.example.uas_mocom.viewmodel.VolleyballViewModel

@Composable
fun VolleyballScreen(viewModel: VolleyballViewModel, onBack: () -> Unit) {
    val homeName by viewModel.homeName.collectAsState()
    val guestName by viewModel.guestName.collectAsState()
    val homeScore by viewModel.homeScore.collectAsState()
    val guestScore by viewModel.guestScore.collectAsState()
    val homeSets by viewModel.homeSets.collectAsState()
    val guestSets by viewModel.guestSets.collectAsState()
    val currentSet by viewModel.currentSet.collectAsState()
    val timeDisplay by viewModel.timeDisplay.collectAsState()
    val isTimerRunning by viewModel.isTimerRunning.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar("VOLLEYBALL", onBack)
        GameTimerBoard(timeDisplay)

        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.Center) {
            Text("SET $currentSet", color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        }

        Row(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
            VolleyTeamCard(homeName, "HOME", homeScore.toString(), homeSets, true,
                PrimaryBlue, Modifier.weight(1f)) { viewModel.updateSets(true, it) }
            Spacer(modifier = Modifier.width(16.dp))
            VolleyTeamCard(guestName, "GUEST", guestScore.toString(), guestSets, false,
                CardSurface, Modifier.weight(1f)) { viewModel.updateSets(false, it) }
        }

        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Button(onClick = { viewModel.updateHomeScore(1) }, modifier = Modifier.weight(1f).height(60.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) { Icon(Icons.Default.Add, null) }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { viewModel.updateGuestScore(1) }, modifier = Modifier.weight(1f).height(60.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = CardSurface)) { Icon(Icons.Default.Add, null) }
        }

        Row(modifier = Modifier.padding(16.dp)) {
            OutlinedButton(onClick = { viewModel.startNewSet() }, modifier = Modifier.weight(1f).height(50.dp)) { Icon(Icons.Default.Refresh, null); Text(" New Set") }
        }

        BottomControls(
            onReset = { viewModel.resetTimer(0) },
            onToggleTimer = { viewModel.toggleTimer() },
            isTimerRunning = isTimerRunning
        )
    }
}

@Composable
fun VolleyTeamCard(name: String, type: String, score: String, sets: Int, isServing: Boolean, color: Color, modifier: Modifier, onSetChange: (Int) -> Unit) {
    val textColor = if (color == PrimaryBlue) TextWhite else TextGray
    Column(modifier = modifier.fillMaxHeight().clip(RoundedCornerShape(16.dp)).background(color).border(if (color == CardSurface) 0.dp else 2.dp,
        PrimaryBlue, RoundedCornerShape(16.dp)), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextWhite, maxLines = 1)
        Text(type, fontSize = 12.sp, color = textColor)
        Spacer(modifier = Modifier.height(32.dp))
        Text(score, fontSize = 80.sp, fontWeight = FontWeight.Black, color = TextWhite)
        Spacer(modifier = Modifier.height(32.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onSetChange(-1) }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Remove, null, tint = textColor, modifier = Modifier.size(14.dp)) }
            Text("Sets: $sets", color = textColor, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 4.dp))
            IconButton(onClick = { onSetChange(1) }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Add, null, tint = textColor, modifier = Modifier.size(14.dp)) }
        }
    }
}

