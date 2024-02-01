package com.mustapha.fishingsystemgs.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.mustapha.fishingsystemgs.classes.PGR;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mustapha.fishingsystemgs.R;
import com.mustapha.fishingsystemgs.classes.TS;
import com.mustapha.fishingsystemgs.databases.PGRDatabase;
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
        });

        holder.delete.setOnClickListener(view -> {
            try {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setMessage("Do you really want to delete this PGR?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            pgrs.remove(position);
                            PGRDatabase pgrDb = new PGRDatabase(context,
                                    "pgrs.db", null, 1);
                            pgrDb.delete(pgr.getDna());
                            notifyItemRemoved(position);
                        })
                        .create();
                dialog.show();
            }catch (Exception e)
            {

            }
        });

        holder.copy.setOnClickListener(view -> {
            copy(pgr.getName());
            //TODO
        });

        holder.edit.setOnClickListener(view -> {
            holder.addTs.setVisibility(View.GONE);
            holder.relativeLayout2.setVisibility(View.VISIBLE);
            holder.firstDecimal.setText(String.valueOf(pgr.getFirstDecimalNumber()));
            holder.secondDecimal.setText(String.valueOf(pgr.getSecondDecimalNumber()));
            holder.edit.setVisibility(View.GONE);
        });

        holder.secondDecimal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    String s1 = holder.firstDecimal.getText().toString();
                    String s2 = holder.secondDecimal.getText().toString();
                    double firstDecimalNum = Double.parseDouble(s1);
                    double secondDecimalNum = Double.parseDouble(s2);
                    double thirdDecimalNum = firstDecimalNum - secondDecimalNum;
                    DecimalFormat df = new DecimalFormat("#.#####");
                    df.setRoundingMode(RoundingMode.CEILING);
                    String s3 = "PGR = " + s1 + "-" + s2 + " " + df.format(thirdDecimalNum);
                    holder.formedPGR.setText(s3);
                }catch (Exception ignored)
                {

                }
            }
        });

        holder.addBtn.setOnClickListener(view -> {
            try {
                String s1 = holder.firstDecimal.getText().toString().replace(" ", "");
                String s2 = holder.secondDecimal.getText().toString().replace(" ", "");
                double firstDecimalNum = Double.parseDouble(s1);
                double secondDecimalNum = Double.parseDouble(s2);
                double thirdDecimalNum = firstDecimalNum - secondDecimalNum;
                DecimalFormat df = new DecimalFormat("#.#####");
                df.setRoundingMode(RoundingMode.CEILING);
                String name = s1 + "-" + s2 + " " + df.format(thirdDecimalNum);
                String dna = pgr.getDna();
                PGR pgr2 = new PGR(name, firstDecimalNum, secondDecimalNum, thirdDecimalNum, dna);
                PGRDatabase pgrDb = new PGRDatabase(context,
                        "pgrs.db", null, 1);
                pgrDb.update(pgr.getDna(), pgr2);
                pgrs.remove(position);
                pgrs.add(position, pgr2);
                notifyItemChanged(position);
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setMessage("Your PGR has been successfully edited.")
                        .setCancelable(true)
                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                            holder.relativeLayout2.setVisibility(View.GONE);
                            holder.edit.setVisibility(View.VISIBLE);
                        })
                        .create();
                dialog.show();
            }catch (Exception ignored)
            {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setMessage("Please type First Decimal first, then type the Second Decimal and then click on the Add button.")
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
                        s1 + " " + pgr.getThirdDecimalNumber();
                holder.ts.setText(s2);
            }
        });

        holder.add.setOnClickListener(view -> {
            try {
                holder.relativeLayout.setVisibility(View.GONE);
                holder.addTs.setVisibility(View.VISIBLE);
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
                    String s2 = s1 + " " + pgr.getThirdDecimalNumber();
                    TSDatabase tsDb = new TSDatabase(context,
                            "tss.db", null, 1);
                    String id = UUID.randomUUID().toString();
                    TS ts = new TS(s2, pgr.getThirdDecimalNumber(), pgr.getDna(), id, s1);
                    tsDb.insert(ts);
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setMessage("Your new TS has been successfully stored.")
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

    }


    @Override
    public int getItemCount() {
        return pgrs.size();
    }

    public void setPGR(List<PGR> pgrs) {
        this.pgrs = pgrs;
        notifyDataSetChanged();
    }

    public void copy(String text)
    {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copy PGR", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "PGR copied to clipboard", Toast.LENGTH_LONG).show();
    }
    public void filter(List<PGR> pgrs) {
        this.pgrs = pgrs;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, ts, number;
        private final Button addTs, add;
        private final RelativeLayout relativeLayout, relativeLayout2;
        private final EditText tsNameEdit;

        private final TextView delete, copy;
        private final TextView edit;
        Button addBtn;
        EditText firstDecimal;
        EditText secondDecimal;
        TextView formedPGR;

        ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name);
            addTs =view.findViewById(R.id.add_ts);
            number = view.findViewById(R.id.number);
            add = view.findViewById(R.id.add);
            delete = view.findViewById(R.id.delete);
            copy = view.findViewById(R.id.copy);
            edit = view.findViewById(R.id.edit);
            addBtn = view.findViewById(R.id.edit_pgr);
            firstDecimal = view.findViewById(R.id.firstDecimalEdit);
            secondDecimal = view.findViewById(R.id.secondDecimalEdit);
            formedPGR = view.findViewById(R.id.third_decimal);
            relativeLayout = view.findViewById(R.id.house1);
            relativeLayout2 = view.findViewById(R.id.house2);
            tsNameEdit = view.findViewById(R.id.tsNameEdit);
            ts = view.findViewById(R.id.ts);
        }
    }
}

