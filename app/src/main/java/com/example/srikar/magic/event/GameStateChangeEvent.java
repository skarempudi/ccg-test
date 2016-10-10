package com.example.srikar.magic.event;

/**
 * Events used to notify that state has changed in a certain way, listened to by the view models
 * Created by Srikar on 7/12/2016.
 */
public class GameStateChangeEvent implements MagicEvent {
    /**
     * Actions
     */
    public static final int SWITCH_VIEW_PLAYER = 0; //changes backgrounds
    public static final int NEXT_STEP = 1; //next step, updates game log
    public static final int NEXT_TURN = 2; //next turn, updates game log and changes backgrounds
    /**
     * Life totals have changed, updated display
     * Requires detail, player ID whose life has changed
     */
    public static final int LIFE_CHANGE = 3;

    /**
     * Details
     * Most actions don't use these
     * With LIFE_CHANGE, detail is player ID whose life was changed
     */
    public static final int NO_DETAIL = -1; //

    public final int action;
    public final int detail;

    public GameStateChangeEvent(int updateAction) {
        action = updateAction;
        detail = NO_DETAIL;
    }

    /**
     * Some events, like LIFE_CHANGE, will take an additional detail, such as player ID in that case
     * @param updateAction Action taken from this class
     * @param msgDetail Detail, which can vary
     */
    public GameStateChangeEvent(int updateAction, int msgDetail) {
        action = updateAction;
        detail = msgDetail;
    }

    @Override
    public String toString() {
        String actions[] = {"switch view player", "next step", "next turn", "life change"};
        String detailStr = "";
        if (action == LIFE_CHANGE) {
            String details[] = {" Alice", " Bob"};
            detailStr = details[detail];
        }
        return actions[action] + detailStr;
    }
}
