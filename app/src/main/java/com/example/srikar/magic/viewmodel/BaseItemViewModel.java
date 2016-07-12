package com.example.srikar.magic.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.example.srikar.magic.R;
import com.example.srikar.magic.event.RecyclerViewEvent;
import com.squareup.picasso.Picasso;

/**
 * Base View Model for item views, CardViewModel and PermanentViewModel
 * Instead of directly working with the views, act through View Models, which are created by the
 * XML layout files
 * Created by Srikar on 5/20/2016.
 */
public abstract class BaseItemViewModel extends BaseObservable {
    RecyclerViewEvent.Target mTargetList;
    int mPosition;

    /**
     * Implement loading image.
     */
    public abstract void loadImage();

    /**
     * When RecyclerView Adapter binds View Holder, set the list and position where can find the
     * relevant Card or Permanent.
     * @param targetList Target that maps to a data model list
     * @param position Position in that list, which should be the same as in the RecyclerView
     */
    public void setListPosition(RecyclerViewEvent.Target targetList, int position) {
        mTargetList = targetList;
        mPosition = position;
    }
}
