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
    static String apiBase;
    static String themeColor;

    private static void initializeConfig(InputStream inputStreamX) {
        inputStream = inputStreamX;
    }

    private static void initializeMenu(MenuChoice menuChoiceX) {
        menuChoice = menuChoiceX;
        Log.d("seleted_menu", String.valueOf(menuChoiceX));
    }

    /**
     * Sets the theme color to be used on the app for toolbar and icons
     *
     * @param themeColor_
     */
    public static void initializeThemeColor(String themeColor_) {
        themeColor = themeColor_;
    }

    /**
     * Use this to set the theme color of the app. You should use a string value from a value.xml file
     *
     * @return
     */
    public static String getThemeColor() {
        if (themeColor == null)
            return themeColor;
        else
            return "#" + themeColor.substring(3);
    }

    /**
     * This initializes the FaithGen SDK into your current project
     *
     * @param contextX      use the app context,its used for a lot of staff in the SDK
     * @param inputStreamX  use the InputStream of the config.json file. The SDK will make models out of that one
     * @param menuChoiceX   use the menu option you want to use on the app, we prefer you load the menu in your menu settings
     * @param apiBaseX      this is the apiBase e.g https://api.faithgen.net/api/ if you pass @null the SDK will use the faithgen baseUrl
     * @param subscriptionX this is the ministry`s subscription level
     */
    public static void initializeSDK(Context contextX, InputStream inputStreamX, MenuChoice menuChoiceX, Subscription subscriptionX) {
        initializeConfig(inputStreamX);
        initializeMenu(menuChoiceX);
        initializeContext(contextX);
        initializeSubscription(subscriptionX);
    }

    public static Subscription getSubscription() {
        return subscription;
    }

    public static void initializeApiBase(String apiBaseX) {
        apiBase = apiBaseX;
    }

    public static String getApiBase() {
        return apiBase;
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

    public static Ministry getMinistry() throws NullPointerException {
        if (MinistrySingleton.getInstance().getMinistry() == null)
            throw new NullPointerException("Couldn`t find any ministry, you have to set the proper config file");
        return MinistrySingleton.getInstance().getMinistry();
    }

    public static Config getConfig() {
        return MinistrySingleton.getInstance().getConfig();
    }
}
