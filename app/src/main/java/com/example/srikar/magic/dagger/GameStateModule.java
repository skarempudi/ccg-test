package com.example.srikar.magic.dagger;

import com.example.srikar.magic.event.GameStateChangeBus;
import com.example.srikar.magic.model.state.Combat;
import com.example.srikar.magic.model.state.LifeTotals;
import com.example.srikar.magic.model.state.PlayerInfo;
import com.example.srikar.magic.model.state.Turn;
import com.example.srikar.magic.model.zone.Battlefield;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides a state object
 * Created by Srikar on 4/26/2016.
 */
@Module
class GameStateModule {
    @Provides
    @Singleton
    public Combat provideCombat() {
        return new Combat();
    }

    @Provides
    @Singleton
    public LifeTotals provideLifeTotals(GameStateChangeBus gameStateChangeBus) {
        return new LifeTotals(gameStateChangeBus);
    }

    @Provides
    @Singleton
    public PlayerInfo providePlayerInfo(GameStateChangeBus gameStateChangeBus) {
        return new PlayerInfo(gameStateChangeBus);
    }

    @Provides
    @Singleton
    public Turn provideTurn(GameStateChangeBus gameStateChangeBus, Battlefield battlefield,
                            PlayerInfo playerInfo, Combat combat) {
        return new Turn(gameStateChangeBus, battlefield, playerInfo, combat);
    }
}
