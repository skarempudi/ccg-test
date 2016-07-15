package com.example.srikar.magic.model;

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
}
