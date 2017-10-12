package com.icerrate.bakingapp.data.source;

import com.icerrate.bakingapp.data.model.Recipe;
import com.icerrate.bakingapp.view.common.BaseCallback;

import java.util.ArrayList;

/**
 * @author Ivan Cerrate.
 */

public interface RecipeDataSource {

    void getRecipes(BaseCallback<ArrayList<Recipe>> callback);
}
