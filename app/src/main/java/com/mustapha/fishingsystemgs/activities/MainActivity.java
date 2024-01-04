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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

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

    private PGRDatabase pgrDb;

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
                String s1 = firstDecimal.getText().toString().replace(" ", "");
                String s2 = secondDecimal.getText().toString().replace(" ", "");
                double firstDecimalNum = Double.parseDouble(s1);
                double secondDecimalNum = Double.parseDouble(s2);
                double thirdDecimalNum = firstDecimalNum - secondDecimalNum;
                DecimalFormat df = new DecimalFormat("#.#####");
                df.setRoundingMode(RoundingMode.CEILING);
                String name = s1 + "-" + s2 + " " + df.format(thirdDecimalNum);
                String dna = UUID.randomUUID().toString();
                PGR pgr = new PGR(name, firstDecimalNum, secondDecimalNum, thirdDecimalNum, dna);
                pgrDb.insert(pgr);
                displayAlert("Your new PGR has been successfully stored.");
            }catch (Exception ignored)
            {
                displayAlert("Please type First Decimal first, then type the Second Decimal and then click on the Add button.");
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
            String s = pgr.getFirstDecimalNumber() + "-" +pgr.getSecondDecimalNumber();
            if (query.equals(name) || query.equals(s)) {
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

        return tsList;
    }

    private List<TS> filterTS(String query) {
        query = query.trim();
        List<TS> tsList = new ArrayList<>();
        for (TS ts : this.list) {
            if (query.equals(ts.getName()) ||
                    query.equals(ts.getTsName())) {
                tsList.add(ts);
            }
        }

        return tsList;
    }

    private void displayAlert(@Nullable String message) {
        runOnUiThread(() -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(message)
                    .setCancelable(true)
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                        recreate();
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
        int id = item.getItemId();
        if (id == R.id.download_data) {
            download();
            return true;
        } else if (id == R.id.share_data) {
            share("");
            return true;
        } else {
            onBackPressed();
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    private void share(String data) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, data);
            Intent intent = Intent.createChooser(shareIntent, getResources().getString(R.string.shareWith));
            startActivity(intent);
        } catch (Exception e) {

        }
    }

    private void download() {
        try {
            //TODO
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        list = new ArrayList<>();
        pgrList = new ArrayList<>();

        pgrSearched = findViewById(R.id.pgr_searched);

        pgrDb = new PGRDatabase(MainActivity.this,
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

        super.onResume();
    }
}