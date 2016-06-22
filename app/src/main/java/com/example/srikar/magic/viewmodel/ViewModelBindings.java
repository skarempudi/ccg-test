package com.example.srikar.magic.viewmodel;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

/**
 * Container class for custom setters used in layout data binding.
 * Basically, used to override layout settings.
 * Created by Srikar on 6/21/2016.
 */
public class ViewModelBindings {
    /**
     * Called in layout for RecyclerView with attribute bind:recyclerViewModel="@{recyclerViewModel}"
     * Sets up the RecyclerView with the Adapter and LayoutManager
     * @param recyclerView
     * @param viewModel
     */
    @BindingAdapter("recyclerViewModel")
    public static void setRecyclerViewModel(RecyclerView recyclerView,
                                            BaseRecyclerViewModel viewModel) {
        viewModel.setupRecyclerView(recyclerView);
    }
}
