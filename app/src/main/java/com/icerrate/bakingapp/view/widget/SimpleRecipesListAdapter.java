package com.icerrate.bakingapp.view.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.data.model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ivan Cerrate.
 */

public class SimpleRecipesListAdapter extends RecyclerView.Adapter<SimpleRecipesListAdapter.SimpleViewHolder> {

    private ArrayList<Recipe> recipesList;

    private OnItemClickListener onItemClickListener;

    public SimpleRecipesListAdapter(OnItemClickListener onItemClickListener) {
        this(new ArrayList<Recipe>(), onItemClickListener);
    }

    public SimpleRecipesListAdapter(ArrayList<Recipe> recipes, OnItemClickListener onItemClickListener) {
        this.recipesList = recipes;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_recipe, parent, false);
        return new SimpleViewHolder(layoutView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        Recipe recipe = recipesList.get(position);
        holder.titleTextView.setText(recipe.getName());
        holder.itemView.setTag(recipe);
    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }

    public void addItems(ArrayList<Recipe> items) {
        this.recipesList.clear();
        this.recipesList.addAll(items);
        notifyDataSetChanged();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.title)
        public TextView titleTextView;

        private OnItemClickListener onItemClickListener;

        public SimpleViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view);
            }
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view);
    }
}