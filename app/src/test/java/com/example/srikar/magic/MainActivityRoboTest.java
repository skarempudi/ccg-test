package com.example.srikar.magic;

import android.os.Build;
import android.widget.TextView;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Robolectric test for MainActivity
 * Created by Srikar on 10/17/2016.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class MainActivityRoboTest {
    private MainActivity activity;

    public MainActivityRoboTest() {
        MagicLog.setLogging(false);
    }

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void validateStartingText() {
        TextView tvTurn = (TextView) activity.findViewById(R.id.turn_counter);
        assertNotNull("Turn counter could not be found", tvTurn);

        String text = tvTurn.getText().toString();
        String correct = "Turn 1\nAlice";
        assertTrue("Turn counter has incorrect text, is " + text,
                text.equals(correct));
    }
}
