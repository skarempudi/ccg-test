package com.example.srikar.magic.dagger;

import com.example.srikar.magic.event.RecyclerViewEvent;
import com.example.srikar.magic.event.RxEventBus;
import com.example.srikar.magic.model.Battlefield;
import com.example.srikar.magic.model.GameState;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides a Battlefield object
 * Created by Srikar on 5/18/2016.
 */
@Module(includes = RxEventBusModule.class)
class BattlefieldModule {
    @Provides
    @Singleton
    public Battlefield provideBattlefield(RxEventBus<RecyclerViewEvent> rvEventBus,
                                          GameState state) {
        return new Battlefield(rvEventBus, state);
    }
}
