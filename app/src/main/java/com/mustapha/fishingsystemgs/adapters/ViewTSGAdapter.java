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
import com.mustapha.fishingsystemgs.classes.TSG;
import com.mustapha.fishingsystemgs.databases.KVSDatabase;
import com.mustapha.fishingsystemgs.databases.TSGDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ViewTSGAdapter extends RecyclerView.Adapter<ViewTSGAdapter.ViewHolder> {
    private final Context context;

    private List<TSG> tsgList;

    public ViewTSGAdapter(Context context) {
        this.context = context;
        this.tsgList = new ArrayList<>();
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
        TSG tsg = tsgList.get(position);
        holder.name.setText(tsg.getName());
        String n = "(" + (position + 1) + ")";
        holder.number.setText(n);

        holder.addKVS.setOnClickListener(view -> {
            holder.addKVS.setVisibility(View.GONE);
            holder.relativeLayout.setVisibility(View.VISIBLE);
            holder.tsgName.setText(context.getResources().getString(R.string.type_kvs));
            holder.editBtn.setVisibility(View.GONE);
            holder.add.setVisibility(View.VISIBLE);
        });

        holder.delete.setOnClickListener(view -> {
            try {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setMessage("Do you really want to delete this PGR?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            tsgList.remove(position);
                            TSGDatabase pgrDb = new TSGDatabase(context,
                                    "tsg.db", null, 1);
                            pgrDb.delete(tsg.getDna());
                            notifyItemRemoved(position);
                        })
                        .create();
                dialog.show();
            }catch (Exception e)
            {

            }
        });

        holder.copy.setOnClickListener(view -> {
            copy(tsg.getName());
            //TODO
        });

        holder.edit.setOnClickListener(view -> {
            holder.addKVS.setVisibility(View.GONE);
            holder.edit.setVisibility(View.GONE);
            holder.editBtn.setVisibility(View.VISIBLE);
            holder.add.setVisibility(View.GONE);
            holder.tsgName.setText(context.getResources().getString(R.string.tsg_name));
        });



        holder.editBtn.setOnClickListener(view -> {
            try {
//                DecimalFormat df = new DecimalFormat("#.#####");
//                df.setRoundingMode(RoundingMode.CEILING);
//                String name = s1 + "-" + s2 + " " + df.format(thirdDecimalNum);

                String name = holder.tsgNameEdit.getText().toString().trim().replace(" ", "");
                String dna = tsg.getDna();
                TSG tsg1 = new TSG(name, dna);
                TSGDatabase tsgDatabase = new TSGDatabase(context,
                        "tsg.db", null, 1);
                tsgDatabase.update(tsg.getDna(), tsg1);
                tsgList.remove(position);
                tsgList.add(position, tsg1);
                notifyItemChanged(position);
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setMessage("Your TSG has been successfully edited.")
                        .setCancelable(true)
                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                            holder.edit.setVisibility(View.VISIBLE);
                        })
                        .create();
                dialog.show();
            }catch (Exception ignored)
            {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setMessage("Please type TSG name and then click on the Add button.")
                        .setCancelable(true)
                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                        })
                        .create();
                dialog.show();
            }

        });

        holder.add.setOnClickListener(view -> {
            try {
                holder.relativeLayout.setVisibility(View.GONE);
                holder.addKVS.setVisibility(View.VISIBLE);

                String s1 = holder.tsgNameEdit.getText().toString().replace(" ", "");
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
                    String id = UUID.randomUUID().toString();
                    KVS ts = new KVS(s1, tsg.getDna(), id);
                    tsDb.insert(ts);
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setMessage("Your new KVS has been successfully stored.")
                            .setCancelable(true)
                            .setPositiveButton("Ok", (dialogInterface, i) -> {
                            })
                            .create();
                    dialog.show();
                }

            }catch (Exception ignored)
            {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setMessage("Please first type the KVS name and click on the Add button.")
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
        return tsgList.size();
    }

    public void setTSG(List<TSG> tsgList) {
        this.tsgList = tsgList;
        notifyDataSetChanged();
    }

    public void copy(String text)
    {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copy TSG", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "TSG copied to clipboard", Toast.LENGTH_LONG).show();
    }
    public void filter(List<TSG> pgrs) {
        this.tsgList = pgrs;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, number;
        private final Button addKVS, add, editBtn;
        private final RelativeLayout relativeLayout;
        private final EditText tsgNameEdit;

        private final TextView delete, copy, tsgName;
        private final TextView edit;

        ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name);
            addKVS =view.findViewById(R.id.add_kvs);
            number = view.findViewById(R.id.number);
            add = view.findViewById(R.id.add);
            delete = view.findViewById(R.id.delete);
            copy = view.findViewById(R.id.copy);
            editBtn = view.findViewById(R.id.edi);
            edit = view.findViewById(R.id.edit);
            tsgName = view.findViewById(R.id.tsg_name);
            relativeLayout = view.findViewById(R.id.house1);
            tsgNameEdit = view.findViewById(R.id.tsgNameEdit);
        }
    }
}

