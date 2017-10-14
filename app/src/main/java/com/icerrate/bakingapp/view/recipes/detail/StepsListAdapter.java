package com.icerrate.bakingapp.view.recipes.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.data.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ivan Cerrate.
 */

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.StepViewHolder> {

    private ArrayList<Step> data;

    private OnItemClickListener onItemClickListener;

    public StepsListAdapter(OnItemClickListener onItemClickListener) {
        this(new ArrayList<Step>(), onItemClickListener);
    }

    public StepsListAdapter(ArrayList<Step> steps, OnItemClickListener onItemClickListener) {
        this.data = steps;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false);
        return new StepViewHolder(layoutView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        Step step = data.get(position);
        holder.descriptionTextView.setText(step.getShortDescription());
        holder.itemView.setTag(step);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItems(ArrayList<Step> items) {
        this.data.clear();
        this.data.addAll(items);
        notifyDataSetChanged();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.description)
        public TextView descriptionTextView;

        private OnItemClickListener onItemClickListener;

        public StepViewHolder(View itemView, OnItemClickListener onItemClickListener) {
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