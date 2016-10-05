package com.example.srikar.magic.view;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.srikar.magic.UiUtil;
import com.example.srikar.magic.viewmodel.list.BaseCardListViewModel;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

/**
 * Base Adapter for RecyclerViews, uses RecyclerViewModel to handle interaction with data model.
 * Created by Srikar on 7/11/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    //the RecyclerViewModel that created this
    private final BaseCardListViewModel mRecyclerViewModel;
    /**
     * Constructor
     * @param recyclerViewModel View model for the RecyclerView, interacts with data model
     */
    public RecyclerViewAdapter(BaseCardListViewModel recyclerViewModel) {
        super();
        mRecyclerViewModel = recyclerViewModel;
    }

    /**
     * Base ViewHolder, holds Binding and ViewModel for layout being held.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ViewDataBinding binding;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            //set on click
            RxView.clicks(binding.getRoot())
                    .throttleFirst(UiUtil.CLICK_DELAY, TimeUnit.MILLISECONDS)//ignore double clicks
                    .subscribe(empty -> onClick());
        }

        public void onClick() {
            mRecyclerViewModel.onItemClick(getAdapterPosition());
        }
    }

    /**
     * When override, create Binding and ViewModel for layout and use to create ViewHolder.
     * Putting here to emphasize that have to override in subclasses.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = mRecyclerViewModel.getCardLayout();
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                layoutId,
                parent,
                false //don't attach to parent, handled by RecyclerView
        );
        return new ViewHolder(binding);
    }

    /**
     * When list entry becomes visible on screen, store the new position in the view model
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //use view model to update view
        mRecyclerViewModel.onItemLoad(holder.binding, position);
    }

    /**
     * Used to determine the number of list elements to display
     */
    @Override
    public int getItemCount() {
        return mRecyclerViewModel.getItemCount();
    }
}
