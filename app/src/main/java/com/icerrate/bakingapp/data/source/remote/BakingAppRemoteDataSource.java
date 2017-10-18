package com.icerrate.bakingapp.data.source.remote;

import com.icerrate.bakingapp.data.model.Ingredient;
import com.icerrate.bakingapp.data.model.Recipe;
import com.icerrate.bakingapp.data.model.Step;
import com.icerrate.bakingapp.data.source.BakingAppDataSource;
import com.icerrate.bakingapp.provider.cloud.BaseService;
import com.icerrate.bakingapp.provider.cloud.ServerServiceRequest;
import com.icerrate.bakingapp.provider.cloud.ServiceRequest;
import com.icerrate.bakingapp.provider.cloud.api.RecipeAPI;
import com.icerrate.bakingapp.view.common.BaseCallback;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * @author Ivan Cerrate.
 */

public class BakingAppRemoteDataSource extends BaseService implements BakingAppDataSource {

    private RecipeAPI recipeAPI;

    public BakingAppRemoteDataSource(RecipeAPI recipeAPI) {
        this.recipeAPI = recipeAPI;
    }

    @Override
    public void getRecipes(BaseCallback<ArrayList<Recipe>> callback) {
        final Call<ArrayList<Recipe>> call = recipeAPI.getRecipesList();
        ServiceRequest<ArrayList<Recipe>> serviceRequest = new ServerServiceRequest<>(call);
        enqueue(serviceRequest, callback);
    }

    @Override
    public void getRecipeDetail(Integer recipeId, BaseCallback<Recipe> callback) {
        throw new UnsupportedOperationException("Operation is not available!");
    }

    @Override
    public void getIngredients(Integer recipeId, BaseCallback<ArrayList<Ingredient>> callback) {
        throw new UnsupportedOperationException("Operation is not available!");
    }

    @Override
    public void getSteps(Integer recipeId, BaseCallback<ArrayList<Step>> callback) {
        throw new UnsupportedOperationException("Operation is not available!");
    }

    @Override
    public void getStepDetail(Integer recipeId, Integer stepId, BaseCallback<Step> callback) {
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

    @Override
    public void prepareDataSource() {
        throw new UnsupportedOperationException("Operation is not available!");
    }
}
