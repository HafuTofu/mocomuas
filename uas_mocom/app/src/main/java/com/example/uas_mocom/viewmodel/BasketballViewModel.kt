package com.example.uas_mocom.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BasketballViewModel : BaseGameViewModel(initialTimeSeconds = 12 * 60, countUp = false) {

    private val _homeFouls = MutableStateFlow(0)
    val homeFouls = _homeFouls.asStateFlow()

    private val _guestFouls = MutableStateFlow(0)
    val guestFouls = _guestFouls.asStateFlow()

    private val _quarter = MutableStateFlow(1)
    val quarter = _quarter.asStateFlow()

    fun updateHomeFouls(delta: Int) {
        _homeFouls.update { (it + delta).coerceAtLeast(0) }
    }

    fun updateGuestFouls(delta: Int) {
        _guestFouls.update { (it + delta).coerceAtLeast(0) }
    }

    fun setQuarter(q: Int) {
        _quarter.value = q
    }

    fun swapSides() {
        val tempName = _homeName.value; _homeName.value = _guestName.value; _guestName.value = tempName
        val tempScore = _homeScore.value; _homeScore.value = _guestScore.value; _guestScore.value = tempScore
        val tempFouls = _homeFouls.value; _homeFouls.value = _guestFouls.value; _guestFouls.value = tempFouls
    }
}