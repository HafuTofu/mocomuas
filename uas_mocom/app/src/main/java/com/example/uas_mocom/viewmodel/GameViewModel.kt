package com.example.uas_mocom.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {

    private val _homeName = MutableStateFlow("Home")
    val homeName = _homeName.asStateFlow()

    private val _guestName = MutableStateFlow("Guest")
    val guestName = _guestName.asStateFlow()

    private val _homeScore = MutableStateFlow(0)
    val homeScore = _homeScore.asStateFlow()

    private val _guestScore = MutableStateFlow(0)
    val guestScore = _guestScore.asStateFlow()

    fun setupGame(homeName: String, guestName: String) {
        _homeName.value = homeName
        _guestName.value = guestName
        _homeScore.value = 0
        _guestScore.value = 0
    }

    fun updateHomeScore(delta: Int) {
        _homeScore.value = (_homeScore.value + delta).coerceAtLeast(0)
    }

    fun updateGuestScore(delta: Int) {
        _guestScore.value = (_guestScore.value + delta).coerceAtLeast(0)
    }

    fun resetGame() {
        _homeScore.value = 0
        _guestScore.value = 0
    }
}
