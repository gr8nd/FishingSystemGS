package com.mustapha.fishingsystemgs.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;

import com.mustapha.fishingsystemgs.R;
import com.mustapha.fishingsystemgs.adapters.ViewPGRAdapter;
import com.mustapha.fishingsystemgs.classes.PGR;

import com.mustapha.fishingsystemgs.databases.PGRDatabase;

import java.util.ArrayList;
import java.util.List;

public class ViewPGRActivity extends AppCompatActivity {
private List<PGR> pgrs;
private ViewPGRAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pgractivity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        PGRDatabase pgrDb = new PGRDatabase(ViewPGRActivity.this,
                "pgrs.db", null, 1);
        pgrs = pgrDb.getPGRs();

        adapter = new ViewPGRAdapter(this);
        adapter.setPGR(pgrs);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewPGRActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                query = query.trim().replace(" ", "");
                filterPGR(query);

                return true;
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    private void filterPGR(String query)
    {
        List<PGR> pgrList = new ArrayList<>();
        for (PGR pgr : this.pgrs) {
            String name = pgr.getName();
            String s = pgr.getFirstDecimalNumber() + "-" +pgr.getSecondDecimalNumber();
            if (name.contains(query) || s.contains(query)) {
                pgrList.add(pgr);
            }
        }
        adapter.filter(pgrList);
    }
}
