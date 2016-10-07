package com.example.srikar.magic.viewmodel.list;

import android.databinding.ViewDataBinding;
import android.view.View;
import android.widget.ImageView;

import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.databinding.PermanentBinding;
import com.example.srikar.magic.model.Card;
import com.example.srikar.magic.model.detail.CreatureDetails;
import com.example.srikar.magic.model.state.Combat;
import com.example.srikar.magic.model.state.Turn;
import com.example.srikar.magic.view.BoardBinding;
import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.R;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.zone.Battlefield;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

/**
 * View model for Battlefield list of your creatures or lands
 * Created by Srikar on 7/7/2016.
 */
public class BattlefieldListViewModel extends BaseCardListViewModel {
    @Inject
    protected Battlefield mBattlefield;
    @Inject
    protected Combat mCombat;
    @Inject
    protected Turn mTurn;

    public BattlefieldListViewModel(BoardBinding binding, int listName) {
        super(binding, listName);
        //injects singleton instance of Battlefield
        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);

        //set in binding, based on list name
        if (listName == DataModelConstants.LIST_MY_CREATURES) {
            binding.get().setCreaturesModel(this);
        }
        else if (listName == DataModelConstants.LIST_LANDS) {
            binding.get().setLandsModel(this);
        }
    }

    /**
     * Gets the number of items in the RecyclerView from a list in the Battlefield specified during
     * construction of this class.
     * @return Number of items in Battlefield list
     */
    @Override
    public int getItemCount() {
        return mBattlefield.getViewPlayerListSize(mListName);
    }

    @Override
    public int getCardLayout() {
        return R.layout.permanent;
    }

    @Override
    public void onItemClick(ViewDataBinding binding, int position) {
        if (mListName == DataModelConstants.LIST_MY_CREATURES) {
            //get creature
            Card creature = retrievePermanent(position);
            PermanentBinding permBinding = (PermanentBinding)binding;

            //if is own turn, declare attackers step, and attack has not been confirmed, then
            //toggle creature attack declaration
            if (mPlayerInfo.getCurrentPlayer() == mPlayerInfo.getViewPlayer()
                    && mTurn.getCurrentStep() == DataModelConstants.STEP_DECLARE_ATTACKERS
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
        //get the Permanent
        Card perm = retrievePermanent(position);

        //if tapped, rotate 90 degrees
        //in future, will instead have a set of custom transformations that apply
        if (perm.isTapped()) {
            MagicLog.d(TAG, "onItemLoad: Drawing tapped");
            Picasso.with(view.getContext())
                    .load(R.drawable.ic_launcher)
                    .rotate(90f)
                    .into(view);
        }
        else {
            MagicLog.d(TAG, "onItemLoad: Drawing untapped");
            Picasso.with(view.getContext())
                    .load(R.drawable.ic_launcher)
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
        if (mListName == DataModelConstants.LIST_MY_CREATURES) {
            permBinding.powerToughness.setText(getPTText(perm));
        }
    }

    /**
     * Using the listName list and the position in that list, get the Permanent Card that matches to this
     * position in the RecyclerView
     * @param position Position in list of Cards
     * @return Permanent
     */
    private Card retrievePermanent(int position) {
        return mBattlefield.getViewPlayerPermanent(mListName, position);
    }

    /**
     * Get text to display for creature power and toughness
     * @param perm Card that getting details from
     * @return Power/toughness string
     */
    public String getPTText(Card perm) {
        CreatureDetails details = perm.details;
        return details.power + "/" + details.toughness;
    }

    /**
     * Used to update the background color of this view, based on the current player or view player
     */
    @Override
    public void updateBackground() {
        //background based on view player
        int backgroundResource = getViewPlayerBackground();

        //choose which view to set based on list name
        if (mListName == DataModelConstants.LIST_LANDS) {
            mBinding.get().landsRecyclerview.setBackgroundResource(backgroundResource);
        }
        else if (mListName == DataModelConstants.LIST_MY_CREATURES) {
            mBinding.get().creaturesRecyclerview.setBackgroundResource(backgroundResource);
        }
    }
}
