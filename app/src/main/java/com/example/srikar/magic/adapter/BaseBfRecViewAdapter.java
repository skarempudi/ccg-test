package com.example.srikar.magic.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.R;
import com.example.srikar.magic.databinding.PermanentBinding;
import com.example.srikar.magic.model.Battlefield;
import com.example.srikar.magic.model.Permanent;
import com.example.srikar.magic.viewmodel.PermanentViewModel;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Base adapter for the RecyclerViews that represent the Battlefield
 * Interacts with data model to get the Permanent object at a specific point in a Battlefield list,
 * and know how many entries are in the list; and it throttles click events for the Permanent view;
 * but more complex data model interaction is handled by the PermanentViewModel it creates.
 * The Battlefield list it uses is specified by its subclasses.
 * Created by Srikar on 5/20/2016.
 */
public abstract class BaseBfRecViewAdapter extends RecyclerView.Adapter<BaseBfRecViewAdapter.PermanentViewHolder> {
    protected static final String TAG = "BaseBfRecViewAdapter";
    private static final int CLICK_DELAY = 500;
    @Inject
    protected Battlefield mBattlefield;
    protected final Context mContext;

    //used to store all onClick subscriptions, so can unsubscribe when destroy
    protected CompositeSubscription mOnClickSubs;

    /**
     * Constructor
     * @param activityContext Used to inflate views
     */
    protected BaseBfRecViewAdapter(Context activityContext) {
        super();
        mContext = activityContext;
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);

        //used to hold onClick subscriptions
        mOnClickSubs = new CompositeSubscription();
    }

    /**
     * Used to hold the layout data for each element of the list
     */
    public class PermanentViewHolder extends RecyclerView.ViewHolder {
        private PermanentBinding binding;
        private PermanentViewModel viewModel;

        public PermanentViewHolder(PermanentBinding binding, PermanentViewModel viewModel) {
            super(binding.getRoot());
            this.binding = binding;
            this.viewModel = viewModel;
        }

        /**
         * When bind view holder, don't create new view model, but rather set the relevant Permanent
         * @param permanent The new Permanent
         */
        public void setPermanent(Permanent permanent) {
            viewModel.setPermanent(permanent);
        }

        /**
         * Calls the onClick() method for the view model, passing in the layout position.
         * Will use that position to call methods in Battlefield, instead of the set permanent.
         */
        public void onClick() {
            int position = getLayoutPosition();
            Log.d(TAG, "onClick: Clicking position " + position);
            viewModel.onClick(position);
        }
    }

    /**
     * Used to create the view holder for each list entry
     * When first create a list entry, create a binding to the permanent layout
     */
    @Override
    public PermanentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PermanentBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.permanent,
                parent,
                false //don't attach to parent, handled by RecyclerView
        );

        //create new view model and set in binding and holder
        PermanentViewModel permViewModel = new PermanentViewModel();
        binding.setPermanentViewModel(permViewModel);

        //create the view holder
        PermanentViewHolder holder = new PermanentViewHolder(binding, permViewModel);

        //subscribe to the onClick for the ImageView, handle with holder's onClick()
        Subscription sub = RxView.clicks(binding.cardImage)
                .throttleFirst(CLICK_DELAY, TimeUnit.MILLISECONDS) //ignore double clicks
                .subscribe(empty -> holder.onClick());

        //store so can unsubscribe later
        mOnClickSubs.add(sub);

        return holder;
    }

    /**
     * When list entry becomes visible on screen, set the Permanent used in view model
     */
    @Override
    public void onBindViewHolder(PermanentViewHolder holder, int position) {
        //get permanent that corresponds to this position
        Permanent perm = getPermanent(position);

        //set in the holder
        holder.setPermanent(perm);
    }

    /**
     * Used in onBindViewHolder()
     * Gets the permanent from the relevant list affiliated with the given position
     * @param position Position clicked in RecyclerView, which should match position in data list
     * @return Permanent object at that position in the affiliated data list
     */
    protected abstract Permanent getPermanent(int position);

    /**
     * Used to determine the number of list elements to display
     */
    @Override
    public abstract int getItemCount();

    /**
     * When RecyclerView this is attached to detaches adapter, remove all subscriptions
     */
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        Log.d(TAG, "onDetachedFromRecyclerView: Unsubscribing subscriptions");
        super.onDetachedFromRecyclerView(recyclerView);

        mOnClickSubs.unsubscribe();
    }
}
