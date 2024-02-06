package com.mustapha.fishingsystemgs.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mustapha.fishingsystemgs.R;
import com.mustapha.fishingsystemgs.adapters.ViewTSGAdapter;
import com.mustapha.fishingsystemgs.classes.KVS;
import com.mustapha.fishingsystemgs.classes.TSG;
import com.mustapha.fishingsystemgs.databases.KVSDatabase;
import com.mustapha.fishingsystemgs.databases.TSGDatabase;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class ViewTSGActivity extends AppCompatActivity {

    private List<TSG> tsgList;
    private List<KVS> kvsList;

    private ViewTSGAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tsg_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TSGDatabase tsgDatabase = new TSGDatabase(this,
                "tsg.db", null, 1);
        tsgList = tsgDatabase.getTSGs();

        KVSDatabase kvsDatabase = new KVSDatabase(this,
                "kvs.db", null, 1);
        kvsList = kvsDatabase.getKVSs();

        adapter = new ViewTSGAdapter(this);
        adapter.setTSG(tsgList);
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
                filterTSG(query);

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

    private void filterTSG(String query)
    {
        List<TSG> tsgArrayList = new ArrayList<>();
        for (TSG tsg : this.tsgList) {
            String name = tsg.getName();
            if (name.contains(query)) {
                tsgArrayList.add(tsg);
            }
        }

        for (KVS kvs : this.kvsList) {
            if (kvs.getName().contains(query)) {
                for (TSG tsg : this.tsgList) {
                    if (tsg.getDna().contains(kvs.getDnaOfMother())) {
                        tsgArrayList.add(tsg);
                    }
                }
            }
        }

        adapter.filter(tsgArrayList);
    }
}