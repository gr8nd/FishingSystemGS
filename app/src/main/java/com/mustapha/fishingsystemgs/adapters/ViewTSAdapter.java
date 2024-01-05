package com.mustapha.fishingsystemgs.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.mustapha.fishingsystemgs.R;
import com.mustapha.fishingsystemgs.classes.TS;
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
                        notifyItemRemoved(position);
                    })
                    .create();
            dialog.show();
        });

        holder.edit.setOnClickListener(view -> {
            holder.relativeLayout.setVisibility(View.VISIBLE);
            holder.edit.setVisibility(View.GONE);
            holder.tsNameEdit.setText(ts.getTsName());
        });

        holder.add.setOnClickListener(view -> {
            try {
                holder.relativeLayout.setVisibility(View.GONE);
                holder.edit.setVisibility(View.VISIBLE);
                String s1 = holder.tsNameEdit.getText().toString().replace(" ", "");
                if(s1.isEmpty())
                {
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setMessage("Please first type the TS name and click on the Add button.")
                            .setCancelable(true)
                            .setPositiveButton("Ok", (dialogInterface, i) -> {
                            })
                            .create();
                    dialog.show();
                }else {
                    String s2 = s1 + " " + ts.getThirdDecimalOfMother();
                    TSDatabase tsDb = new TSDatabase(context,
                            "tss.db", null, 1);
                    TS ts1 = new TS(s2, ts.getThirdDecimalOfMother(), ts.getDnaOfMother(), ts.getId(), s1);
                    tsDb.update(ts.getId(), ts1);
                    tsList.remove(position);
                    tsList.add(position, ts1);
                    notifyItemChanged(position);
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setMessage("Your TS has been successfully edited.")
                            .setCancelable(true)
                            .setPositiveButton("Ok", (dialogInterface, i) -> {
                            })
                            .create();
                    dialog.show();
                }

            }catch (Exception ignored)
            {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setMessage("Please first type the TS name and click on the Add button.")
                        .setCancelable(true)
                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                        })
                        .create();
                dialog.show();
            }
        });

        holder.tsNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s1 = holder.tsNameEdit.getText().toString();
                String s2 = context.getResources().getString(R.string.ts) + " = " +
                        s1 + " " + ts.getThirdDecimalOfMother();
                holder.ts.setText(s2);
            }
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
        this.tsList = tsList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, number, ts;
        private final TextView delete, edit;

        private final EditText tsNameEdit;
        private final RelativeLayout relativeLayout;
        private final Button add;

        ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name);
            delete =view.findViewById(R.id.delete);
            edit = view.findViewById(R.id.edit);
            tsNameEdit = view.findViewById(R.id.tsNameEdit);
            relativeLayout = view.findViewById(R.id.house1);
            ts = view.findViewById(R.id.ts);
            add = view.findViewById(R.id.add);
            number = view.findViewById(R.id.number);
        }
    }
}

