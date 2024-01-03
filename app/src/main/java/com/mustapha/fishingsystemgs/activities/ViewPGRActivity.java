package com.mustapha.fishingsystemgs.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.mustapha.fishingsystemgs.R;
import com.mustapha.fishingsystemgs.adapters.ViewPGRAdapter;
import com.mustapha.fishingsystemgs.classes.PGR;
import com.mustapha.fishingsystemgs.databases.PGRDatabase;

import java.util.List;

public class ViewPGRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pgractivity);
        PGRDatabase pgrDb = new PGRDatabase(ViewPGRActivity.this,
                "pgrs.db", null, 1);
        List<PGR> pgrs = pgrDb.getPGRs();

        ViewPGRAdapter adapter = new ViewPGRAdapter(this);
        adapter.setPGR(pgrs);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewPGRActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
