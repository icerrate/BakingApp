package com.icerrate.bakingapp.view.recipes;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.data.model.Recipe;
import com.icerrate.bakingapp.data.source.RecipeRepository;
import com.icerrate.bakingapp.view.common.BaseCallback;
import com.icerrate.bakingapp.view.common.BasePresenter;

import java.util.ArrayList;

/**
 * @author Ivan Cerrate.
 */

public class RecipesListPresenter extends BasePresenter<RecipesListView> {

    private ArrayList<Recipe> recipesList;

    private RecipeRepository recipeRepository;

    public RecipesListPresenter(RecipesListView view, RecipeRepository recipeRepository) {
        super(view);
        this.recipeRepository = recipeRepository;
    }

    public void onRefreshView() {
        recipesList = null;
        loadRecipes();
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
        recipeRepository.getRecipes(new BaseCallback<ArrayList<Recipe>>() {
            @Override
            public void onSuccess(ArrayList<Recipe> response) {
                recipesList = response;
                showRecipes(response, getStringRes(R.string.recipes_no_data));
                finishLoading();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
                if (recipesList.isEmpty()) {
                    view.showNoDataView(true);
                }
                finishLoading();
            }
        });
    }

    public void showRecipes(ArrayList<Recipe> recipes, String noDataText) {
        if (recipes != null && !recipes.isEmpty()) {
            view.showNoDataView(false);
            view.showRecipes(recipes);
        } else {
            if (recipesList.isEmpty()) {
                view.showNoDataView(true);
            }
        }
    }

    private void finishLoading() {
        view.showProgressBar(false);
        view.showRefreshLayout(false);
    }

    public void onRecipeItemClick(final Recipe recipe) {
        view.goToRecipeDetail(recipe);
    }

    public ArrayList<Recipe> getRecipes() {
        return recipesList;
    }

    public void loadPresenterState(ArrayList<Recipe> recipesList) {
        this.recipesList = recipesList;
    }
}
