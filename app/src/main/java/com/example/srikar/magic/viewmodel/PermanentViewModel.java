package com.example.srikar.magic.viewmodel;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.R;
import com.example.srikar.magic.model.Battlefield;
import com.example.srikar.magic.model.Permanent;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

/**
 * Tied to permanent.xml
 * Instead of directly handling the permanent views, work with this model that uses data binding
 * Created by Srikar on 5/20/2016.
 */
public class PermanentViewModel extends BaseItemViewModel {
    //the data model
    private Permanent mPermanent;
    @Inject
    protected Battlefield mBattlefield;

    public PermanentViewModel() {
        super(null, null);
        mPermanent = null;
        //get instance of Battlefield
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
     * Looks at the Permanent at the given position in Battlefield's creature list and the game state
     * and acts based on that
     * @param position
     */
    public void onClick(int position) {
        mBattlefield.onViewPlayerCreatureClicked(position);
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
     * When permanent.xml tries to load image using android:src, instead calls this function
     * @param view The view being loaded into
     * @param url The URL of the image, currently unused
     */
    @Override
    protected void handleSettingImage(ImageView view, String url) {
        //if tapped, rotate 90 degrees
        //in future, will instead have a set of custom transformations that apply
        if (mPermanent.isTapped()) {
            Picasso.with(view.getContext())
                    .load(R.drawable.ic_launcher)
                    .rotate(90f)
                    .into(view);
        }
        else {
            Picasso.with(view.getContext())
                    .load(R.drawable.ic_launcher)
                    .into(view);
        }
    }
}
