package com.mustapha.fishingsystemgs.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mustapha.fishingsystemgs.R;
import com.mustapha.fishingsystemgs.adapters.KVSAdapter;
import com.mustapha.fishingsystemgs.classes.KVS;
import com.mustapha.fishingsystemgs.databases.KVSDatabase;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class ViewKVSActivity extends AppCompatActivity {

    private List<KVS> kvsList;
    private KVSAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_kvsactivity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        KVSDatabase kvsDatabase = new KVSDatabase(this,
                "kvs.db", null, 1);
        kvsList = kvsDatabase.getKVSs();

        adapter = new KVSAdapter(this);
        adapter.setKVS(kvsList);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
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
                filterKVS(query);

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

    private void filterKVS(String query) {
        List<KVS> list = new ArrayList<>();
        for (KVS kvs : this.kvsList) {
            if (kvs.getName().contains(query)) {
                list.add(kvs);
            }
        }

        adapter.filter(list);
    }
}