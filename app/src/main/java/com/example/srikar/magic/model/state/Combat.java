package com.example.srikar.magic.model.state;

import com.example.srikar.magic.model.Card;

import java.util.ArrayList;
import java.util.List;

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
    private List<Card> attackers;

    public Combat() {
        duringCombat = false;
        attackConfirmed = false;
        attackers = new ArrayList<>();
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
        attackers.clear();
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

    /**
     * Add to list of attacking creatures
     * @param creature Creature that will attack
     */
    public void addAttacker(Card creature) {
        attackers.add(creature);
    }

    /**
     * Remove from list of attacking creatures
     * @param creature Creature that won't attack
     */
    public void removeAttacker(Card creature) {
        attackers.remove(creature);
    }
}
