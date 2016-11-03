package com.example.srikar.magic.viewmodel.list;

import android.databinding.ViewDataBinding;
import android.view.View;
import android.widget.ImageView;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.R;
import com.example.srikar.magic.databinding.PermanentBinding;
import com.example.srikar.magic.model.Card;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.detail.CreatureDetails;
import com.example.srikar.magic.model.state.Combat;
import com.example.srikar.magic.model.state.Turn;
import com.example.srikar.magic.model.zone.Battlefield;
import com.example.srikar.magic.view.BoardBinding;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

/**
 * Base class for all view models that display a list of creatures
 * Created by Srikar on 11/1/2016.
 */

public abstract class CreaturesListViewModel extends BaseCardListViewModel {
    @Inject
    Battlefield mBattlefield;
    @Inject
    Combat mCombat;
    @Inject
    Turn mTurn;

    public CreaturesListViewModel(BoardBinding binding, int listName) {
        super(binding, listName);
        //injects singleton instances
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);
    }

    @Override
    public int getCardLayout() {
        return R.layout.permanent;
    }

    /**
     * Called by the CreaturesListViewModel implementations on the game board
     * @param binding Binding for the view
     * @param position What card in list was clicked
     */
    protected void onBoardItemClick(ViewDataBinding binding, int position) {
        //get creature
        Card creature = retrieveCreature(position);
        PermanentBinding permBinding = (PermanentBinding)binding;

        //if is active player list
        if (isActivePlayerList()) {
            //if declare attackers and attack hasn't been confirmed, toggle creature's attack declaration
            if (mTurn.getCurrentStep() == DataModelConstants.STEP_DECLARE_ATTACKERS
                    && !mCombat.isAttackConfirmed()) {
                //if declared attacking, declare not attacking
                if (creature.isDeclaredAttacking()) {
                    //remove from combat list
                    mCombat.removeAttacker(creature);
                    creature.declareAttack(false);
                    //set attack icon to invisible
                    permBinding.attackIcon.setVisibility(View.INVISIBLE);
                }
                //if not declared attacking, declare attacking
                else {
                    //add to combat list
                    mCombat.addAttacker(creature);
                    creature.declareAttack(true);
                    //set attack icon to visible
                    permBinding.attackIcon.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onItemLoad(ViewDataBinding binding, int position) {
        PermanentBinding permBinding = (PermanentBinding)binding;

        //get the image view
        ImageView view = permBinding.cardImage;
        //get the permanent
        Card perm = retrieveCreature(position);

        //if tapped, rotate 90 degrees from untapped rotation
        if (perm.isTapped()) {
            MagicLog.d(TAG, "onItemLoad: Drawing tapped");
            Picasso.with(view.getContext())
                    .load(R.drawable.ic_launcher)
                    .rotate(getTappedRotation())
                    .into(view);
        }
        else {
            MagicLog.d(TAG, "onItemLoad: Drawing untapped");
            Picasso.with(view.getContext())
                    .load(R.drawable.ic_launcher)
                    .rotate(getUntappedRotation())
                    .into(view);
        }

        //if declared attacking, make icon visible, or invisible if not
        if (perm.isDeclaredAttacking()) {
            permBinding.attackIcon.setVisibility(View.VISIBLE);
        }
        else {
            permBinding.attackIcon.setVisibility(View.INVISIBLE);
        }

        //set power and toughness
        permBinding.powerToughness.setText(getPTText(perm));
    }

    /**
     * Using the relevant creature list, get the Card that matches to this position in the RecyclerView
     * @param position Position in list of Cards
     * @return Creature
     */
    protected abstract Card retrieveCreature(int position);

    /**
     * Gets how much the card image should be rotated when untapped
     * @return Rotation in degrees
     */
    protected abstract float getUntappedRotation();

    /**
     * Gets how much the card image should be rotated when tapped
     * @return Rotation in degrees
     */
    protected abstract float getTappedRotation();

    /**
     * Get text to display for creature power and toughness
     * @param perm Card that getting details from
     * @return Power/toughness string
     */
    protected String getPTText(Card perm) {
        CreatureDetails details = perm.details;
        return details.power + "/" + details.toughness;
    }

    /**
     * Used for onClick events, determine if interacting with list for the active player
     * @return If interacting with list belonging to active player
     */
    protected boolean isActivePlayerList() {
        //if interacting with MY_CREATURES list while viewing as active player, or if interacting
        //with OPP_CREATURES list while viewing as opposing player
        return (mListName == DataModelConstants.LIST_MY_CREATURES) ==
                (mPlayerInfo.getActivePlayer() == mPlayerInfo.getViewPlayer());
    }
}
