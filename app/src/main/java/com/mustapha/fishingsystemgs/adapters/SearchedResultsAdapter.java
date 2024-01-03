package com.mustapha.fishingsystemgs.adapters;


import android.content.Context;
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
    private final Context context;
    private List<Object> objects;

    public SearchedResultsAdapter(Context context) {
        this.context = context;
        this.objects = new ArrayList<>();
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
        Object o = objects.get(position);
        if(o instanceof PGR)
        {
            PGR pgr = (PGR) o;
            holder.name.setText(pgr.getName());
        }else if(o instanceof TS)
        {
            TS ts = (TS) o;
            holder.name.setText(ts.getName());
        }

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }

    public void filter(List<Object> objects) {
        this.objects = new ArrayList<>(objects);
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

