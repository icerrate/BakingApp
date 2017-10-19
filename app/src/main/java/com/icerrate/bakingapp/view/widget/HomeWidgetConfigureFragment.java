package com.icerrate.bakingapp.view.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
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
import com.icerrate.bakingapp.provider.widget.HomeWidgetProvider;
import com.icerrate.bakingapp.service.HomeWidgetService;
import com.icerrate.bakingapp.utils.InjectionUtils;
import com.icerrate.bakingapp.view.common.BaseFragment;
import com.icerrate.bakingapp.view.common.SimpleDividerItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * @author Ivan Cerrate.
 */

public class HomeWidgetConfigureFragment extends BaseFragment implements HomeWidgetConfigureView, SimpleRecipesListAdapter.OnItemClickListener {

    public static String KEY_RECIPES = "RECIPES_KEY";

    @BindView(R.id.recipes)
    public RecyclerView recipesRecyclerView;

    @BindView(R.id.recipes_no_data)
    public TextView noDataTextView;

    private SimpleRecipesListAdapter adapter;

    private HomeWidgetConfigurePresenter presenter;

    public static HomeWidgetConfigureFragment newInstance(Integer widgetId) {
        Bundle bundle = new Bundle();
        if (widgetId != null) {
            bundle.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        }
        HomeWidgetConfigureFragment fragment = new HomeWidgetConfigureFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new HomeWidgetConfigurePresenter(this,
                InjectionUtils.bakingRepository(getContext()),
                InjectionUtils.bakingAppPreferences(getContext()));
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
        if (savedInstanceState == null) {
            Integer widgetId = getArguments().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            presenter.setWidgetId(widgetId);
        } else {
            restoreInstanceState(savedInstanceState);
        }
        presenter.loadRecipes();
    }

    @Override
    protected void saveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_RECIPES, presenter.getRecipes());
        outState.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, presenter.getWidgetId());
    }

    @Override
    protected void restoreInstanceState(Bundle savedState) {
        Integer widgetId = savedState.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
        ArrayList<Recipe> recipesList = savedState.getParcelableArrayList(KEY_RECIPES);
        presenter.loadPresenterState(widgetId, recipesList);
    }

    private void setupView() {
        adapter = new SimpleRecipesListAdapter(this);
        recipesRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources().getDrawable(R.drawable.line_divider)));
        recipesRecyclerView.setAdapter(adapter);
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
    public void onConfigurationCompleted(Integer widgetId) {
        Intent resultValue = new Intent(HomeWidgetService.UPDATE_WIDGET, null, getActivity(), HomeWidgetProvider.class);
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        getActivity().sendBroadcast(resultValue);
        fragmentListener.setActivityResult(RESULT_OK, resultValue);
        fragmentListener.closeActivity();
    }
}
