package com.icerrate.bakingapp.view.recipes;

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

    public void loadRecipes() {
        if (recipesList == null) {
            recipesList = new ArrayList<>();
            getInternalRecipesList();
        } else {
            view.showRecipes(recipesList);
        }
    }

    private void getInternalRecipesList() {
        view.showProgressBar(false);
        recipeRepository.getRecipes(new BaseCallback<ArrayList<Recipe>>() {
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
        view.goToRecipeDetail(recipe);
    }

    public ArrayList<Recipe> getRecipes() {
        return recipesList;
    }

    public void loadPresenterState(ArrayList<Recipe> recipesList) {
        this.recipesList = recipesList;
    }
}
