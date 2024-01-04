package com.mustapha.fishingsystemgs.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.mustapha.fishingsystemgs.R;
import com.mustapha.fishingsystemgs.classes.TS;
import com.mustapha.fishingsystemgs.databases.PGRDatabase;
import com.mustapha.fishingsystemgs.databases.TSDatabase;

import java.util.ArrayList;
import java.util.List;

public class ViewTSAdapter extends RecyclerView.Adapter<ViewTSAdapter.ViewHolder> {
    private final Context context;

    private List<TS> tsList;

    public ViewTSAdapter(Context context) {
        this.context = context;
        this.tsList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ts_recycler,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        TS ts = tsList.get(position);
        holder.name.setText(ts.getName());
        String n = "(" + (position + 1) + ")";
        holder.number.setText(n);

        holder.delete.setOnClickListener(view -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setMessage("Do you really want to delete this TS?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        tsList.remove(position);
                        TSDatabase tsDb = new TSDatabase(context,
                                "tss.db", null, 1);
                        tsDb.delete(ts.getId());
                        notifyDataSetChanged();
                    })
                    .create();
            dialog.show();
        });


    }


    @Override
    public int getItemCount() {
        return tsList.size();
    }

    public void setTS(List<TS> tsList) {
        this.tsList = tsList;
        notifyDataSetChanged();
    }

    public void filter(List<TS> tsList) {
        this.tsList = new ArrayList<>(tsList);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, number;
        private final TextView delete;

        ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name);
            delete =view.findViewById(R.id.delete);
            number = view.findViewById(R.id.number);
        }
    }
}

