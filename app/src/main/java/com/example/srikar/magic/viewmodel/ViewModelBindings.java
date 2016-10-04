package com.example.srikar.magic.viewmodel;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.example.srikar.magic.viewmodel.recyclerview.BaseRecyclerViewModel;

/**
 * Container class for custom setters used in layout data binding.
 * Basically, used to override layout settings.
 * Created by Srikar on 6/21/2016.
 */
public class ViewModelBindings {
    private static final String TAG = "ViewModelBindings";

    /**
     * ACTIVITY_MAIN.XML
     * Called by activity_main.xml RecyclerView with attribute
     * bind:recyclerViewModel="@{recyclerViewModel}".
     * Sets up the RecyclerView with the Adapter and LayoutManager.
     * Have to do this way to pass parameters into View Model on construction.
     * @param recyclerView The RecyclerView being loaded
     * @param viewModel The View Model being used for the RecyclerView
     */
    @BindingAdapter("recyclerViewModel")
    public static void setRecyclerViewModel(RecyclerView recyclerView,
                                            BaseRecyclerViewModel viewModel) {
        viewModel.setupRecyclerView(recyclerView);
    }
}
