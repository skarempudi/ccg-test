package com.example.srikar.magic.viewmodel.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.srikar.magic.adapter.HandViewAdapter;
import com.example.srikar.magic.event.RecyclerViewEvent;

/**
 * Using data binding, the layout uses this View Model to interact with the rest of the code.
 * View model for the RecyclerView that stores Cards in hand
 * Created by Srikar on 6/29/2016.
 */
public class HandRecViewModel extends BaseRecyclerViewModel {
    public HandRecViewModel(Context appContext) {
        super(appContext);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new HandViewAdapter(mContext);
    }

    @Override
    protected RecyclerViewEvent.Target getThisTarget() {
        return RecyclerViewEvent.Target.HAND;
    }


}
