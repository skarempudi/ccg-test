package com.example.srikar.magic.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.support.annotation.CallSuper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.adapter.BaseBfRecViewAdapter;
import com.example.srikar.magic.event.RecyclerViewEvent;
import com.example.srikar.magic.model.Battlefield;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Using data binding, the layout uses this View Model to interact with the rest of the code.
 * This is the base class used for RecyclerView View Models.
 * The Adapter and LayoutManager are set here, with the Adapter provided in subclasses.
 * Created by Srikar on 6/21/2016.
 */
public abstract class BaseRecyclerViewModel extends BaseObservable {
    private static final String TAG = "BaseRecyclerViewModel";

    protected final Context mContext;
    protected BaseBfRecViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Inject
    protected Battlefield mBattlefield;
    //listens for changes in Battlefield model to can update display
    protected Subscription mBattlefieldEventSub;

    /**
     * Don't use this constructor, only here to provide default constructor.
     */
    public BaseRecyclerViewModel() {
        mContext = null;
    }

    /**
     * Since LayoutManager needs Context to create, have to manually construct this View Model and
     * pass it into the RecyclerView's binding.
     * @param appContext
     */
    public BaseRecyclerViewModel(Context appContext) {
        mContext = appContext;
        //injects instance of Battlefield
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);
        //implemented by children, listens for changes in models to update RecyclerView
        mBattlefieldEventSub = registerEventBus();
    }

    /**
     * Use this function to attach the Adapter and LayoutManager to the bound RecyclerView
     * Called by custom binding for attribute bind:recyclerViewModel="@{recyclerViewModel}"
     * @param recyclerView
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
     * Implemented by subclasses
     * Gets the Adapter used by the RecyclerView being modeled
     * @return Adapter
     */
    protected abstract BaseBfRecViewAdapter getAdapter();

    /**
     * Writing in separate function so can potentially override.
     * Gets the LayoutManager used by the RecyclerView being modeled.
     * By default, linear layout with horizontal orientation.
     * @return
     */
    protected RecyclerView.LayoutManager getLayoutManager() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        return manager;
    }

    /**
     * Call when containing View or Fragment is destroyed, will unregister Subscriptions
     */
    public void onDestroy() {
        if (mBattlefieldEventSub != null) {
            mBattlefieldEventSub.unsubscribe();
        }
    }


    /***********************************************************************************************
     * EVENT BUS
     **********************************************************************************************/

    /**
     * Register to Battlefield's event bus for RecyclerView events
     * @return The subscripton
     */
    public Subscription registerEventBus() {
        Log.d(TAG, "registerEventBus: ");
        return mBattlefield.getRecyclerViewEvents()
                .filter(e -> e.target == getThisTarget())
                .subscribe(this::actOnEvent);
    }

    /**
     * When hear of event where relevant list updated, update the view to match
     * @param event The event that acting on, either adding or removing element
     */
    @CallSuper
    public void actOnEvent(RecyclerViewEvent event) {
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


    /**
     * When filtering what events on event bus, specify target affiliated with this subclass
     * @return
     */
    protected abstract RecyclerViewEvent.Target getThisTarget();
}
