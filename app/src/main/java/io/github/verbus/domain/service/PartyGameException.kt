package io.github.verbus.domain.service

import io.github.verbus.domain.model.PartyGameError

class PartyGameException(
    val error: PartyGameError,
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)
