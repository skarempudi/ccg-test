package com.example.srikar.magic.adapter;

import android.content.Context;

import com.example.srikar.magic.event.RecyclerViewEvent;
import com.example.srikar.magic.model.Permanent;

/**
 * Adapter for the RecyclerView that shows the viewing player's lands
 * Created by Srikar on 6/2/2016.
 */
public class LandsBfRecViewAdapter extends BaseBfRecViewAdapter {
    private static final String TAG = "LandsBfRecViewAdapter";

    public LandsBfRecViewAdapter(Context activityContext) {
        super(activityContext);
    }

    /**
     * Get permanent from the land list
     * @param position Position clicked in RecyclerView, which should match position in data list
     * @return
     */
    @Override
    protected Permanent getPermanent(int position) {
        return mBattlefield.getViewPlayerLand(position);
    }

    @Override
    public int getItemCount() {
        return mBattlefield.getViewPlayerLandsSize();
    }
}
