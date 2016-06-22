package com.example.srikar.magic.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.srikar.magic.R;
import com.example.srikar.magic.databinding.FragmentBattlefieldBinding;
import com.example.srikar.magic.viewmodel.CreaturesBfRecViewModel;
import com.example.srikar.magic.viewmodel.LandsBfRecViewModel;

/**
 * Fragment that collects all RecyclerViews connected to the Battlefield.
 * Created by Srikar on 5/20/2016.
 */
public class BattlefieldViewFragment extends Fragment {
    private static final String TAG = "BattlefieldViewFragment";

    private Context mContext;

    /**
     * RecyclerView Models, which handle more complex interactions and communicate with the
     * data model classes.
     */
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
        View rootView = inflater.inflate(R.layout.fragment_battlefield, container, false);

        //get binding for the Fragment's layout
        FragmentBattlefieldBinding binding = FragmentBattlefieldBinding.bind(rootView);

        //create the RecyclerViewModels
        createRecyclerViewModels();

        //attach the view models to the binding
        binding.setLandsModel(mLandsRecViewModel);
        binding.setCreaturesModel(mCreaturesRecViewModel);

        return rootView;
    }

    /**
     * Used to create the RecyclerViewModels that handle interaction with the data model
     */
    private void createRecyclerViewModels() {
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
        mLandsRecViewModel.onDestroy();
        mCreaturesRecViewModel.onDestroy();
    }
}
