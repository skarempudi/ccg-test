package com.example.srikar.magic.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.srikar.magic.R;
import com.example.srikar.magic.adapter.HandViewAdapter;

/**
 * Created by Srikar on 4/14/2016.
 */
public class HandViewFragment extends Fragment {
    RecyclerView mRecyclerView;

    HandViewAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize data set
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hand, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.hand_recyclerview);

        //set mRecyclerView attributes
        mLayoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager)mLayoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new HandViewAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
