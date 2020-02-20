package net.faithgen.sdk.http

/**
 * The links brought about by pagination
 */
final data class Links(
        val first: String,
        val last: String,
        val prev: String,
        val next: String
)