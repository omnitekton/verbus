package io.github.verbus.domain.repository

import io.github.verbus.domain.model.ActiveRound

interface ActiveRoundRepository {
    suspend fun getActiveRound(): ActiveRound?
    suspend fun saveActiveRound(round: ActiveRound)
    suspend fun clearActiveRound()
}
