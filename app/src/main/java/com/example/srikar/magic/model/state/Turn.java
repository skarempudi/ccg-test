package com.example.srikar.magic.model.state;

import com.example.srikar.magic.event.GameStateChangeBus;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.zone.Battlefield;

/**
 * Keeps track of the current turn number and the step in the turn, handles next step and turn
 * Created by Srikar on 9/15/2016.
 */
public class Turn extends BaseGameState {
    private static final String TAG = "Turn";

    private int mTurn; //what turn it currently is
    private int mStep; //what step in the turn it currently is

    /**
     * Used to determine if no creatures, to see if can skip steps
     */
    private final Battlefield mBattlefield;
    /**
     * Alerts player info about start of new turn
     */
    private final PlayerInfo mPlayerInfo;
    private final Combat mCombat;

    public Turn(GameStateChangeBus gameStateChangeBus, Battlefield battlefield, PlayerInfo playerInfo,
                Combat combat) {
        super(gameStateChangeBus);

        mBattlefield = battlefield;
        mPlayerInfo = playerInfo;
        mCombat = combat;

        mTurn = 1; //first turn is 1
        mStep = DataModelConstants.STEP_UNTAP; //first step is untap
    }

    /**
     * Get the current turn number, which is incremented by going through all steps in a turn
     * @return Turn number
     */
    public int getTurnNumber() {
        return mTurn;
    }

    /**
     * Get the current step, returning a value from DataModelConstants (STEP_)
     * @return The current step, as an int
     */
    public int getCurrentStep() {
        return mStep;
    }

    /**
     * Goes to the next step in the turn.
     * When it reaches the end of the steps, it goes to the next player
     */
    public void nextStep() {
        //next step will be in same turn
        if (mStep < DataModelConstants.STEP_END) {
            mStep++;

            //if starting combat with no creatures, instead go to second main phase
            if (mStep == DataModelConstants.STEP_DECLARE_ATTACKERS && mBattlefield.getCurrentPlayerCreaturesSize() == 0) {
                mStep = DataModelConstants.STEP_POSTCOMBAT_MAIN;
            }

            //change state of Combat
            switch (mStep) {
                case DataModelConstants.STEP_START_OF_COMBAT:
                    mCombat.startCombat();
                    break;

                case DataModelConstants.STEP_POSTCOMBAT_MAIN:
                    mCombat.endCombat();
                    break;
            }

            //add to event bus
            addGameStateChangeEvent(GameStateChangeEvent.NEXT_STEP);
        }
        //next step will be in next turn
        else {
            nextTurn();
        }
    }

    /**
     * Start the next turn
     */
    private void nextTurn() {
        //next turn
        mTurn++;
        //go to untap step
        mStep = DataModelConstants.STEP_UNTAP;
        //switch current player and change view player to it
        mPlayerInfo.onNextTurn();

        //have permanents untap for now current player
        mBattlefield.onUntapStep();

        //alert listening BoardFragmentModel to handle change in turn
        addGameStateChangeEvent(GameStateChangeEvent.NEXT_TURN);
    }
}
