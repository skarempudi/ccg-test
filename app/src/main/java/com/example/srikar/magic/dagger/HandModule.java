package com.example.srikar.magic.dagger;

import com.example.srikar.magic.event.ListChangeBus;
import com.example.srikar.magic.model.state.PlayerInfo;
import com.example.srikar.magic.model.zone.Hand;

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
    public Hand provideHand(ListChangeBus listChangeBus, PlayerInfo playerInfo) {
        return new Hand(listChangeBus, playerInfo);
    }
}
