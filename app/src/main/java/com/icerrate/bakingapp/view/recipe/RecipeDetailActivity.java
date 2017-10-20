package com.icerrate.bakingapp.view.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.data.model.Step;
import com.icerrate.bakingapp.view.common.BaseActivity;
import com.icerrate.bakingapp.view.step.StepDetailActivity;
import com.icerrate.bakingapp.view.step.StepDetailFragment;

public class RecipeDetailActivity extends BaseActivity implements RecipeDetailActivityListener {

    public static String KEY_RECIPE_ID = "RECIPE_ID_KEY";

    public static String KEY_SELECTED_STEP = "SELECTED_STEP_KEY";

    private Integer recipeId;

    public static Intent makeIntent(Context context, Integer recipeId) {
        return new Intent(context, RecipeDetailActivity.class)
                .putExtra(KEY_RECIPE_ID, recipeId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        setNavigationToolbar(true);
        if (savedInstanceState == null && getIntent().hasExtra(KEY_RECIPE_ID)) {
            recipeId = getIntent().getIntExtra(KEY_RECIPE_ID, 0);
            if(isPhone) {
                RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(recipeId, -1);
                replaceFragment(R.id.content, recipeDetailFragment);
            } else {
                RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(recipeId, 0);
                replaceFragment(R.id.content, recipeDetailFragment);
                StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(recipeId, 0);
                replaceFragment(R.id.sub_content, stepDetailFragment);
            }
        } else {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_RECIPE_ID, recipeId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recipeId = savedInstanceState.getInt(KEY_RECIPE_ID);
    }

    @Override
    public void onRecipeStepSelected(Step step) {
        if(isPhone){
            startActivity(StepDetailActivity.makeIntent(this, recipeId, step.getId()));
        } else {
            StepDetailFragment stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.sub_content);
            if (stepDetailFragment != null) {
                getSupportFragmentManager().beginTransaction().remove(stepDetailFragment).commit();
            }
            // Replace frame layout with correct detail fragment
            StepDetailFragment recipeDetailFragment = StepDetailFragment.newInstance(recipeId, step.getId());
            replaceFragment(R.id.sub_content, recipeDetailFragment);
        }
    }
}
