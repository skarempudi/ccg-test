package com.example.srikar.magic;

import com.example.srikar.magic.databinding.ActivityMainBinding;

/**
 * Wrapper for the ViewDataBinding that holds bindings for game board's XML
 * So, if change name of the XML file, only have to change this
 * Created by Srikar on 10/4/2016.
 */

public class BoardBinding {
    private final ActivityMainBinding mBinding;

    public BoardBinding(ActivityMainBinding binding) {
        mBinding = binding;
    }

    public ActivityMainBinding get() {
        return mBinding;
    }
}
