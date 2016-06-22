package com.example.srikar.magic.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.example.srikar.magic.R;
import com.squareup.picasso.Picasso;

/**
 * Base View Model for item views, CardViewModel and PermanentViewModel
 * Instead of directly working with the views, act through View Models, which are created by the
 * XML layout files
 * Created by Srikar on 5/20/2016.
 */
public abstract class BaseItemViewModel extends BaseObservable {
    private Context mContext;
    private View.OnClickListener mOnClickListener;

    public BaseItemViewModel(Context context, View.OnClickListener listener) {
        mContext = context;
        mOnClickListener = listener;
    }

    public String getImageUrl() {
        //does nothing for now
        return "";
    }

    protected abstract void handleSettingImage(ImageView view, String url);

    public View.OnClickListener onClickImage() {
        return mOnClickListener;
    }
}
