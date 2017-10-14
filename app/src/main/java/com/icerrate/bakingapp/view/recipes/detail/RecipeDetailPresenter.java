package com.icerrate.bakingapp.view.recipes.detail;

import com.icerrate.bakingapp.data.model.Ingredient;
import com.icerrate.bakingapp.data.model.Recipe;
import com.icerrate.bakingapp.data.model.Step;
import com.icerrate.bakingapp.data.source.RecipeRepository;
import com.icerrate.bakingapp.view.common.BasePresenter;

import java.util.ArrayList;

/**
 * @author Ivan Cerrate.
 */

public class RecipeDetailPresenter extends BasePresenter<RecipeDetailView> {

    private Recipe recipeDetail;

    private RecipeRepository recipeRepository;

    public RecipeDetailPresenter(RecipeDetailView view, RecipeRepository recipeRepository) {
        super(view);
        this.recipeRepository = recipeRepository;
    }

    public void loadRecipeDetail() {
        if (recipeDetail != null) {
            showIngredients(recipeDetail.getIngredients());
            showSteps(recipeDetail.getSteps());
        }
    }

    public void showIngredients(ArrayList<Ingredient> ingredients) {
        if (ingredients != null && !ingredients.isEmpty()) {
            String stringIngredients = "";
            for (int i=0; i<ingredients.size(); i++) {
                Ingredient ingredient = ingredients.get(i);
                String line = "-" + ingredient.getQuantity() + " " + ingredient.getMeasure() + " " + ingredient.getIngredient();
                if (i < ingredients.size()-1) {
                    line = line + "\n";
                }
                stringIngredients = stringIngredients + line;
            }
            view.showIngredients(stringIngredients);
        }
    }

    public void showSteps(ArrayList<Step> steps) {
        if (steps != null && !steps.isEmpty()) {
            view.showStepsNoDataView(false);
            view.showSteps(steps);
        } else {
            if (steps == null || steps.isEmpty()) {
                view.showStepsNoDataView(true);
            }
        }
    }

    public void onStepItemClick(final Step step) {
        view.goToStepDetail(step);
    }

    public void setRecipeDetail(Recipe recipeDetail) {
        this.recipeDetail = recipeDetail;
    }

    public Recipe getRecipeDetail() {
        return recipeDetail;
    }

    public void loadPresenterState(Recipe recipeDetail) {
        this.recipeDetail = recipeDetail;
    }
}
