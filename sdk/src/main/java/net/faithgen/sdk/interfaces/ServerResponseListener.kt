package net.faithgen.sdk.interfaces

import net.faithgen.sdk.http.ErrorResponse

interface ServerResponseListener {
    fun onResponse(serverResponse: String?)
    fun onError(errorResponse: ErrorResponse?)
}