package com.example.srikar.magic.json;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.srikar.magic.MainActivity;
import com.example.srikar.magic.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Uses Espresso to test UI interactions in MainActivity
 * Since data models handled by Dagger as singletons, won't be reset by Activity being destroyed.
 * If this becomes an issue, can implement ability to reset data by following this advice:
 * http://stackoverflow.com/questions/26939340/how-do-you-override-a-module-dependency-in-a-unit-test-with-dagger-2-0
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testStartingState() {
        //check text of turn counter
        String turn = "Turn 1\nAlice";
        onView(withId(R.id.turn_counter))
                .check(matches(withText(turn)));
    }
}
