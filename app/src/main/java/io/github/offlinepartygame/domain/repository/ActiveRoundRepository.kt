package io.github.offlinepartygame.domain.repository

import io.github.offlinepartygame.domain.model.ActiveRound

interface ActiveRoundRepository {
    suspend fun getActiveRound(): ActiveRound?
    suspend fun saveActiveRound(round: ActiveRound)
    suspend fun clearActiveRound()
}
