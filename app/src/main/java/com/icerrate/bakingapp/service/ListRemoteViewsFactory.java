package com.icerrate.bakingapp.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.data.model.Ingredient;
import com.icerrate.bakingapp.data.source.BakingAppRepository;
import com.icerrate.bakingapp.provider.preference.BakingAppPreferences;
import com.icerrate.bakingapp.utils.InjectionUtils;
import com.icerrate.bakingapp.view.common.BaseCallback;

import java.util.ArrayList;

/**
 * @author Ivan Cerrate.
 */

public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    public final int ITEM_TYPE_COUNT = 1;

    private Context context = null;

    private int appWidgetId;

    private ArrayList<Ingredient> ingredientsList;

    public ListRemoteViewsFactory(Context context, Intent intent){
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        getIngredients();
    }

    @Override
    public void onDataSetChanged() {
        getIngredients();
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_TYPE_COUNT;
    }

    @Override
    public void onDestroy() {
        //Needs to be empty
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        return ingredientsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_home_item);
        Ingredient ingredient = ingredientsList.get(position);
        String line = "- " + ingredient.getQuantity() + " " + ingredient.getMeasure() + " " + ingredient.getIngredient();
        remoteViews.setTextViewText(R.id.ingredient_name, line);
        return remoteViews;
    }

    public void getIngredients() {
        ingredientsList = new ArrayList<>();
        BakingAppPreferences bakingAppPreferences = InjectionUtils.bakingAppPreferences(context);
        int recipeId = bakingAppPreferences.getWidgetIdRecipeId(appWidgetId);
        BakingAppRepository recipeRepository = InjectionUtils.bakingRepository(context);
        recipeRepository.getIngredients(recipeId, new BaseCallback<ArrayList<Ingredient>>() {
            @Override
            public void onSuccess(ArrayList<Ingredient> response) {
                ingredientsList = response;
            }

            @Override
            public void onFailure(String errorMessage) {
            }
        });
    }
}