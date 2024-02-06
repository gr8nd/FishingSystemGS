package com.mustapha.fishingsystemgs.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mustapha.fishingsystemgs.R;
import com.mustapha.fishingsystemgs.classes.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LauncherActivity extends AppCompatActivity {
    private static final String APPLICATION_ID = "com.mustapha.fishingsystemgs";
    private static final String ADMIN_TOKEN_KEY = APPLICATION_ID + "ADMIN_TOKEN_KEY";
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        relativeLayout = findViewById(R.id.relativeLayout);
        EditText adminKeyEdit = findViewById(R.id.admin_key_edit);
        Button continueBtn = findViewById(R.id.go_on);

        ProgressBar progressBar = findViewById(R.id.progress_bar);
        TextView connecting = findViewById(R.id.connecting);



        if(loadAdminKey() != null) {
            DatabaseReference adminsRef = FirebaseDatabase.getInstance().getReference("admins");
            adminsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean isAdmin = false;
                    for(DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
                        Admin admin = dataSnapshot.getValue(Admin.class);
                        if(admin != null && admin.getId().equals(loadAdminKey()))
                        {
                            Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                            startActivity(intent);
                            isAdmin = true;
                            finish();
                        }
                    }
                    if(!isAdmin)
                    {
                        relativeLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        connecting.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }


          continueBtn.setOnClickListener(view -> {
              progressBar.setVisibility(View.VISIBLE);
              connecting.setVisibility(View.VISIBLE);
              String key = adminKeyEdit.getText().toString().trim();
              DatabaseReference adminsRef = FirebaseDatabase.getInstance().getReference("admins");
            adminsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean isAdmin = false;
                    for(DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
                        Admin admin = dataSnapshot.getValue(Admin.class);
                        if(admin != null && admin.getId().equals(key))
                        {
                            saveAdminKey(key);
                            Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                            startActivity(intent);
                            isAdmin = true;
                            finish();
                        }
                    }
                    if(!isAdmin)
                    {
                        AlertDialog dialog = new AlertDialog.Builder(LauncherActivity.this)
                                .setMessage("Error! Wrong admin key detected.")
                                .setCancelable(true)
                                .setPositiveButton("Ok", (dialogInterface, i) -> {
                                })
                                .create();
                        dialog.show();
                        relativeLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        connecting.setVisibility(View.GONE);

                    }else {
                        Toast.makeText(LauncherActivity.this,
                                "Your admin key has been authenticated.", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        connecting.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
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

    public void saveAdminKey(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ADMIN_TOKEN_KEY, key);
        editor.apply();
    }


    public String loadAdminKey() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(ADMIN_TOKEN_KEY, null);
    }

}