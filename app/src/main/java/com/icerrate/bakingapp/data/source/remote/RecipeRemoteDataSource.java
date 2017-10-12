package com.icerrate.bakingapp.data.source.remote;

import com.icerrate.bakingapp.data.model.Recipe;
import com.icerrate.bakingapp.data.source.RecipeDataSource;
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

public class RecipeRemoteDataSource extends BaseService implements RecipeDataSource {

    private RecipeAPI recipeAPI;

    public RecipeRemoteDataSource(RecipeAPI recipeAPI) {
        this.recipeAPI = recipeAPI;
    }

    @Override
    public void getRecipes(BaseCallback<ArrayList<Recipe>> callback) {
        final Call<ArrayList<Recipe>> call = recipeAPI.getRecipesList();
        ServiceRequest<ArrayList<Recipe>> serviceRequest = new ServerServiceRequest<>(call);
        enqueue(serviceRequest, callback);
    }
}
