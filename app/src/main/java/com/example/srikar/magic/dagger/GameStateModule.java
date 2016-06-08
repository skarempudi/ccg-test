package com.example.srikar.magic.dagger;

import com.example.srikar.magic.model.GameState;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides a GameState object
 * Created by Srikar on 4/26/2016.
 */
@Module
public class GameStateModule {
    @Provides
    @Singleton
    public GameState provideGameState() {
        return new GameState();
    }
}
