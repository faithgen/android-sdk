package net.faithgen.sdk.interfaces

import net.faithgen.sdk.http.ErrorResponse

/**
 * Listens to HTTP events
 */
interface ServerResponseListener {
    /**
     * Handles the response on a successful request
     *
     * @param serverResponse The response from the server
     */
    fun onResponse(serverResponse: String?)

    /**
     * Handles the response on a failed request
     *
     * @param errorResponse The manufactured error response from Volley
     */
    fun onError(errorResponse: ErrorResponse?)
}