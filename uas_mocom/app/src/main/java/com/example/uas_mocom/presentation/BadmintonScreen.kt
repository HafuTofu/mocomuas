package com.example.uas_mocom.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.rounded.SportsTennis
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas_mocom.viewmodel.BadmintonViewModel

@Composable
fun BadmintonScreen(viewModel: BadmintonViewModel, onBack: () -> Unit) {
    val homeName by viewModel.homeName.collectAsState()
    val guestName by viewModel.guestName.collectAsState()
    val homeScore by viewModel.homeScore.collectAsState()
    val guestScore by viewModel.guestScore.collectAsState()
    val homeSets by viewModel.homeSets.collectAsState()
    val guestSets by viewModel.guestSets.collectAsState()
    val isHomeServing by viewModel.isHomeServing.collectAsState()
    val timeDisplay by viewModel.timeDisplay.collectAsState()
    val isTimerRunning by viewModel.isTimerRunning.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar("BADMINTON", onBack)
        GameTimerBoard(timeDisplay)

        BadmintonPlayerCard(homeName, homeScore.toString(), homeSets, isHomeServing, Color(0xFF1A202C), Modifier.weight(1f).padding(16.dp),
            onScoreChange = { viewModel.updateHomeScore(it) }, onSetChange = { viewModel.updateSets(true, it) })

        BadmintonPlayerCard(guestName, guestScore.toString(), guestSets, !isHomeServing, Color(0xFF1A202C), Modifier.weight(1f).padding(16.dp),
            onScoreChange = { viewModel.updateGuestScore(it) }, onSetChange = { viewModel.updateSets(false, it) })

        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            BadmintonActionButton(Icons.Default.SwapHoriz, "SERVE", Modifier.weight(1f)) { viewModel.toggleServe() }
            BadmintonActionButton(Icons.Default.Flag, "END SET", Modifier.weight(1f), isRed = true) {}
        }
        BottomControls(
            onReset = { viewModel.resetTimer() },
            onToggleTimer = { viewModel.toggleTimer() },
            isTimerRunning = isTimerRunning
        )
    }
}

@Composable
fun BadmintonPlayerCard(name: String, score: String, sets: Int, isServing: Boolean, color: Color, modifier: Modifier, onScoreChange: (Int) -> Unit, onSetChange: (Int) -> Unit) {
    Card(modifier = modifier, shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = color)) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(4.dp, 24.dp).background(if (isServing) SuccessGreen else TextGray, RoundedCornerShape(2.dp)))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 20.sp, maxLines = 1)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) { repeat(3) { Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(if (it<sets) SuccessGreen else Color.DarkGray)) } }
                }
            }
            if (isServing) {
                Box(modifier = Modifier.align(Alignment.TopEnd).clip(RoundedCornerShape(8.dp)).background(Color(0xFF064E3B)).padding(horizontal = 12.dp, vertical = 6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Rounded.SportsTennis, null, tint = SuccessGreen, modifier = Modifier.size(12.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("SERVE", color = SuccessGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                }
            }
            Row(modifier = Modifier.align(Alignment.Center).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                FilledIconButton(onClick = { onScoreChange(-1) }, modifier = Modifier.size(48.dp), colors = IconButtonDefaults.filledIconButtonColors(containerColor = CardSurface)) { Icon(Icons.Default.Remove, null, tint = TextGray) }
                Text(score, fontSize = 80.sp, fontWeight = FontWeight.Black, color = TextWhite)
                FilledIconButton(onClick = { onScoreChange(1) }, modifier = Modifier.size(64.dp), colors = IconButtonDefaults.filledIconButtonColors(containerColor = SuccessGreen)) { Icon(Icons.Default.Add, null, tint = Color.Black) }
            }
            Row(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onSetChange(-1) }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Remove, null, tint = TextGray, modifier = Modifier.size(14.dp)) }
                Text("Sets: $sets", fontSize = 12.sp, color = TextGray, modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = { onSetChange(1) }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Add, null, tint = TextGray, modifier = Modifier.size(14.dp)) }
            }
        }
    }
}

@Composable
fun BadmintonActionButton(icon: ImageVector, label: String, modifier: Modifier, isRed: Boolean = false, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = modifier.height(60.dp), shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, if(isRed) AccentRed.copy(0.5f) else CardSurface), colors = ButtonDefaults.outlinedButtonColors(containerColor = if(isRed) AccentRed.copy(0.1f) else Color.Transparent)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = if(isRed) AccentRed else TextGray, modifier = Modifier.size(20.dp))
            Text(label, fontSize = 10.sp, color = if(isRed) AccentRed else TextGray, fontWeight = FontWeight.Bold)
        }
    }
}