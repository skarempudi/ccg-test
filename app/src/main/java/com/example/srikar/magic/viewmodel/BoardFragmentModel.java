package com.example.srikar.magic.viewmodel;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.R;
import com.example.srikar.magic.UiUtil;
import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.event.RxEventBus;
import com.example.srikar.magic.fragment.CombatDialogFragment;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.GameState;
import com.example.srikar.magic.model.zone.Battlefield;
import com.example.srikar.magic.viewmodel.recyclerview.BattlefieldRecViewModel;
import com.example.srikar.magic.viewmodel.recyclerview.HandRecViewModel;
import com.jakewharton.rxbinding.view.RxView;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Handles interaction between the BoardFragment and the data models.
 * Created by Srikar on 7/6/2016.
 */
public class BoardFragmentModel extends BaseObservable {
    private static final String TAG = "BoardFragmentModel";
    @Inject
    protected Battlefield mBattlefield;
    @Inject
    protected GameState mGameState;

    private final Activity mActivity;
    //view bindings for the Fragment
    private final FragmentBoardBinding mBinding;

    @Inject
    protected RxEventBus<GameStateChangeEvent> mGameStateChangeEventBus;
    //used to store all subscriptions, so can unsubscribe when destroy
    private final CompositeSubscription mSubscriptions;

    /**
     * RecyclerView Models, which handle more complex interactions and communicate with the
     * data model classes.
     */
    private HandRecViewModel mHandRecViewModel;
    private BattlefieldRecViewModel mLandsRecViewModel;
    private BattlefieldRecViewModel mCreaturesRecViewModel;

    public BoardFragmentModel(Activity context, FragmentBoardBinding binding) {
        MagicLog.d(TAG, "BoardFragmentModel: Created");
        //get instance of GameState
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);

        mActivity = context;
        mBinding = binding;

        //used to hold onClick subscriptions
        mSubscriptions = new CompositeSubscription();

        //set the backgrounds
        setBackgrounds();

        //set the turn text
        setTurnText();

        //set the life total texts
        setLifeText();

        //set the game action log starting text
        setLogText();

        //set next step button text
        setNextStepButtonText();

        //create the RecyclerViewModels
        createRecyclerViewModels();

        //attach the view models to the binding
        binding.setHandModel(mHandRecViewModel);
        binding.setLandsModel(mLandsRecViewModel);
        binding.setCreaturesModel(mCreaturesRecViewModel);

        //register on click event handlers
        registerOnClicks();

