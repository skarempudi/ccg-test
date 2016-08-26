package com.example.srikar.magic.viewmodel;

import android.databinding.BaseObservable;

import com.example.srikar.magic.event.ListChangeEvent;

/**
 * Base View Model for item views, HandCardViewModel and BattlefieldCardViewModel
 * Instead of directly working with the views, act through View Models, which are created by the
 * XML layout files
 * Created by Srikar on 5/20/2016.
 */
public abstract class BaseItemViewModel extends BaseObservable {
    int mListName;
    int mPosition;

    /**
     * Implement loading image.
     */
    public abstract void loadImage();

    /**
     * When RecyclerView Adapter binds View Holder, set the list and position where can find the
     * relevant Card or Permanent, then update the view
     * @param listName ListName constant from DataModelConstants: hand, lands, or creatures
     * @param position Position in that list, which should be the same as in the RecyclerView
     */
    public void setDataModel(int listName, int position) {
        mListName = listName;
        mPosition = position;

        //update view, which just involves image for now
        loadImage();
    }
}
