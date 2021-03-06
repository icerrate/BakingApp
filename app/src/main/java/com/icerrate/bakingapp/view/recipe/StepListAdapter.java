package com.icerrate.bakingapp.view.recipe;

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

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.StepViewHolder> {

    private int selectedPos = RecyclerView.NO_POSITION;

    private ArrayList<Step> stepsList;

    private OnItemClickListener onItemClickListener;

    public StepListAdapter(OnItemClickListener onItemClickListener) {
        this(new ArrayList<Step>(), onItemClickListener);
    }

    public StepListAdapter(ArrayList<Step> steps, OnItemClickListener onItemClickListener) {
        this.stepsList = steps;
        this.onItemClickListener = onItemClickListener;
    }

    public void setSelectedStep(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false);
        return new StepViewHolder(layoutView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        Step step = stepsList.get(position);
        holder.itemView.setSelected(selectedPos == position);
        holder.descriptionTextView.setText(step.getShortDescription());
        holder.itemView.setTag(step);
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    public void addItems(ArrayList<Step> items) {
        this.stepsList.clear();
        this.stepsList.addAll(items);
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
                onItemClickListener.onItemClick(getAdapterPosition(), view);
                if (selectedPos == RecyclerView.NO_POSITION) return;
                notifyItemChanged(selectedPos);
                selectedPos = getAdapterPosition();
                notifyItemChanged(selectedPos);
            }
        }
    }

    public interface OnItemClickListener {

        void onItemClick(int index, View view);
    }
}