        //register listener for changes in GameState
        mSubscriptions.add(registerEventBus());
    }

    /**
     * Used to create the RecyclerViewModels that handle interaction with the data model
     */
    private void createRecyclerViewModels() {
        if (mHandRecViewModel == null) {
            mHandRecViewModel = new HandRecViewModel(mActivity);
        }
        if (mLandsRecViewModel == null) {
            mLandsRecViewModel = new BattlefieldRecViewModel(mActivity, DataModelConstants.LIST_LANDS);
        }
        if (mCreaturesRecViewModel == null) {
            mCreaturesRecViewModel = new BattlefieldRecViewModel(mActivity, DataModelConstants.LIST_CREATURES);
        }
    }

    /**
     * Set backgrounds based on the current view player
     */
    private void setBackgrounds() {
        int backgroundResource;
        if (mGameState.getViewPlayer() == DataModelConstants.PLAYER_ALICE) {
            MagicLog.d(TAG, "setBackgrounds: Alice");
            backgroundResource = R.drawable.alice_border;
        }
        else {
            MagicLog.d(TAG, "setBackgrounds: Bob");
            backgroundResource = R.drawable.bob_border;
        }

        //turn background based on current player, not view player
        int turnBackgroundResource;
        if (mGameState.getCurrentPlayer() == DataModelConstants.PLAYER_ALICE) {
            MagicLog.d(TAG, "setBackgrounds: Current player is Alice");
            turnBackgroundResource = R.drawable.alice_border;
        }
        else {
            MagicLog.d(TAG, "setBackgrounds: Current player is Bob");
            turnBackgroundResource = R.drawable.bob_border;
        }

        //first row - turns, life, switch player
        //turn's background is based on current player, not view player
        mBinding.turnCounter.setBackgroundResource(turnBackgroundResource);
        mBinding.lifeCounterAlice.setBackgroundResource(backgroundResource);
        mBinding.lifeCounterBob.setBackgroundResource(backgroundResource);
        mBinding.switchPlayer.setBackgroundResource(backgroundResource);

        //second row - game action log, next step
        mBinding.gameActionLog.setBackgroundResource(backgroundResource);
        mBinding.nextStep.setBackgroundResource(backgroundResource);

        //third row - opposing creatures
        mBinding.oppCreatures.setBackgroundResource(backgroundResource);

        //fourth row - my creatures
        mBinding.creaturesRecyclerview.setBackgroundResource(backgroundResource);

        //fifth row - lands, library, graveyard
        mBinding.landsRecyclerview.setBackgroundResource(backgroundResource);
        mBinding.library.setBackgroundResource(backgroundResource);
        mBinding.graveyard.setBackgroundResource(backgroundResource);

        //sixth row - hand
        mBinding.handRecyclerview.setBackgroundResource(backgroundResource);
    }

    /**
     * Set the current turn number and current player in the turn display
     */
    private void setTurnText() {
        //get unformatted string
        String unformatted = mActivity.getResources().getString(R.string.unformat_turn_display);

        //get turn number
        int turn = mGameState.getTurnNumber();

        //get current player name
        int currentPlayer = mGameState.getCurrentPlayer();
        String name;
        if (currentPlayer == DataModelConstants.PLAYER_ALICE) {
            name = mActivity.getResources().getString(R.string.alice);
        }
        else {
            name = mActivity.getResources().getString(R.string.bob);
        }

        //format the string
        String partial = String.format(unformatted, turn, name);
        //uses HTML to bold player name
        CharSequence formatted = UiUtil.formatHTML(partial);

        //set in turn display
        mBinding.turnCounter.setText(formatted);
    }

    /**
     * Set text for life total displays based on information in game state data model
     */
    private void setLifeText() {
        //get names
        String aliceName = mActivity.getResources().getString(R.string.alice);
        String bobName = mActivity.getResources().getString(R.string.bob);

        //get colors
        String aliceColor = mActivity.getResources().getString(R.string.alice_color);
        String bobColor = mActivity.getResources().getString(R.string.bob_color);

        //get life totals
        int aliceLife = mGameState.getLifeTotal(DataModelConstants.PLAYER_ALICE);
        int bobLife = mGameState.getLifeTotal(DataModelConstants.PLAYER_BOB);

        //get unformatted strings
        String unformat = mActivity.getResources().getString(R.string.unformat_life);

        //format the strings: color, name, life total
        String partialAlice = String.format(unformat, aliceColor, aliceName, aliceLife);
        String partialBob = String.format(unformat, bobColor, bobName, bobLife);

        //use HTML to finalize
        CharSequence formatAlice = UiUtil.formatHTML(partialAlice);
        CharSequence formatBob = UiUtil.formatHTML(partialBob);

        //put in life total displays
        mBinding.lifeCounterAlice.setText(formatAlice, TextView.BufferType.SPANNABLE);
        mBinding.lifeCounterBob.setText(formatBob, TextView.BufferType.SPANNABLE);
    }

    /**
     * Set text in game action log based on current step in game state data model
     */
    private void setLogText() {
        //get current step
        int step = mGameState.getCurrentStep();
        //get the string resource ID
        int stringId = DataModelConstants.getStepLogText(step);

        //set the text in log
        mBinding.gameActionLog.setText(stringId);
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

        //for each RecyclerViewModel, remove the subscriptions to RxJava Observables
        mHandRecViewModel.onDestroy();
        mLandsRecViewModel.onDestroy();
        mCreaturesRecViewModel.onDestroy();
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
     * EVENT BUS LISTENERS
     **********************************************************************************************/
    /**
     * When switch view player, change backgrounds and update Adapters
     * Triggered by change in game state data model
     */
    private void handleSwitchViewPlayer() {
        //updates the backgrounds
        setBackgrounds();

        //notifies the RecyclerViewModels to update Adapters
        mHandRecViewModel.onViewPlayerSwitched();
        mLandsRecViewModel.onViewPlayerSwitched();
        mCreaturesRecViewModel.onViewPlayerSwitched();

        //enable switch button
        mBinding.switchPlayer.setEnabled(true);
    }

    /**
     * When go to next step, update display on game log
     * Triggered by change in game state data model
     */
    private void handleNextStep() {
        //set log text based on current step listed in game state
        setLogText();
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
        //since next turn, update turn text
        setTurnText();

        //update the backgrounds and lists
        handleSwitchViewPlayer();

        //do normal stuff affiliated with handling next step
        handleNextStep();
    }

    /***********************************************************************************************
     * EVENT BUS
     **********************************************************************************************/

    /**
     * After get event bus from Dagger injection, subscribe to it. Returns subscription so can
     * reference it to unregister later.
     * @return Subscription to event bus
     */
    private Subscription registerEventBus() {
        MagicLog.d(TAG, "registerEventBus: ");
        return mGameStateChangeEventBus.getEvents()
                .subscribe(this::actOnEvent);
    }

    /**
     * When receive event from event bus, handle by calling another method depending on event type.
     * @param event Event received from event bus
     */
    private void actOnEvent(GameStateChangeEvent event) {
        MagicLog.d(TAG, "actOnEvent: " + event.toString());
        //if switching view player
        if (event.action == GameStateChangeEvent.SWITCH_VIEW_PLAYER) {
            handleSwitchViewPlayer();
        }
        //if going to next step in turn
        else if (event.action == GameStateChangeEvent.NEXT_STEP) {
            handleNextStep();
        }
        //if going to next turn
        else if (event.action == GameStateChangeEvent.NEXT_TURN) {
            handleNextTurn();
        }
    }
}
