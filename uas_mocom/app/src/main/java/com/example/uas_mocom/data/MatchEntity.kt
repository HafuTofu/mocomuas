package com.example.uas_mocom.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val homeTeam: String,
    val guestTeam: String,
    val homeScore: Int,
    val guestScore: Int,
    val timestamp: Long
)
