package com.icerrate.bakingapp.view.widget;

import com.icerrate.bakingapp.data.model.Recipe;
import com.icerrate.bakingapp.data.source.BakingAppRepository;
import com.icerrate.bakingapp.provider.preference.BakingAppPreferences;
import com.icerrate.bakingapp.view.common.BaseCallback;
import com.icerrate.bakingapp.view.common.BasePresenter;

import java.util.ArrayList;

/**
 * @author Ivan Cerrate.
 */

public class HomeWidgetConfigurePresenter extends BasePresenter<HomeWidgetConfigureView> {

    private Integer widgetId;

    private ArrayList<Recipe> recipesList;

    private BakingAppRepository bakingAppRepository;

    private BakingAppPreferences bakingAppPreferences;

    public HomeWidgetConfigurePresenter(HomeWidgetConfigureView view,
                                        BakingAppRepository bakingAppRepository,
                                        BakingAppPreferences bakingAppPreferences) {
        super(view);
        this.bakingAppRepository = bakingAppRepository;
        this.bakingAppPreferences = bakingAppPreferences;
    }

    public void loadRecipes() {
        if (recipesList == null) {
            recipesList = new ArrayList<>();
            getInternalRecipesList();
        } else {
            view.showRecipes(recipesList);
        }
    }

    private void getInternalRecipesList() {
        view.showProgressBar(true);
        bakingAppRepository.getRecipes(new BaseCallback<ArrayList<Recipe>>() {
            @Override
            public void onSuccess(ArrayList<Recipe> response) {
                recipesList = response;
                showRecipes(response);
                view.showProgressBar(false);
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
                if (recipesList.isEmpty()) {
                    view.showNoDataView(true);
                }
                view.showProgressBar(false);
            }
        });
    }

    public void showRecipes(ArrayList<Recipe> recipes) {
        if (recipes != null && !recipes.isEmpty()) {
            view.showNoDataView(false);
            view.showRecipes(recipes);
        } else {
            if (recipesList.isEmpty()) {
                view.showNoDataView(true);
            }
        }
    }

    public void onRecipeItemClick(final Recipe recipe) {
        bakingAppPreferences.saveWidgetIdRecipe(widgetId, recipe.getId(), recipe.getName());
        view.onConfigurationCompleted(widgetId);
    }

    public void setWidgetId(Integer widgetId) {
        this.widgetId = widgetId;
    }

    public Integer getWidgetId() {
        return widgetId;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipesList;
    }

    public void loadPresenterState(Integer widgetId, ArrayList<Recipe> recipesList) {
        this.widgetId = widgetId;
        this.recipesList = recipesList;
    }
}
