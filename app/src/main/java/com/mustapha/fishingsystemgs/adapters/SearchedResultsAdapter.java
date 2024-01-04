package com.mustapha.fishingsystemgs.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mustapha.fishingsystemgs.R;
import com.mustapha.fishingsystemgs.classes.PGR;
import com.mustapha.fishingsystemgs.classes.TS;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class SearchedResultsAdapter extends RecyclerView.Adapter<SearchedResultsAdapter.ViewHolder> {
    private List<Object> objectList;

    public SearchedResultsAdapter() {
        this.objectList = new ArrayList<>();
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
        Object o = objectList.get(position);
        if(o instanceof TS)
        {
            TS ts = (TS) o;
            holder.name.setText(ts.getName());
        }else if(o instanceof PGR)
        {
            PGR pgr = (PGR) o;
            holder.name.setText(pgr.getName());
        }


    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    public void setObjectList(List<Object> objectList) {
        this.objectList.addAll(objectList);
        notifyDataSetChanged();
    }

    public void updateList(List<Object> tsList){
        this.objectList = tsList;
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

