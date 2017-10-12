package com.icerrate.bakingapp.view;

import android.os.Bundle;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.view.common.BaseActivity;
import com.icerrate.bakingapp.view.recipes.RecipesListFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setTitle(getString(R.string.title_activity_recipes));
        setNavigationToolbar(false);
        if (savedInstanceState == null) {
            RecipesListFragment recipesListFragment = RecipesListFragment.newInstance();
            replaceFragment(R.id.content, recipesListFragment);
        }
    }
}
