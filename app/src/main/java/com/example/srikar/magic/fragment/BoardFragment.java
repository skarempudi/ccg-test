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
import com.example.srikar.magic.viewmodel.BoardFragmentModel;
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
     * Model for this Fragment, which handles interactions and communication with the data models.
     */
    BoardFragmentModel mBoardFragmentModel;

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

        //create the view model for this Fragment
        //it handles the binding for the inner views
        mBoardFragmentModel = new BoardFragmentModel(mContext, binding);

        //attach the view model to the binding
        binding.setFragmentBoardModel(mBoardFragmentModel);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //remove subscriptions to RxJava Observables in view model
        if (mBoardFragmentModel != null) {
            mBoardFragmentModel.onDestroy();
        }
    }
}
