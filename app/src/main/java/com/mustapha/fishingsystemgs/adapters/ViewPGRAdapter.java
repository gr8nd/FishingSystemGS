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
    private final CharSequence[] options;
    private List<PGR> pgrs;

    public ViewPGRAdapter(Context context) {
        this.context = context;
        this.pgrs = new ArrayList<>();

        options = new CharSequence[]{

                context.getResources().getString(R.string.add_ts_to),
                context.getResources().getString(R.string.copy_pgr),
                context.getResources().getString(R.string.delete),
                context.getResources().getString(R.string.edit),

        };
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
                boolean isDuplicate = false;
                for (PGR p: pgrDb.getPGRs())
                {
                    if (p.getName().equals(pgr.getName())) {
                        isDuplicate = true;
                        break;
                    }
                }

                if(!isDuplicate)
                {
                    pgrDb.update(pgr.getDna(), pgr2);
                    pgrs.remove(position);
                    pgrs.add(position, pgr2);
                    notifyItemChanged(position);
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setMessage("Your PGR has been successfully edited.")
                            .setCancelable(true)
                            .setPositiveButton("Ok", (dialogInterface, i) -> {
                                holder.relativeLayout2.setVisibility(View.GONE);
                            })
                            .create();
                    dialog.show();
                }else
                {
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setMessage("The PGR already exists.")
                            .setCancelable(true)
                            .setPositiveButton("Ok", (dialogInterface, i) -> {
                                holder.relativeLayout2.setVisibility(View.GONE);
                            })
                            .create();
                    dialog.show();
                }

            }catch (Exception ignored)
            {
                alert("Please type First Decimal first, then type the Second Decimal and then click on the Add button.");
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
                String s1 = holder.tsNameEdit.getText().toString().replace(" ", "");
                if(s1.isEmpty())
                {
                    alert("Please first type the TS name and click on the Add button.");
                }else {
                    String s2 = s1 + " " + pgr.getThirdDecimalNumber();
                    TSDatabase tsDb = new TSDatabase(context,
                            "tss.db", null, 1);
                    String id = UUID.randomUUID().toString();
                    TS ts = new TS(s2, pgr.getThirdDecimalNumber(), pgr.getDna(), id, s1);
                    tsDb.insert(ts);
                    alert("Your new TS has been successfully stored.");
                }

            }catch (Exception ignored)
            {
                alert("Please first type the TS name and click on the Add button.");
            }
        });

        holder.relativeLayout3.setOnLongClickListener(v -> {
            //selectOption();
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setItems(options, (dialog, index) -> {
                CharSequence option = options[index];
                if (option.equals(context.getResources().getString(R.string.delete)))
                {
                    try {
                        AlertDialog dialog2 = new AlertDialog.Builder(context)
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
                        dialog2.show();
                    }catch (Exception ignored)
                    {

                    }
                }  else if (option.equals(context.getResources().getString(R.string.edit)))
                {
                    holder.relativeLayout2.setVisibility(View.VISIBLE);
                    holder.firstDecimal.setText(String.valueOf(pgr.getFirstDecimalNumber()));
                    holder.secondDecimal.setText(String.valueOf(pgr.getSecondDecimalNumber()));
                }else if(option.equals(context.getResources().getString(R.string.add_ts_to)))
                {
                    holder.relativeLayout.setVisibility(View.VISIBLE);
                }else if(option.equals(context.getResources().getString(R.string.copy_pgr)))
                {
                    copy(pgr.getName());
                }
            });
            builder.show();
            return false;
        });

    }

    private void alert(String message)
    {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                })
                .create();
        dialog.show();
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
        private final Button add;
        private final RelativeLayout relativeLayout, relativeLayout2, relativeLayout3;
        private final EditText tsNameEdit;
        Button addBtn;
        EditText firstDecimal;
        EditText secondDecimal;
        TextView formedPGR;

        ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name);
            number = view.findViewById(R.id.number);
            add = view.findViewById(R.id.add);
            addBtn = view.findViewById(R.id.edit_pgr);
            firstDecimal = view.findViewById(R.id.firstDecimalEdit);
            secondDecimal = view.findViewById(R.id.secondDecimalEdit);
            formedPGR = view.findViewById(R.id.third_decimal);
            relativeLayout = view.findViewById(R.id.house1);
            relativeLayout2 = view.findViewById(R.id.house2);
            relativeLayout3 = view.findViewById(R.id.relativeLayout);
            tsNameEdit = view.findViewById(R.id.tsNameEdit);
            ts = view.findViewById(R.id.ts);
        }
    }
}

