package com.example.srikar.magic.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
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
public class CardViewModel extends BaseViewModel {
    private Card mCard;

    public CardViewModel(Context context, Card card, View.OnClickListener listener) {
        super(context, listener);
        mCard = card;
    }
}
