package com.icerrate.bakingapp.view.recipes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.data.model.Recipe;
import com.icerrate.bakingapp.utils.InjectionUtils;
import com.icerrate.bakingapp.view.common.BaseFragment;
import com.icerrate.bakingapp.view.common.VerticalSpaceItemDecoration;
import com.icerrate.bakingapp.view.recipe.RecipeDetailActivity;
import com.icerrate.bakingapp.view.recipe.RecipeDetailFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ivan Cerrate.
 */

public class RecipesListFragment extends BaseFragment implements RecipesListView, RecipesListAdapter.OnItemClickListener {

    public static String KEY_RECIPES = "RECIPES_KEY";

    @BindView(R.id.recipes)
    public RecyclerView recipesRecyclerView;

    @BindView(R.id.recipes_no_data)
    public TextView noDataTextView;

    private RecipesListAdapter adapter;

    private RecipesListPresenter presenter;

    public static RecipesListFragment newInstance() {
        return new RecipesListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RecipesListPresenter(this,
                InjectionUtils.recipeRepository());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupView();
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }
        presenter.loadRecipes();
    }

    @Override
    protected void saveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_RECIPES, presenter.getRecipes());
    }

    @Override
    protected void restoreInstanceState(Bundle savedState) {
        ArrayList<Recipe> recipesList = savedState.getParcelableArrayList(KEY_RECIPES);
        presenter.loadPresenterState(recipesList);
    }

    private void setupView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), getColumns());
        adapter = new RecipesListAdapter(this);
        recipesRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(8,8));
        recipesRecyclerView.setAdapter(adapter);
        recipesRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private int getColumns() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        if (display.getRotation() == Surface.ROTATION_0) {
            return 1;
        } else {
            return 3;
        }
    }

    @Override
    public void onItemClick(View view) {
        Recipe recipe = (Recipe) view.getTag();
        if (recipe != null) {
            presenter.onRecipeItemClick(recipe);
        }
    }

    @Override
    public void showRecipes(ArrayList<Recipe> recipes) {
        adapter.addItems(recipes);
    }

    @Override
    public void showNoDataView(boolean show) {
        noDataTextView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void goToRecipeDetail(Recipe recipe) {
        startActivity(RecipeDetailActivity.makeIntent(getContext())
                .putExtra(RecipeDetailFragment.KEY_RECIPE_DETAIL, recipe));
    }
}
