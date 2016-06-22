package com.example.srikar.magic.viewmodel;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.srikar.magic.R;
import com.example.srikar.magic.model.Card;
import com.squareup.picasso.Picasso;

/**
 * Tied to card.xml
 * Instead of directly handling the card views, work with this model that uses data binding
 * Created by Srikar on 5/10/2016.
 */
public class CardViewModel extends BaseItemViewModel {
    private Card mCard;

    public CardViewModel(Context context, Card card, View.OnClickListener listener) {
        super(context, listener);
        mCard = card;
    }

    /**
     * When card.xml tries to load image using android:src, instead calls this function
     * @param view The view being loaded into
     * @param url The URL of the image, currently unused
     */
    @Override
    protected void handleSettingImage(ImageView view, String url) {
        Picasso.with(view.getContext())
                .load(R.drawable.ic_launcher)
                .into(view);
    }
}
