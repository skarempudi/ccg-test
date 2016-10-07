package com.example.srikar.magic.viewmodel.list;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.srikar.magic.view.BoardBinding;
import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.view.RecyclerViewAdapter;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.event.ListChangeBus;
import com.example.srikar.magic.event.ListChangeEvent;
import com.example.srikar.magic.viewmodel.BaseBoardModel;

import javax.inject.Inject;

/**
 * Using data binding, the layout uses this View Model to interact with the rest of the code.
 * This is the base class used for RecyclerView View Models. List to use determined by constructor.
 * The Adapter and LayoutManager are set here.
 * Created by Srikar on 6/21/2016.
 */
public abstract class BaseCardListViewModel extends BaseBoardModel implements
        ListChangeBus.ListChangeListener {
    static final String TAG = "BaseCardListViewModel";

    //adapter handles changes to the list, creates view models for Permanents or Cards
    RecyclerViewAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    //listens for changes in model so can update display
    @Inject
    ListChangeBus mListChangeBus;

    //used to determine which data model list to populate the RecyclerView with
    final int mListName;

    /**
     * Base View Model for RecyclerView, which will handle interactions with the data model.
     * Subclasses handle which list from data model to use.
     * @param binding Binding used to access view that will update
     * @param listName Which data model list is being used, using DataModelConstants
     */
    BaseCardListViewModel(BoardBinding binding, int listName) {
        //won't set the background in super constructor
        super(binding, false);
        //injects instance of RecyclerView event bus
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);

        mListName = listName;

        //now that have list name, will set background
        updateBackground();

        //add subscription to listen for changes in list
        mSubscriptions.add(mListChangeBus.subscribeListChangeListener(this, mListName));
    }

    /**
     * Use this function to attach the Adapter and LayoutManager to the bound RecyclerView
     * Called by custom binding for attribute bind:recyclerViewModel="@{recyclerViewModel}"
     * @param recyclerView The RecyclerView being set up
     */
    public final void setupRecyclerView(RecyclerView recyclerView) {
        if (mAdapter == null) {
            mAdapter = getAdapter();
        }
        if (mLayoutManager == null) {
            mLayoutManager = getLayoutManager();
        }

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    /**
     * Gets the Adapter
     * @return Adapter
     */
    private RecyclerViewAdapter getAdapter() {
        return new RecyclerViewAdapter(this);
    }

    /**
     * Writing in separate function so can potentially override.
     * Gets the LayoutManager used by the RecyclerView being modeled.
     * By default, linear layout with horizontal orientation.
     * @return New horizontal LinearLayoutManager
     */
    private RecyclerView.LayoutManager getLayoutManager() {
        Context context = mBinding.get().getRoot().getContext();
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        return manager;
    }

    /**
     * Called by the Adapter created by this view model, to determine the size of the RecyclerView
     * Which list to use is determined by subclass
     * @return Number of items in relevant list
     */
    public abstract int getItemCount();

    /**
     * Returns layout ID of card, as desired for this list
     * @return Layout ID
     */
    public abstract int getCardLayout();

    /**
     * Called by view for this list
     * @param binding Binding for the view
     * @param position What card in list was clicked
     */
    public abstract void onItemClick(ViewDataBinding binding, int position);

    /**
     * Called by view for this list when load
     * Loads image
     * @param binding Binding for the view that will load
     * @param position What card in list is being loaded
     */
    public abstract void onItemLoad(ViewDataBinding binding, int position);

    /**
     * Call when containing View or Fragment is destroyed, will unregister Subscriptions
     */
    @Override
    public void onDestroy() {
        //unsubscribe to event bus
        super.onDestroy();

        //remove adapter
        mAdapter = null;
    }


    /***********************************************************************************************
     * EVENT BUS LISTENERS
     **********************************************************************************************/
    @Override
    public void onGameStateChange(GameStateChangeEvent event) {
        //updates background on view player switch
        super.onGameStateChange(event);
        //when the view player switches, the content of the RecyclerView is changed
        if (mAdapter == null) return;
        if (event.action == GameStateChangeEvent.SWITCH_VIEW_PLAYER) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onListChange(ListChangeEvent event) {
        if (mAdapter == null) return;

        switch (event.action) {
            case ListChangeEvent.ADD:
                //update list at index added at
                mAdapter.notifyItemInserted(event.index);
                break;
            case ListChangeEvent.REMOVE:
                //update list at index removed from
                mAdapter.notifyItemRemoved(event.index);
                break;

            case ListChangeEvent.UPDATE:
                //update list at index
                mAdapter.notifyItemChanged(event.index);
                break;
            case ListChangeEvent.UPDATE_ALL:
                //update entire list
                mAdapter.notifyDataSetChanged();
                break;
        }
    }
}
