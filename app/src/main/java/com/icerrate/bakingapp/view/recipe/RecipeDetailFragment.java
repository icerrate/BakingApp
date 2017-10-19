package com.icerrate.bakingapp.view.recipe;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.data.model.Ingredient;
import com.icerrate.bakingapp.data.model.Step;
import com.icerrate.bakingapp.utils.InjectionUtils;
import com.icerrate.bakingapp.view.common.BaseFragment;
import com.icerrate.bakingapp.view.common.VerticalSpaceItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.icerrate.bakingapp.R.id.ingredients;
import static com.icerrate.bakingapp.R.id.steps;
import static com.icerrate.bakingapp.view.recipe.RecipeDetailActivity.KEY_RECIPE_ID;
import static com.icerrate.bakingapp.view.recipe.RecipeDetailActivity.KEY_SELECTED_STEP;

/**
 * @author Ivan Cerrate.
 */

public class RecipeDetailFragment extends BaseFragment implements RecipeDetailView, StepListAdapter.OnItemClickListener {

    public static String KEY_INGREDIENTS = "INGREDIENTS_KEY";

    public static String KEY_STEPS = "STEPS_KEY";

    @BindView(ingredients)
    public TextView ingredientsTextView;

    @BindView(steps)
    public RecyclerView stepsRecyclerView;

    @BindView(R.id.steps_no_data)
    public TextView stepsNoDataTextView;

    private StepListAdapter adapter;

    private RecipeDetailPresenter presenter;

    private RecipeDetailActivityListener recipeDetailActivityListener;

    public static RecipeDetailFragment newInstance(Integer recipeId, Integer selectedStep) {
        Bundle bundle = new Bundle();
        if (recipeId != null) {
            bundle.putInt(KEY_RECIPE_ID, recipeId);
        }
        if (recipeId != null) {
            bundle.putInt(KEY_SELECTED_STEP, selectedStep);
        }
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RecipeDetailPresenter(this,
                InjectionUtils.bakingRepository(getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            Integer recipeId = getArguments().getInt(KEY_RECIPE_ID);
            Integer selectedStep = getArguments().getInt(KEY_SELECTED_STEP);
            presenter.setRecipeId(recipeId);
            presenter.setSelectedStep(selectedStep);
        } else {
            restoreInstanceState(savedInstanceState);
        }
        setupView();
        presenter.loadViewContent();
    }

    @Override
    protected void saveInstanceState(Bundle outState) {
        outState.putInt(KEY_RECIPE_ID, presenter.getRecipeId());
        outState.putParcelableArrayList(KEY_INGREDIENTS, presenter.getIngredientsList());
        outState.putParcelableArrayList(KEY_STEPS, presenter.getStepsList());
        outState.putInt(KEY_SELECTED_STEP, presenter.getSelectedStep());
    }

    @Override
    protected void restoreInstanceState(Bundle savedState) {
        Integer recipeId = savedState.getInt(KEY_RECIPE_ID);
        ArrayList<Ingredient> ingredientsList = savedState.getParcelableArrayList(KEY_INGREDIENTS);
        ArrayList<Step> stepsList = savedState.getParcelableArrayList(KEY_STEPS);
        Integer selectedStep = savedState.getInt(KEY_SELECTED_STEP);
        presenter.loadPresenterState(recipeId, ingredientsList, stepsList, selectedStep);
    }

    private void setupView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new StepListAdapter(this);
        if(!isPhone){
            adapter.setSelectedStep(presenter.getSelectedStep());
        }
        stepsRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(8,4));
        stepsRecyclerView.setAdapter(adapter);
        stepsRecyclerView.setLayoutManager(linearLayoutManager);
        stepsRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onItemClick(int index, View view) {
        Step step = (Step) view.getTag();
        if (step != null) {
            presenter.onStepItemClick(index, step);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        castOrThrowException(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        castOrThrowException(context);
    }

    private void castOrThrowException(Context context) {
        try {
            recipeDetailActivityListener = (RecipeDetailActivityListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement RecipeDetailActivityListener");
        }
    }

    @Override
    public void showRecipeName(String recipeName) {
        fragmentListener.setTitle(recipeName);
    }

    @Override
    public void showIngredients(String ingredients) {
        ingredientsTextView.setText(ingredients);
    }

    @Override
    public void showSteps(ArrayList<Step> steps) {
        adapter.addItems(steps);
    }

    @Override
    public void showStepsNoDataView(boolean show) {
        stepsNoDataTextView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void goToStepDetail(Step step) {
        recipeDetailActivityListener.onRecipeStepSelected(step);
    }
}
