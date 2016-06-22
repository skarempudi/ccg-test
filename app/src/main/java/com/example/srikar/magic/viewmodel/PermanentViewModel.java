package com.example.srikar.magic.viewmodel;

import android.view.View;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.event.RecyclerViewEvent;
import com.example.srikar.magic.event.RxEventBus;
import com.example.srikar.magic.model.Permanent;

import javax.inject.Inject;

/**
 * Tied to permanent.xml
 * Instead of directly handling the permanent views, work with this model that uses data binding
 * Created by Srikar on 5/20/2016.
 */
public class PermanentViewModel extends BaseViewModel {
    //the data model
    private Permanent mPermanent;
    @Inject
    private RxEventBus<RecyclerViewEvent> mRecyclerViewEventBus;

    public PermanentViewModel() {
        super(null, null);
        mPermanent = null;
        //used to inject RxEventBus, to alert RecyclerViews about change to Permanent data model
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);
    }

    public Permanent getPermanent() {
        return mPermanent;
    }

    /**
     * Change what Permanent will be using for data model
     * @param permanent Permanent
     */
    public void setPermanent(Permanent permanent) {
        mPermanent = permanent;
    }

    /**
     * Looks at the Permanent and state of the game to determine what to do when click
     */
    public void onClick() {
        mPermanent.onClick();
    }

    @Override
    /**
     * Using RxBinding so can throttle number of clicks
     * Overriding here to note that not used in permanent.xml
     */
    public View.OnClickListener onClickImage() {
        return null;
    }

    /**
     * Add event to mRecyclerViewEventBus, which alerts the specified RecyclerView to update
     * @param target Which RecyclerView, using RecyclerViewEvent.Target
     * @param action Whether to add or remove, using RecyclerViewEvent.Action
     * @param index What index to update in list
     */
    private void addRecyclerViewEvent(RecyclerViewEvent.Target target, RecyclerViewEvent.Action action, int index) {
        RecyclerViewEvent event = new RecyclerViewEvent(target, action, index);
//        Log.d(TAG, "addRecyclerViewEvent: " + event.toString());
        mRecyclerViewEventBus.addEvent(event);
    }
}
