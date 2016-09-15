package com.example.srikar.magic.viewmodel.board;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.R;
import com.example.srikar.magic.databinding.FragmentBoardBinding;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.fragment.CombatDialogFragment;
import com.example.srikar.magic.model.zone.Battlefield;
import com.example.srikar.magic.viewmodel.BaseBoardModel;
import com.jakewharton.rxbinding.view.RxView;

import javax.inject.Inject;

import rx.Subscription;

/**
 * View model for next step button
 * Created by Srikar on 8/25/2016.
 */
public class NextStepModel extends BaseBoardModel {
    //bound to layout
    //changes text
    public ObservableInt buttonTextId = new ObservableInt(R.string.next_step);
    //enables and disables button
    public ObservableBoolean enabled = new ObservableBoolean(true);

    @Inject
    Battlefield mBattlefield;

    /**
     * View model for next step button
     * @param binding Binding used to access view that will update
     */
    public NextStepModel(FragmentBoardBinding binding) {
        super(binding);

        //inject Battlefield
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);

        //set in binding
        binding.setNextStepModel(this);

        //set text
        setNextStepButtonText();
    }

    /**
     * Used to update the background color of this view, based on the current player or view player
     */
    @Override
    public void updateBackground() {
        //background based on view player
        int backgroundResource = getViewPlayerBackground();

        //set resource
        mBinding.nextStep.setBackgroundResource(backgroundResource);
    }

    /**
     * Set text in next step button depending on if want to confirm combat steps first or not
     */
    void setNextStepButtonText() {
        //if during declare attackers and haven't confirmed attackers
        if (mBattlefield.shouldConfirmAttack()) {
            buttonTextId.set(R.string.confirm_attack);
            return;
        }

        //not during declare attackers, or don't need to confirm
        buttonTextId.set(R.string.next_step);
    }

    /**
     * When click the next step button, goes to the next step in the turn in the data model
     * Called from fragment_board.xml
     */
    public void nextStepOnClick(View view) {
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
            AppCompatActivity activity = (AppCompatActivity)mBinding.getRoot().getContext();
            dialogFragment.show(activity.getSupportFragmentManager(), "confirm attackers");
        }
        //if don't need to confirm attack, go to next step
        else {
            goToNextStep();
        }
    }

    /**
     * Actually proceed to the next step
     */
    public void goToNextStep() {
        //disable next step button
        enabled.set(false);
        //go to next step in the data model
        mGameState.nextStep();
    }

    /***********************************************************************************************
     * DIALOG RESPONSE LISTENERS
     **********************************************************************************************/
    public void attackersConfirmNextStep(DialogInterface dialog, int id) {
        //confirm attack
        mBattlefield.confirmAttack();
        //proceed to next step
        goToNextStep();
    }

    public void attackersConfirmSameStep(DialogInterface dialog, int id) {
        //confirm attack
        mBattlefield.confirmAttack();
        //don't go to the next step, but update next step button
        setNextStepButtonText();
    }

    public void attackersCancel(DialogInterface dialog, int id) {
        //do nothing
    }

    /***********************************************************************************************
     * EVENT BUS LISTENER
     **********************************************************************************************/
    @Override
    public void onGameStateChange(GameStateChangeEvent event) {
        //update background
        super.onGameStateChange(event);

        //handles next step and next turn
        if (event.action == GameStateChangeEvent.NEXT_STEP ||
                event.action == GameStateChangeEvent.NEXT_TURN) {
            //set next step text, which can change during combat
            setNextStepButtonText();

            //enable next step button
            enabled.set(true);
        }
    }
}
