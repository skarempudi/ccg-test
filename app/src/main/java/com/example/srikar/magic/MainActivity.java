package com.example.srikar.magic;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.srikar.magic.databinding.ActivityMainBinding;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.viewmodel.GameViewModel;
import com.example.srikar.magic.viewmodel.board.CurrentlyUnusedModel;
import com.example.srikar.magic.viewmodel.board.GameActionLogModel;
import com.example.srikar.magic.viewmodel.board.LifeCounterModel;
import com.example.srikar.magic.viewmodel.board.NextStepModel;
import com.example.srikar.magic.viewmodel.board.SwitchPlayerModel;
import com.example.srikar.magic.viewmodel.board.TurnCounterModel;
import com.example.srikar.magic.viewmodel.recyclerview.BattlefieldRecViewModel;
import com.example.srikar.magic.viewmodel.recyclerview.HandRecViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //list of view models, so can destroy them in onDestroy()
    private List<GameViewModel> mGameViewModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        BoardBinding binding = new BoardBinding(mainBinding);

        //create the ViewModels and populate mGameViewModels
        createViewModels(binding);
    }

    /**
     * Creates the ViewModels that handle interaction with the data model
     * @param binding Binding holding views in Fragment
     */
    private void createViewModels(BoardBinding binding) {
        //clear out list
        mGameViewModels.clear();

        //add view models
        mGameViewModels.add(new TurnCounterModel(binding));
        mGameViewModels.add(new LifeCounterModel(binding));
        mGameViewModels.add(new SwitchPlayerModel(binding));
        mGameViewModels.add(new GameActionLogModel(binding));
        mGameViewModels.add(new NextStepModel(binding));
        mGameViewModels.add(new CurrentlyUnusedModel(binding));
        //add recycler view models
        mGameViewModels.add(new HandRecViewModel(binding));
        mGameViewModels.add(new BattlefieldRecViewModel(binding, DataModelConstants.LIST_LANDS));
        mGameViewModels.add(new BattlefieldRecViewModel(binding, DataModelConstants.LIST_CREATURES));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //call onDestroy for each view model, removing the Subscriptions to RxJava Observables
        for (GameViewModel viewModel : mGameViewModels) {
            viewModel.onDestroy();
        }

        //clear list of view models
        mGameViewModels.clear();
    }
}
