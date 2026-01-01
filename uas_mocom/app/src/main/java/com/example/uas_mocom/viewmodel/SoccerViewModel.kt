package com.example.uas_mocom.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SoccerViewModel : BaseGameViewModel(initialTimeSeconds = 0, countUp = true) {

    // Home Stats
    private val _homeYellow = MutableStateFlow(0)
    val homeYellow = _homeYellow.asStateFlow()

    private val _homeRed = MutableStateFlow(0)
    val homeRed = _homeRed.asStateFlow()

    private val _homeCorners = MutableStateFlow(0)
    val homeCorners = _homeCorners.asStateFlow()

    // Guest Stats
    private val _guestYellow = MutableStateFlow(0)
    val guestYellow = _guestYellow.asStateFlow()

    private val _guestRed = MutableStateFlow(0)
    val guestRed = _guestRed.asStateFlow()

    private val _guestCorners = MutableStateFlow(0)
    val guestCorners = _guestCorners.asStateFlow()

    fun updateCard(isHome: Boolean, isRed: Boolean, delta: Int) {
        if (isHome) {
            if (isRed) _homeRed.update { (it + delta).coerceAtLeast(0) }
            else _homeYellow.update { (it + delta).coerceAtLeast(0) }
        } else {
            if (isRed) _guestRed.update { (it + delta).coerceAtLeast(0) }
            else _guestYellow.update { (it + delta).coerceAtLeast(0) }
        }
    }

    fun updateCorners(isHome: Boolean, delta: Int) {
        if (isHome) _homeCorners.update { (it + delta).coerceAtLeast(0) }
        else _guestCorners.update { (it + delta).coerceAtLeast(0) }
    }
}