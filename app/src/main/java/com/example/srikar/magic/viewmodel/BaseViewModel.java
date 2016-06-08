package com.example.srikar.magic.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.example.srikar.magic.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Srikar on 5/20/2016.
 */
public class BaseViewModel extends BaseObservable {
    private Context mContext;
    private View.OnClickListener mOnClickListener;

    public BaseViewModel(Context context, View.OnClickListener listener) {
        mContext = context;
        mOnClickListener = listener;
    }

    public String getImageUrl() {
        //does nothing for now
        return "";
    }

    /**
     * When card.xml tries to load image using android:src, instead calls this function
     * @param view
     * @param url
     */
    @BindingAdapter("android:src")
    public static void setImageUrl(ImageView view, String url) {
        if (url == null || url == "") {
            Picasso.with(view.getContext())
                    .load(R.drawable.ic_launcher)
                    .into(view);
        }
    }

    public View.OnClickListener onClickImage() {
        return mOnClickListener;
    }
}
