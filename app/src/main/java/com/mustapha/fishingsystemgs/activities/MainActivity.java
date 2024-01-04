package com.mustapha.fishingsystemgs.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.mustapha.fishingsystemgs.R;
import com.mustapha.fishingsystemgs.adapters.SearchedResultsAdapter;
import com.mustapha.fishingsystemgs.classes.PGR;
import com.mustapha.fishingsystemgs.classes.TS;
import com.mustapha.fishingsystemgs.databases.PGRDatabase;
import com.mustapha.fishingsystemgs.databases.TSDatabase;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private List<PGR> pgrList;
    private List<TS> list;
    private RelativeLayout relativeLayout;
    private Button addPGRBtn;
    private TextView pgrSearched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        list = new ArrayList<>();
        pgrList = new ArrayList<>();

        pgrSearched = findViewById(R.id.pgr_searched);

        PGRDatabase pgrDb = new PGRDatabase(MainActivity.this,
                "pgrs.db", null, 1);
        List<PGR> pgrs = pgrDb.getPGRs();

        TSDatabase tsDb = new TSDatabase(MainActivity.this,
                "tss.db", null, 1);
        List<TS> ts = tsDb.getTss();

        TextView pgrCounter = findViewById(R.id.pgr_count);
        TextView tsCounter = findViewById(R.id.ts_count);
        String pc = getResources().getString(R.string.pgr_count) + ": " + pgrDb.count();
        pgrCounter.setText(pc);
        String tc = getResources().getString(R.string.ts_count) + ": " + tsDb.count();
        tsCounter.setText(tc);

        pgrList.addAll(pgrs);
        list.addAll(ts);

        Button viewPGRBtn = findViewById(R.id.view_pgr);
        addPGRBtn = findViewById(R.id.add_mother);
        relativeLayout = findViewById(R.id.house1);
        Button addBtn = findViewById(R.id.add);
        SearchView searchView = findViewById(R.id.searchView);

        SearchedResultsAdapter adapter = new SearchedResultsAdapter();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        EditText firstDecimal = findViewById(R.id.firstDecimalEdit);
        EditText secondDecimal = findViewById(R.id.secondDecimalEdit);
        TextView formedMother = findViewById(R.id.third_decimal);

        viewPGRBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ViewPGRActivity.class);
            startActivity(intent);
        });

        addPGRBtn.setOnClickListener(view -> {
            relativeLayout.setVisibility(View.VISIBLE);
            addPGRBtn.setVisibility(View.GONE);
        });

        addBtn.setOnClickListener(view -> {
            try {
                String s1 = firstDecimal.getText().toString();
                String s2 = secondDecimal.getText().toString();
                double firstDecimalNum = Double.parseDouble(s1);
                double secondDecimalNum = Double.parseDouble(s2);
                double thirdDecimalNum = firstDecimalNum - secondDecimalNum;
                DecimalFormat df = new DecimalFormat("#.#####");
                df.setRoundingMode(RoundingMode.CEILING);
                String name = s1 + "-" + s2 + " " + df.format(thirdDecimalNum);
                String dna = UUID.randomUUID().toString();
                PGR pgr = new PGR(name, firstDecimalNum, secondDecimalNum, thirdDecimalNum, dna);
                pgrDb.insert(pgr);
                displayAlert("Your new PGR has been successfully stored.", "Stored");
            }catch (Exception ignored)
            {
                displayAlert("Please type First Decimal first, then type the Second Decimal and then click on the Add button.", "Error Occurred");
            }
            relativeLayout.setVisibility(View.GONE);
            addPGRBtn.setVisibility(View.VISIBLE);
        });

        secondDecimal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    String s1 = firstDecimal.getText().toString();
                    String s2 = secondDecimal.getText().toString();
                    double firstDecimalNum = Double.parseDouble(s1);
                    double secondDecimalNum = Double.parseDouble(s2);
                    double thirdDecimalNum = firstDecimalNum - secondDecimalNum;
                    DecimalFormat df = new DecimalFormat("#.#####");
                    df.setRoundingMode(RoundingMode.CEILING);
                    String s3 = "PGR = " + s1 + "-" + s2 + " " + df.format(thirdDecimalNum);
                    formedMother.setText(s3);
                }catch (Exception ignored)
                {

                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.trim().length() > 0) {
                    List<TS> filteredList = filterTS(s);
                    if (!filteredList.isEmpty()) {
                        adapter.setTsList(filteredList);
                    }else
                    {
                        List<TS> filteredList2 = filterPGR(s);
                        adapter.setTsList(filteredList2);
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.trim().length() > 0) {
                    List<TS> filteredList = filterTS(s);
                    if (!filteredList.isEmpty()) {
                        adapter.setTsList(filteredList);
                    }else
                    {
                        List<TS> filteredList2 = filterPGR(s);
                        adapter.setTsList(filteredList2);
                    }
                }
                return true;
            }
        });
    }

    private List<TS> filterPGR(String query) {
        query = query.trim();
        List<TS> tsList = new ArrayList<>();
        String dna = null;
        for (PGR pgr : this.pgrList) {
            String name = pgr.getName();
            if (name.equals(query)) {
                pgrSearched.setText(name);
                dna = pgr.getDna();
                break;
            }
        }

        for (TS ts : this.list) {
            String name = ts.getDnaOfMother();
            if (name.equals(dna)) {
                tsList.add(ts);
            }
        }

        //Toast.makeText(this, "TS: " + tsList.size(), Toast.LENGTH_LONG).show();

        return tsList;
    }

    private List<TS> filterTS(String query) {
        query = query.trim();
        List<TS> tsList = new ArrayList<>();
        for (TS ts : this.list) {
            String name = ts.getName();
            if (name.equals(query)) {
                tsList.add(ts);
            }
        }

        return tsList;
    }

    private void displayAlert(@Nullable String message, String title) {
        runOnUiThread(() -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(true)
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                    })
                    .create();
            dialog.show();
        });
    }

    @Override
    public void onBackPressed() {
        relativeLayout.setVisibility(View.GONE);
        addPGRBtn.setVisibility(View.VISIBLE);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();

        return super.onOptionsItemSelected(item);
    }
}