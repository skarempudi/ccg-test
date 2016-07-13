package com.example.srikar.magic.dagger;

import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.event.ListChangeEvent;
import com.example.srikar.magic.event.RxEventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * This event bus is used to note to listening RecyclerViews that their data set changed.
 * Created by Srikar on 6/7/2016.
 */
@Module
class RxEventBusModule {
    @Provides
    @Singleton
    public RxEventBus<ListChangeEvent> provideListChangeEventBus() {
        return new RxEventBus<>();
    }

    @Provides
    @Singleton
    public RxEventBus<GameStateChangeEvent> provideGameStateChangeEventBus() {
        return new RxEventBus<>();
    }
}
