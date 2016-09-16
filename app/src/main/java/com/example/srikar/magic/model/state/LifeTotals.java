package com.example.srikar.magic.model.state;

import com.example.srikar.magic.event.GameStateChangeBus;

/**
 * Used to keep track of life totals
 * When player drops to 0, triggers game end
 * Created by Srikar on 8/22/2016.
 */
public class LifeTotals extends BaseGameState {
    private static final int STARTING_LIFE = 20;

    private int[] lifeTotals = {STARTING_LIFE, STARTING_LIFE};

    public LifeTotals(GameStateChangeBus gameStateChangeBus) {
        super(gameStateChangeBus);
    }

    /**
     * Get the current life total for the given player
     * @param playerID Either DataModelConstants.PLAYER_ALICE or PLAYER_BOB
     * @return Life total
     */
    public int getLifeTotal(int playerID) {
        return lifeTotals[playerID];
    }
}
