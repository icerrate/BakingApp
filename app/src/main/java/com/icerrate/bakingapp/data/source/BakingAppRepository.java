package com.icerrate.bakingapp.data.source;

import com.icerrate.bakingapp.data.model.Ingredient;
import com.icerrate.bakingapp.data.model.Recipe;
import com.icerrate.bakingapp.data.model.Step;
import com.icerrate.bakingapp.view.common.BaseCallback;

import java.util.ArrayList;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * @author Ivan Cerrate.
 */

public class BakingAppRepository implements BakingAppDataSource {

    private BakingAppDataSource localDataSource;

    private BakingAppDataSource remoteDataSource;

    private static BakingAppRepository INSTANCE = null;

    public BakingAppRepository(BakingAppDataSource localDataSource, BakingAppDataSource remoteDataSource) {
        this.localDataSource = checkNotNull(localDataSource);
        this.remoteDataSource = checkNotNull(remoteDataSource);
    }

    public static BakingAppRepository getInstance(BakingAppDataSource localDataSource,
                                                  BakingAppDataSource remoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new BakingAppRepository(localDataSource, remoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getRecipes(final BaseCallback<ArrayList<Recipe>> callback) {
        remoteDataSource.getRecipes(new BaseCallback<ArrayList<Recipe>>() {
            @Override
            public void onSuccess(ArrayList<Recipe> recipes) {
                localDataSource.prepareDataSource();
                localDataSource.saveRecipes(recipes);
                for (Recipe recipe: recipes) {
                    localDataSource.saveIngredients(recipe.getId(), recipe.getIngredients());
                    localDataSource.saveSteps(recipe.getId(), recipe.getSteps());
                }
                callback.onSuccess(recipes);
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }

    @Override
    public void getRecipeDetail(Integer recipeId, BaseCallback<Recipe> callback) {
        localDataSource.getRecipeDetail(recipeId, callback);
    }

    @Override
    public void getIngredients(Integer recipeId, BaseCallback<ArrayList<Ingredient>> callback) {
        localDataSource.getIngredients(recipeId, callback);
    }

    @Override
    public void getSteps(Integer recipeId, BaseCallback<ArrayList<Step>> callback) {
        localDataSource.getSteps(recipeId, callback);
    }

    @Override
    public void getStepDetail(Integer recipeId, Integer stepId, BaseCallback<Step> callback) {
        localDataSource.getStepDetail(recipeId, stepId, callback);
    }

    @Override
    public void prepareDataSource() {
        throw new UnsupportedOperationException("Operation is not available!");
    }

    @Override
    public void saveRecipes(ArrayList<Recipe> recipesList) {
        throw new UnsupportedOperationException("Operation is not available!");
    }

    @Override
    public void saveIngredients(Integer recipeId, ArrayList<Ingredient> ingredientsList) {
        throw new UnsupportedOperationException("Operation is not available!");
    }

    @Override
    public void saveSteps(Integer recipeId, ArrayList<Step> stepsList) {
        throw new UnsupportedOperationException("Operation is not available!");
    }
}
