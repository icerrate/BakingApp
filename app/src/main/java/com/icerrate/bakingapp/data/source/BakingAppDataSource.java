package com.icerrate.bakingapp.data.source;

import com.icerrate.bakingapp.data.model.Ingredient;
import com.icerrate.bakingapp.data.model.Recipe;
import com.icerrate.bakingapp.data.model.Step;
import com.icerrate.bakingapp.view.common.BaseCallback;

import java.util.ArrayList;

/**
 * @author Ivan Cerrate.
 */

public interface BakingAppDataSource {

    void getRecipes(BaseCallback<ArrayList<Recipe>> callback);

    void getRecipeDetail(Integer recipeId, BaseCallback<Recipe> callback);

    void getIngredients(Integer recipeId, BaseCallback<ArrayList<Ingredient>> callback);

    void getSteps(Integer recipeId, BaseCallback<ArrayList<Step>> callback);

    void getStepDetail(Integer recipeId, Integer stepId, BaseCallback<Step> callback);

    void saveRecipes(ArrayList<Recipe> recipesList);

    void saveIngredients(Integer recipeId, ArrayList<Ingredient> ingredientsList);

    void saveSteps(Integer recipeId, ArrayList<Step> stepsList);

    void prepareDataSource();
}
