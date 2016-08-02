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
public class HandCardViewModel extends BaseItemViewModel {
    private final CardBinding mBinding;

    public HandCardViewModel(CardBinding binding) {
        mBinding = binding;
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
