package net.faithgen.sdk.singletons

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.itishka.gsonflatten.FlattenTypeAdapterFactory

class GSONSingleton {
    val gson: Gson

    companion object {
        val instance = GSONSingleton()
    }

    init {
        gson = GsonBuilder()
                .registerTypeAdapterFactory(FlattenTypeAdapterFactory())
                .disableHtmlEscaping()
                .serializeNulls()
                .create()
    }
}