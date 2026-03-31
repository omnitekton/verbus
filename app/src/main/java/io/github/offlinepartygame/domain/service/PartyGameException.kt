package io.github.offlinepartygame.domain.service

import io.github.offlinepartygame.domain.model.PartyGameError

class PartyGameException(
    val error: PartyGameError,
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)
