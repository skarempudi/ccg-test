package com.example.srikar.magic.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.R;
import com.example.srikar.magic.databinding.PermanentBinding;
import com.example.srikar.magic.event.RecyclerViewEvent;
import com.example.srikar.magic.model.Battlefield;
import com.example.srikar.magic.model.Permanent;
import com.example.srikar.magic.viewmodel.PermanentViewModel;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Adapter for the RecyclerView used to hold lands
 * Created by Srikar on 5/20/2016.
 */
public abstract class BaseBfViewAdapter extends RecyclerView.Adapter<BaseBfViewAdapter.PermanentViewHolder> {
    protected static final String TAG = "BaseBfViewAdapter";
    @Inject
    protected Battlefield mBattlefield;
    protected final Context mContext;

    //listens for changes in Battlefield model to can update display
    protected Subscription mBattlefieldEventSub;
    //used to store all onClick subscriptions, so can unsubscribe when destroy
    protected CompositeSubscription mOnClickSubs;

    /**
     * Constructor
     * @param activityContext Used to inflate views
     */
    protected BaseBfViewAdapter(Context activityContext) {
        super();
        mContext = activityContext;
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);

        //implemented by children
        mBattlefieldEventSub = registerEventBus();
        //used to hold onClick subscriptions
        mOnClickSubs = new CompositeSubscription();
    }

    /**
     * Used to hold the layout data for each element of the list
     */
    public class PermanentViewHolder extends RecyclerView.ViewHolder {
        private PermanentBinding binding;

        public PermanentViewHolder(PermanentBinding binding) {
            super(binding.cardImage);
            this.binding = binding;
        }

        public PermanentBinding getBinding() {
            return binding;
        }
    }

    @Override
    /**
     * Used to create the view holder for each list entry
     * When first create a list entry, create a binding to the permanent layout
     */
    public PermanentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PermanentBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.permanent,
                parent,
                false //don't attach to parent, handled by RecyclerView
        );

        return new PermanentViewHolder(binding);
    }

    @Override
    /**
     * When list entry becomes visible on screen, set the view model
     */
    public void onBindViewHolder(PermanentViewHolder holder, int position) {
        //get binding from holder
        PermanentBinding binding = holder.getBinding();

        //get creature in combat permanent that corresponds to this position
        Permanent perm = getPermanent(position);

        //set the view model, including the OnClick
        //on click, will remove from combat and put back in noncombat creatures list
        binding.setPermanentViewModel(
                new PermanentViewModel(
                        mContext,
                        perm,
//                        v -> onClick(holder, holder.getLayoutPosition())
                        null //not binding onClick to view, instead using RxBinding
                )
        );

        //subscribe to the onClick for the ImageView
        Subscription sub = RxView.clicks(binding.cardImage)
                .throttleFirst(500, TimeUnit.MILLISECONDS) //ignore double clicks
                .subscribe(empty -> onClick(holder, holder.getLayoutPosition()));

        //store so can unsubscribe later
        mOnClickSubs.add(sub);
    }

    /**
     * Used in onBindViewHolder()
     * Gets the permanent from the relevant list affiliated with the given position
     * @param position
     * @return
     */
    protected abstract Permanent getPermanent(int position);

    @CallSuper
    /**
     * The onClickListener action for list elements, used in onBindViewHolder()
     */
    protected void onClick(PermanentViewHolder holder, int position) {
        Permanent perm = holder.getBinding().getPermanentViewModel().getPermanent();
        Log.d(TAG, "onClick: " + ((perm == null)? "no permanent" : perm.toString()) );
    }

    @Override
    /**
     * Used to determine the number of list elements to display
     */
    public abstract int getItemCount();

    /**
     * When RecyclerView this is attached to is destroyed, remove all subscriptions
     */
    public void onDestroy() {
        Log.d(TAG, "onDestroy: Unsubscribing subscriptions");

        mBattlefieldEventSub.unsubscribe();

        mOnClickSubs.unsubscribe();
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
                .subscribe(e -> actOnEvent(e));
    }

    @CallSuper
    /**
     * When hear of event where relevant list updated, update the view to match
     * @param event The event that acting on, either adding or removing element
     */
    public void actOnEvent(RecyclerViewEvent event) {
        Log.d(TAG, "actOnEvent: " + event.toString());
        //if adding
        if (event.action == RecyclerViewEvent.Action.ADD) {
            notifyItemInserted(event.index);
        }
        //if removing
        else if (event.action == RecyclerViewEvent.Action.REMOVE) {
            notifyItemRemoved(event.index);
        }
    }

    /**
     * When filtering what events on event bus, specify target affiliated with this subclass
     * @return
     */
    protected abstract RecyclerViewEvent.Target getThisTarget();
}
