package com.example.srikar.magic.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.srikar.magic.R;
import com.example.srikar.magic.databinding.PermanentBinding;
import com.example.srikar.magic.event.ListChangeEvent;
import com.example.srikar.magic.viewmodel.PermanentViewModel;
import com.example.srikar.magic.viewmodel.recyclerview.BaseRecyclerViewModel;

/**
 * Base adapter for the RecyclerViews that represent the Battlefield
 * Data model interaction is handled by the PermanentViewModel it creates and the RecyclerViewModel
 * that created it.
 * Created by Srikar on 5/20/2016.
 */
public class BattlefieldRecViewAdapter extends BaseRecViewAdapter {
    private static final String TAG = "BattlefieldRecViewAdapter";

    /**
     * Constructor, takes RecyclerViewModel and ListName to make sure data model interaction is handled
     * by view models instead of this
     * @param activityContext Context used to inflate layout
     * @param recViewModel RecyclerViewModel that created this Adapter
     * @param listName ListName that maps to a list in Battlefield, used to populate this RecyclerView
     */
    public BattlefieldRecViewAdapter(Context activityContext, BaseRecyclerViewModel recViewModel,
                                     int listName) {
        super(activityContext, recViewModel, listName);
    }

    /**
     * Used to create the view holder for each list entry
     * When first create a list entry, create a binding to the permanent layout
     */
    @Override
    public BaseRecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PermanentBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.permanent,
                parent,
                false //don't attach to parent, handled by RecyclerView
        );

        //create new view model, which takes binding
        //the view model handles onClick events and such
        PermanentViewModel permViewModel = new PermanentViewModel(binding);

        //create the view holder
        return new BaseRecViewHolder(binding, permViewModel);
    }
}
