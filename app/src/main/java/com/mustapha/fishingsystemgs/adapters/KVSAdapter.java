package com.mustapha.fishingsystemgs.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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

import com.mustapha.fishingsystemgs.R;
import com.mustapha.fishingsystemgs.classes.KVS;
import com.mustapha.fishingsystemgs.databases.KVSDatabase;

import java.util.ArrayList;
import java.util.List;

public class KVSAdapter extends RecyclerView.Adapter<KVSAdapter.ViewHolder> {
    private final Context context;

    private List<KVS> kvsList;

    public KVSAdapter(Context context) {
        this.context = context;
        this.kvsList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_kvs_recycler,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        KVS kvs = kvsList.get(position);
        holder.name.setText(kvs.getName());
        String n = "(" + (position + 1) + ")";
        holder.number.setText(n);

        holder.delete.setOnClickListener(view -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setMessage("Do you really want to delete this KVS?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        kvsList.remove(position);
                        KVSDatabase kvsDatabase = new KVSDatabase(context,
                                "kvs.db", null, 1);
                        kvsDatabase.delete(kvs.getDnaOfMother());
                        notifyItemRemoved(position);
                    })
                    .create();
            dialog.show();
        });

        holder.copy.setOnClickListener(view -> {
            copy(kvs.getName());
            //TODO
        });

        holder.edit.setOnClickListener(view -> {
            holder.relativeLayout.setVisibility(View.VISIBLE);
            holder.KVSEdit.setText(kvs.getName());
            holder.edit.setVisibility(View.GONE);
            holder.name.setText(kvs.getName());
        });


        holder.editBtn.setOnClickListener(view -> {
            try {
                holder.relativeLayout.setVisibility(View.GONE);
                holder.edit.setVisibility(View.VISIBLE);
                String s1 = holder.KVSEdit.getText().toString().replace(" ", "").trim();
                if(s1.isEmpty())
                {
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setMessage("Please first type the KVS name and click on the Add button.")
                            .setCancelable(true)
                            .setPositiveButton("Ok", (dialogInterface, i) -> {
                            })
                            .create();
                    dialog.show();
                }else {
                    KVSDatabase tsDb = new KVSDatabase(context,
                            "kvs.db", null, 1);
                    KVS ts1 = new KVS(s1,  kvs.getDnaOfMother(), kvs.getId());
                    tsDb.update(kvs.getDnaOfMother(), ts1);
                    kvsList.remove(position);
                    kvsList.add(position, ts1);
                    notifyItemChanged(position);
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setMessage("Your KVS has been successfully edited.")
                            .setCancelable(true)
                            .setPositiveButton("Ok", (dialogInterface, i) -> {
                            })
                            .create();
                    dialog.show();
                }

            }catch (Exception ignored)
            {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setMessage("Please first type the KVS name and click on the Edit button.")
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
        return kvsList.size();
    }

    public void setKVS(List<KVS> tsList) {
        this.kvsList = tsList;
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
    public void filter(List<KVS> tsList) {
        this.kvsList = tsList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, number;
        private final TextView delete, edit, copy;
        private final EditText KVSEdit;

        private final RelativeLayout relativeLayout;
        private final Button editBtn;

        ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name);
            delete =view.findViewById(R.id.delete);
            edit = view.findViewById(R.id.edit);
            copy = view.findViewById(R.id.copy);
            editBtn = view.findViewById(R.id.edit_kvs);
            KVSEdit = view.findViewById(R.id.kvs_edit);
            relativeLayout = view.findViewById(R.id.house2);
            number = view.findViewById(R.id.number);
        }
    }
}


