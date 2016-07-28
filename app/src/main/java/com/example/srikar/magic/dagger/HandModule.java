package com.example.srikar.magic.dagger;

import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.event.ListChangeEvent;
import com.example.srikar.magic.event.RxEventBus;
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
    public Hand provideHand(RxEventBus<ListChangeEvent> rvEventBus, GameState state,
                            RxEventBus<GameStateChangeEvent> gscEventBus) {
        return new Hand(rvEventBus, state, gscEventBus);
    }
}
