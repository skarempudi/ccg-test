package com.example.srikar.magic.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import com.example.srikar.magic.model.Permanent;

/**
 * Tied to permanent.xml
 * Instead of directly handling the permanent views, work with this model that uses data binding
 * Created by Srikar on 5/20/2016.
 */
public class PermanentViewModel extends BaseViewModel {
    //the data model
    private Permanent mPermanent;

    public PermanentViewModel(Context context, Permanent perm, View.OnClickListener listener) {
        super(context, listener);
        mPermanent = perm;
    }

    public Permanent getPermanent() {
        return mPermanent;
    }

    @Override
    /**
     * Using RxBinding so can throttle number of clicks
     * Overriding here to note that not used in permanent.xml
     */
    public View.OnClickListener onClickImage() {
        return null;
    }
}
