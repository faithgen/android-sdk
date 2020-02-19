package net.faithgen.sdk.models

data class User(
        val id: String,
        val name: String,
        val picture: String,
        val email: String,
        val is_admin: Boolean
)