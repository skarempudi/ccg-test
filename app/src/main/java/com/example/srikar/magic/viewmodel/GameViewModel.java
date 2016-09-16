package com.example.srikar.magic.viewmodel;

/**
 * GameViewModels have an onDestroy() method implemented
 * Created by Srikar on 8/25/2016.
 */
public interface GameViewModel {
    /**
     * Used to unsubscribe when Activity goes through onDestroy()
     */
    void onDestroy();
}
