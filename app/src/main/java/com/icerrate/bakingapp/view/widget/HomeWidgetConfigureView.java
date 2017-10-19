package com.icerrate.bakingapp.view.widget;

import com.icerrate.bakingapp.data.model.Recipe;
import com.icerrate.bakingapp.view.common.BaseView;

import java.util.ArrayList;

/**
 * @author Ivan Cerrate.
 */

public interface HomeWidgetConfigureView extends BaseView {

    void showRecipes(ArrayList<Recipe> recipes);

    void showNoDataView(boolean show);

    void onConfigurationCompleted(Integer widgetId);
}
