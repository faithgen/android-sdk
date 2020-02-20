package net.faithgen.sdk.http

import net.faithgen.sdk.models.Comment

final data class Response<T>(
        val data : T,
        val success : Boolean,
        val message : String,
        val comment : Comment
)