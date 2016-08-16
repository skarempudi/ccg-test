package com.example.srikar.magic.model.action;

/**
 * Class used to describe combat, including:
 * start of combat
 * declare attackers
 * declare blockers
 * combat damage
 * end of combat
 * Created by Srikar on 8/15/2016.
 */
public class Combat {
    private boolean attackConfirmed;

    public Combat() {
        attackConfirmed = false;
    }

    /**
     * Called when confirm attackers
     */
    public void setAttackers() {
        attackConfirmed = true;
    }

    public boolean isAttackConfirmed() {
        return attackConfirmed;
    }
}
