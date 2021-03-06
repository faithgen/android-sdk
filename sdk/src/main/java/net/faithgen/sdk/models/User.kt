package net.faithgen.sdk.models

data class User(
        val id: String,
        val name: String,
        val avatar: Avatar,
        val email: String,
        val phone: String,
        val provider: String,
        val is_admin: Boolean,
        val active: Boolean,
        val joined: Date
)