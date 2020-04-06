package net.faithgen.sdk.http

import net.faithgen.sdk.models.User

data class TypingResponse(
        val user: User,
        val status: String
)