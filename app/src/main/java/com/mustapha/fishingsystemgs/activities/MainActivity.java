package com.mustapha.fishingsystemgs.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
    private List<Object> objects;
    private RelativeLayout relativeLayout;
    private Button addPGRBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        objects = new ArrayList<>();

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

        objects.addAll(pgrs);
        objects.addAll(ts);

        Button viewPGRBtn = findViewById(R.id.view_pgr);
        addPGRBtn = findViewById(R.id.add_mother);
        relativeLayout = findViewById(R.id.house1);
        Button addBtn = findViewById(R.id.add);
        SearchView searchView = findViewById(R.id.searchView);

        SearchedResultsAdapter adapter = new SearchedResultsAdapter(this);
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
                displayAlert("Please type first decimal and second decimal and then click on the Add button.", "Error Occurred");
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
                    List<Object> filteredChatList = filter(s);
                    if (!filteredChatList.isEmpty()) {
                        adapter.filter(filteredChatList);
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });
    }

    private List<Object> filter(String query) {
        query = query.toLowerCase().trim();
        List<Object> objectArrayList = new ArrayList<>();
        for (Object object : this.objects) {
            if (object instanceof PGR) {
                PGR pgr = (PGR) object;
                String name = pgr.getName().toLowerCase();
                if (name.contains(query)) {
                    objectArrayList.add(object);
                }
            } else if (object instanceof TS) {
                TS ts = (TS) object;
                String name = ts.getName().toLowerCase();
                if (name.contains(query)) {
                    objectArrayList.add(object);
                }
            }
        }
        return objectArrayList;
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
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();

        return super.onOptionsItemSelected(item);
    }
}