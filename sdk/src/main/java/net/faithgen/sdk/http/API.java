package net.faithgen.sdk.http;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import net.faithgen.sdk.SDK;
import net.faithgen.sdk.http.types.ServerResponse;
import net.faithgen.sdk.http.types.ServerSilentResponse;
import net.faithgen.sdk.singletons.VolleySingleton;
import net.faithgen.sdk.utils.Progress;

import java.util.HashMap;
import java.util.Map;

public class API {
    private static final String API_KEY = "API-KEY";
    private static final String REQUEST_TAG = "Request_XTag";
    private static String ROOT_PATH = "http://192.168.8.100:8001/app/";
    private static StringRequest stringRequest;
    private static ErrorResponse errorResponse;

    private static String getRoute(String route) {
        if (route.contains("http")) return route;
        else return ROOT_PATH + route;
    }

    /**
     * Makes a post request for the user
     *
     * @param context        the context to show some progress on
     * @param route          the route you want to post to
     * @param params         the parameters you want to send to the server
     * @param finishOnFail   whether or not to close the current activity when the request fails
     * @param serverResponse the one to return server data for further processing on success
     */
    public static void post(Context context, String route, HashMap<String, String> params, boolean finishOnFail, ServerResponse serverResponse) {
        makeRequest(context, Request.Method.POST, route, params, finishOnFail, serverResponse);
    }

    /**
     * Makes a {@link #post(Context, String, HashMap, boolean, ServerResponse)}  POST} request but without some progress displayed
     *
     * @param route
     * @param params
     * @param errorListener the callback to run when it fails
     */
    public static void silentPost(String route, HashMap<String, String> params, ServerSilentResponse errorListener) {
        makeSilentRequest(Request.Method.POST, route, params, errorListener);
    }

    /**
     * Makes a get request
     *
     * @param context        the context to show some progress on
     * @param route          the route you want to get from
     * @param params         the parameters you want to send to the server
     * @param finishOnFail   whether or not to close the current activity when the request fails
     * @param serverResponse the one to return server data for further processing on success
     */
    public static void get(Context context, String route, HashMap<String, String> params, boolean finishOnFail, ServerResponse serverResponse) {
        makeRequest(context, Request.Method.GET, route, params, finishOnFail, serverResponse);
    }

    /**
     * Makes a {@link #get(Context, String, HashMap, boolean, ServerResponse) GET} request without the progress display
     *
     * @param route
     * @param params
     * @param serverSilentResponse the callback to run when it fails
     */
    public static void silentGet(String route, HashMap<String, String> params, ServerSilentResponse serverSilentResponse) {
        makeSilentRequest(Request.Method.GET, route, params, serverSilentResponse);
    }

    /**
     * This runs a delete request
     *
     * @param context        the context to show some progress on
     * @param route          the route you want to delete from
     * @param params         the parameters you want to send to the server
     * @param finishOnFail   whether or not to close the current activity when the request fails
     * @param serverResponse the one to return server data for further processing on success
     */
    public static void delete(Context context, String route, HashMap<String, String> params, boolean finishOnFail, ServerResponse serverResponse) {
        makeRequest(context, Request.Method.DELETE, route, params, finishOnFail, serverResponse);
    }

    /**
     * Makes a {@link #delete(Context, String, HashMap, boolean, ServerResponse)  DELETE} request but without progress display
     *
     * @param route
     * @param params
     * @param serverResponse
     */
    public static void silentDelete(String route, HashMap<String, String> params, ServerSilentResponse serverResponse) {
        makeSilentRequest(Request.Method.DELETE, route, params, serverResponse);
    }

    /**
     * Makes a put request
     *
     * @param context        the context to show some progress on
     * @param route          the route you want to put to
     * @param params         the parameters you want to send to the server
     * @param finishOnFail   whether or not to close the current activity when the request fails
     * @param serverResponse the one to return server data for further processing on success
     */
    public static void put(Context context, String route, HashMap<String, String> params, boolean finishOnFail, ServerResponse serverResponse) {
        makeRequest(context, Request.Method.PUT, route, params, finishOnFail, serverResponse);
    }

    /**
     * Makes a {@link #put(Context, String, HashMap, boolean, ServerResponse)}  PUT} request but without progress display
     *
     * @param route
     * @param params
     * @param serverSilentResponse
     */
    public static void silentPut(String route, HashMap<String, String> params, ServerSilentResponse serverSilentResponse) {
        makeSilentRequest(Request.Method.PUT, route, params, serverSilentResponse);
    }

    private static void makeRequest(Context context, int method, String route, HashMap<String, String> params, boolean finishOnFail, ServerResponse serverResponse) {
        Progress.showProgress(context, null);
        stringRequest = new StringRequest(method, getRoute(route), response -> {
            Log.d("Tag", "makeRequest: " + response);
            Progress.dismissProgress();
            if (serverResponse != null) {
                serverResponse.setUpCall(context, finishOnFail);
                serverResponse.onResponse(response);
            }
        }, error -> {
            error.printStackTrace();
            Progress.dismissProgress();
            errorResponse = ServerResponseListener.makeErrorResponse(error);
            if (serverResponse != null)
                serverResponse.onError(errorResponse);
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (params != null)
                    return params;
                else return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(API_KEY, SDK.getMinistry().getApiKey());
                return headers;
            }
        };

        stringRequest.setShouldCache(false);
        stringRequest.setTag(REQUEST_TAG);
        VolleySingleton.getInstance().getRequestQueue().add(stringRequest);
    }

    private static void makeSilentRequest(int method, String route, HashMap<String, String> params, ServerSilentResponse serverResponse) {
        Log.d("server_access", "makeSilentRequest: " + getRoute(route));

        stringRequest = new StringRequest(method, getRoute(route), response -> {
            Log.d("server_response", "onResponse: " + response);
            if (serverResponse != null)
                serverResponse.onResponse(response);
        }, error -> {
            errorResponse = ServerResponseListener.makeErrorResponse(error);
            if (serverResponse != null)
                serverResponse.onError(errorResponse);
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (params != null)
                    return params;
                else return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(API_KEY, SDK.getMinistry().getApiKey());
                return headers;
            }
        };
        stringRequest.setShouldCache(false);
        stringRequest.setTag(REQUEST_TAG);
        VolleySingleton.getInstance().getRequestQueue().add(stringRequest);
    }

    /**
     * Releases resources used by the request queue
     */
    public void cancelRequests() {
        VolleySingleton.getInstance().getRequestQueue().cancelAll(REQUEST_TAG);
    }
}
