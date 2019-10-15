package net.faithgen.sdk.singletons;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.itishka.gsonflatten.FlattenTypeAdapterFactory;

public class GSONSingleton {
    private static GSONSingleton gsonSingleton = new GSONSingleton();
    private Gson gson;

    public static GSONSingleton getInstance() {
        return gsonSingleton;
    }

    public GSONSingleton() {
        gson = new GsonBuilder()
                .registerTypeAdapterFactory(new FlattenTypeAdapterFactory())
                .serializeNulls()
                .create();
    }

    public Gson getGson() {
        return gson;
    }
}
