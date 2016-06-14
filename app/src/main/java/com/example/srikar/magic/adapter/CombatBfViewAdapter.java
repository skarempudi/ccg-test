package com.example.srikar.magic.adapter;

import android.content.Context;

import com.example.srikar.magic.event.RecyclerViewEvent;
import com.example.srikar.magic.model.Permanent;


/**
 * Adapter for the RecyclerView for creatures in combat
 * Created by Srikar on 6/2/2016.
 */
public class CombatBfViewAdapter extends BaseBfViewAdapter {
    private static final String TAG = "CombatBfViewAdapter";

    public CombatBfViewAdapter(Context activityContext) {
        super(activityContext);
    }

    @Override
    /**
     * Permanents are taken from the combat list
     * @param position
     */
    protected Permanent getPermanent(int position) {
        return mBattlefield.getViewPlayerCombatCreature(position);
    }

    @Override
    /**
     * When click creature in combat, remove from combat (move back to creature list)
     * @param position
     */
    protected void onClick(PermanentViewHolder holder, int position) {
        super.onClick(holder, position);
        mBattlefield.undoAttackDeclaration(position);
    }

    @Override
    /**
     * Number of items displayed in list is number of creatures in combat
     */
    public int getItemCount() {
        return mBattlefield.getViewPlayerCombatSize();
    }

    /**
     * EVENT BUS
     */
    @Override
    /**
     * On event bus, only listen for events affiliated with this subclass
     */
    public RecyclerViewEvent.Target getThisTarget() {
        return RecyclerViewEvent.Target.COMBAT;
    }
}
