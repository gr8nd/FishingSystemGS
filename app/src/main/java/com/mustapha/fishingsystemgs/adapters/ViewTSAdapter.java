package com.mustapha.fishingsystemgs.adapters;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.content.ClipboardManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.mustapha.fishingsystemgs.R;
import com.mustapha.fishingsystemgs.classes.TS;
import com.mustapha.fishingsystemgs.databases.TSDatabase;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class ViewTSAdapter extends RecyclerView.Adapter<ViewTSAdapter.ViewHolder> {
    private final Context context;
    private String result;
    private final CharSequence[] options;
    private List<TS> tsList;

    public ViewTSAdapter(Context context) {
        this.context = context;
        this.tsList = new ArrayList<>();
        options = new CharSequence[]{

                context.getResources().getString(R.string.subtract),
                context.getResources().getString(R.string.copy_ts),
                context.getResources().getString(R.string.delete),
                context.getResources().getString(R.string.edit),

        };
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

        holder.sub.setOnClickListener(view -> {
            String firstTs = holder.firstTsEdit.getText().toString().trim().replaceFirst(" ", "");
            String secondTs = holder.secondTsEdit.getText().toString().trim().replace(" ", "");
            try {
                String decimalOfFirstTs = firstTs.substring(firstTs.lastIndexOf(".") - 1);
                String decimalOfSecondTs = secondTs.substring(secondTs.lastIndexOf(".") - 1);
                String sharedPart = firstTs.substring(0, firstTs.indexOf("/") + 1);
                String firstPart1 = firstTs.substring(firstTs.lastIndexOf("-")-1, firstTs.lastIndexOf("-") + 2);
                String secondPart1 = secondTs.substring(secondTs.lastIndexOf("-")-1, secondTs.lastIndexOf("-") + 2);
                double subValue = Math.abs(Double.parseDouble(decimalOfFirstTs) - Double.parseDouble(decimalOfSecondTs));
                result = sharedPart + firstPart1 + ";" + secondPart1 + "/" + subValue;
                String sr = "Copy      Born TS: " + result;
                holder.copyChild.setText(sr);
                holder.copyChild.setVisibility(View.VISIBLE);
            }catch (Exception ignored)
            {

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
                    String s2 = s1 + " " + ts.getThirdDecimalOfMother();
                    TSDatabase tsDb = new TSDatabase(context,
                            "tss.db", null, 1);
                    TS ts1 = new TS(s2, ts.getThirdDecimalOfMother(), ts.getDnaOfMother(), ts.getId(), s1);
                    tsDb.update(ts.getId(), ts1);
                    tsList.remove(position);
                    tsList.add(position, ts1);
                    notifyItemChanged(position);
                    alert("Your TS has been successfully edited.");
                }

            }catch (Exception ignored)
            {
                alert("Please first type the TS name and click on the Add button.");
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

        holder.relativeLayout3.setOnLongClickListener(v -> {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setItems(options, (dialog, index) -> {
                CharSequence option = options[index];
                if (option.equals(context.getResources().getString(R.string.delete)))
                {
                    AlertDialog dialog2 = new AlertDialog.Builder(context)
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
                    dialog2.show();

                }  else if (option.equals(context.getResources().getString(R.string.edit)))
                {
                    holder.relativeLayout.setVisibility(View.VISIBLE);
                    holder.tsNameEdit.setText(ts.getTsName());
                }else if(option.equals(context.getResources().getString(R.string.subtract)))
                {
                    holder.subtractRelativeLayout.setVisibility(View.VISIBLE);
                }else if(option.equals(context.getResources().getString(R.string.copy_ts)))
                {
                    copy(ts.getName());
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
        return tsList.size();
    }

    public void setTS(List<TS> tsList) {
        this.tsList = tsList;
        notifyDataSetChanged();
    }

    public void copy(String text)
    {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copy TS", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "TS copied to clipboard", Toast.LENGTH_LONG).show();
    }

    public void copyResult(String text)
    {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copy New TS child", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "New TS child copied to clipboard", Toast.LENGTH_LONG).show();
    }
    public void filter(List<TS> tsList) {
        this.tsList = tsList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, number, ts;

        private final EditText tsNameEdit;

        private final EditText firstTsEdit, secondTsEdit;
        private final RelativeLayout relativeLayout, subtractRelativeLayout, relativeLayout3;
        private final Button add, sub;
        public TextView copyChild;

        ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name);
            copyChild = view.findViewById(R.id.copy_child);
            subtractRelativeLayout = view.findViewById(R.id.subtract_relative_layout);
            firstTsEdit = view.findViewById(R.id.firstTSEdit);
            secondTsEdit = view.findViewById(R.id.secondTSEdit);
            tsNameEdit = view.findViewById(R.id.tsNameEdit);
            relativeLayout = view.findViewById(R.id.house1);
            relativeLayout3 = view.findViewById(R.id.relativeLayout);
            ts = view.findViewById(R.id.ts);
            add = view.findViewById(R.id.add);
            sub = view.findViewById(R.id.sub);
            number = view.findViewById(R.id.number);
        }
    }
}

