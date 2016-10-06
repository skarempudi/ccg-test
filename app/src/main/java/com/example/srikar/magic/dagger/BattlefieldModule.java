package com.example.srikar.magic.dagger;

import com.example.srikar.magic.event.ListChangeBus;
import com.example.srikar.magic.model.state.LifeTotals;
import com.example.srikar.magic.model.state.PlayerInfo;
import com.example.srikar.magic.model.zone.Battlefield;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides a Battlefield object
 * Created by Srikar on 5/18/2016.
 */
@Module
class BattlefieldModule {
    @Provides
    @Singleton
    public Battlefield provideBattlefield(ListChangeBus listChangeBus,
                                          PlayerInfo playerInfo,
                                          LifeTotals lifeTotals) {
        return new Battlefield(listChangeBus, playerInfo, lifeTotals);
    }
}
