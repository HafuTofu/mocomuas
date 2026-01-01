package com.example.uas_mocom.model

data class Match(
    val id: Int,
    val homeTeam: String,
    val guestTeam: String,
    val homeScore: Int,
    val guestScore: Int,
    val date: String
)

// Dummy data for recent matches
val dummyMatches = listOf(
    Match(1, "Lakers", "Bulls", 102, 98, "Dec 31, 2025"),
    Match(2, "Team A", "Team B", 3, 2, "Dec 30, 2025"),
    Match(3, "Warriors", "Celtics", 115, 110, "Dec 29, 2025"),
    Match(4, "Red Team", "Blue Team", 45, 42, "Dec 28, 2025"),
    Match(5, "Home", "Guest", 21, 18, "Dec 27, 2025")
)
