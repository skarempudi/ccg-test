package com.example.srikar.magic.adapter;

import android.content.Context;

import com.example.srikar.magic.event.RecyclerViewEvent;
import com.example.srikar.magic.model.Permanent;

/**
 * Adapter for the RecyclerView that displays viewing player's creatures
 * Created by Srikar on 6/2/2016.
 */
public class CreaturesBfRecViewAdapter extends BaseBfRecViewAdapter {
    private static final String TAG = "CreaturesBfRecViewAdapter";

    public CreaturesBfRecViewAdapter(Context activityContext) {
        super(activityContext);
    }

    @Override
    /**
     * Permanents are taken from the noncombat creature list
     * @param position
     */
    protected Permanent getPermanent(int position) {
        return mBattlefield.getViewPlayerCreature(position);
    }

    @Override
    /**
     * Number of elements in list is equal to the number of creatures not in combat
     */
    public int getItemCount() {
        return mBattlefield.getViewPlayerCreaturesSize();
    }
}
