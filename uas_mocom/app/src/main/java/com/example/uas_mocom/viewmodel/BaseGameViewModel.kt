package com.example.uas_mocom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

open class BaseGameViewModel(
    initialTimeSeconds: Long = 0,
    val countUp: Boolean = true
) : ViewModel() {

    protected val _homeName = MutableStateFlow("Home")
    val homeName = _homeName.asStateFlow()

    protected val _guestName = MutableStateFlow("Guest")
    val guestName = _guestName.asStateFlow()

    protected val _homeScore = MutableStateFlow(0)
    val homeScore = _homeScore.asStateFlow()

    protected val _guestScore = MutableStateFlow(0)
    val guestScore = _guestScore.asStateFlow()

    private val _timeSeconds = MutableStateFlow(initialTimeSeconds)
    val timeDisplay = MutableStateFlow(formatTime(initialTimeSeconds))

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning = _isTimerRunning.asStateFlow()

    private var timerJob: Job? = null

    // --- Common Actions ---

    fun setTeamNames(home: String, guest: String) {
        _homeName.value = home
        _guestName.value = guest
    }

    open fun updateHomeScore(delta: Int) {
        _homeScore.update { (it + delta).coerceAtLeast(0) }
    }

    open fun updateGuestScore(delta: Int) {
        _guestScore.update { (it + delta).coerceAtLeast(0) }
    }

    fun toggleTimer() {
        if (_isTimerRunning.value) pauseTimer() else startTimer()
    }

    fun resetTimer(resetTo: Long = 0) {
        pauseTimer()
        _timeSeconds.value = resetTo
        timeDisplay.value = formatTime(resetTo)
    }

    private fun startTimer() {
        timerJob?.cancel()
        _isTimerRunning.value = true
        timerJob = viewModelScope.launch {
            while (_isTimerRunning.value) {
                delay(1000L)
                val current = _timeSeconds.value
                val next = if (countUp) current + 1 else current - 1

                if (!countUp && next < 0) {
                    pauseTimer()
                } else {
                    _timeSeconds.value = next
                    timeDisplay.value = formatTime(next)
                }
            }
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
        _isTimerRunning.value = false
    }

    private fun formatTime(seconds: Long): String {
        val m = seconds / 60
        val s = seconds % 60
        return String.format(Locale.US, "%02d:%02d", m, s)
    }
}