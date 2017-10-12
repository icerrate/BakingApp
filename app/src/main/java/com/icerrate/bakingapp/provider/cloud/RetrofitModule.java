package com.icerrate.bakingapp.provider.cloud;

import com.icerrate.bakingapp.BuildConfig;
import com.icerrate.bakingapp.provider.cloud.api.RecipeAPI;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Ivan Cerrate.
 */

public class RetrofitModule {

    private static RetrofitModule instance = null;

    protected RetrofitModule() {
        // Exists only to defeat instantiation.
    }

    public static RetrofitModule get() {
        if(instance == null) {
            instance = new RetrofitModule();
        }
        return instance;
    }

    public OkHttpClient providesOkHttpClient() {
        return new OkHttpClient();
    }


    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        OkHttpClient.Builder builder = okHttpClient.newBuilder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .baseUrl(BuildConfig.API_BASE_URL)
                .build();
    }

    public RecipeAPI provideRecipeAPI() {
        return get().provideRetrofit(get().providesOkHttpClient()).create(RecipeAPI.class);
    }
}
