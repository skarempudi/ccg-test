package com.example.srikar.magic.adapter;

import android.content.Context;

import com.example.srikar.magic.event.RecyclerViewEvent;
import com.example.srikar.magic.model.Permanent;

/**
 * Created by Srikar on 6/2/2016.
 */
public class LandsBfViewAdapter extends BaseBfViewAdapter {
    private static final String TAG = "LandsBfViewAdapter";

    public LandsBfViewAdapter(Context activityContext) {
        super(activityContext);
    }

    @Override
    protected Permanent getPermanent(int position) {
        return mBattlefield.getViewPlayerLand(position);
    }

    @Override
    /**
     * When click land, just logs data
     */
    protected void onClick(PermanentViewHolder holder, int position) {
        super.onClick(holder, position);
    }

    @Override
    public int getItemCount() {
        return mBattlefield.getViewPlayerLandsSize();
    }

    /**
     * EVENT BUS
     */
    @Override
    /**
     * On event bus, only listen for events affiliated with this subclass
     */
    public RecyclerViewEvent.Target getThisTarget() {
        return RecyclerViewEvent.Target.LANDS;
    }
}
