package com.example.srikar.magic.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.srikar.magic.R;
import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.viewmodel.BoardFragmentModel;

/**
 * Fragment that collects all RecyclerViews connected to the Hand and Battlefield.
 * Created by Srikar on 5/20/2016.
 */
public class BoardFragment extends Fragment {
    private static final String TAG = "BoardFragment";
    /**
     * Model for this Fragment, which handles interactions and communication with the data models.
     */
    private BoardFragmentModel mBoardFragmentModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_board, container, false);

        //get binding for the Fragment's layout
        FragmentBoardBinding binding = FragmentBoardBinding.bind(rootView);

        //create the view model for this Fragment
        //it handles the binding for the inner views
        mBoardFragmentModel = new BoardFragmentModel(binding);

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
