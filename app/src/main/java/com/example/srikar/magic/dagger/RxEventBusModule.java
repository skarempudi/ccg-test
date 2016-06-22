package com.example.srikar.magic.dagger;

import com.example.srikar.magic.event.RecyclerViewEvent;
import com.example.srikar.magic.event.RxEventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * This event bus is used to note to listening RecyclerViews that their data set changed.
 * Created by Srikar on 6/7/2016.
 */
@Module
public class RxEventBusModule {
    @Provides
    @Singleton
    public RxEventBus<RecyclerViewEvent> provideRecyclerViewEventBus() {
        return new RxEventBus<>();
    }
}
