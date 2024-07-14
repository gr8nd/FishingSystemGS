package com.mustapha.fishingsystemgs.activities;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SearchView;


import com.mustapha.fishingsystemgs.R;
import com.mustapha.fishingsystemgs.adapters.ViewPGRAdapter;
import com.mustapha.fishingsystemgs.classes.ClickListener;
import com.mustapha.fishingsystemgs.classes.PGR;

import com.mustapha.fishingsystemgs.classes.TS;
import com.mustapha.fishingsystemgs.databases.PGRDatabase;
import com.mustapha.fishingsystemgs.databases.TSDatabase;

import java.util.ArrayList;
import java.util.List;

public class ViewPGRActivity extends AppCompatActivity {
    private List<PGR> pgrs;
    private List<TS> tsList;
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

        TSDatabase tsDatabase = new TSDatabase(ViewPGRActivity.this,
                "tss.db", null, 1);
        tsList = tsDatabase.getTss();

        adapter = new ViewPGRAdapter(this);
        adapter.setPGR(pgrs);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewPGRActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.searchView);
//        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(ViewPGRActivity.this,
//                recyclerView, new ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                //triggers when click
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//                //triggers when you long press
//            }
//        }));

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
            String s = pgr.getFirstDecimalNumber() + "-" + pgr.getSecondDecimalNumber();
            if (name.contains(query) || s.contains(query)) {
                pgrList.add(pgr);
            }
        }

        for (TS ts : this.tsList) {
            if (ts.getName().contains(query) ||
                    ts.getTsName().contains(query)) {
                for (PGR pgr : this.pgrs) {
                    if (pgr.getDna().contains(ts.getDnaOfMother())) {
                        pgrList.add(pgr);
                    }
                }
            }
        }

        adapter.filter(pgrList);
    }

//    public class RecyclerViewItemClickListener implements RecyclerView.OnItemTouchListener {
//
//        //GestureDetector to detect touch event.
//        private GestureDetector gestureDetector;
//        private ClickListener clickListener;
//
//        public RecyclerViewItemClickListener(Context context, final RecyclerView recyclerView,
//                                             final ClickListener clickListener) {
//            this.clickListener = clickListener;
//            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
//                @Override
//                public boolean onSingleTapUp(MotionEvent e) {
//                    return true;
//                }
//
//                @Override
//                public void onLongPress(MotionEvent e) {
//                    //Find child on x and y position relative to screen
//                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
//                    if (child != null && clickListener != null) {
//                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
//                    }
//                }
//            });
//        }
//
//        @Override
//        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//            //On Touch event
//            View child = rv.findChildViewUnder(e.getX(), e.getY());
//            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
//                clickListener.onClick(child, rv.getChildLayoutPosition(child));
//            }
//            return false;
//        }
//
//        @Override
//        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//        }
//
//        @Override
//        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//        }
//    }
}
