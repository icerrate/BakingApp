package com.icerrate.bakingapp;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.icerrate.bakingapp.view.recipes.RecipesListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author Ivan Cerrate.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipesListActivityTest {

    @Rule
    public ActivityTestRule<RecipesListActivity> activityTestRule =
            new ActivityTestRule<>(RecipesListActivity.class);

    @Test
    public void titleIsDisplayed(){
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void recipesListRecyclerViewIsDisplayed(){
        onView(withId(R.id.recipes)).check(matches(isDisplayed()));
    }
}
