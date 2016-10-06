package com.example.srikar.magic.model.state;

import com.example.srikar.magic.event.GameStateChangeBus;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.model.Card;

import java.util.List;

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
     * @param playerId Either DataModelConstants.PLAYER_ALICE or PLAYER_BOB
     * @return Life total
     */
    public int getLifeTotal(int playerId) {
        return lifeTotals[playerId];
    }

    /**
     * Given list of creatures, each deals damage equal to its power to given player
     * @param playerId Player being damaged
     * @param creatures List of creatures attacking the player
     */
    public void dealCombatDamage(int playerId, List<Card> creatures) {
        int totalDamage = 0;
        for (Card creature : creatures) {
            totalDamage += creature.details.power;
        }
        lifeTotals[playerId] -= totalDamage;
        //alert life counter
        addGameStateChangeEvent(GameStateChangeEvent.LIFE_CHANGE);
    }
}
