package com.mustapha.fishingsystemgs.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mustapha.fishingsystemgs.R;
import com.mustapha.fishingsystemgs.adapters.ViewTSAdapter;

import com.mustapha.fishingsystemgs.classes.TS;
import com.mustapha.fishingsystemgs.databases.TSDatabase;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class ViewTSActivity extends AppCompatActivity {
private List<TS> tsList;
private ViewTSAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tsactivity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TSDatabase pgrDb = new TSDatabase(ViewTSActivity.this,
                "tss.db", null, 1);
        tsList = pgrDb.getTss();
        adapter = new ViewTSAdapter(this);
        adapter.setTS(tsList);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewTSActivity.this);
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
                filterTS(query);

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

    private void filterTS(String query) {
        List<TS> tsList = new ArrayList<>();
        for (TS ts : this.tsList) {
            if (ts.getName().contains(query) ||
                    ts.getTsName().contains(query)) {
                tsList.add(ts);
            }
        }

        adapter.filter(tsList);
    }
}