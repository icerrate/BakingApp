package com.icerrate.bakingapp.view.recipes;

import android.os.Bundle;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.view.common.BaseActivity;

public class RecipesListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setNavigationToolbar(true);
        setTitle(getString(R.string.title_activity_recipes));
        if (savedInstanceState == null) {
            RecipesListFragment recipesListFragment = RecipesListFragment.newInstance();
            replaceFragment(R.id.content, recipesListFragment);
        }
    }
}
