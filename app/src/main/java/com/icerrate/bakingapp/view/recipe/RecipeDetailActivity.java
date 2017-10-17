package com.icerrate.bakingapp.view.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.data.model.Recipe;
import com.icerrate.bakingapp.data.model.Step;
import com.icerrate.bakingapp.view.common.BaseActivity;
import com.icerrate.bakingapp.view.step.StepDetailActivity;
import com.icerrate.bakingapp.view.step.StepDetailFragment;

import butterknife.BindView;

import static com.icerrate.bakingapp.view.recipe.RecipeDetailFragment.KEY_RECIPE_DETAIL;

public class RecipeDetailActivity extends BaseActivity implements RecipeDetailActivityListener {

    public String KEY_IS_TWO_PANE = "IS_TWO_PANE_KEY";

    @Nullable
    @BindView(R.id.sub_content)
    public FrameLayout subContentFrameLayout;

    private boolean isTwoPane = false;

    private Recipe recipeDetail;

    public static Intent makeIntent(Context context) {
        return new Intent(context, RecipeDetailActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        setNavigationToolbar(true);
        if (savedInstanceState == null) {
            recipeDetail = getIntent().getParcelableExtra(KEY_RECIPE_DETAIL);
            RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(recipeDetail);
            replaceFragment(R.id.content, recipeDetailFragment);
        } else {
            onRestoreInstanceState(savedInstanceState);
        }
        determinePaneLayout();
        setTitle(recipeDetail.getName());
    }

    private void determinePaneLayout() {
        if (subContentFrameLayout != null) {
            isTwoPane = true;
            StepDetailFragment stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.sub_content);
            if (stepDetailFragment != null) {
                subContentFrameLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_RECIPE_DETAIL, recipeDetail);
        outState.putBoolean(KEY_IS_TWO_PANE, isTwoPane);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recipeDetail = savedInstanceState.getParcelable(KEY_RECIPE_DETAIL);
        isTwoPane = savedInstanceState.getBoolean(KEY_IS_TWO_PANE);
    }

    @Override
    public void onRecipeStepSelected(Step step) {
        if (isTwoPane) { // single activity with list and detail
            subContentFrameLayout.setVisibility(View.VISIBLE);
            StepDetailFragment stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.sub_content);
            if (stepDetailFragment != null) {
                getSupportFragmentManager().beginTransaction().remove(stepDetailFragment).commit();
            }
            // Replace frame layout with correct detail fragment
            StepDetailFragment recipeDetailFragment = StepDetailFragment.newInstance(step);
            replaceFragment(R.id.sub_content, recipeDetailFragment);
        } else { // separate activities
            // launch detail activity using intent
            startActivity(StepDetailActivity.makeIntent(this)
                    .putExtra(StepDetailFragment.KEY_STEP_DETAIL, step));
        }
    }
}
