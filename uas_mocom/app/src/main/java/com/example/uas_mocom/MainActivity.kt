package com.example.uas_mocom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Undo
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.Locale

// --- 1. MAIN ACTIVITY ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SportScoreboardApp()
        }
    }
}

// --- 2. THEME & COLORS ---
val DarkBackground = Color(0xFF0F172A)
val CardSurface = Color(0xFF1E293B)
val PrimaryBlue = Color(0xFF3B82F6)
val TextWhite = Color(0xFFFFFFFF)
val TextGray = Color(0xFF94A3B8)
val AccentRed = Color(0xFFEF4444)
val AccentYellow = Color(0xFFEAB308)
val SuccessGreen = Color(0xFF22C55E)

@Composable
fun ScoreboardTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = DarkBackground, surface = CardSurface, primary = PrimaryBlue,
            onBackground = TextWhite, onSurface = TextWhite
        ),
        content = content
    )
}

// --- 3. DATA MODELS ---
enum class SportType(val title: String, val icon: ImageVector) {
    BASKETBALL("Basketball", Icons.Rounded.SportsBasketball),
    VOLLEYBALL("Volleyball", Icons.Rounded.SportsVolleyball),
    SOCCER("Soccer", Icons.Rounded.SportsSoccer),
    BADMINTON("Badminton", Icons.Rounded.SportsTennis)
}

data class MatchData(
    val homeName: String = "Home",
    val guestName: String = "Guest",
    val sport: SportType = SportType.BASKETBALL
)

// --- 4. MAIN APP NAVIGATION ---
@Composable
fun SportScoreboardApp() {
    var currentScreen by remember { mutableStateOf("HOME") }
    var matchData by remember { mutableStateOf(MatchData()) }

    ScoreboardTheme {
        Scaffold(
            containerColor = DarkBackground,
            bottomBar = {
                if (currentScreen == "HOME") {
                    NewBottomNavBar(
                        onHomeClick = { },
                        onNewGameClick = {
                            matchData = matchData.copy(sport = SportType.BASKETBALL)
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
                            matchData = matchData.copy(sport = sport)
                            currentScreen = "SETUP"
                        }
                    )
                    "SETUP" -> SetupScreen(
                        initialSport = matchData.sport,
                        onBack = { currentScreen = "HOME" },
                        onStartMatch = { sport, hName, gName ->
                            matchData = MatchData(hName, gName, sport)
                            currentScreen = "GAME"
                        }
                    )
                    "GAME" -> {
                        when (matchData.sport) {
                            SportType.BASKETBALL -> BasketballScreen(matchData, onBack = { currentScreen = "SETUP" })
                            SportType.SOCCER -> SoccerScreen(matchData, onBack = { currentScreen = "SETUP" })
                            SportType.VOLLEYBALL -> VolleyballScreen(matchData, onBack = { currentScreen = "SETUP" })
                            SportType.BADMINTON -> BadmintonScreen(matchData, onBack = { currentScreen = "SETUP" })
                        }
                    }
                }
            }
        }
    }
}

// --- 5. SCREEN: HOME ---
@Composable
fun HomeScreen(onNavigateToSetup: (SportType) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp).verticalScroll(rememberScrollState())
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(top = 48.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(PrimaryBlue), contentAlignment = Alignment.Center) {
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
            HomeSportCard("Basketball", "5v5, 3v3", Icons.Rounded.SportsBasketball, Brush.radialGradient(colors = listOf(Color(0xFFEA580C), Color(0xFF000000)), center = androidx.compose.ui.geometry.Offset(100f, 100f), radius = 300f), Modifier.weight(1f)) {
                onNavigateToSetup(SportType.BASKETBALL)
            }
            HomeSportCard("Volleyball", "Indoor, Beach", Icons.Rounded.SportsVolleyball, Brush.radialGradient(colors = listOf(Color(0xFFCA8A04), Color(0xFF000000)), center = androidx.compose.ui.geometry.Offset(100f, 100f), radius = 300f), Modifier.weight(1f)) {
                onNavigateToSetup(SportType.VOLLEYBALL)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            HomeSportCard("Soccer", "11v11, Futsal", Icons.Rounded.SportsSoccer, Brush.radialGradient(colors = listOf(Color(0xFF166534), Color(0xFF000000)), center = androidx.compose.ui.geometry.Offset(100f, 100f), radius = 300f), Modifier.weight(1f)) {
                onNavigateToSetup(SportType.SOCCER)
            }
            HomeSportCard("Badminton", "Singles, Doubles", Icons.Rounded.SportsTennis, Brush.radialGradient(colors = listOf(Color(0xFF0D9488), Color(0xFF000000)), center = androidx.compose.ui.geometry.Offset(100f, 100f), radius = 300f), Modifier.weight(1f)) {
                onNavigateToSetup(SportType.BADMINTON)
            }
        }
        Spacer(modifier = Modifier.height(120.dp))
    }
}

