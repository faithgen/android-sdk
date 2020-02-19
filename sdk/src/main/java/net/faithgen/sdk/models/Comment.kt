package net.faithgen.sdk.models

data class Comment(
        val id: String,
        val comment: String,
        val creator: User,
        val date: Date
)