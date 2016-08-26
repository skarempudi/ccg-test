package com.example.srikar.magic.dagger;

import com.example.srikar.magic.event.GameStateChangeBus;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.event.RxEventBus;
import com.example.srikar.magic.model.GameState;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides a GameState object
 * Created by Srikar on 4/26/2016.
 */
@Module
class GameStateModule {
    @Provides
    @Singleton
    public GameState provideGameState(GameStateChangeBus gameStateChangeBus) {
        return new GameState(gameStateChangeBus);
    }
}
