package com.example.uas_mocom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.uas_mocom.data.MatchDatabase
import com.example.uas_mocom.data.MatchEntity
import com.example.uas_mocom.data.MatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameViewModel(private val repository: MatchRepository) : ViewModel() {

    private val _homeName = MutableStateFlow("Home")
    val homeName = _homeName.asStateFlow()

    private val _guestName = MutableStateFlow("Guest")
    val guestName = _guestName.asStateFlow()

    private val _homeScore = MutableStateFlow(0)
    val homeScore = _homeScore.asStateFlow()

    private val _guestScore = MutableStateFlow(0)
    val guestScore = _guestScore.asStateFlow()

    val matchHistory: StateFlow<List<MatchEntity>> = repository.matches
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

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

    fun saveCurrentMatch() {
        viewModelScope.launch {
            repository.saveMatch(
                MatchEntity(
                    homeTeam = _homeName.value,
                    guestTeam = _guestName.value,
                    homeScore = _homeScore.value,
                    guestScore = _guestScore.value,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                    ?: error("Application not found in ViewModelFactory")
                val database = MatchDatabase.getInstance(application.applicationContext)
                val repository = MatchRepository(database.matchDao())
                GameViewModel(repository)
            }
        }
    }
}