// --- 6. GAME SETUP SCREEN ---
@Composable
fun SetupScreen(initialSport: SportType, onBack: () -> Unit, onStartMatch: (SportType, String, String) -> Unit) {
    var selectedSport by remember { mutableStateOf(initialSport) }
    var homeName by remember { mutableStateOf("Lakers") }
    var guestName by remember { mutableStateOf("Bulls") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            IconButton(onClick = onBack) { Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite) }
            Text("New Game Setup", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextWhite, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.size(48.dp))
        }
        Text("Select Sport", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(vertical = 16.dp)) {
            items(SportType.entries) { sport -> SportCard(sport, selectedSport == sport) { selectedSport = sport } }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Team Names", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 16.dp))
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

// --- 7. REUSABLE LOGIC & COMPONENTS ---

data class GameTimerControl(
    val timeDisplay: String,
    val isRunning: Boolean,
    val toggle: () -> Unit,
    val reset: () -> Unit
)

@Composable
fun useGameTimer(initialSeconds: Long, countUp: Boolean = false): GameTimerControl {
    var time by remember { mutableLongStateOf(initialSeconds) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000L)
            if (countUp) time++ else if (time > 0) time--
        }
    }

    val minutes = time / 60
    val seconds = time % 60
    val timeString = String.format(Locale.US, "%02d:%02d", minutes, seconds)

    return GameTimerControl(
        timeDisplay = timeString,
        isRunning = isRunning,
        toggle = { isRunning = !isRunning },
        reset = {
            isRunning = false
            time = initialSeconds
        }
    )
}

@Composable
fun TopBar(title: String, onBack: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = TextWhite) }
        Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextWhite)
        IconButton(onClick = {}) { Icon(Icons.Default.Settings, null, tint = TextWhite) }
    }
}

@Composable
fun GameTimerBoard(mainText: String) {
    Box(modifier = Modifier.fillMaxWidth().padding(16.dp).height(140.dp).clip(RoundedCornerShape(24.dp)).background(CardSurface), contentAlignment = Alignment.Center) {
        Text(mainText, fontSize = 64.sp, fontWeight = FontWeight.Black, color = TextWhite)
    }
}

@Composable
fun BottomControls(onReset: () -> Unit, onToggleTimer: () -> Unit, isTimerRunning: Boolean) {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp).background(DarkBackground), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(onClick = onReset, modifier = Modifier.weight(0.3f).height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = CardSurface), shape = RoundedCornerShape(12.dp)) { Icon(Icons.Default.Refresh, null, tint = TextWhite); Text(" Reset Timer", color = TextWhite) }
        Button(onClick = onToggleTimer, modifier = Modifier.weight(0.7f).height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = if (isTimerRunning) AccentRed else PrimaryBlue), shape = RoundedCornerShape(12.dp)) {
            Icon(if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow, null); Text(if (isTimerRunning) " Pause" else " Start Timer")
        }
    }
}

// --- 8. SPORT SCREENS ---

