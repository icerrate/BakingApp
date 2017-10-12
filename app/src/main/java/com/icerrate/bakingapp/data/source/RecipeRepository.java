package com.icerrate.bakingapp.data.source;

import com.icerrate.bakingapp.data.model.Recipe;
import com.icerrate.bakingapp.view.common.BaseCallback;

import java.util.ArrayList;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * @author Ivan Cerrate.
 */

public class RecipeRepository implements RecipeDataSource {

    private RecipeDataSource remoteDataSource;

    private static RecipeRepository INSTANCE = null;

    public RecipeRepository(RecipeDataSource remoteDataSource) {
        this.remoteDataSource = checkNotNull(remoteDataSource);
    }

    public static RecipeRepository getInstance(RecipeDataSource remoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RecipeRepository(remoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getRecipes(BaseCallback<ArrayList<Recipe>> callback) {
        remoteDataSource.getRecipes(callback);
    }
}
