package com.example.srikar.magic.dagger;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.viewmodel.BattlefieldCardViewModel;
import com.example.srikar.magic.viewmodel.BoardFragmentModel;
import com.example.srikar.magic.viewmodel.BaseBoardModel;
import com.example.srikar.magic.viewmodel.recyclerview.BaseRecyclerViewModel;
import com.example.srikar.magic.viewmodel.recyclerview.BattlefieldRecViewModel;
import com.example.srikar.magic.viewmodel.recyclerview.HandRecViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Use this for Dagger injections, to handle dependencies and spread singletons.
 * Created by Srikar on 4/28/2016.
 */
@Singleton
@Component (modules = {BattlefieldModule.class, GameStateModule.class, HandModule.class})
public interface MainComponent {
    void inject(BoardFragmentModel viewModel);

    //non-recyclerview views
    void inject(BaseBoardModel viewModel);

    void inject(BaseRecyclerViewModel viewModel);
    void inject(BattlefieldRecViewModel viewModel);
    void inject(HandRecViewModel viewModel);
    void inject(BattlefieldCardViewModel viewModel);
    //remove later
    void inject(MagicApplication app);
}
