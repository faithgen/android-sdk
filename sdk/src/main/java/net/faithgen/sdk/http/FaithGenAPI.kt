package net.faithgen.sdk.http

import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import net.faithgen.sdk.SDK
import net.faithgen.sdk.http.types.ServerResponse
import net.faithgen.sdk.singletons.VolleySingleton
import net.faithgen.sdk.utils.Progress
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class FaithGenAPI(val context: Context) {
    private var params: HashMap<String, String>? = null
    private var finish: Boolean = false
    private var route: String? = null
    private var isSilentCall: Boolean = false
    private var stringRequest: StringRequest? = null
    private var method: Int = Request.Method.GET
    private var serverResponse: ServerResponse? = null
    private var errorResponse: ErrorResponse? = null
    private var headers: HashMap<String, String> = hashMapOf()

    /**
     * Makes the Http call with the given data
     */
    fun request(url: String) : FaithGenAPI {
        this.route = url

        if(method === Request.Method.GET || method === Request.Method.PUT)
            this.route = trimUrl(url)

        headers[API_KEY] = SDK.getMinistry().apiKey

        if (isSilentCall) makeSilentRequest()
        else makeRequest()
        return this
    }

    /**
     * Encodes parameters for get request
     */
    private fun encodeParams(hasPage: Boolean): String {
        val iterator = params!!.entries.iterator()
        var encodedQueries = ""
        while (iterator.hasNext()) {
            val entry = iterator.next() as Map.Entry<String, String>
            try {
                encodedQueries += "&" + URLEncoder.encode(entry.key, "UTF-8") + "=" + URLEncoder.encode(entry.value, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            iterator.remove()
        }
        return (if (hasPage) "&" else "?") + encodedQueries.substring(1)
    }

    /**
     * Trims the url
     */
    private fun trimUrl(url: String): String {
        val paramsIndex: Int = url.indexOf("?")
        if (params == null) return url
        if (url.contains("page=")) return url + encodeParams(true)
        else if (paramsIndex == -1)
            return url + encodeParams(false);
        return url.substring(0, paramsIndex) + encodeParams(false);
    }

    /**
     * Sets the call to be silent or one that shows
     */
    fun setIsSilentCall(isSilentCall: Boolean): FaithGenAPI {
        this.isSilentCall = isSilentCall
        return this
    }

    /**
     * Sets the route for the request
     */
    fun setMethod(method: Int): FaithGenAPI {
        this.method = method
        return this
    }

    /**
     * Sets the parameters for the request
     */
    fun setParams(params: HashMap<String, String>?): FaithGenAPI {
        this.params = params
        return this
    }

    /**
     * Sets whether or not the dialog should close on failure
     */
    fun setFinishOnFail(finish: Boolean): FaithGenAPI {
        this.finish = finish
        return this
    }

    /**
     * Formats the route
     */
    private fun getRoute(): String {
        if (route == null) throw NullPointerException("You did not set the route to this request")
        if (route!!.contains("http")) return route!!
        if (SDK.getApiBase() != null)
            return SDK.getApiBase() + route;
        return ROOT_PATH + route;
    }

    /**
     * Sets the reaction to a complete or failed network request
     */
    fun setServerResponse(serverResponse: ServerResponse?): FaithGenAPI {
        this.serverResponse = serverResponse
        return this
    }

    fun cancelRequests() {
        VolleySingleton.getInstance().requestQueue.cancelAll(REQUEST_TAG)
    }

    /**
     * Makes the API call
     */
    private fun makeRequest() {
        Progress.showProgress(context, null)
        stringRequest = object : StringRequest(method, getRoute(), { response ->
            Progress.dismissProgress()
            if (serverResponse != null) {
                serverResponse!!.setUpCall(context!!, finish)
                serverResponse!!.onResponse(response)
            }
        }, { error: VolleyError? ->
            error!!.printStackTrace()
            Progress.dismissProgress()
            processError(error)
        }) {
            override fun getParams(): MutableMap<String, String> {
                if (params != null)
                    return params
                return super.getParams()
            }

            override fun getHeaders(): MutableMap<String, String> {

                return addUserToHeaders()
            }
        }
        launchRequest()
    }

    private fun launchRequest() {
        stringRequest?.setShouldCache(false)
        stringRequest?.tag = REQUEST_TAG
        VolleySingleton.getInstance().requestQueue.add(stringRequest)
    }

    /**
     * Processes the server response
     */
    private fun processError(error: VolleyError) {
        errorResponse = ErrorResponse.makeErrorResponse(error)
        if (serverResponse != null)
            serverResponse?.onError(errorResponse)
    }

    /**
     * Adds the user headers if the user is authenticated
     */
    private fun addUserToHeaders(): HashMap<String, String> {
        if (SDK.getUser() != null) headers[USER_KEY] = SDK.getUser().id
        return headers
    }

    private fun makeSilentRequest() {}

    companion object {
        const val ROOT_PATH = "http://192.168.8.101:8001/api/"
        const val REQUEST_TAG = "Request_XTag"
        const val API_KEY = "x-api-key"
        const val USER_KEY = "x-user-key"
    }
}