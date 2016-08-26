package com.example.srikar.magic.viewmodel.recyclerview;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.adapter.BaseRecViewAdapter;
import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.event.ListChangeEvent;
import com.example.srikar.magic.event.RxEventBus;
import com.example.srikar.magic.viewmodel.BaseBoardModel;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Using data binding, the layout uses this View Model to interact with the rest of the code.
 * This is the base class used for RecyclerView View Models. List to use determined by constructor.
 * The Adapter and LayoutManager are set here.
 * Created by Srikar on 6/21/2016.
 */
public abstract class BaseRecyclerViewModel extends BaseBoardModel {
    private static final String TAG = "BaseRecyclerViewModel";

    protected Activity mActivity;
    //adapter handles changes to the list, creates view models for Permanents or Cards
    private BaseRecViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //listens for changes in model so can update display
    @Inject
    protected RxEventBus<ListChangeEvent> mListChangeEventBus;
    private final Subscription mRecyclerViewEventSub;

    //used to determine which data model list to populate the RecyclerView with
    final int mListName;

    /**
     * Base View Model for RecyclerView, which will handle interactions with the data model.
     * Subclasses handle which list from data model to use.
     * @param binding Binding used to access view that will update
     * @param activity Context used to create the LayoutManager
     * @param listName Which data model list is being used, using DataModelConstants
     */
    BaseRecyclerViewModel(FragmentBoardBinding binding, Activity activity, int listName) {
        super(binding);

        mActivity = activity;
        mListName = listName;
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
    protected abstract BaseRecViewAdapter getAdapter();

    /**
     * Writing in separate function so can potentially override.
     * Gets the LayoutManager used by the RecyclerView being modeled.
     * By default, linear layout with horizontal orientation.
     * @return New horizontal LinearLayoutManager
     */
    private RecyclerView.LayoutManager getLayoutManager() {
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
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
        //unsubscribe to event bus
        if (mRecyclerViewEventSub != null) {
            mRecyclerViewEventSub.unsubscribe();
        }

        //remove adapter reference to context
        if (mAdapter != null) {
            mAdapter.onDestroy();
        }

        //remove adapter
        mAdapter = null;

        //clear reference to context
        mActivity = null;
    }


    /***********************************************************************************************
     * EVENT BUS
     **********************************************************************************************/

    /**
     * Register to event bus for RecyclerView events
     * Filtering handled based on list name given during creation of this view model
     * @return The subscripton
     */
    private Subscription registerEventBus() {
        MagicLog.d(TAG, "registerEventBus: ");
        return mListChangeEventBus.getEvents()
                .filter(e -> e.listName == mListName)
                .subscribe(this::actOnEvent);
    }

    /**
     * When hear of event where relevant list updated, update the view to match
     * @param event The event that acting on, either adding or removing element
     */
    private void actOnEvent(ListChangeEvent event) {
        MagicLog.d(TAG, "actOnEvent: " + event.toString());
        //if adding
        if (event.action == ListChangeEvent.ADD) {
            mAdapter.notifyItemInserted(event.index);
        }
        //if removing
        else if (event.action == ListChangeEvent.REMOVE) {
            mAdapter.notifyItemRemoved(event.index);
        }
        //if updating
        else if (event.action == ListChangeEvent.UPDATE) {
            mAdapter.notifyItemChanged(event.index);
        }
        //if updating all
        else if (event.action == ListChangeEvent.UPDATE_ALL) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
