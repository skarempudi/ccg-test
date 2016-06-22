package com.example.srikar.magic.adapter;

import android.content.Context;

import com.example.srikar.magic.event.RecyclerViewEvent;
import com.example.srikar.magic.model.Permanent;

/**
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
     * When click creature, move to combat (move to combat list)
     * @param position
     */
    protected void onClick(PermanentViewHolder holder, int position) {
        super.onClick(holder, position);
        mBattlefield.moveToAttack(position);
    }

    @Override
    /**
     * Number of elements in list is equal to the number of creatures not in combat
     */
    public int getItemCount() {
        return mBattlefield.getViewPlayerCreaturesSize();
    }

    /**
     * EVENT BUS
     */
    @Override
    /**
     * On event bus, only listen for events affiliated with this subclass
     */
    public RecyclerViewEvent.Target getThisTarget() {
        return RecyclerViewEvent.Target.CREATURES;
    }
}
