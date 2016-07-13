package com.example.srikar.magic.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.srikar.magic.event.ListChangeEvent;
import com.example.srikar.magic.viewmodel.BaseItemViewModel;
import com.example.srikar.magic.viewmodel.recyclerview.BaseRecyclerViewModel;

/**
 * Base Adapter for RecyclerViews, uses RecyclerViewModel to handle interaction with data model.
 * Created by Srikar on 7/11/2016.
 */
public abstract class BaseRecViewAdapter extends RecyclerView.Adapter<BaseRecViewAdapter.BaseRecViewHolder> {
    private static final String TAG = "BaseRecViewAdapter";
    final Context mContext;
    //the RecyclerViewModel that created this
    private final BaseRecyclerViewModel mRecyclerViewModel;
    //which data model list to display
    private final ListChangeEvent.ListName mListName;

    /**
     * Constructor
     * @param activityContext Context used to inflate views
     * @param recyclerViewModel View model for the RecyclerView, interacts with data model
     * @param listName ListName that maps to a data model list, used to populate this RecyclerView
     */
    BaseRecViewAdapter(Context activityContext, BaseRecyclerViewModel recyclerViewModel,
                       ListChangeEvent.ListName listName) {
        super();
        mContext = activityContext;
        mRecyclerViewModel = recyclerViewModel;
        mListName = listName;
    }

    /**
     * Base ViewHolder, holds Binding and ViewModel for layout being held.
     */
    public class BaseRecViewHolder extends RecyclerView.ViewHolder {
        public final ViewDataBinding binding;
        public final BaseItemViewModel viewModel;

        public BaseRecViewHolder(ViewDataBinding binding, BaseItemViewModel viewModel) {
            super(binding.getRoot());
            this.binding = binding;
            this.viewModel = viewModel;
        }
    }

    /**
     * When override, create Binding and ViewModel for layout and use to create ViewHolder.
     * Putting here to emphasize that have to override in subclasses.
     */
    @Override
    public abstract BaseRecViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    /**
     * When list entry becomes visible on screen, store the new position in the view model
     */
    @Override
    public void onBindViewHolder(BaseRecViewHolder holder, int position) {
        //update position of the view model
        holder.viewModel.setListPosition(mListName, position);
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
