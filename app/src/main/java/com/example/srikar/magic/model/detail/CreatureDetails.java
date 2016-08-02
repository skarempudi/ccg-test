package com.example.srikar.magic.model.detail;

/**
 * Used to store details exclusive to creature, like power and toughness
 * Created by Srikar on 7/28/2016.
 */
public class CreatureDetails extends BaseDetails {
    /**
     * Creature power and toughness
     * For now, will always be 3/3
     */
    public int power, toughness;

    public CreatureDetails() {
        power = 3;
        toughness = 3;
    }
}
