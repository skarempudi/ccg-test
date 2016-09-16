package com.example.srikar.magic.model.state;

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
    private boolean duringCombat;
    private boolean attackConfirmed;

    public Combat() {
        duringCombat = false;
        attackConfirmed = false;
    }

    /**
     * Called by Turn at start of combat
     */
    public void startCombat() {
        duringCombat = true;
        attackConfirmed = false;
    }

    /**
     * Called by Turn after combat ends
     */
    public void endCombat() {
        duringCombat = false;
        attackConfirmed = false;
    }

    public boolean isDuringCombat() {
        return duringCombat;
    }

    /**
     * Called when confirm attackers
     */
    public void confirmAttack() {
        attackConfirmed = true;
    }

    public boolean isAttackConfirmed() {
        return attackConfirmed;
    }
}
