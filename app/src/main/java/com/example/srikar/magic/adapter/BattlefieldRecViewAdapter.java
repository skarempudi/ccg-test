package com.example.srikar.magic.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.srikar.magic.R;
import com.example.srikar.magic.databinding.PermanentBinding;
import com.example.srikar.magic.event.RecyclerViewEvent;
import com.example.srikar.magic.viewmodel.PermanentViewModel;
import com.example.srikar.magic.viewmodel.recyclerview.BaseRecyclerViewModel;

/**
 * Base adapter for the RecyclerViews that represent the Battlefield
 * Data model interaction is handled by the PermanentViewModel it creates and the RecyclerViewModel
 * that created it.
 * Created by Srikar on 5/20/2016.
 */
public class BattlefieldRecViewAdapter extends RecyclerView.Adapter<BattlefieldRecViewAdapter.PermanentViewHolder> {
    protected static final String TAG = "BattlefieldRecViewAdapter";
    protected final Context mContext;
    //the RecyclerViewModel that created this
    protected final BaseRecyclerViewModel mRecyclerViewModel;
    //which data model list from Battlefield to display
    protected final RecyclerViewEvent.Target mTargetList;

    /**
     * Constructor, takes RecyclerViewModel and Target to make sure data model interaction is handled
     * by view models instead of this
     * @param activityContext Context used to inflate layout
     * @param recViewModel RecyclerViewModel that created this Adapter
     * @param targetList Target that maps to a list in Battlefield, used to populate this RecyclerView
     */
    public BattlefieldRecViewAdapter(Context activityContext, BaseRecyclerViewModel recViewModel,
                                     RecyclerViewEvent.Target targetList) {
        super();
        mContext = activityContext;
        mRecyclerViewModel = recViewModel;
        mTargetList = targetList;
    }

    /**
     * Used to hold the layout data for each element of the list
     */
    public class PermanentViewHolder extends RecyclerView.ViewHolder {
        public PermanentBinding binding;
        public PermanentViewModel viewModel;

        public PermanentViewHolder(PermanentBinding binding, PermanentViewModel viewModel) {
            super(binding.getRoot());
            this.binding = binding;
            this.viewModel = viewModel;
        }
    }

    /**
     * Used to create the view holder for each list entry
     * When first create a list entry, create a binding to the permanent layout
     */
    @Override
    public PermanentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        return new PermanentViewHolder(binding, permViewModel);
    }

    /**
     * When list entry becomes visible on screen, store the new position in the view model
     */
    @Override
    public void onBindViewHolder(PermanentViewHolder holder, int position) {
        //set in the holder's view model the target list for this RecyclerView and the position
        holder.viewModel.setListPosition(mTargetList, position);
        //load the image
        holder.viewModel.loadImage();
    }

    /**
     * Used to determine the number of list elements to display
     */
    @Override
    public int getItemCount() {
        return mRecyclerViewModel.getItemCount();
    }
}
