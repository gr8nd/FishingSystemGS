package com.mustapha.fishingsystemgs.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mustapha.fishingsystemgs.R;
import com.mustapha.fishingsystemgs.adapters.SearchedResultsAdapter;
import com.mustapha.fishingsystemgs.classes.Admin;
import com.mustapha.fishingsystemgs.classes.KVS;
import com.mustapha.fishingsystemgs.classes.PGR;
import com.mustapha.fishingsystemgs.classes.TS;
import com.mustapha.fishingsystemgs.classes.TSG;
import com.mustapha.fishingsystemgs.databases.KVSDatabase;
import com.mustapha.fishingsystemgs.databases.PGRDatabase;
import com.mustapha.fishingsystemgs.databases.TSDatabase;
import com.mustapha.fishingsystemgs.databases.TSGDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private List<PGR> pgrList;
    private List<TS> list;

    private List<TSG> tsgList;

    private static final String APPLICATION_ID = "com.mustapha.fishingsystemgs";
    private static final String ADMIN_TOKEN_KEY = APPLICATION_ID + "ADMIN_TOKEN_KEY";
    private List<KVS> kvsList;

    private PGRDatabase pgrDb;
    private TSGDatabase tsgDb;

    private RelativeLayout relativeLayout, relativeLayout2;
    private Button addPGRBtn, addTSGBtn;
    private TextView pgrSearched;
    private SearchedResultsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Button viewPGRBtn = findViewById(R.id.view_pgr);
        Button viewTSBtn = findViewById(R.id.view_tss);

        Button viewKVSBtn = findViewById(R.id.view_kvs);
        Button viewTSGBtn = findViewById(R.id.view_tsg);

        addPGRBtn = findViewById(R.id.add_mother);
        addTSGBtn = findViewById(R.id.add_tsg);

        Button addTSG = findViewById(R.id.add_tsg_btn);

        relativeLayout = findViewById(R.id.house1);
        relativeLayout2 = findViewById(R.id.house3);
        Button addBtn = findViewById(R.id.add);
        SearchView searchView = findViewById(R.id.searchView);

        initialise();

        syncFromOffline();


        EditText firstDecimal = findViewById(R.id.firstDecimalEdit);
        EditText secondDecimal = findViewById(R.id.secondDecimalEdit);
        TextView formedMother = findViewById(R.id.third_decimal);

        EditText tsgEdit = findViewById(R.id.tsgNameEdit);

        viewPGRBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ViewPGRActivity.class);
            startActivity(intent);
        });

        viewTSBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ViewTSActivity.class);
            startActivity(intent);
        });

        viewKVSBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ViewKVSActivity.class);
            startActivity(intent);
        });

        viewTSGBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ViewTSGActivity.class);
            startActivity(intent);
        });

        addPGRBtn.setOnClickListener(view -> {
            relativeLayout.setVisibility(View.VISIBLE);
            relativeLayout2.setVisibility(View.GONE);
            addPGRBtn.setVisibility(View.GONE);
        });

        addTSGBtn.setOnClickListener(view -> {
            relativeLayout2.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
            addTSGBtn.setVisibility(View.GONE);
        });

        addTSG.setOnClickListener(view -> {
            try {

                String name = tsgEdit.getText().toString().trim().replace(" ", "");
                if(!name.isEmpty())
                {
                    String dna = UUID.randomUUID().toString();;
                    TSG tsg = new TSG(name, dna);
                    TSGDatabase tsgDatabase = new TSGDatabase(this,
                            "tsg.db", null, 1);
                    tsgDatabase.insert(tsg);
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("Your TSG has been successfully stored.")
                            .setCancelable(true)
                            .setPositiveButton("Ok", (dialogInterface, i) -> {
                                relativeLayout.setVisibility(View.GONE);
                                relativeLayout2.setVisibility(View.GONE);
                                addPGRBtn.setVisibility(View.VISIBLE);
                                addTSGBtn.setVisibility(View.VISIBLE);
                            })
                            .create();
                    dialog.show();
                }else {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("Please type TSG name and then click on the Add button.")
                            .setCancelable(true)
                            .setPositiveButton("Ok", (dialogInterface, i) -> {
                            })
                            .create();
                    dialog.show();
                }

            }catch (Exception ignored)
            {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage("Please type TSG name and then click on the Add button.")
                        .setCancelable(true)
                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                        })
                        .create();
                dialog.show();
            }

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
            relativeLayout2.setVisibility(View.GONE);
            addPGRBtn.setVisibility(View.VISIBLE);
            addTSGBtn.setVisibility(View.VISIBLE);
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
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                query = query.trim().replace(" ", "");
                filterTSAndKVS(query);

                return true;
            }
        });
    }

    private void filterTSAndKVS(String query) {
        List<Object> tsList = new ArrayList<>();
        for (TS ts : this.list) {
            if (ts.getName().contains(query) ||
                    ts.getTsName().contains(query)) {
                tsList.add(ts);
            }
        }

        for (KVS ts : this.kvsList) {
            if (ts.getName().contains(query)) {
                tsList.add(ts);
            }
        }


        for (PGR pgr : this.pgrList) {
            String name = pgr.getName();
            String s = pgr.getFirstDecimalNumber() + "-" +pgr.getSecondDecimalNumber();
            if (name.contains(query) || s.contains(query)) {
                pgrSearched.setText(name);
                String dna = pgr.getDna();
                for (TS ts : this.list) {
                    String dnaOfMother = ts.getDnaOfMother();
                    if (dnaOfMother.equals(dna)) {
                        tsList.add(ts);
                    }
                }
            }
        }

        for (TSG tsg : this.tsgList) {
            String name = tsg.getName();
            if (name.contains(query)) {
                pgrSearched.setText(name);
                String dna = tsg.getDna();
                for (KVS kvs : this.kvsList) {
                    String dnaOfMother = kvs.getDnaOfMother();
                    if (dnaOfMother.equals(dna)) {
                        tsList.add(kvs);
                    }
                }
            }
        }

        adapter.updateList(tsList);
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
            StringBuilder data = new StringBuilder();
            for(PGR pgr: pgrList)
            {
                data.append(pgr.getName()).append("\n\n");
                for(TS ts: list)
                {
                    if(ts.getDnaOfMother().equals(pgr.getDna()))
                    {
                        data.append(ts.getName()).append("\n");
                    }
                }
                data.append("\n\n");
            }
            share(data.toString());
            return true;
        } else if(id == R.id.upload_data){
            uploadData();
            return true;
        }else if(id == R.id.admin_panel){
            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
            startActivity(intent);
            return true;
        } else{
            onBackPressed();
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem adminIcon = menu.findItem(R.id.admin_panel);
        DatabaseReference adminsRef = FirebaseDatabase.getInstance().getReference("admins");
        adminsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    Admin admin = dataSnapshot.getValue(Admin.class);
                    if(admin != null && admin.getId().equals(loadAdminKey()))
                    {
                        adminIcon.setVisible(true);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return true;
    }

    public String loadAdminKey() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(ADMIN_TOKEN_KEY, null);
    }

    private void syncFromOnline()
    {
        PGRDatabase pgrDb = new PGRDatabase(MainActivity.this,
                "pgrs.db", null, 1);

        TSDatabase tssDb = new TSDatabase(MainActivity.this,
                "tss.db", null, 1);

        TSGDatabase tsgsDb = new TSGDatabase(MainActivity.this,
                "tsg.db", null, 1);

        KVSDatabase kvssDb = new KVSDatabase(MainActivity.this,
                "kvs.db", null, 1);

        DatabaseReference pgrsRef = FirebaseDatabase.getInstance().getReference("pgrs");
        pgrsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    PGR pgr = dataSnapshot.getValue(PGR.class);
                    if(pgr != null && pgr.getDna() != null)
                    {
                        pgrDb.insert(pgr);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference tssRef = FirebaseDatabase.getInstance().getReference("tsgs");
        tssRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    TS ts = dataSnapshot.getValue(TS.class);
                    if(ts != null && ts.getDnaOfMother() != null)
                    {
                        tssDb.insert(ts);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        DatabaseReference tsgsRef = FirebaseDatabase.getInstance().getReference("pgrs");
        tsgsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    TSG tsg = dataSnapshot.getValue(TSG.class);
                    if(tsg != null && tsg.getDna() != null)
                    {
                        tsgsDb.insert(tsg);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        DatabaseReference kvsRef = FirebaseDatabase.getInstance().getReference("kvs");
        kvsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    KVS kvs = dataSnapshot.getValue(KVS.class);
                    if(kvs != null && kvs.getDnaOfMother() != null)
                    {
                        kvssDb.insert(kvs);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void syncFromOffline()
    {
        PGRDatabase pgrDb = new PGRDatabase(MainActivity.this,
                "pgrs.db", null, 1);
        List<PGR> pgrs = pgrDb.getPGRs();

        TSDatabase tssDb = new TSDatabase(MainActivity.this,
                "tss.db", null, 1);
        List<TS> tss = tssDb.getTss();

        TSGDatabase tsgsDb = new TSGDatabase(MainActivity.this,
                "tsg.db", null, 1);
        List<TSG> tsgs = tsgsDb.getTSGs();

        KVSDatabase kvssDb = new KVSDatabase(MainActivity.this,
                "kvs.db", null, 1);
        List<KVS> kvss = kvssDb.getKVSs();


        DatabaseReference pgrsRef = FirebaseDatabase.getInstance().getReference("pgrs");
        for(PGR pgr: pgrs)
        {
            pgrsRef.child(pgr.getDna()).setValue(pgr);
        }
        DatabaseReference tssRef = FirebaseDatabase.getInstance().getReference("tss");
        for (TS ts: tss)
        {
            tssRef.child(ts.getId()).setValue(ts);
        }
        DatabaseReference tsgsRef = FirebaseDatabase.getInstance().getReference("tsgs");
        for (TSG tsg: tsgs)
        {
            tsgsRef.child(tsg.getDna()).setValue(tsg);
        }
        DatabaseReference kvsRef = FirebaseDatabase.getInstance().getReference("kvs");
        for (KVS kvs: kvss)
        {
            kvsRef.child(kvs.getId()).setValue(kvs);
        }

        syncFromOnline();
    }
    private void writeToFile() {
        StringBuilder data = new StringBuilder();
        for(PGR pgr: pgrList)
        {
            data.append(pgr.getName()).append("\n\n");
            for(TS ts: list)
            {
                if(ts.getDnaOfMother().equals(pgr.getDna()))
                {
                    data.append(ts.getName()).append("\n");
                }
            }
            data.append("\n|n");
        }
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput("fsgs.txt",
                    this.MODE_PRIVATE));
            outputStreamWriter.write(data.toString());
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = this.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private void share(String data) {

        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, data.toString());
            Intent intent = Intent.createChooser(shareIntent, getResources().getString(R.string.shareWith));
            startActivity(intent);
        } catch (Exception ignored) {

        }
    }

    private void uploadData()
    {
        File dir = new File(Environment.getExternalStorageDirectory() +
                File.separator + Environment.DIRECTORY_DOWNLOADS);

        if(Build.VERSION.SDK_INT >= 30)
        {
            dir = new File(this.getExternalCacheDir() + File.separator +
                    getResources().getString(R.string.download_dir));
        }
        String extension = "fsgs"  + ".txt";
        if(!dir.exists())
        {
            dir.mkdirs();
        }
        File file = new File(dir, extension);
        try {
            int length = (int) file.length();

            byte[] bytes = new byte[length];

            FileInputStream in = new FileInputStream(file);
            try {
                in.read(bytes);
            } finally {
                in.close();
            }
            String contents = new String(bytes);
            share(contents);
        }catch (Exception e)
        {
            Log.e("Upload error", e.toString());
        }
    }

    private void download() {

        StringBuilder data = new StringBuilder();
        for(PGR pgr: pgrList)
        {
            data.append(pgr.getName()).append("\n\n");
            for(TS ts: list)
            {
                if(ts.getDnaOfMother().equals(pgr.getDna()))
                {
                    data.append(ts.getName()).append("\n");
                }
            }
            data.append("\n\n");
        }

            File dir = new File(Environment.getExternalStorageDirectory() +
                    File.separator + Environment.DIRECTORY_DOWNLOADS);

            if(Build.VERSION.SDK_INT >= 30)
            {
                dir = new File(this.getExternalCacheDir() + File.separator +
                        getResources().getString(R.string.download_dir));
            }
            String extension = "fsgs"  + ".txt";
            if(!dir.exists())
            {
                dir.mkdirs();
            }
            try {
                File file = new File(dir, extension);
                FileOutputStream stream = new FileOutputStream(file);
                try {
                    stream.write(data.toString().getBytes());
                } finally {
                    stream.flush();
                    stream.close();
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("Your data has been downloaded successfully.")
                            .setCancelable(true)
                            .setPositiveButton("Ok", (dialogInterface, i) -> {
                            })
                            .create();
                    dialog.show();
                }
            }catch (Exception e)
            {
                Log.e("Download error", e.toString());
            }
    }

    @Override
    protected void onResume() {

        initialise();

        super.onResume();
    }

    private void initialise()
    {
        adapter = new SearchedResultsAdapter();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        List<Object> objectList = new ArrayList<>();

        list = new ArrayList<>();
        pgrList = new ArrayList<>();

        tsgList = new ArrayList<>();
        kvsList = new ArrayList<>();

        pgrSearched = findViewById(R.id.pgr_searched);

        pgrDb = new PGRDatabase(MainActivity.this,
                "pgrs.db", null, 1);
        List<PGR> pgrs = pgrDb.getPGRs();

        TSDatabase tsDb = new TSDatabase(MainActivity.this,
                "tss.db", null, 1);
        List<TS> ts = tsDb.getTss();


        tsgDb = new TSGDatabase(MainActivity.this,
                "tsg.db", null, 1);
        List<TSG> tsgs = tsgDb.getTSGs();

        KVSDatabase kvsDb = new KVSDatabase(MainActivity.this,
                "kvs.db", null, 1);
        List<KVS> kvs = kvsDb.getKVSs();

        TextView pgrCounter = findViewById(R.id.pgr_count);
        TextView tsCounter = findViewById(R.id.ts_count);
        TextView tsgCounter = findViewById(R.id.tsg_count);
        TextView kvsCounter = findViewById(R.id.kvs_count);

        String pc = getResources().getString(R.string.pgr_count) + ": " + pgrDb.count();
        pgrCounter.setText(pc);
        String tc = getResources().getString(R.string.ts_count) + ": " + tsDb.count();
        tsCounter.setText(tc);

        String tsgc = getResources().getString(R.string.tsg_count) + ": " + tsgDb.count();
        tsgCounter.setText(tsgc);
        String kvsc = getResources().getString(R.string.kvs_count) + ": " + kvsDb.count();
        kvsCounter.setText(kvsc);

        pgrList.addAll(pgrs);
        list.addAll(ts);

        tsgList.addAll(tsgs);
        kvsList.addAll(kvs);

        objectList.clear();
        objectList.addAll(pgrList);
        objectList.addAll(list);

        objectList.addAll(tsgList);
        objectList.addAll(kvsList);

        adapter.setObjectList(objectList);
    }
}