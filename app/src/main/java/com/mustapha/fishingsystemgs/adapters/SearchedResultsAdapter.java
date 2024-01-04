package com.mustapha.fishingsystemgs.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mustapha.fishingsystemgs.R;
import com.mustapha.fishingsystemgs.classes.TS;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class SearchedResultsAdapter extends RecyclerView.Adapter<SearchedResultsAdapter.ViewHolder> {
    private List<TS> tsList;

    public SearchedResultsAdapter() {
        this.tsList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searched_results_recycler,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        TS ts = tsList.get(position);
        holder.name.setText(ts.getName());

    }

    @Override
    public int getItemCount() {
        return tsList.size();
    }

    public void setTsList(List<TS> tsList) {
        this.tsList.addAll(tsList);
        notifyDataSetChanged();
    }

    public void filter(List<TS> tsList) {
        this.tsList = new ArrayList<>(tsList);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;

        ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name);
        }
    }
}