@Composable
fun BasketballScreen(data: MatchData, onBack: () -> Unit) {
    var currentHomeName by remember { mutableStateOf(data.homeName) }
    var currentGuestName by remember { mutableStateOf(data.guestName) }

    var homeScore by remember { mutableIntStateOf(0) }
    var guestScore by remember { mutableIntStateOf(0) }
    var homeFouls by remember { mutableIntStateOf(0) }
    var guestFouls by remember { mutableIntStateOf(0) }
    var quarter by remember { mutableIntStateOf(1) }

    val timer = useGameTimer(0L, countUp = true)

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar("BASKETBALL", onBack)
        GameTimerBoard(timer.timeDisplay)

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            listOf(1, 2, 3, 4).forEach { q ->
                Button(onClick = { quarter = q }, colors = ButtonDefaults.buttonColors(containerColor = if (quarter == q) PrimaryBlue else CardSurface), shape = RoundedCornerShape(8.dp), modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
                    Text("Q$q", color = if (quarter == q) TextWhite else TextGray)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
            BasketballTeamCard(currentHomeName, homeScore, homeFouls, true, Modifier.weight(1f).padding(end = 8.dp),
                onScore = { homeScore += it }, onFoul = { homeFouls = (homeFouls + it).coerceAtLeast(0) })
            BasketballTeamCard(currentGuestName, guestScore, guestFouls, false, Modifier.weight(1f).padding(start = 8.dp),
                onScore = { guestScore += it }, onFoul = { guestFouls = (guestFouls + it).coerceAtLeast(0) })
        }

        Row(modifier = Modifier.padding(16.dp)) {
            OutlinedButton(
                onClick = {
                    val tName = currentHomeName; currentHomeName = currentGuestName; currentGuestName = tName
                    val tScore = homeScore; homeScore = guestScore; guestScore = tScore
                    val tFouls = homeFouls; homeFouls = guestFouls; guestFouls = tFouls
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Icon(Icons.Default.SwapHoriz, null); Text(" Swap Sides")
            }
        }

        BottomControls(onReset = { timer.reset() }, onToggleTimer = timer.toggle, isTimerRunning = timer.isRunning)
    }
}

@Composable
fun SoccerScreen(data: MatchData, onBack: () -> Unit) {
    var currentHomeName by remember { mutableStateOf(data.homeName) }
    var currentGuestName by remember { mutableStateOf(data.guestName) }

    var homeScore by remember { mutableIntStateOf(0) }
    var guestScore by remember { mutableIntStateOf(0) }

    // Detailed stats for Home
    var homeYellow by remember { mutableIntStateOf(0) }
    var homeRed by remember { mutableIntStateOf(0) }
    var homeFouls by remember { mutableIntStateOf(0) }
    var homeCorners by remember { mutableIntStateOf(0) }
    var homeShots by remember { mutableIntStateOf(0) }
    var homeOffsides by remember { mutableIntStateOf(0) }

    // Detailed stats for Guest
    var guestYellow by remember { mutableIntStateOf(0) }
    var guestRed by remember { mutableIntStateOf(0) }
    var guestFouls by remember { mutableIntStateOf(0) }
    var guestCorners by remember { mutableIntStateOf(0) }
    var guestShots by remember { mutableIntStateOf(0) }
    var guestOffsides by remember { mutableIntStateOf(0) }

    val timer = useGameTimer(0L, countUp = true)

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        TopBar("SOCCER", onBack)
        GameTimerBoard(timer.timeDisplay)
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.Center) {
            listOf("1st", "2nd", "ET").forEach { SuggestionChip(onClick = {}, label = { Text(it) }, modifier = Modifier.padding(horizontal = 4.dp)) }
        }

        // Team Scores
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            SoccerTeamCard(
                label = currentHomeName, score = homeScore.toString(), fouls = homeFouls,
                yellow = homeYellow, red = homeRed, isHome = true, modifier = Modifier.weight(1f),
                onScoreChange = { homeScore = (homeScore + it).coerceAtLeast(0) },
                onYellowChange = { homeYellow = (homeYellow + it).coerceAtLeast(0) },
                onRedChange = { homeRed = (homeRed + it).coerceAtLeast(0) },
                onFoulChange = { homeFouls = (homeFouls + it).coerceAtLeast(0) }
            )
            Spacer(modifier = Modifier.width(16.dp))
            SoccerTeamCard(
                label = currentGuestName, score = guestScore.toString(), fouls = guestFouls,
                yellow = guestYellow, red = guestRed, isHome = false, modifier = Modifier.weight(1f),
                onScoreChange = { guestScore = (guestScore + it).coerceAtLeast(0) },
                onYellowChange = { guestYellow = (guestYellow + it).coerceAtLeast(0) },
                onRedChange = { guestRed = (guestRed + it).coerceAtLeast(0) },
                onFoulChange = { guestFouls = (guestFouls + it).coerceAtLeast(0) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Swap Button
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            OutlinedButton(
                onClick = {
                    // Swap Names
                    val tName = currentHomeName; currentHomeName = currentGuestName; currentGuestName = tName
                    // Swap Scores
                    val tScore = homeScore; homeScore = guestScore; guestScore = tScore
                    // Swap Cards & Fouls
                    val tY = homeYellow; homeYellow = guestYellow; guestYellow = tY
                    val tR = homeRed; homeRed = guestRed; guestRed = tR
                    val tF = homeFouls; homeFouls = guestFouls; guestFouls = tF
                    // Swap Stats
                    val tC = homeCorners; homeCorners = guestCorners; guestCorners = tC
                    val tS = homeShots; homeShots = guestShots; guestShots = tS
                    val tO = homeOffsides; homeOffsides = guestOffsides; guestOffsides = tO
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Icon(Icons.Default.SwapHoriz, null); Text(" Swap Sides")
            }
        }

        // Detailed Stats Rows (Manual Input)
        Column(modifier = Modifier.padding(16.dp)) {
            SoccerStatRow("CORNERS", homeCorners, guestCorners,
                onHomeChange = { homeCorners = (homeCorners + it).coerceAtLeast(0) },
                onGuestChange = { guestCorners = (guestCorners + it).coerceAtLeast(0) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            SoccerStatRow("SHOTS", homeShots, guestShots,
                onHomeChange = { homeShots = (homeShots + it).coerceAtLeast(0) },
                onGuestChange = { guestShots = (guestShots + it).coerceAtLeast(0) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            SoccerStatRow("OFFSIDES", homeOffsides, guestOffsides,
                onHomeChange = { homeOffsides = (homeOffsides + it).coerceAtLeast(0) },
                onGuestChange = { guestOffsides = (guestOffsides + it).coerceAtLeast(0) }
            )
        }

        BottomControls(
            onReset = { timer.reset() },
            onToggleTimer = timer.toggle,
            isTimerRunning = timer.isRunning
        )
    }
}

@Composable
fun VolleyballScreen(data: MatchData, onBack: () -> Unit) {
    var currentHomeName by remember { mutableStateOf(data.homeName) }
    var currentGuestName by remember { mutableStateOf(data.guestName) }
    var homeScore by remember { mutableIntStateOf(0) }
    var guestScore by remember { mutableIntStateOf(0) }
    var homeSets by remember { mutableIntStateOf(0) }
    var guestSets by remember { mutableIntStateOf(0) }
    var currentSet by remember { mutableIntStateOf(1) }

    val timer = useGameTimer(0L, countUp = true)

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar("VOLLEYBALL", onBack)
        GameTimerBoard(timer.timeDisplay)

        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier.clip(RoundedCornerShape(50)).background(CardSurface).padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { if(currentSet > 1) currentSet-- }, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Remove, null, tint = TextGray, modifier = Modifier.size(16.dp)) }
                Text("SET $currentSet", color = PrimaryBlue, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = { currentSet++ }, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Add, null, tint = TextWhite, modifier = Modifier.size(16.dp)) }
            }
        }

        Row(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
            VolleyTeamCard(currentHomeName, "HOME", homeScore.toString(), homeSets, true, PrimaryBlue, Modifier.weight(1f)) { homeSets = (homeSets + it).coerceAtLeast(0) }
            Spacer(modifier = Modifier.width(16.dp))
            VolleyTeamCard(currentGuestName, "GUEST", guestScore.toString(), guestSets, false, CardSurface, Modifier.weight(1f)) { guestSets = (guestSets + it).coerceAtLeast(0) }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Button(onClick = { homeScore++ }, modifier = Modifier.weight(1f).height(60.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) { Icon(Icons.Default.Add, null) }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { guestScore++ }, modifier = Modifier.weight(1f).height(60.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = CardSurface)) { Icon(Icons.Default.Add, null) }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.padding(16.dp)) {
            OutlinedButton(onClick = {
                val tName = currentHomeName; currentHomeName = currentGuestName; currentGuestName = tName
                val tScore = homeScore; homeScore = guestScore; guestScore = tScore
                val tSets = homeSets; homeSets = guestSets; guestSets = tSets
            }, modifier = Modifier.weight(1f).height(50.dp)) { Icon(Icons.Default.SwapHoriz, null); Text(" Swap Sides") }
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedButton(onClick = { currentSet++; homeScore=0; guestScore=0 }, modifier = Modifier.weight(1f).height(50.dp)) { Icon(Icons.Default.Refresh, null); Text(" New Set") }
        }
        BottomControls(onReset = { timer.reset() }, onToggleTimer = timer.toggle, isTimerRunning = timer.isRunning)
    }
}

