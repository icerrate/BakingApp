package com.icerrate.bakingapp.utils;

import android.content.Context;

import com.icerrate.bakingapp.data.source.BakingAppRepository;
import com.icerrate.bakingapp.data.source.local.BakingAppLocalDataSource;
import com.icerrate.bakingapp.data.source.remote.BakingAppRemoteDataSource;
import com.icerrate.bakingapp.provider.cloud.RetrofitModule;
import com.icerrate.bakingapp.provider.preference.BakingAppPreferences;

/**
 * @author Ivan Cerrate.
 */

public class InjectionUtils {

    public static BakingAppPreferences bakingAppPreferences(Context context) {
        return new BakingAppPreferences(context);
    }

    public static BakingAppRepository bakingRepository(Context context) {
        return BakingAppRepository.getInstance(
                new BakingAppLocalDataSource(context),
                new BakingAppRemoteDataSource(RetrofitModule.get().provideRecipeAPI())
        );
    }
}
