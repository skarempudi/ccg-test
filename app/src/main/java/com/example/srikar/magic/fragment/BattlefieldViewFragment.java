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
import com.example.srikar.magic.adapter.CombatBfViewAdapter;
import com.example.srikar.magic.adapter.CreaturesBfViewAdapter;
import com.example.srikar.magic.adapter.LandsBfViewAdapter;

/**
 * Created by Srikar on 5/20/2016.
 */
public class BattlefieldViewFragment extends Fragment {
    private static final String TAG = "BattlefieldViewFragment";

    private Context mContext;

    RecyclerView mLandsRecyclerView, mCreaturesRecyclerView, mCombatRecyclerView;
    LandsBfViewAdapter mLandsAdapter;
    CreaturesBfViewAdapter mCreaturesAdapter;
    CombatBfViewAdapter mCombatAdapter;
    RecyclerView.LayoutManager mLandsLayoutManager, mCreaturesLayoutManager, mCombatLayoutManager;

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
        mCombatRecyclerView = (RecyclerView) rootView.findViewById(R.id.combat_recyclerview);

        //set layout managers
        //lands layout
        mLandsLayoutManager = new LinearLayoutManager(mContext);
        setLayoutManager(mLandsRecyclerView, mLandsLayoutManager);
        //creatures layout
        mCreaturesLayoutManager = new LinearLayoutManager(mContext);
        setLayoutManager(mCreaturesRecyclerView, mCreaturesLayoutManager);
        //combat layout
        mCombatLayoutManager = new LinearLayoutManager(mContext);
        setLayoutManager(mCombatRecyclerView, mCombatLayoutManager);

        //create adapters
        mLandsAdapter = new LandsBfViewAdapter(mContext);
        mCreaturesAdapter = new CreaturesBfViewAdapter(mContext);
        mCombatAdapter = new CombatBfViewAdapter(mContext);

        //set adapters
        mLandsRecyclerView.setAdapter(mLandsAdapter);
        mCreaturesRecyclerView.setAdapter(mCreaturesAdapter);
        mCombatRecyclerView.setAdapter(mCombatAdapter);

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
        mCombatAdapter.onDestroy();
    }
}
