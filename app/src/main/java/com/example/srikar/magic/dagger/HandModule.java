package com.example.srikar.magic.dagger;

import com.example.srikar.magic.model.GameState;
import com.example.srikar.magic.model.Hand;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides a Hand object.
 * Created by Srikar on 4/27/2016.
 */
@Module
class HandModule {
    @Provides
    @Singleton
    public Hand provideHand(GameState state) {
        return new Hand(state);
    }
}
