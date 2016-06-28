package com.example.srikar.magic.viewmodel.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.srikar.magic.adapter.LandsBfRecViewAdapter;
import com.example.srikar.magic.event.RecyclerViewEvent;

/**
 * Using data binding, the layout uses this View Model to interact with the rest of the code.
 * View model for the RecyclerView that stores land Permanents
 * Created by Srikar on 6/22/2016.
 */
public class LandsBfRecViewModel extends BaseRecyclerViewModel {
    public LandsBfRecViewModel(Context appContext) {
        super(appContext);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new LandsBfRecViewAdapter(mContext);
    }

    @Override
    protected RecyclerViewEvent.Target getThisTarget() {
        return RecyclerViewEvent.Target.LANDS;
    }
}
