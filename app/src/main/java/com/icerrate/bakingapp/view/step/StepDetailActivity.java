package com.icerrate.bakingapp.view.step;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.view.common.BaseActivity;

import static com.icerrate.bakingapp.view.recipe.RecipeDetailActivity.KEY_RECIPE_ID;
import static com.icerrate.bakingapp.view.recipe.RecipeDetailActivity.KEY_SELECTED_STEP;

public class StepDetailActivity extends BaseActivity {

    public static Intent makeIntent(Context context, Integer recipeId, Integer selectedStep) {
        return new Intent(context, StepDetailActivity.class)
                .putExtra(KEY_RECIPE_ID, recipeId)
                .putExtra(KEY_SELECTED_STEP, selectedStep);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setNavigationToolbar(true);
        enableRotation();
        if (savedInstanceState == null) {
            Integer recipeId = getIntent().getIntExtra(KEY_RECIPE_ID, -1);
            Integer selectedStep = getIntent().getIntExtra(KEY_SELECTED_STEP, -1);
            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(recipeId, selectedStep);
            replaceFragment(R.id.content, stepDetailFragment);
        } else {
            onRestoreInstanceState(savedInstanceState);
        }
    }
}
