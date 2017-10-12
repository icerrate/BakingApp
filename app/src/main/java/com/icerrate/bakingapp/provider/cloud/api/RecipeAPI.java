package com.icerrate.bakingapp.provider.cloud.api;

import com.icerrate.bakingapp.data.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author Ivan Cerrate.
 */

public interface RecipeAPI {

    @GET(ServerPaths.RECIPES_LIST)
    Call<ArrayList<Recipe>> getRecipesList();
}
