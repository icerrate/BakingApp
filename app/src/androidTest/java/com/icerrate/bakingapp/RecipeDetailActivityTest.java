package com.icerrate.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.icerrate.bakingapp.view.recipe.RecipeDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.icerrate.bakingapp.view.recipe.RecipeDetailActivity.KEY_RECIPE_ID;

/**
 * @author Ivan Cerrate.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeDetailActivityTest {

    private final int RECIPE_ID = 1;

    @Rule
    public ActivityTestRule<RecipeDetailActivity> activityTestRule =
            new ActivityTestRule<RecipeDetailActivity>(RecipeDetailActivity.class){
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, RecipeDetailActivity.class);
                    result.putExtra(KEY_RECIPE_ID, RECIPE_ID);
                    return result;
                }
            };

    @Test
    public void titleIsDisplayed(){
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void ingredientsViewIsDisplayed(){
        onView(withId(R.id.ingredients)).check(matches(isDisplayed()));
    }

    @Test
    public void stepsListRecyclerViewIsDisplayed(){
        onView(withId(R.id.steps)).check(matches(isDisplayed()));
    }
}
