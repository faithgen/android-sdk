package net.faithgen.sdk;

import android.content.Context;
import android.util.Log;

import net.faithgen.sdk.enums.Subscription;
import net.faithgen.sdk.menu.MenuChoice;
import net.faithgen.sdk.models.Config;
import net.faithgen.sdk.models.Ministry;
import net.faithgen.sdk.singletons.MinistrySingleton;

import java.io.InputStream;

public class SDK {
    static InputStream inputStream;
    static MenuChoice menuChoice;
    static Context context;
    static Subscription subscription;

    private static void initializeConfig(InputStream inputStreamX) {
        inputStream = inputStreamX;
    }

    private static void initializeMenu(MenuChoice menuChoiceX) {
        menuChoice = menuChoiceX;
        Log.d("seleted_menu", String.valueOf(menuChoiceX));
    }

    public static void initializeSDK(Context contextX, InputStream inputStreamX, MenuChoice menuChoiceX, Subscription subscriptionX) {
        initializeConfig(inputStreamX);
        initializeMenu(menuChoiceX);
        initializeContext(contextX);
        initializeSubscription(subscriptionX);
    }

    public static Subscription getSubscription() {
        return subscription;
    }

    private static void initializeContext(Context contextX) {
        context = contextX;
    }

    private static void initializeSubscription(Subscription subscriptionX) {
        subscription = subscriptionX;
    }

    public static Context getContext() {
        return context;
    }

    public static InputStream getInputStream() {
        return inputStream;
    }

    public static MenuChoice getMenuChoice() {
        return menuChoice;
    }

    public static void setInputStream(InputStream inputStream) {
        SDK.inputStream = inputStream;
    }

    public static Ministry getMinistry() {
        return MinistrySingleton.getInstance().getMinistry();
    }

    public static Config getConfig() {
        return MinistrySingleton.getInstance().getConfig();
    }
}
