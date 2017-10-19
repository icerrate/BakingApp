package com.icerrate.bakingapp.provider.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.icerrate.bakingapp.BuildConfig;

/**
 * @author Ivan Cerrate.
 */

public class BakingAppPreferences {

    private static final String CHARS_DIVIDERS = "//";

    private static final String WIDGET_ID_PREFIX = "_widget_id_";

    private SharedPreferences sharedPreferences;

    public BakingAppPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID + ".shared_preferences", Context.MODE_PRIVATE);
    }

    public void saveWidgetIdRecipe(int widgetId, int recipeId, String recipeName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String key = WIDGET_ID_PREFIX + widgetId;
        String value = recipeId + CHARS_DIVIDERS + recipeName;
        editor.putString(key, value);
        editor.apply();
    }

    public Integer getWidgetIdRecipeId(int widgetId) {
        String key = WIDGET_ID_PREFIX + widgetId;
        String recipeCode = sharedPreferences.getString(key, null);
        String[] parts = recipeCode.split(CHARS_DIVIDERS);
        return Integer.valueOf(parts[0]);
    }

    public String getWidgetIdRecipeName(int widgetId) {
        String key = WIDGET_ID_PREFIX + widgetId;
        String recipeCode = sharedPreferences.getString(key, null);
        if (recipeCode == null) {
            return null;
        } else {
            String[] parts = recipeCode.split(CHARS_DIVIDERS);
            return parts[1];
        }
    }
}
