package com.example.srikar.magic.viewmodel.recyclerview;

import android.content.Context;
import android.databinding.BaseObservable;
import android.support.annotation.CallSuper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.event.RecyclerViewEvent;
import com.example.srikar.magic.event.RxEventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Using data binding, the layout uses this View Model to interact with the rest of the code.
 * This is the base class used for RecyclerView View Models. Subclasses determine which data model
 * list to use.
 * The Adapter and LayoutManager are set here.
 * Created by Srikar on 6/21/2016.
 */
public abstract class BaseRecyclerViewModel extends BaseObservable {
    private static final String TAG = "BaseRecyclerViewModel";

    protected final Context mContext;
    //adapter handles changes to the list, creates view models for Permanents or Cards
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    //listens for changes in model so can update display
    @Inject
    protected RxEventBus<RecyclerViewEvent> mEventBus;
    protected Subscription mRecyclerViewEventSub;

    //used to determine which data model list to populate the RecyclerView with
    protected final RecyclerViewEvent.Target mTargetList;

    /**
     * Base View Model for RecyclerView, which will handle interactions with the data model.
     * Subclasses handle which list from data model to use.
     * @param appContext Context used to create the LayoutManager
     */
    protected BaseRecyclerViewModel(Context appContext, RecyclerViewEvent.Target targetList) {
        mContext = appContext;
        mTargetList = targetList;
        //injects instance of RecyclerView event bus
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);
        //partially implemented by children, listens for changes in models to update RecyclerView
        mRecyclerViewEventSub = registerEventBus();
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
    protected abstract RecyclerView.Adapter getAdapter();

    /**
     * Writing in separate function so can potentially override.
     * Gets the LayoutManager used by the RecyclerView being modeled.
     * By default, linear layout with horizontal orientation.
     * @return New horizontal LinearLayoutManager
     */
    protected RecyclerView.LayoutManager getLayoutManager() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
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
     * When the view player switches, the content of the RecyclerView is changed.
     */
    public void onViewPlayerSwitched() {
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Call when containing View or Fragment is destroyed, will unregister Subscriptions
     */
    public void onDestroy() {
        if (mRecyclerViewEventSub != null) {
            mRecyclerViewEventSub.unsubscribe();
        }
    }


    /***********************************************************************************************
     * EVENT BUS
     **********************************************************************************************/

    /**
     * Register to event bus for RecyclerView events
     * Filtering handled by child implementation of getThisTarget()
     * @return The subscripton
     */
    protected Subscription registerEventBus() {
        Log.d(TAG, "registerEventBus: ");
        return mEventBus.getEvents()
                .filter(e -> e.target == mTargetList)
                .subscribe(this::actOnEvent);
    }

    /**
     * When hear of event where relevant list updated, update the view to match
     * @param event The event that acting on, either adding or removing element
     */
    @CallSuper
    protected void actOnEvent(RecyclerViewEvent event) {
        Log.d(TAG, "actOnEvent: " + event.toString());
        //if adding
        if (event.action == RecyclerViewEvent.Action.ADD) {
            mAdapter.notifyItemInserted(event.index);
        }
        //if removing
        else if (event.action == RecyclerViewEvent.Action.REMOVE) {
            mAdapter.notifyItemRemoved(event.index);
        }
        //if updating
        else if (event.action == RecyclerViewEvent.Action.UPDATE) {
            mAdapter.notifyItemChanged(event.index);
        }
    }
}
