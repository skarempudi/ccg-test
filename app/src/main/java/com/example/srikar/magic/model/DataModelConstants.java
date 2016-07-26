package com.example.srikar.magic.model;

import com.example.srikar.magic.R;

/**
 * Used to store constants related to the data model.
 * Created by Srikar on 6/13/2016.
 */
public class DataModelConstants {
    /**
     * List name
     * Used to access lists from view models and provide address for events in event bus.
     */
    public static final int LIST_HAND = 0, LIST_LANDS = 1, LIST_CREATURES = 2;

    /**
     * In classes like Battlefield, store data for both players in array
     * These store the index that each player uses
     */
    public static final int PLAYER_ALICE = 0, PLAYER_BOB = 1;

    /**
     * Game steps
     * All of the steps in a turn
     */
    public static final int STEP_UNTAP = 0, STEP_UPKEEP = 1, STEP_DRAW = 2, STEP_PRECOMBAT_MAIN = 3,
        STEP_START_OF_COMBAT = 4, STEP_DECLARE_ATTACKERS = 5, STEP_DECLARE_BLOCKERS = 6,
        STEP_COMBAT_DAMAGE = 7, STEP_END_OF_COMBAT = 8, STEP_POSTCOMBAT_MAIN = 9, STEP_END = 10;

    /**
     * Get the string resource ID affiliated with the log display for a step
     * @param step Step, taken from DataModelConstants with STEP_ prefix
     * @return String resource ID
     */
    public static int getStepLogText(int step) {
        switch(step) {
            case STEP_UNTAP:
                return R.string.step_untap;
            case STEP_UPKEEP:
                return R.string.step_upkeep;
            case STEP_DRAW:
                return R.string.step_draw;
            case STEP_PRECOMBAT_MAIN:
                return R.string.step_precombat_main;
            case STEP_START_OF_COMBAT:
                return R.string.step_start_of_combat;
            case STEP_DECLARE_ATTACKERS:
                return R.string.step_declare_attackers;
            case STEP_DECLARE_BLOCKERS:
                return R.string.step_declare_blockers;
            case STEP_COMBAT_DAMAGE:
                return R.string.step_combat_damage;
            case STEP_END_OF_COMBAT:
                return R.string.step_end_of_combat;
            case STEP_POSTCOMBAT_MAIN:
                return R.string.step_postcombat_main;
            case STEP_END:
                return R.string.step_end;

            default:
                return 0;
        }
    }
}
