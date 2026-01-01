package com.example.uas_mocom.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TopBar(title: String, onBack: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = TextWhite) }
        Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextWhite)
        Spacer(modifier = Modifier.size(48.dp))
    }
}

@Composable
fun GameTimerBoard(mainText: String) {
    Box(modifier = Modifier.fillMaxWidth().padding(16.dp).height(140.dp).clip(RoundedCornerShape(24.dp)).background(
        CardSurface
    ), contentAlignment = Alignment.Center) {
        Text(mainText, fontSize = 64.sp, fontWeight = FontWeight.Black, color = TextWhite)
    }
}

@Composable
fun BottomControls(onReset: () -> Unit, onToggleTimer: () -> Unit, isTimerRunning: Boolean) {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp).background(DarkBackground), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(onClick = onReset, modifier = Modifier.weight(0.3f).height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = CardSurface), shape = RoundedCornerShape(12.dp)) { Icon(Icons.Default.Refresh, null, tint = TextWhite); Text(" Reset", color = TextWhite) }
        Button(onClick = onToggleTimer, modifier = Modifier.weight(0.7f).height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = if (isTimerRunning) AccentRed else PrimaryBlue), shape = RoundedCornerShape(12.dp)) {
            Icon(if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow, null); Text(if (isTimerRunning) " Pause" else " Start Timer")
        }
    }
}

@Composable
fun SimpleGameControls(
    onReset: () -> Unit,
    onEndMatch: () -> Unit,
    onToggleTimer: () -> Unit,
    isTimerRunning: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = onReset,
                modifier = Modifier.weight(1f).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CardSurface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Refresh, null, tint = TextWhite)
                Text(" Reset", color = TextWhite)
            }
            Button(
                onClick = onEndMatch,
                modifier = Modifier.weight(1f).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Flag, null, tint = TextWhite)
                Text(" End Match", color = TextWhite)
            }
        }
        Button(
            onClick = onToggleTimer,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = if (isTimerRunning) AccentRed else PrimaryBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow, null)
            Text(if (isTimerRunning) " Pause" else " Start Timer")
        }
    }
}

@Composable
fun NewBottomNavBar(
    activeTab: String = "HOME",
    onHomeClick: () -> Unit,
    onNewGameClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkBackground)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            // Home
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onHomeClick() }
            ) {
                val isActive = activeTab == "HOME"
                Icon(Icons.Default.Home, "Home", tint = if (isActive) PrimaryBlue else TextGray, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text("Home", color = if (isActive) PrimaryBlue else TextGray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
            
            // New Game (Center)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .offset(y = (-10).dp)
                    .clickable { onNewGameClick() }
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue)
                        .border(4.dp, DarkBackground.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, "New Game", tint = TextWhite, modifier = Modifier.size(32.dp))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("New Game", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
            
            // History
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onHistoryClick() }
            ) {
                val isActive = activeTab == "HISTORY"
                Icon(Icons.Default.History, "History", tint = if (isActive) PrimaryBlue else TextGray, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text("History", color = if (isActive) PrimaryBlue else TextGray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun HomeSportCard(title: String, subtitle: String, icon: ImageVector, color: Brush, modifier: Modifier, onClick: () -> Unit) {
    Box(modifier = modifier.height(160.dp).clip(RoundedCornerShape(24.dp)).background(color).clickable { onClick() }) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White.copy(alpha = 0.1f), modifier = Modifier.size(140.dp).align(Alignment.BottomEnd).offset(x = 30.dp, y = 30.dp))
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) { Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(Color.White.copy(0.2f)), contentAlignment = Alignment.Center) { Icon(icon, null, tint = TextWhite, modifier = Modifier.size(16.dp)) } }
            Column { Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextWhite); Text(subtitle, fontSize = 12.sp, color = TextWhite.copy(alpha = 0.8f)) }
        }
    }
}

@Composable
fun TeamInput(label: String, icon: ImageVector, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(label, color = TextGray, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(value = value, onValueChange = onValueChange, leadingIcon = { Icon(icon, null, tint = PrimaryBlue) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CardSurface, unfocusedBorderColor = CardSurface, focusedContainerColor = CardSurface, unfocusedContainerColor = CardSurface, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite))
    }
}