package com.example.srikar.magic.dagger;

import com.example.srikar.magic.model.Hand;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides a Hand object.
 * Created by Srikar on 4/27/2016.
 */
@Module
public class HandModule {
    @Provides
    @Singleton
    public Hand provideHand() {
        return new Hand();
    }
}
