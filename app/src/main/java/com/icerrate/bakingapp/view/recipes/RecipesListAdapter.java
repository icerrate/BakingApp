package com.icerrate.bakingapp.view.recipes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.data.model.Recipe;
import com.icerrate.bakingapp.view.common.GlideApp;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ivan Cerrate.
 */

public class RecipesListAdapter extends RecyclerView.Adapter<RecipesListAdapter.RecipeViewHolder> {

    private ArrayList<Recipe> recipesList;

    private OnItemClickListener onItemClickListener;

    public RecipesListAdapter(OnItemClickListener onItemClickListener) {
        this(new ArrayList<Recipe>(), onItemClickListener);
    }

    public RecipesListAdapter(ArrayList<Recipe> recipes, OnItemClickListener onItemClickListener) {
        this.recipesList = recipes;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(layoutView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = recipesList.get(position);
        Context context = holder.itemView.getContext();

        holder.titleTextView.setText(recipe.getName());
        String description = context.getResources().getString(R.string.recipe_description,
                recipe.getIngredients().size(),
                recipe.getSteps().size(),
                recipe.getServings());
        holder.descriptionTextView.setText(description);
        GlideApp.with(context)
                .load(recipe.getImage())
                .placeholder(R.drawable.square_placeholder)
                .into(holder.photoImageView);
        holder.viewButton.setTag(recipe);
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

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.title)
        public TextView titleTextView;

        @BindView(R.id.description)
        public TextView descriptionTextView;

        @BindView(R.id.photo)
        public ImageView photoImageView;

        @BindView(R.id.view_action)
        public Button viewButton;

        private OnItemClickListener onItemClickListener;

        public RecipeViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.onItemClickListener = onItemClickListener;
            viewButton.setOnClickListener(this);
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