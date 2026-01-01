package com.example.uas_mocom.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VolleyballViewModel : BaseGameViewModel(initialTimeSeconds = 0, countUp = true) {

    private val _homeSets = MutableStateFlow(0)
    val homeSets = _homeSets.asStateFlow()

    private val _guestSets = MutableStateFlow(0)
    val guestSets = _guestSets.asStateFlow()

    private val _currentSet = MutableStateFlow(1)
    val currentSet = _currentSet.asStateFlow()

    fun updateSets(isHome: Boolean, delta: Int) {
        if (isHome) _homeSets.update { (it + delta).coerceAtLeast(0) }
        else _guestSets.update { (it + delta).coerceAtLeast(0) }
    }

    fun startNewSet() {
        _homeScore.value = 0
        _guestScore.value = 0
        _currentSet.update { it + 1 }
    }
}