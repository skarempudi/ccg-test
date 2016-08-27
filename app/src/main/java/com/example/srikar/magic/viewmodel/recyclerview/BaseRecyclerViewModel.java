package com.example.srikar.magic.viewmodel.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.adapter.BaseRecViewAdapter;
import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.event.ListChangeBus;
import com.example.srikar.magic.event.ListChangeEvent;
import com.example.srikar.magic.viewmodel.BaseBoardModel;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Using data binding, the layout uses this View Model to interact with the rest of the code.
 * This is the base class used for RecyclerView View Models. List to use determined by constructor.
 * The Adapter and LayoutManager are set here.
 * Created by Srikar on 6/21/2016.
 */
public abstract class BaseRecyclerViewModel extends BaseBoardModel implements
        ListChangeBus.AddListener, ListChangeBus.RemoveListener,
        ListChangeBus.UpdateListener, ListChangeBus.UpdateAllListener {
    static final String TAG = "BaseRecyclerViewModel";

    //adapter handles changes to the list, creates view models for Permanents or Cards
    BaseRecViewAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    //listens for changes in model so can update display
    @Inject
    ListChangeBus mListChangeBus;
    //store subscriptions to ListChangeBus
    final CompositeSubscription mEventSubs;

    //used to determine which data model list to populate the RecyclerView with
    final int mListName;

    /**
     * Base View Model for RecyclerView, which will handle interactions with the data model.
     * Subclasses handle which list from data model to use.
     * @param binding Binding used to access view that will update
     * @param listName Which data model list is being used, using DataModelConstants
     */
    BaseRecyclerViewModel(FragmentBoardBinding binding, int listName) {
        //won't set the background in super constructor
        super(binding, false);
        //injects instance of RecyclerView event bus
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);

        mListName = listName;

        //now that have list name, will set background
        updateBackground();

        //add subscriptions to listen for changes in lists
        mEventSubs = new CompositeSubscription();
        mEventSubs.add(mListChangeBus.subscribeAddListener(this, mListName));
        mEventSubs.add(mListChangeBus.subscribeRemoveListener(this, mListName));
        mEventSubs.add(mListChangeBus.subscribeUpdateListener(this, mListName));
        mEventSubs.add(mListChangeBus.subscribeUpdateAllListener(this, mListName));
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
     * Gets the Adapter used by the RecyclerView being modeled
     * Adapter type determined by subclasses
     * @return Adapter
     */
    protected abstract BaseRecViewAdapter getAdapter();

    /**
     * Writing in separate function so can potentially override.
     * Gets the LayoutManager used by the RecyclerView being modeled.
     * By default, linear layout with horizontal orientation.
     * @return New horizontal LinearLayoutManager
     */
    private RecyclerView.LayoutManager getLayoutManager() {
        Context context = mBinding.getRoot().getContext();
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
     * Call when containing View or Fragment is destroyed, will unregister Subscriptions
     */
    public void onDestroy() {
        //unsubscribe to event bus
        if (mEventSubs != null) {
            mEventSubs.unsubscribe();
        }

        //remove adapter reference to context
        if (mAdapter != null) {
            mAdapter.onDestroy();
        }

        //remove adapter
        mAdapter = null;
    }


    /***********************************************************************************************
     * EVENT BUS LISTENERS
     **********************************************************************************************/
    @Override
    public void onAdd(ListChangeEvent event) {
        //update list at index added at
        mAdapter.notifyItemInserted(event.index);
    }

    @Override
    public void onRemove(ListChangeEvent event) {
        //update list at index removed from
        mAdapter.notifyItemRemoved(event.index);
    }

    @Override
    public void onUpdate(ListChangeEvent event) {
        //update list at index
        mAdapter.notifyItemChanged(event.index);
    }

    @Override
    public void onUpdateAll(ListChangeEvent event) {
        //update entire list
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSwitchViewPlayer(GameStateChangeEvent event) {
        //updates background
        super.onSwitchViewPlayer(event);
        //when the view player switches, the content of the RecyclerView is changed.
        mAdapter.notifyDataSetChanged();
    }
}
