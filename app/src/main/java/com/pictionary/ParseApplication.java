package com.pictionary;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    private static final String BACK4APP_APPLICATION_ID = BuildConfig.BACK4APP_APPLICATION_ID;
    private static final String BACK4APP_CLIENT_KEY = BuildConfig.BACK4APP_CLIENT_KEY;

    @Override
    public void onCreate() {
        super.onCreate();

        // Register Parse models
        ParseObject.registerSubclass(Phrase.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(BACK4APP_APPLICATION_ID)
                .clientKey(BACK4APP_CLIENT_KEY)
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
