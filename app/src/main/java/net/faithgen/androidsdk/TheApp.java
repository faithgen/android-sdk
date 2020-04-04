package net.faithgen.androidsdk;

import android.annotation.SuppressLint;
import android.app.Application;

import net.faithgen.sdk.SDK;
import net.faithgen.sdk.enums.Subscription;
import net.faithgen.sdk.menu.MenuChoice;

import java.io.IOException;

import nouri.in.goodprefslib.GoodPrefs;

public class TheApp extends Application {
    @SuppressLint("ResourceType")
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            GoodPrefs.init(this);
            SDK.initializeApiBase("http://192.168.8.100:8001/api/");
            SDK.initializeSDK(this, this.getAssets().open("config.json"), getResources().getString(R.color.colorPrimaryDark), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
