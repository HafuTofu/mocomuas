package com.example.uas_mocom

import android.os.Bundle
import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }
        setContent {
            SportScoreboardApp(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun SportScoreboardApp(modifier: Modifier) {
    var currentScreen by remember { mutableStateOf("HOME") }

    // Single unified ViewModel
    val gameViewModel: GameViewModel = viewModel(factory = GameViewModel.Factory)
    val matchHistory by gameViewModel.matchHistory.collectAsState()

    ScoreboardTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
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
                        matches = matchHistory,
                        onViewHistory = { currentScreen = "HISTORY" }
                    )

                    "HISTORY" -> HistoryScreen(
                        matches = matchHistory,
                        onClearHistory = { gameViewModel.clearHistory() }
                    )

                    "SETUP" -> SetupScreen(
                        onBack = { currentScreen = "HOME" },
                        onStartMatch = { homeName, guestName ->
                            gameViewModel.setupGame(homeName, guestName)
                            currentScreen = "GAME"
                        }
                    )

                    "GAME" -> GameScreen(
                        viewModel = gameViewModel,
                        onBackToHome = { currentScreen = "HOME" }
                    )
                }
            }
        }
    }
}