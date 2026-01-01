package com.example.uas_mocom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas_mocom.presentation.DarkBackground
import com.example.uas_mocom.presentation.GameScreen
import com.example.uas_mocom.presentation.HistoryScreen
import com.example.uas_mocom.presentation.HomeScreen
import com.example.uas_mocom.presentation.NewBottomNavBar
import com.example.uas_mocom.presentation.ScoreboardTheme
import com.example.uas_mocom.presentation.SetupScreen
import com.example.uas_mocom.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SportScoreboardApp()
        }
    }
}

@Composable
fun SportScoreboardApp() {
    var currentScreen by remember { mutableStateOf("HOME") }

    // Single unified ViewModel
    val gameViewModel: GameViewModel = viewModel()

    ScoreboardTheme {
        Scaffold(
            containerColor = DarkBackground,
            bottomBar = {
                if (currentScreen == "HOME" || currentScreen == "HISTORY") {
                    NewBottomNavBar(
                        activeTab = currentScreen,
                        onHomeClick = { currentScreen = "HOME" },
                        onNewGameClick = { currentScreen = "SETUP" },
                        onHistoryClick = { currentScreen = "HISTORY" }
                    )
                }
            }
        ) { padding ->
            val modifier = if (currentScreen == "HOME" || currentScreen == "HISTORY") 
                Modifier else Modifier.padding(padding)

            Box(modifier = modifier.fillMaxSize()) {
                when (currentScreen) {
                    "HOME" -> HomeScreen(
                        onViewHistory = { currentScreen = "HISTORY" }
                    )

                    "HISTORY" -> HistoryScreen()

                    "SETUP" -> SetupScreen(
                        onBack = { currentScreen = "HOME" },
                        onStartMatch = { homeName, guestName ->
                            gameViewModel.setupGame(homeName, guestName)
                            currentScreen = "GAME"
                        }
                    )

                    "GAME" -> GameScreen(
                        viewModel = gameViewModel,
                        onBack = { currentScreen = "HOME" }
                    )
                }
            }
        }
    }
}