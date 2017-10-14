package com.icerrate.bakingapp.view.recipes.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.data.model.Recipe;
import com.icerrate.bakingapp.data.model.Step;
import com.icerrate.bakingapp.view.common.BaseFragment;
import com.icerrate.bakingapp.view.common.VerticalSpaceItemDecoration;
import com.icerrate.bakingapp.view.step.StepDetailActivity;
import com.icerrate.bakingapp.view.step.StepDetailFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ivan Cerrate.
 */

public class RecipeDetailFragment extends BaseFragment implements RecipeDetailView, StepsListAdapter.OnItemClickListener {

    public static String KEY_RECIPE_DETAIL = "RECIPE_DETAIL_KEY";

    @BindView(R.id.ingredients)
    public TextView ingredientsTextView;

    @BindView(R.id.steps)
    public RecyclerView stepsRecyclerView;

    @BindView(R.id.steps_no_data)
    public TextView stepsNoDataTextView;

    private StepsListAdapter adapter;

    private RecipeDetailPresenter presenter;

    public static RecipeDetailFragment newInstance(Recipe recipe) {
        Bundle bundle = new Bundle();
        if (recipe != null) {
            bundle.putParcelable(KEY_RECIPE_DETAIL, recipe);
        }
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RecipeDetailPresenter(this);
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
        setupView();
        if (savedInstanceState == null) {
            Recipe recipeDetail = getArguments().getParcelable(KEY_RECIPE_DETAIL);
            presenter.setRecipeDetail(recipeDetail);
        } else {
            restoreInstanceState(savedInstanceState);
        }
        presenter.loadRecipeDetail();
    }

    @Override
    protected void saveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_RECIPE_DETAIL, presenter.getRecipeDetail());
    }

    @Override
    protected void restoreInstanceState(Bundle savedState) {
        Recipe recipeDetail = savedState.getParcelable(KEY_RECIPE_DETAIL);
        presenter.loadPresenterState(recipeDetail);
    }

    private void setupView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new StepsListAdapter(this);
        stepsRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(8,4));
        stepsRecyclerView.setAdapter(adapter);
        stepsRecyclerView.setLayoutManager(linearLayoutManager);
        stepsRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onItemClick(View view) {
        Step step = (Step) view.getTag();
        if (step != null) {
            presenter.onStepItemClick(step);
        }
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
        startActivity(StepDetailActivity.makeIntent(getContext())
                .putExtra(StepDetailFragment.KEY_STEP_DETAIL, step));
    }
}
