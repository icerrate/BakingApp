package com.icerrate.bakingapp.utils;

import com.icerrate.bakingapp.data.source.RecipeRepository;
import com.icerrate.bakingapp.data.source.remote.RecipeRemoteDataSource;
import com.icerrate.bakingapp.provider.cloud.RetrofitModule;

/**
 * @author Ivan Cerrate.
 */

public class InjectionUtils {

    public static RecipeRepository recipeRepository() {
        return RecipeRepository.getInstance(
                new RecipeRemoteDataSource(RetrofitModule.get().provideRecipeAPI())
        );
    }
}