@Composable
fun BadmintonScreen(data: MatchData, onBack: () -> Unit) {
    var currentHomeName by remember { mutableStateOf(data.homeName) }
    var currentGuestName by remember { mutableStateOf(data.guestName) }
    var homeScore by remember { mutableIntStateOf(0) }
    var guestScore by remember { mutableIntStateOf(0) }
    var homeSets by remember { mutableIntStateOf(0) }
    var guestSets by remember { mutableIntStateOf(0) }
    var currentSet by remember { mutableIntStateOf(1) }

    val timer = useGameTimer(0L, true)

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar("BADMINTON", onBack)
        GameTimerBoard(timer.timeDisplay)

        BadmintonPlayerCard(currentHomeName, homeScore.toString(), homeSets, true, Color(0xFF1A202C), Modifier.weight(1f).padding(16.dp),
            onScoreChange = { homeScore += it },
            onSetChange = { homeSets = (homeSets + it).coerceAtLeast(0) }
        )

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier.clip(RoundedCornerShape(50)).background(CardSurface).padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { if(currentSet > 1) currentSet-- }, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Remove, null, tint = TextGray, modifier = Modifier.size(16.dp)) }
                Text("SET $currentSet", color = TextWhite, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = { currentSet++ }, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Add, null, tint = TextWhite, modifier = Modifier.size(16.dp)) }
            }
            Text("Best of 3", color = TextGray, fontSize = 12.sp)
        }

        BadmintonPlayerCard(currentGuestName, guestScore.toString(), guestSets, false, Color(0xFF1A202C), Modifier.weight(1f).padding(16.dp),
            onScoreChange = { guestScore += it },
            onSetChange = { guestSets = (guestSets + it).coerceAtLeast(0) }
        )

        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            BadmintonActionButton(Icons.AutoMirrored.Filled.Undo, "UNDO", Modifier.weight(1f)) { if(homeScore>0) homeScore-- }
            BadmintonActionButton(Icons.Default.SwapHoriz, "SWAP", Modifier.weight(1f)) {
                val tName = currentHomeName; currentHomeName = currentGuestName; currentGuestName = tName
                val tScore = homeScore; homeScore = guestScore; guestScore = tScore
                val tSets = homeSets; homeSets = guestSets; guestSets = tSets
            }
            BadmintonActionButton(Icons.Default.Flag, "END SET", Modifier.weight(1f), isRed = true) {}
        }
        BottomControls(
            onReset = { timer.reset() },
            onToggleTimer = timer.toggle,
            isTimerRunning = timer.isRunning
        )
    }
}

