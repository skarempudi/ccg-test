package com.example.srikar.magic.dagger;

import com.example.srikar.magic.MagicApplication;
import com.example.srikar.magic.viewmodel.board.GameActionLogModel;
import com.example.srikar.magic.viewmodel.board.LifeCounterModel;
import com.example.srikar.magic.viewmodel.board.NextStepModel;
import com.example.srikar.magic.viewmodel.BaseBoardModel;
import com.example.srikar.magic.viewmodel.board.TurnCounterModel;
import com.example.srikar.magic.viewmodel.list.BaseCardListViewModel;
import com.example.srikar.magic.viewmodel.list.MyBattlefieldListViewModel;
import com.example.srikar.magic.viewmodel.list.HandListViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Use this for Dagger injections, to handle dependencies and spread singletons.
 * Created by Srikar on 4/28/2016.
 */
@Singleton
@Component (modules = {BattlefieldModule.class, GameStateModule.class, HandModule.class, RxEventBusModule.class})
public interface MainComponent {
    void inject(BaseBoardModel viewModel);

    void inject(TurnCounterModel viewModel);
    void inject(LifeCounterModel viewModel);
    void inject(GameActionLogModel viewModel);
    void inject(NextStepModel viewModel);

    void inject(BaseCardListViewModel viewModel);
    void inject(MyBattlefieldListViewModel viewModel);
    void inject(HandListViewModel viewModel);
    //remove later
    void inject(MagicApplication app);
}
