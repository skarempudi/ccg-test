package com.example.srikar.magic;

import android.app.Application;

import com.example.srikar.magic.dagger.DaggerMainComponent;
import com.example.srikar.magic.dagger.MainComponent;

/**
 * Created by Srikar on 4/26/2016.
 */
public class MagicApplication extends Application {
    private static MagicApplication INSTANCE;

    MainComponent mainComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        mainComponent = DaggerMainComponent
                                .builder()
                                .build();
    }

    public static MagicApplication getInstance() {
        return INSTANCE;
    }

    public MainComponent getMainComponent() {
        return mainComponent;
    }
}