// --- 9. HELPERS ---

@Composable
fun BasketballTeamCard(name: String, score: Int, fouls: Int, isHome: Boolean, modifier: Modifier, onScore: (Int) -> Unit, onFoul: (Int) -> Unit) {
    Column(modifier = modifier.fillMaxHeight().clip(RoundedCornerShape(16.dp)).background(CardSurface).padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(if (isHome) "HOME" else "GUEST", fontSize = 10.sp, color = TextGray)
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(if (isHome) AccentYellow else AccentRed))
        }
        Text(name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextWhite, maxLines = 1)
        Spacer(modifier = Modifier.weight(1f))
        Text(score.toString(), fontSize = 60.sp, fontWeight = FontWeight.Bold, color = if (isHome) PrimaryBlue else TextWhite)
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { onScore(1) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) { Text("+1") }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onScore(2) }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(0.1f))) { Text("+2") }
            Button(onClick = { onScore(3) }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(0.1f))) { Text("+3") }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("FOULS", fontSize = 12.sp, color = TextGray)
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color.Black).padding(4.dp).clickable { onFoul(1) }) { Text("-  $fouls  +", color = TextWhite, fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
fun SoccerTeamCard(
    label: String, score: String, fouls: Int, yellow: Int, red: Int, isHome: Boolean, modifier: Modifier,
    onScoreChange: (Int) -> Unit, onYellowChange: (Int) -> Unit, onRedChange: (Int) -> Unit, onFoulChange: (Int) -> Unit
) {
    Column(modifier = modifier.fillMaxHeight().clip(RoundedCornerShape(16.dp)).background(CardSurface).padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(if (isHome) PrimaryBlue else SuccessGreen), contentAlignment = Alignment.Center) { Text(label.take(1), fontWeight = FontWeight.Bold, color = TextWhite) }
        Text(label, fontSize = 12.sp, color = TextGray, modifier = Modifier.padding(top = 8.dp), maxLines = 1)
        Spacer(modifier = Modifier.weight(1f))
        Text(score, fontSize = 64.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        Spacer(modifier = Modifier.weight(1f))

        // MODIFIED: Goal Button with +/- logic and identical look
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(PrimaryBlue),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { onScoreChange(-1) }) {
                Icon(Icons.Default.Remove, contentDescription = null, tint = TextWhite)
            }
            Text("GOAL", fontWeight = FontWeight.Bold, color = TextWhite)
            IconButton(onClick = { onScoreChange(1) }) {
                Icon(Icons.Default.Add, contentDescription = null, tint = TextWhite)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Yellow Card Row
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Style, null, tint = AccentYellow)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onYellowChange(-1) }, modifier = Modifier.size(20.dp)) { Icon(Icons.Default.Remove, null, tint = TextGray, modifier = Modifier.size(12.dp)) }
                Text("$yellow", fontSize = 12.sp, color = TextWhite, modifier = Modifier.padding(horizontal = 4.dp))
                IconButton(onClick = { onYellowChange(1) }, modifier = Modifier.size(20.dp)) { Icon(Icons.Default.Add, null, tint = TextWhite, modifier = Modifier.size(12.dp)) }
            }
        }

        // Red Card Row
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Style, null, tint = AccentRed)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onRedChange(-1) }, modifier = Modifier.size(20.dp)) { Icon(Icons.Default.Remove, null, tint = TextGray, modifier = Modifier.size(12.dp)) }
                Text("$red", fontSize = 12.sp, color = TextWhite, modifier = Modifier.padding(horizontal = 4.dp))
                IconButton(onClick = { onRedChange(1) }, modifier = Modifier.size(20.dp)) { Icon(Icons.Default.Add, null, tint = TextWhite, modifier = Modifier.size(12.dp)) }
            }
        }

        // Fouls Row
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("Fouls", fontSize = 12.sp, color = TextGray)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onFoulChange(-1) }, modifier = Modifier.size(20.dp)) { Icon(Icons.Default.Remove, null, tint = TextGray, modifier = Modifier.size(12.dp)) }
                Text("$fouls", fontSize = 12.sp, color = TextWhite, modifier = Modifier.padding(horizontal = 4.dp))
                IconButton(onClick = { onFoulChange(1) }, modifier = Modifier.size(20.dp)) { Icon(Icons.Default.Add, null, tint = TextWhite, modifier = Modifier.size(12.dp)) }
            }
        }
    }
}

