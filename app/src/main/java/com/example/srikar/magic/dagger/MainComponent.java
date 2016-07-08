package com.example.srikar.magic.dagger;

import com.example.srikar.magic.viewmodel.BoardFragmentModel;
import com.example.srikar.magic.viewmodel.recyclerview.BaseRecyclerViewModel;
import com.example.srikar.magic.viewmodel.PermanentViewModel;
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
    void inject(BaseRecyclerViewModel viewModel);
    void inject(BattlefieldRecViewModel viewModel);
    void inject(HandRecViewModel viewModel);
    void inject(PermanentViewModel viewModel);
}
