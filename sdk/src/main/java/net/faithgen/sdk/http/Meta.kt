package net.faithgen.sdk.http

final data class Meta(
        val current_page: Int,
        val from: Int,
        val to: Int,
        val total: Int,
        val per_page: Int,
        val path: String
)