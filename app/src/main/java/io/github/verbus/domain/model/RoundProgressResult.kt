package io.github.verbus.domain.model

data class RoundProgressResult(
    val activeRound: ActiveRound? = null,
    val summary: RoundSummary? = null,
    val warning: PartyGameError? = null,
)
