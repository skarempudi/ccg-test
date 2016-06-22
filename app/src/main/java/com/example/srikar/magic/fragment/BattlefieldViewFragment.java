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
import com.example.srikar.magic.adapter.CreaturesBfRecViewAdapter;
import com.example.srikar.magic.adapter.LandsBfRecViewAdapter;

/**
 * Fragment that collects all RecyclerViews connected to the Battlefield.
 * Created by Srikar on 5/20/2016.
 */
public class BattlefieldViewFragment extends Fragment {
    private static final String TAG = "BattlefieldViewFragment";

    private Context mContext;

    RecyclerView mLandsRecyclerView, mCreaturesRecyclerView;
    LandsBfRecViewAdapter mLandsAdapter;
    CreaturesBfRecViewAdapter mCreaturesAdapter;
    RecyclerView.LayoutManager mLandsLayoutManager, mCreaturesLayoutManager;

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

        //get the RecyclerViews
        mLandsRecyclerView = (RecyclerView) rootView.findViewById(R.id.lands_recyclerview);
        mCreaturesRecyclerView = (RecyclerView) rootView.findViewById(R.id.creatures_recyclerview);

        //set layout managers
        //lands layout
        mLandsLayoutManager = new LinearLayoutManager(mContext);
        setLayoutManager(mLandsRecyclerView, mLandsLayoutManager);
        //creatures layout
        mCreaturesLayoutManager = new LinearLayoutManager(mContext);
        setLayoutManager(mCreaturesRecyclerView, mCreaturesLayoutManager);

        //create adapters
        mLandsAdapter = new LandsBfRecViewAdapter(mContext);
        mCreaturesAdapter = new CreaturesBfRecViewAdapter(mContext);

        //set adapters
        mLandsRecyclerView.setAdapter(mLandsAdapter);
        mCreaturesRecyclerView.setAdapter(mCreaturesAdapter);

        return rootView;
    }

    private void setLayoutManager(RecyclerView recyclerView, RecyclerView.LayoutManager manager) {
        ((LinearLayoutManager)manager).setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //for each adapter, remove the subscriptions to RxJava Observables
        mLandsAdapter.onDestroy();
        mCreaturesAdapter.onDestroy();
    }
}
