package com.example.srikar.magic.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.srikar.magic.R;
import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.viewmodel.recyclerview.CreaturesBfRecViewModel;
import com.example.srikar.magic.viewmodel.recyclerview.HandRecViewModel;
import com.example.srikar.magic.viewmodel.recyclerview.LandsBfRecViewModel;

/**
 * Fragment that collects all RecyclerViews connected to the Hand and Battlefield.
 * Created by Srikar on 5/20/2016.
 */
public class BoardFragment extends Fragment {
    private static final String TAG = "BoardFragment";

    private Context mContext;

    /**
     * RecyclerView Models, which handle more complex interactions and communicate with the
     * data model classes.
     */
    HandRecViewModel mHandRecViewModel;
    LandsBfRecViewModel mLandsRecViewModel;
    CreaturesBfRecViewModel mCreaturesRecViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize data set
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_board, container, false);

        //get binding for the Fragment's layout
        FragmentBoardBinding binding = FragmentBoardBinding.bind(rootView);

        //create the RecyclerViewModels
        createRecyclerViewModels();

        //attach the view models to the binding
        binding.setHandModel(mHandRecViewModel);
        binding.setLandsModel(mLandsRecViewModel);
        binding.setCreaturesModel(mCreaturesRecViewModel);

        return rootView;
    }

    /**
     * Used to create the RecyclerViewModels that handle interaction with the data model
     */
    private void createRecyclerViewModels() {
        if (mHandRecViewModel == null) {
            mHandRecViewModel = new HandRecViewModel(mContext);
        }
        if (mLandsRecViewModel == null) {
            mLandsRecViewModel = new LandsBfRecViewModel(mContext);
        }
        if (mCreaturesRecViewModel == null) {
            mCreaturesRecViewModel = new CreaturesBfRecViewModel(mContext);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //for each RecyclerViewModel, remove the subscriptions to RxJava Observables
        mHandRecViewModel.onDestroy();
        mLandsRecViewModel.onDestroy();
        mCreaturesRecViewModel.onDestroy();
    }
}
