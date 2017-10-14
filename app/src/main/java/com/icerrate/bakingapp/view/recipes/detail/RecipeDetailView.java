package com.icerrate.bakingapp.view.recipes.detail;

import com.icerrate.bakingapp.data.model.Step;
import com.icerrate.bakingapp.view.common.BaseView;

import java.util.ArrayList;

/**
 * @author Ivan Cerrate.
 */

public interface RecipeDetailView extends BaseView {

    void showIngredients(String ingredients);

    void showSteps(ArrayList<Step> steps);

    void showStepsNoDataView(boolean show);

    void goToStepDetail(Step step);
}
