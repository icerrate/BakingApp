package com.icerrate.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.icerrate.bakingapp.view.step.StepDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.icerrate.bakingapp.view.recipe.RecipeDetailActivity.KEY_RECIPE_ID;
import static com.icerrate.bakingapp.view.recipe.RecipeDetailActivity.KEY_SELECTED_STEP;

/**
 * @author Ivan Cerrate.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class StepDetailActivityTest {

    private final int RECIPE_ID = 1;

    private final int SELECTED_STEP = 0;

    @Rule
    public ActivityTestRule<StepDetailActivity> activityTestRule =
            new ActivityTestRule<StepDetailActivity>(StepDetailActivity.class){
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, StepDetailActivity.class);
                    result.putExtra(KEY_RECIPE_ID, RECIPE_ID);
                    result.putExtra(KEY_SELECTED_STEP, SELECTED_STEP);
                    return result;
                }
            };

    @Test
    public void titleIsDisplayed(){
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void videoViewIsDisplayed(){
        onView(withId(R.id.video)).check(matches(isDisplayed()));
    }

    @Test
    public void descriptionIsDisplayed(){
        onView(withId(R.id.description)).check(matches(isDisplayed()));
    }
}
