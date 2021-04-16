package com.pictionary;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Register Parse models
        ParseObject.registerSubclass(Phrase.class);
        ParseObject.registerSubclass(Game.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("")
                .clientKey("")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
