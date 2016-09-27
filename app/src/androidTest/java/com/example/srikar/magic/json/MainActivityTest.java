package com.example.srikar.magic.json;

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

    /**
     * Check text of turn counter
     * @param turnNum Expected turn
     * @param player Expected player name
     */
    private void checkTurnText(int turnNum, String player) {
        String turn = "Turn " + turnNum + "\n" + player;
        onView(withId(R.id.turn_counter))
                .check(matches(withText(turn)));
    }

    /**
     * Check text of Alice life counter
     * @param lifeVal Expected life value
     */
    private void checkLifeAliceText(int lifeVal) {
        String life = "Alice\n" + lifeVal;
        onView(withId(R.id.life_counter_alice))
                .check(matches(withText(life)));
    }

    /**
     * Check text of Bob life counter
     * @param lifeVal Expected life value
     */
    private void checkLifeBobText(int lifeVal) {
        String life = "Bob\n" + lifeVal;
        onView(withId(R.id.life_counter_bob))
                .check(matches(withText(life)));
    }

    /**
     * Check text of game action log
     * @param expected Expected message
     */
    private void checkLogText(String expected) {
        onView(withId(R.id.game_action_log))
                .check(matches(withText(expected)));
    }

    /**
     * Check text of next step button
     * @param isConfirmAttack If button says to confirm attack
     */
    private void checkNextStepText(boolean isConfirmAttack) {
        String expected = isConfirmAttack? "Confirm Attack" : "Next Step";
        onView(withId(R.id.next_step))
                .check(matches(withText(expected)));
    }

    @Test
    public void testStartingState() {
        //check text of turn counter
        checkTurnText(1, "Alice");

        //check text of first life counter
        checkLifeAliceText(20);

        //check text of second life counter
        checkLifeBobText(20);

        //check text of game action log
        checkLogText("Untap step - your permanents untap");

        //check text of next step button
        checkNextStepText(false);
    }
}
