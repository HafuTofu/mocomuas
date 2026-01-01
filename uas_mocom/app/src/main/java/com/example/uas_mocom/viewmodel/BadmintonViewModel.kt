package com.example.uas_mocom.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BadmintonViewModel : BaseGameViewModel(initialTimeSeconds = 0, countUp = true) {

    private val _homeSets = MutableStateFlow(0)
    val homeSets = _homeSets.asStateFlow()

    private val _guestSets = MutableStateFlow(0)
    val guestSets = _guestSets.asStateFlow()

    private val _isHomeServing = MutableStateFlow(true)
    val isHomeServing = _isHomeServing.asStateFlow()

    fun updateSets(isHome: Boolean, delta: Int) {
        if (isHome) _homeSets.update { (it + delta).coerceAtLeast(0) }
        else _guestSets.update { (it + delta).coerceAtLeast(0) }
    }

    fun toggleServe() {
        _isHomeServing.update { !it }
    }
}