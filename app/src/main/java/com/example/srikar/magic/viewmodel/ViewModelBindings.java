package com.example.srikar.magic.viewmodel;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.example.srikar.magic.viewmodel.recyclerview.BaseRecyclerViewModel;

/**
 * Container class for custom setters used in layout data binding.
 * Basically, used to override layout settings.
 * Created by Srikar on 6/21/2016.
 */
public class ViewModelBindings {
    /**
     * FRAGMENT_BATTLEFIELD.XML
     * Called by fragment_battlefield.xml for RecyclerView with attribute
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

    /**
     * CARD.XML, PERMANENT.XML
     * When card.xml or permanent.xml tries to load image using android:src, instead calls this function
     * @param view View being loaded into
     * @param url URL being loaded from, currently not used
     * @param itemViewModel The View Model performing the loading operation
     */
    @BindingAdapter({"android:src", "itemViewModel"})
    public static void setImageUrl(ImageView view, String url, BaseItemViewModel itemViewModel) {
        itemViewModel.handleSettingImage(view, url);
    }
}
