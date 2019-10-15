package net.faithgen.androidsdk;

import android.app.Application;

import net.faithgen.sdk.SDK;
import net.faithgen.sdk.enums.Subscription;
import net.faithgen.sdk.menu.MenuChoice;

import java.io.IOException;

public class TheApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            SDK.initializeSDK(this, this.getAssets().open("config.json"), MenuChoice.CONTEXTUAL_MENU, null, Subscription.PremiumPlus);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
