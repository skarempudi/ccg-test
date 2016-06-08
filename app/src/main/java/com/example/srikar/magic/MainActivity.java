package com.example.srikar.magic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.srikar.magic.model.GameState;
import com.example.srikar.magic.model.Hand;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Inject
    GameState gameState;
    @Inject
    Hand hand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MagicApplication.getInstance()
                .getMainComponent()
                .inject(this);

        String name = gameState.getName();
        Log.d(TAG, "onCreate: " + name);
    }
}
