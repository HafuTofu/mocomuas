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
import com.example.uas_mocom.model.SportType
import com.example.uas_mocom.presentation.BadmintonScreen
import com.example.uas_mocom.presentation.DarkBackground
import com.example.uas_mocom.presentation.HomeScreen
import com.example.uas_mocom.presentation.NewBottomNavBar
import com.example.uas_mocom.presentation.ScoreboardTheme
import com.example.uas_mocom.presentation.SetupScreen
import com.example.uas_mocom.presentation.SoccerScreen
import com.example.uas_mocom.presentation.VolleyballScreen
import com.example.uas_mocom.viewmodel.*

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
    var selectedSport by remember { mutableStateOf(SportType.BASKETBALL) }

    // Initialize ViewModels
    val basketballVM: BasketballViewModel = viewModel()
    val soccerVM: SoccerViewModel = viewModel()
    val volleyballVM: VolleyballViewModel = viewModel()
    val badmintonVM: BadmintonViewModel = viewModel()

    ScoreboardTheme {
        Scaffold(
            containerColor = DarkBackground,
            bottomBar = {
                if (currentScreen == "HOME") {
                    NewBottomNavBar(
                        onHomeClick = { },
                        onNewGameClick = {
                            selectedSport = SportType.BASKETBALL
                            currentScreen = "SETUP"
                        }
                    )
                }
            }
        ) { padding ->
            val modifier = if (currentScreen == "HOME") Modifier else Modifier.padding(padding)

            Box(modifier = modifier.fillMaxSize()) {
                when (currentScreen) {
                    "HOME" -> HomeScreen(
                        onNavigateToSetup = { sport ->
                            selectedSport = sport
                            currentScreen = "SETUP"
                        }
                    )

                    "SETUP" -> SetupScreen(
                        initialSport = selectedSport,
                        onBack = { currentScreen = "HOME" },
                        onStartMatch = { sport, hName, gName ->
                            selectedSport = sport
                            // Initialize specific VM
                            when (sport) {
                                SportType.BASKETBALL -> {
                                    basketballVM.setTeamNames(hName, gName)
                                    basketballVM.resetTimer(12 * 60)
                                }

                                SportType.SOCCER -> {
                                    soccerVM.setTeamNames(hName, gName)
                                    soccerVM.resetTimer(0)
                                }

                                SportType.VOLLEYBALL -> {
                                    volleyballVM.setTeamNames(hName, gName)
                                    volleyballVM.resetTimer(0)
                                }

                                SportType.BADMINTON -> {
                                    badmintonVM.setTeamNames(hName, gName)
                                    badmintonVM.resetTimer(0)
                                }
                            }
                            currentScreen = "GAME"
                        }
                    )

                    "GAME" -> {
                        when (selectedSport) {
                            SportType.BASKETBALL -> BasketballScreen(
                                basketballVM,
                                onBack = { currentScreen = "SETUP" })

                            SportType.SOCCER -> SoccerScreen(
                                soccerVM,
                                onBack = { currentScreen = "SETUP" })

                            SportType.VOLLEYBALL -> VolleyballScreen(
                                volleyballVM,
                                onBack = { currentScreen = "SETUP" })

                            SportType.BADMINTON -> BadmintonScreen(
                                badmintonVM,
                                onBack = { currentScreen = "SETUP" })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BasketballScreen(x0: BasketballViewModel, onBack: () -> Unit) {
    TODO("Not yet implemented")
}