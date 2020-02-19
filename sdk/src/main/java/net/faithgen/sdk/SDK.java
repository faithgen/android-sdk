package net.faithgen.sdk;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import net.faithgen.sdk.comments.CommentsActivity;
import net.faithgen.sdk.comments.CommentsDialog;
import net.faithgen.sdk.enums.CommentsDisplay;
import net.faithgen.sdk.enums.Subscription;
import net.faithgen.sdk.interfaces.UserAuthListener;
import net.faithgen.sdk.menu.MenuChoice;
import net.faithgen.sdk.comments.CommentsSettings;
import net.faithgen.sdk.models.Config;
import net.faithgen.sdk.models.Ministry;
import net.faithgen.sdk.models.User;
import net.faithgen.sdk.singletons.GSONSingleton;
import net.faithgen.sdk.singletons.MinistrySingleton;
import net.faithgen.sdk.utils.Constants;

import java.io.InputStream;

import nouri.in.goodprefslib.GoodPrefs;

public class SDK {
    private static InputStream inputStream;
    private static MenuChoice menuChoice;
    private static Context context;
    private static Subscription subscription;
    private static String apiBase;
    private static String themeColor;
    static User user;
    static UserAuthListener userAuthListener;

    private static void initializeUser(User userX){ user = userX;}

    private static void initializeConfig(InputStream inputStreamX) {
        inputStream = inputStreamX;
    }

    /**
     * Sets the theme color to be used on the app for toolbar and icons
     *
     * @param themeColor_ the theme color
     */
    public static void initializeThemeColor(String themeColor_) {
        themeColor = themeColor_;
    }

    /**
     * Use this to set the theme color of the app. You should use a string value from a value.xml file
     *
     * @return String
     */
    public static String getThemeColor() {
        if (themeColor == null)
            return themeColor;
        return "#" + themeColor.substring(3);
    }

    public static User getUser() {
        user = GoodPrefs.getInstance().getObject(Constants.USER, User.class);
        return user;
    }

    /**
     * This initializes the FaithGen SDK into your current project
     *
     * @param contextX      use the app context,its used for a lot of staff in the SDK
     * @param inputStreamX  use the InputStream of the config.json file. The SDK will make models out of that one
     * @param themeColor_ sets the themeColor
     */
    public static void initializeSDK(Context contextX, InputStream inputStreamX, String themeColor_, UserAuthListener userAuthListener_) {
        initializeConfig(inputStreamX);
        initializeContext(contextX);
        initializeThemeColor(themeColor_);
        initializeUserAuthListener(userAuthListener_);
    }

    public static Subscription getSubscription() {
        subscription = GoodPrefs.getInstance().getObject(Constants.SUBSCRIPTION_LEVEL, Subscription.class);
        if(subscription == null)
            subscription = Subscription.Free;
        return subscription;
    }

    private static void initializeUserAuthListener(UserAuthListener userAuthListenerx) {
        userAuthListener = userAuthListenerx;
    }
    /**
     * Initiliaze API bas path
     *
     * @param apiBaseX this is the apiBase e.g https://api.faithgen.net/api/ if you pass @null the SDK will use the faithgen baseUrl
     */
    public static void initializeApiBase(String apiBaseX) {
        apiBase = apiBaseX;
    }

    public static String getApiBase() {
        return apiBase;
    }

    private static void initializeContext(Context contextX) {
        context = contextX;
    }

    public static Context getContext() {
        return context;
    }

    public static InputStream getInputStream() {
        return inputStream;
    }

    public static MenuChoice getMenuChoice() {
        menuChoice = GoodPrefs.getInstance().getObject(Constants.MENU_OPTION, MenuChoice.class);
        if (menuChoice == null)
            menuChoice = MenuChoice.CONTEXTUAL_MENU;
        return menuChoice;
    }

    public static UserAuthListener getUserAuthListener() {
        return userAuthListener;
    }

    public static Ministry getMinistry() throws NullPointerException {
        if (MinistrySingleton.Companion.getInstance().getMinistry() == null)
            throw new NullPointerException("Couldn`t find any ministry, you have to set the proper config file");
        return MinistrySingleton.Companion.getInstance().getMinistry();
    }

    public static Config getConfig() {
        return MinistrySingleton.Companion.getInstance().getConfig();
    }

    public static void openComments(AppCompatActivity activity, CommentsSettings commentsSettings){
        if(commentsSettings.getCommentsDisplay().equals(CommentsDisplay.DIALOG)){
            CommentsDialog commentsDialog = new CommentsDialog(activity, commentsSettings);
            commentsDialog.show(activity.getSupportFragmentManager(), CommentsDialog.TAG);
        }else{
            Intent intent = new Intent(activity, CommentsActivity.class);
            intent.putExtra(Constants.SETTINGS, GSONSingleton.Companion.getInstance().getGson().toJson(commentsSettings));
            activity.startActivity(intent);
        }
    }
}
