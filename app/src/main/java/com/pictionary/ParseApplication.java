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

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("YY2PjRR8IDpkQ6K0ufxTvqRdUu5pfn70TSfn5JnN")
                .clientKey("Y0GaPpCcPq6mNOhghVU3FwHCG1r65fKldTA7ghOM")
                .server("https://parseapi.back4app.com")
                .build()
        );


    }
}
