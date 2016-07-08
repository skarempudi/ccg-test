package com.example.srikar.magic.viewmodel;

import android.widget.ImageView;

import com.example.srikar.magic.R;
import com.example.srikar.magic.databinding.CardBinding;
import com.squareup.picasso.Picasso;

/**
 * Tied to card.xml
 * Instead of directly handling the card views, work with this model that uses data binding
 * Created by Srikar on 5/10/2016.
 */
public class CardViewModel extends BaseItemViewModel {
    private CardBinding mBinding;
    private int mPosition;

    public CardViewModel(CardBinding binding) {
        mBinding = binding;
    }

    /**
     * When RecyclerView Adapter binds View Holder, set the position where can find the relevant
     * Card
     * @param position Position in Hand's card list
     */
    public void setListPosition(int position) {
        mPosition = position;
    }

    /**
     * Load image
     */
    @Override
    public void loadImage() {
        ImageView view = mBinding.cardImage;
        Picasso.with(view.getContext())
                .load(R.drawable.ic_launcher)
                .into(view);
    }
}
