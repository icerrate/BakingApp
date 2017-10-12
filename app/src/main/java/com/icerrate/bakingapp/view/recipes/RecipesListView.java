package com.icerrate.bakingapp.view.recipes;

import com.icerrate.bakingapp.data.model.Recipe;
import com.icerrate.bakingapp.view.common.BaseView;

import java.util.ArrayList;

/**
 * @author Ivan Cerrate.
 */

public interface RecipesListView extends BaseView {

    void showRecipes(ArrayList<Recipe> recipes);

    void showNoDataView(boolean show);

    void goToRecipeDetail(Recipe recipe);
}
