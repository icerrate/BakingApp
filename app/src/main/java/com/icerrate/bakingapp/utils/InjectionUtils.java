package com.icerrate.bakingapp.utils;

import android.content.Context;

import com.icerrate.bakingapp.data.source.BakingAppRepository;
import com.icerrate.bakingapp.data.source.local.BakinAppLocalDataSource;
import com.icerrate.bakingapp.data.source.remote.BakingAppRemoteDataSource;
import com.icerrate.bakingapp.provider.cloud.RetrofitModule;

/**
 * @author Ivan Cerrate.
 */

public class InjectionUtils {

    public static BakingAppRepository recipeRepository(Context context) {
        return BakingAppRepository.getInstance(
                new BakinAppLocalDataSource(context),
                new BakingAppRemoteDataSource(RetrofitModule.get().provideRecipeAPI())
        );
    }
}
