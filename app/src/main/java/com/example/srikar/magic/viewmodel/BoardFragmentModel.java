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
public class BoardFragmentModel implements
        GameStateChangeBus.SwitchViewPlayerListener,
        GameStateChangeBus.NextStepListener,
        GameStateChangeBus.NextTurnListener {
    private static final String TAG = "BoardFragmentModel";
    @Inject
    protected Battlefield mBattlefield;
    @Inject
    protected GameState mGameState;

    private Activity mActivity;
    //view bindings for the Fragment
    private final FragmentBoardBinding mBinding;

    @Inject
    protected GameStateChangeBus mGameStateChangeBus;
    //used to store all subscriptions, so can unsubscribe when destroy
    private final CompositeSubscription mSubscriptions;

    //list of view models, so can destroy them in onDestroy()
    private List<GameViewModel> mGameViewModels = new ArrayList<>();

    public BoardFragmentModel(Activity activity, FragmentBoardBinding binding) {
        MagicLog.d(TAG, "BoardFragmentModel: Created");
        //get instance of GameState
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);

        mActivity = activity;
        mBinding = binding;

        //used to hold onClick subscriptions
        mSubscriptions = new CompositeSubscription();

        //create the ViewModels, populate mGameViewModels, and bind RecyclerViewModels
        createViewModels();

        //TODO: Move to NextStepModel
        //set next step button text
        setNextStepButtonText();

        //TODO: Move to respective ViewModels
        //register on click event handlers
        registerOnClicks();

        //TODO: Remove when no longer have any in this class
        //register listeners for changes in GameState
        subscribeEventBus();
    }

    /**
     * Used to create the ViewModels that handle interaction with the data model
     */
    private void createViewModels() {
        //clear out list
        mGameViewModels.clear();

        //add view models
        mGameViewModels.add(new TurnCounterModel(mBinding));
        mGameViewModels.add(new LifeCounterModel(mBinding));
        mGameViewModels.add(new SwitchPlayerModel(mBinding));
        mGameViewModels.add(new GameActionLogModel(mBinding));
        mGameViewModels.add(new NextStepModel(mBinding));
        mGameViewModels.add(new CurrentlyUnusedModel(mBinding));

        //create recycler view models, which have to set in binding
        BaseRecyclerViewModel handRecViewModel = new HandRecViewModel(mBinding);
        BaseRecyclerViewModel landsRecViewModel = new BattlefieldRecViewModel(mBinding, DataModelConstants.LIST_LANDS);
        BaseRecyclerViewModel creaturesRecViewModel = new BattlefieldRecViewModel(mBinding, DataModelConstants.LIST_CREATURES);

        //set in binding
        mBinding.setHandModel(handRecViewModel);
        mBinding.setLandsModel(landsRecViewModel);
        mBinding.setCreaturesModel(creaturesRecViewModel);

        //add to list
        mGameViewModels.add(handRecViewModel);
        mGameViewModels.add(landsRecViewModel);
        mGameViewModels.add(creaturesRecViewModel);
    }

    /**
     * Set text in next step button depending on if want to confirm combat steps first or not
     */
    private void setNextStepButtonText() {
        //if during declare attackers and haven't confirmed attackers
        if (mBattlefield.shouldConfirmAttack()) {
                mBinding.nextStep.setText(R.string.confirm_attack);
                return;
        }

        //not during declare attackers, or don't need to confirm
        mBinding.nextStep.setText(R.string.next_step);
    }

    /**
     * Call when containing View or Fragment is destroyed, will unregister Subscriptions
     */
    public void onDestroy() {
        //remove all subscriptions
        if (mSubscriptions != null) {
            mSubscriptions.unsubscribe();
        }

        //call onDestroy for each view model, removing the Subscriptions to RxJava Observables
        for (GameViewModel viewModel : mGameViewModels) {
            viewModel.onDestroy();
        }

        //clear list of view models
        mGameViewModels.clear();

        //remove reference to context
        mActivity = null;
    }

    /***********************************************************************************************
     * ON CLICK EVENTS
     **********************************************************************************************/
    /**
     * Register onClick event handling for views other than RecyclerViews
     * RecyclerView onClicks handled by Permanent
     */
    private void registerOnClicks() {
        //subscribe to onClick for player switch button
        Subscription switchSub = RxView.clicks(mBinding.switchPlayer)
                .subscribe(this::switchPlayerOnClick);
        mSubscriptions.add(switchSub);

        //subscribe to onClick for next step button
        Subscription nextStepSub = RxView.clicks(mBinding.nextStep)
                .subscribe(this::nextStepOnClick);
        mSubscriptions.add(nextStepSub);
    }

    /**
     * When click the switch player button, switches the view player in data model
     * @param empty Handles void passed by Observable
     */
    private void switchPlayerOnClick(Void empty) {
        //disable switch button
        mBinding.switchPlayer.setEnabled(false);

        //switch the player in the data model
        mGameState.switchViewPlayer();
    }

    /**
     * When click the next step button, goes to the next step in the turn in the data model
     * @param empty Handles void passed by Observable
     */
    private void nextStepOnClick(Void empty) {
        //disable next step button
        mBinding.nextStep.setEnabled(false);

        //if need to confirm attack, then display dialog
        if (mBattlefield.shouldConfirmAttack()) {
            CombatDialogFragment dialogFragment = new CombatDialogFragment();
            //set listeners for the three buttons
            dialogFragment.setListeners(
                    this::attackersConfirmNextStep,
                    this::attackersConfirmSameStep,
                    this::attackersCancel
                    );

            //create the dialog
            dialogFragment.show(((AppCompatActivity)mActivity).getSupportFragmentManager(), "confirm attackers");
        }
        //if don't need to confirm attack, go to next step
        else {
            //go to next step in the data model
            mGameState.nextStep();
        }
    }

    /***********************************************************************************************
     * DIALOG RESPONSE LISTENERS
     **********************************************************************************************/
    public void attackersConfirmNextStep(DialogInterface dialog, int id) {
        //confirm attack
        mBattlefield.confirmAttack();
        //go to the next step in the data model
        mGameState.nextStep();
    }

    public void attackersConfirmSameStep(DialogInterface dialog, int id) {
        //confirm attack
        mBattlefield.confirmAttack();
        //don't go to the next step, but update next step button
        setNextStepButtonText();
        //enable button
        mBinding.nextStep.setEnabled(true);
    }

    public void attackersCancel(DialogInterface dialog, int id) {
        //enable button
        mBinding.nextStep.setEnabled(true);
    }

    /***********************************************************************************************
     * EVENT BUS HANDLERS
     **********************************************************************************************/
    /**
     * When switch view player, change backgrounds and update Adapters
     * Triggered by change in game state data model
     */
    private void handleSwitchViewPlayer() {
        //enable switch button
        mBinding.switchPlayer.setEnabled(true);
    }

    /**
     * When go to next step, update display on game log
     * Triggered by change in game state data model
     */
    private void handleNextStep() {
        //set next step text, which can change during combat
        setNextStepButtonText();

        //enable next step button
        mBinding.nextStep.setEnabled(true);
    }

    /**
     * When go to next turn, update turn, game log, and background
     * Triggered by change in game state data model
     */
    private void handleNextTurn() {
        //update the lists
        handleSwitchViewPlayer();

        //do normal stuff affiliated with handling next step
        handleNextStep();
    }

    /***********************************************************************************************
     * EVENT BUS LISTENERS
     **********************************************************************************************/
    /**
     * After get event bus from Dagger injection, subscribe to it.
     * Add subscriptions to CompositeSubscription
     */
    private void subscribeEventBus() {
        MagicLog.d(TAG, "subscribeEventBus: ");
        mSubscriptions.add(mGameStateChangeBus.subscribeSwitchViewPlayerListener(this));
        mSubscriptions.add(mGameStateChangeBus.subscribeNextStepListener(this));
        mSubscriptions.add(mGameStateChangeBus.subscribeNextTurnListener(this));
    }

    @Override
    public void onSwitchViewPlayer(GameStateChangeEvent event) {
        handleSwitchViewPlayer();
    }

    @Override
    public void onNextStep(GameStateChangeEvent event) {
        handleNextStep();
    }

    @Override
    public void onNextTurn(GameStateChangeEvent event) {
        handleNextTurn();
    }
}
