package com.example.srikar.magic.viewmodel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.srikar.magic.adapter.BaseBfRecViewAdapter;
import com.example.srikar.magic.adapter.CreaturesBfRecViewAdapter;

import rx.Subscription;

/**
 * Using data binding, the layout uses this View Model to interact with the rest of the code.
 * View model for the RecyclerView that stores creature Permanents
 * Created by Srikar on 6/21/2016.
 */
public class CreaturesBfRecViewModel extends BaseRecyclerViewModel {
    public CreaturesBfRecViewModel(Context appContext) {
        super(appContext);
    }

    @Override
    protected BaseBfRecViewAdapter getAdapter() {
        return new CreaturesBfRecViewAdapter(mContext);
    }

    @Override
    protected Subscription registerEventBus() {
        return null;
    }
}
