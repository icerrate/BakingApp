package com.icerrate.bakingapp.view.recipes.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.data.model.Recipe;
import com.icerrate.bakingapp.view.common.BaseActivity;

public class RecipeDetailActivity extends BaseActivity {

    private Recipe recipeDetail;

    public static Intent makeIntent(Context context) {
        return new Intent(context, RecipeDetailActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setNavigationToolbar(true);
        if (savedInstanceState == null) {
            recipeDetail = getIntent().getParcelableExtra(RecipeDetailFragment.KEY_RECIPE_DETAIL);
            RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(recipeDetail);
            replaceFragment(R.id.content, recipeDetailFragment);
        } else {
            onRestoreInstanceState(savedInstanceState);
        }
        setTitle(recipeDetail.getName());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RecipeDetailFragment.KEY_RECIPE_DETAIL, recipeDetail);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recipeDetail = savedInstanceState.getParcelable(RecipeDetailFragment.KEY_RECIPE_DETAIL);
    }
}
