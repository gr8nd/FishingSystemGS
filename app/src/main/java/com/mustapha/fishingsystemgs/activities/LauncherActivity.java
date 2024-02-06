package com.mustapha.fishingsystemgs.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mustapha.fishingsystemgs.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.List;

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

        if(loadAdminKey() == null) {
            relativeLayout.setVisibility(View.VISIBLE);
        }

        continueBtn.setOnClickListener(view -> {
            String key = adminKeyEdit.getText().toString().trim();
            if(loadAdminKey().equals(key))
            {
                Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                startActivity(intent);
            }else if(loadAdminKey() == null) {
                  relativeLayout.setVisibility(View.VISIBLE);
                  DatabaseReference adminsRef = FirebaseDatabase.getInstance().getReference("admins");
        //        pickUpRef.addValueEventListener(new ValueEventListener() {
        //            @Override
        //            public void onDataChange(@NonNull DataSnapshot snapshot) {
        //                for(DataSnapshot dataSnapshot: snapshot.getChildren())
        //                {
        //                    Admin admin = dataSnapshot.getValue(Admin.class);
        //                    if(admin != null && admin.getId().equal(key)
        //                    {
                                  //saveAdminKey(key);
        //                        Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                                  //startActivity(intent);
        //                    }
        //                }
        //            }
        //            @Override
        //            public void onCancelled(@NonNull DatabaseError error) {
        //            }
        //        });

            }else{
                AlertDialog dialog = new AlertDialog.Builder(LauncherActivity.this)
                        .setMessage("Error! Wrong admin key detected.")
                        .setCancelable(true)
                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                        })
                        .create();
                dialog.show();
            }
        });

        Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
        //startActivity(intent);
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