package com.icerrate.bakingapp.view.recipes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.data.model.Recipe;
import com.icerrate.bakingapp.utils.InjectionUtils;
import com.icerrate.bakingapp.view.common.BaseFragment;
import com.icerrate.bakingapp.view.common.VerticalSpaceItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ivan Cerrate.
 */

public class RecipesListFragment extends BaseFragment implements RecipesListView, RecipesListAdapter.OnItemClickListener {

    public static String KEY_RECIPES = "RECIPES_KEY";

    @BindView(R.id.recipes)
    public RecyclerView movirecipesRecyclerView;

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
            presenter.loadRecipes();
        } else {
            presenter.onRefreshView();
        }
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new RecipesListAdapter(this);
        movirecipesRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(8,8));
        movirecipesRecyclerView.setAdapter(adapter);
        movirecipesRecyclerView.setLayoutManager(linearLayoutManager);
        //refresh
        if (refreshLayout != null) {
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    presenter.onRefreshView();
                }
            });
            refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
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
        //TODO
        /*startActivity(RecipeeDetailActivity.makeIntent(getActivity())
                .putExtra(KEY_RECIPE, recipe);*/
    }
}
