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
import androidx.recyclerview.widget.RecyclerView;

import com.mustapha.fishingsystemgs.classes.PGR;

import java.util.ArrayList;
import java.util.List;
import com.mustapha.fishingsystemgs.R;
import com.mustapha.fishingsystemgs.classes.TS;
import com.mustapha.fishingsystemgs.databases.TSDatabase;


public class ViewPGRAdapter extends RecyclerView.Adapter<ViewPGRAdapter.ViewHolder> {
    private final Context context;

    private List<PGR> pgrs;

    public ViewPGRAdapter(Context context) {
        this.context = context;
        this.pgrs = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pgr_recycler,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        PGR pgr = pgrs.get(position);
        holder.name.setText(pgr.getName());
        String n = "(" + (position + 1) + ")";
        holder.number.setText(n);
        holder.addTs.setOnClickListener(view -> {
            holder.addTs.setVisibility(View.GONE);
            holder.relativeLayout.setVisibility(View.VISIBLE);
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
                            s1 + " " + pgr.getThirdDecimalNumber();
                    holder.ts.setText(s2);
                }
            });
        });

        holder.add.setOnClickListener(view -> {
            try {
                holder.relativeLayout.setVisibility(View.GONE);
                holder.addTs.setVisibility(View.VISIBLE);
                String s1 = holder.tsNameEdit.getText().toString();
                String s2 = s1 + " " + pgr.getThirdDecimalNumber();
                TSDatabase tsDb = new TSDatabase(context,
                        "tss.db", null, 1);
                TS ts = new TS(s2, pgr.getThirdDecimalNumber(), pgr.getDna());
                tsDb.insert(ts);
            }catch (Exception ignored)
            {

            }
        });

    }

    @Override
    public int getItemCount() {
        return pgrs.size();
    }

    public void setPGR(List<PGR> pgrs) {
        this.pgrs = pgrs;
        notifyDataSetChanged();
    }

    public void filter(List<PGR> pgrs) {
        this.pgrs = new ArrayList<>(pgrs);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, ts, number;
        private final Button addTs, add;
        private final RelativeLayout relativeLayout;
        private EditText tsNameEdit;

        ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name);
            addTs =view.findViewById(R.id.add_ts);
            number = view.findViewById(R.id.number);
            add = view.findViewById(R.id.add);
            relativeLayout = view.findViewById(R.id.house1);
            tsNameEdit = view.findViewById(R.id.tsNameEdit);
            ts = view.findViewById(R.id.ts);
        }
    }
}

