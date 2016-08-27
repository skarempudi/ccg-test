package com.example.srikar.magic.viewmodel;

/**
 * GameViewModels can have their background updated to match the current player
 * Created by Srikar on 8/25/2016.
 */
public interface GameViewModel {
    /**
     * Used to unsubscribe when Activity goes through onDestroy()
     */
    void onDestroy();
}
