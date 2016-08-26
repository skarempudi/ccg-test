package com.example.srikar.magic.dagger;

import com.example.srikar.magic.event.GameStateChangeBus;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.event.ListChangeBus;
import com.example.srikar.magic.event.ListChangeEvent;
import com.example.srikar.magic.event.RxEventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * These event buses are used for communication from data models to view models
 * Created by Srikar on 6/7/2016.
 */
@Module
class RxEventBusModule {
    @Provides
    @Singleton
    public ListChangeBus provideListChangeEventBus() {
        return new ListChangeBus();
    }

    @Provides
    @Singleton
    public GameStateChangeBus provideGameStateChangeEventBus() {
        return new GameStateChangeBus();
    }
}
