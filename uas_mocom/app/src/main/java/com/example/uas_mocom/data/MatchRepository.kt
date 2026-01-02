package com.example.uas_mocom.data

import kotlinx.coroutines.flow.Flow

class MatchRepository(private val dao: MatchDao) {
    val matches: Flow<List<MatchEntity>> = dao.observeMatches()

    suspend fun saveMatch(match: MatchEntity) {
        dao.insert(match)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}
