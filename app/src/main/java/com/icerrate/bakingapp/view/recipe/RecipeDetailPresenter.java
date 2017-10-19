package com.icerrate.bakingapp.view.recipe;

import com.icerrate.bakingapp.data.model.Ingredient;
import com.icerrate.bakingapp.data.model.Recipe;
import com.icerrate.bakingapp.data.model.Step;
import com.icerrate.bakingapp.data.source.BakingAppRepository;
import com.icerrate.bakingapp.view.common.BaseCallback;
import com.icerrate.bakingapp.view.common.BasePresenter;

import java.util.ArrayList;

/**
 * @author Ivan Cerrate.
 */

public class RecipeDetailPresenter extends BasePresenter<RecipeDetailView> {

    private int selectedStep = 0;

    private Integer recipeId;

    private Recipe recipeDetail;

    private ArrayList<Ingredient> ingredientsList;

    private ArrayList<Step> stepsList;

    private BakingAppRepository recipeRepository;

    public RecipeDetailPresenter(RecipeDetailView view, BakingAppRepository recipeRepository) {
        super(view);
        this.recipeRepository = recipeRepository;
    }

    public void loadViewContent() {
        if (recipeId != null) {
            loadRecipeDetail();
            loadIngredients();
            loadSteps();
        }
    }

    /* Recipe */

    public void loadRecipeDetail() {
        if (recipeDetail == null) {
            getInternalRecipeDetail(recipeId);
        } else {
            showRecipeDetail(recipeDetail);
        }
    }

    private void getInternalRecipeDetail(Integer recipeId) {
        view.showProgressBar(true);
        recipeRepository.getRecipeDetail(recipeId, new BaseCallback<Recipe>() {
            @Override
            public void onSuccess(Recipe response) {
                recipeDetail = response;
                showRecipeDetail(response);
                view.showProgressBar(false);
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
                view.showProgressBar(false);
            }
        });
    }

    private void showRecipeDetail(Recipe recipeDetail) {
        String name = recipeDetail.getName();
        view.showRecipeName(name);
    }


    /* Ingredients */

    public void loadIngredients() {
        if (ingredientsList == null) {
            getInternalIngredientsList(recipeId);
        } else {
            showIngredients(ingredientsList);
        }
    }

    private void getInternalIngredientsList(Integer recipeId) {
        view.showProgressBar(true);
        recipeRepository.getIngredients(recipeId, new BaseCallback<ArrayList<Ingredient>>() {
            @Override
            public void onSuccess(ArrayList<Ingredient> response) {
                ingredientsList = response;
                showIngredients(response);
                view.showProgressBar(false);
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
                view.showProgressBar(false);
            }
        });
    }

    public void showIngredients(ArrayList<Ingredient> ingredients) {
        if (ingredients != null && !ingredients.isEmpty()) {
            String stringIngredients = "";
            for (int i=0; i<ingredients.size(); i++) {
                Ingredient ingredient = ingredients.get(i);
                String line = ingredient.getQuantity() + " " + ingredient.getMeasure() + " " + ingredient.getIngredient();
                if (i < ingredients.size()-1) {
                    line = line + "\n";
                }
                stringIngredients = stringIngredients + line;
            }
            view.showIngredients(stringIngredients);
        }
    }

    /* Steps */

    public void loadSteps() {
        if (stepsList == null) {
            stepsList = new ArrayList<>();
            getInternalStepsList(recipeId);
        } else {
            showSteps(stepsList);
        }
    }

    private void getInternalStepsList(Integer recipeId) {
        view.showProgressBar(true);
        recipeRepository.getSteps(recipeId, new BaseCallback<ArrayList<Step>>() {
            @Override
            public void onSuccess(ArrayList<Step> response) {
                stepsList = response;
                showSteps(response);
                view.showProgressBar(false);
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
                view.showProgressBar(false);
            }
        });
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

    public void onStepItemClick(int selectedStep, Step step) {
        this.selectedStep = selectedStep;
        view.goToStepDetail(step);
    }

    /* Presenter State */

    public int getSelectedStep() {
        return selectedStep;
    }

    public void setSelectedStep(int selectedStep) {
        this.selectedStep = selectedStep;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public ArrayList<Ingredient> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(ArrayList<Ingredient> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public ArrayList<Step> getStepsList() {
        return stepsList;
    }

    public void setStepsList(ArrayList<Step> stepsList) {
        this.stepsList = stepsList;
    }

    public void loadPresenterState(Integer recipeId, ArrayList<Ingredient> ingredientsList,
                                   ArrayList<Step> stepsList, int selectedStep) {
        this.recipeId = recipeId;
        this.ingredientsList = ingredientsList;
        this.stepsList = stepsList;
        this.selectedStep = selectedStep;
    }
}
