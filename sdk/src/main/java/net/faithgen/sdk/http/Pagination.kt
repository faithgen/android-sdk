package net.faithgen.sdk.http

/**
 * Handles pagination from a server response with pagination data
 */
final data class Pagination(
        val links : Links,
        val meta: Meta
)