package com.example.srikar.magic.dagger;

import com.example.srikar.magic.MainActivity;
import com.example.srikar.magic.adapter.BaseBfRecViewAdapter;
import com.example.srikar.magic.adapter.HandViewAdapter;
import com.example.srikar.magic.viewmodel.BaseRecyclerViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Srikar on 4/28/2016.
 */
@Singleton
@Component (modules = {BattlefieldModule.class, GameStateModule.class, HandModule.class})
public interface MainComponent {
    void inject(MainActivity activity);
    void inject(HandViewAdapter adapter);
    void inject(BaseBfRecViewAdapter adapter);
    void inject(BaseRecyclerViewModel viewModel);
}
