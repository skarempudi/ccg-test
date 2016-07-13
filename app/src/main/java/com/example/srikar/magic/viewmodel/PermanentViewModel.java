package com.example.srikar.magic.viewmodel;

import android.util.Log;
import android.widget.ImageView;

import com.example.srikar.magic.AppConstants;
import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.R;
import com.example.srikar.magic.databinding.PermanentBinding;
import com.example.srikar.magic.model.Battlefield;
import com.example.srikar.magic.model.Permanent;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Tied to permanent.xml
 * Instead of directly handling the permanent views, work with this model that uses data binding
 * Created by Srikar on 5/20/2016.
 */
public class PermanentViewModel extends BaseItemViewModel {
    private static final String TAG = "PermanentViewModel";

    //bindings for views in Permanent layout
    private final PermanentBinding mBinding;

    @Inject
    protected Battlefield mBattlefield;

    public PermanentViewModel(PermanentBinding binding) {
        mBinding = binding;

        //get instance of Battlefield
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);

        //subscribe to the onClick for the ImageView
        RxView.clicks(binding.cardImage)
                .throttleFirst(AppConstants.CLICK_DELAY, TimeUnit.MILLISECONDS) //ignore double clicks
                .subscribe(empty -> onImageClick());
    }

    /**
     * What to do when click image.
     * Right now, just taps or untaps in data model, which will use an event bus to alert a listening
     * RecyclerViewModel, which tells the RecyclerView Adapter to update at this position
     */
    private void onImageClick() {
        mBattlefield.onViewPlayerPermanentClicked(mTargetList, mPosition);
    }

    /**
     * Load image
     */
    @Override
    public void loadImage() {
        //get the view
        ImageView view = mBinding.cardImage;
        //get the Permanent
        Permanent perm = retrievePermanent();

        //if tapped, rotate 90 degrees
        //in future, will instead have a set of custom transformations that apply
        if (perm.isTapped()) {
            MagicLog.d(TAG, "loadImage: Drawing tapped");
            Picasso.with(view.getContext())
                    .load(R.drawable.ic_launcher)
                    .rotate(90f)
                    .into(view);
        }
        else {
            MagicLog.d(TAG, "loadImage: Drawing untapped");
            Picasso.with(view.getContext())
                    .load(R.drawable.ic_launcher)
                    .into(view);
        }
    }

    /**
     * Using the target list and the position in that list, get the Permanent that matches to this
     * position in the RecyclerView
     * @return Permanent
     */
    private Permanent retrievePermanent() {
        return mBattlefield.getViewPlayerPermanent(mTargetList, mPosition);
    }
}