// New Component for Soccer Stats Rows
@Composable
fun SoccerStatRow(title: String, homeVal: Int, guestVal: Int, onHomeChange: (Int) -> Unit, onGuestChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().border(1.dp, CardSurface, RoundedCornerShape(8.dp)).padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
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

@Composable
fun VolleyTeamCard(
    name: String, type: String, score: String, sets: Int, isServing: Boolean, color: Color, modifier: Modifier, onSetChange: (Int) -> Unit
) {
    val textColor = if (color == PrimaryBlue) TextWhite else TextGray
    Column(modifier = modifier.fillMaxHeight().clip(RoundedCornerShape(16.dp)).background(color).border(if (color == CardSurface) 0.dp else 2.dp, PrimaryBlue, RoundedCornerShape(16.dp)), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        if (isServing) Icon(Icons.Rounded.SportsVolleyball, null, tint = TextWhite, modifier = Modifier.align(Alignment.End).padding(8.dp))
        Text(name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextWhite, maxLines = 1)
        Text(type, fontSize = 12.sp, color = textColor)
        Spacer(modifier = Modifier.height(32.dp))
        Text(score, fontSize = 80.sp, fontWeight = FontWeight.Black, color = TextWhite)
        Spacer(modifier = Modifier.height(32.dp))
        Row { repeat(3) { i -> Box(modifier = Modifier.padding(4.dp).size(12.dp).clip(CircleShape).background(if (i < sets) PrimaryBlue else Color.DarkGray)) } }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
            IconButton(onClick = { onSetChange(-1) }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Remove, null, tint = textColor, modifier = Modifier.size(14.dp)) }
            Text("Sets: $sets", color = textColor, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 4.dp))
            IconButton(onClick = { onSetChange(1) }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Add, null, tint = textColor, modifier = Modifier.size(14.dp)) }
        }
    }
}

