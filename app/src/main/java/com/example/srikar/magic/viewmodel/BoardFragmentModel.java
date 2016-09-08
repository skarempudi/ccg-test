package com.example.srikar.magic.viewmodel;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.R;
import com.example.srikar.magic.UiUtil;
import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.event.GameStateChangeBus;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.fragment.CombatDialogFragment;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.GameState;
import com.example.srikar.magic.model.zone.Battlefield;
import com.example.srikar.magic.viewmodel.board.CurrentlyUnusedModel;
import com.example.srikar.magic.viewmodel.board.GameActionLogModel;
import com.example.srikar.magic.viewmodel.board.LifeCounterModel;
import com.example.srikar.magic.viewmodel.board.NextStepModel;
import com.example.srikar.magic.viewmodel.board.SwitchPlayerModel;
import com.example.srikar.magic.viewmodel.board.TurnCounterModel;
import com.example.srikar.magic.viewmodel.recyclerview.BaseRecyclerViewModel;
import com.example.srikar.magic.viewmodel.recyclerview.BattlefieldRecViewModel;
import com.example.srikar.magic.viewmodel.recyclerview.HandRecViewModel;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Handles interaction between the BoardFragment and the data models.
 * Created by Srikar on 7/6/2016.
 */
public class BoardFragmentModel {
    private static final String TAG = "BoardFragmentModel";

    //list of view models, so can destroy them in onDestroy()
    private List<GameViewModel> mGameViewModels = new ArrayList<>();

    public BoardFragmentModel(FragmentBoardBinding binding) {
        MagicLog.d(TAG, "BoardFragmentModel: Created");

        //create the ViewModels and populate mGameViewModels
        createViewModels(binding);
    }

    /**
     * Creates the ViewModels that handle interaction with the data model
     * @param binding Binding holding views in Fragment
     */
    private void createViewModels(FragmentBoardBinding binding) {
        //clear out list
        mGameViewModels.clear();

        //add view models
        mGameViewModels.add(new TurnCounterModel(binding));
        mGameViewModels.add(new LifeCounterModel(binding));
        mGameViewModels.add(new SwitchPlayerModel(binding));
        mGameViewModels.add(new GameActionLogModel(binding));
        mGameViewModels.add(new NextStepModel(binding));
        mGameViewModels.add(new CurrentlyUnusedModel(binding));

        //create recycler view models, which have to set in binding
        BaseRecyclerViewModel handRecViewModel = new HandRecViewModel(binding);
        BaseRecyclerViewModel landsRecViewModel = new BattlefieldRecViewModel(binding, DataModelConstants.LIST_LANDS);
        BaseRecyclerViewModel creaturesRecViewModel = new BattlefieldRecViewModel(binding, DataModelConstants.LIST_CREATURES);

        //set in binding
        binding.setHandModel(handRecViewModel);
        binding.setLandsModel(landsRecViewModel);
        binding.setCreaturesModel(creaturesRecViewModel);

        //add to list
        mGameViewModels.add(handRecViewModel);
        mGameViewModels.add(landsRecViewModel);
        mGameViewModels.add(creaturesRecViewModel);
    }

    /**
     * Call when containing View or Fragment is destroyed, will unregister Subscriptions
     */
    public void onDestroy() {
        //call onDestroy for each view model, removing the Subscriptions to RxJava Observables
        for (GameViewModel viewModel : mGameViewModels) {
            viewModel.onDestroy();
        }

        //clear list of view models
        mGameViewModels.clear();
    }
}
