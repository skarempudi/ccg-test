package com.example.srikar.magic.viewmodel;

import android.widget.ImageView;

import com.example.srikar.magic.UiConstants;
import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.R;
import com.example.srikar.magic.databinding.PermanentBinding;
import com.example.srikar.magic.model.Card;
import com.example.srikar.magic.model.detail.CreatureDetails;
import com.example.srikar.magic.model.zone.Battlefield;
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

        //subscribe to the onClick for the FrameLayout
        RxView.clicks(binding.card)
                .throttleFirst(UiConstants.CLICK_DELAY, TimeUnit.MILLISECONDS) //ignore double clicks
                .subscribe(empty -> onCardClick());
    }

    /**
     * What to do when click image.
     * Right now, just taps or untaps in data model, which will use an event bus to alert a listening
     * RecyclerViewModel, which tells the RecyclerView Adapter to update at this position
     */
    private void onCardClick() {
        mBattlefield.onViewPlayerPermanentClicked(mListName, mPosition);
    }

    /**
     * Used by DataBinding in permanent.xml to display the creature's power and toughness
     * @return String in format "power/toughness"
     */
    public String getPTText() {
        CreatureDetails details = retrievePermanent().details;
        return details.power + "/" + details.toughness;
    }

    /**
     * Load image
     */
    @Override
    public void loadImage() {
        //get the view
        ImageView view = mBinding.cardImage;
        //get the Permanent
        Card perm = retrievePermanent();

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
     * Using the listName list and the position in that list, get the Permanent Card that matches to this
     * position in the RecyclerView
     * @return Permanent
     */
    private Card retrievePermanent() {
        return mBattlefield.getViewPlayerPermanent(mListName, mPosition);
    }
}