@Composable
fun BadmintonPlayerCard(
    name: String,
    score: String,
    sets: Int,
    isServing: Boolean,
    color: Color,
    modifier: Modifier,
    onScoreChange: (Int) -> Unit,
    onSetChange: (Int) -> Unit
) {
    Card(modifier = modifier, shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = color)) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(4.dp, 24.dp).background(if (isServing) SuccessGreen else TextGray, RoundedCornerShape(2.dp)))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 20.sp, maxLines = 1)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) { repeat(3) { Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(if (it==0) SuccessGreen else Color.DarkGray)) } }
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

            // BOTTOM SETS CONTROLS
            Row(
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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

@Composable
fun NewBottomNavBar(onHomeClick: () -> Unit, onNewGameClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().background(DarkBackground).padding(vertical = 12.dp), contentAlignment = Alignment.BottomCenter) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onHomeClick() }) {
                Icon(Icons.Default.Home, "Home", tint = PrimaryBlue, modifier = Modifier.size(28.dp)); Spacer(modifier = Modifier.height(4.dp)); Text("Home", color = PrimaryBlue, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.offset(y = (-10).dp).clickable { onNewGameClick() }) {
                Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(PrimaryBlue).border(4.dp, DarkBackground.copy(alpha=0.5f), CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Default.Add, "New Game", tint = TextWhite, modifier = Modifier.size(32.dp)) }
                Spacer(modifier = Modifier.height(4.dp)); Text("New Game", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { }) {
                Icon(Icons.Default.DateRange, "Scoreboard", tint = TextGray, modifier = Modifier.size(28.dp)); Spacer(modifier = Modifier.height(4.dp)); Text("Scoreboard", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
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
fun SportCard(sport: SportType, isSelected: Boolean, onClick: () -> Unit) {
    val borderColor = if (isSelected) PrimaryBlue else Color.Transparent
    val bg = if (isSelected) PrimaryBlue.copy(alpha = 0.1f) else CardSurface
    Column(modifier = Modifier.width(100.dp).height(120.dp).clip(RoundedCornerShape(16.dp)).background(bg).border(2.dp, borderColor, RoundedCornerShape(16.dp)).clickable { onClick() }.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(sport.icon, null, tint = if (isSelected) PrimaryBlue else TextGray, modifier = Modifier.size(32.dp)); Spacer(modifier = Modifier.height(12.dp)); Text(sport.title, color = if (isSelected) PrimaryBlue else TextGray, fontSize = 12.sp)
    }
}

@Composable
fun TeamInput(label: String, icon: ImageVector, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(label, color = TextGray, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(value = value, onValueChange = onValueChange, leadingIcon = { Icon(icon, null, tint = PrimaryBlue) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CardSurface, unfocusedBorderColor = CardSurface, focusedContainerColor = CardSurface, unfocusedContainerColor = CardSurface, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite))
    }
}

@Composable
fun StatBox(title: String, value: String, modifier: Modifier) {
    Column(modifier = modifier.border(1.dp, CardSurface, RoundedCornerShape(8.dp)).padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, fontSize = 10.sp, color = TextGray); Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
    }
}