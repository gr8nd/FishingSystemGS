package com.mustapha.fishingsystemgs.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mustapha.fishingsystemgs.R;
import com.mustapha.fishingsystemgs.classes.Admin;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView token = findViewById(R.id.token);
        TextView copy = findViewById(R.id.copy);
        TextView share = findViewById(R.id.share);
        Button generateToken = findViewById(R.id.generate);
        EditText adminToken = findViewById(R.id.admin_key_edit);
        Button removeAdmin = findViewById(R.id.remove_admin_btn);

        Button authorize = findViewById(R.id.submit);

        removeAdmin.setOnClickListener(view -> {
            String tokeKey = adminToken.getText().toString().trim();
            if(!tokeKey.isEmpty())
            {
                DatabaseReference adminsRef = FirebaseDatabase.getInstance().getReference("admins");
                adminsRef.child(tokeKey).removeValue().addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        alert("The task has been successfully submitted to the cloud. The admin will be removed if the token is a valid admin token.");
                    }
                });
            }else {
                alert("First type or copy/paste the admin token key in the text box.");
            }


        });

        copy.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) AdminActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copy TS", token.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(AdminActivity.this, "Token copied to clipboard", Toast.LENGTH_LONG).show();
        });

        share.setOnClickListener(view -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, token.getText().toString());
                Intent intent = Intent.createChooser(shareIntent, getResources().getString(R.string.shareWith));
                startActivity(intent);
            } catch (Exception ignored) {

            }
        });

        generateToken.setOnClickListener(view -> {
            String secretToken = UUID.randomUUID().toString();
            token.setText(secretToken);
            copy.setVisibility(View.VISIBLE);
            share.setVisibility(View.VISIBLE);
            alert("Token generated successfully. To make someone an admin first copy the token and authorize it by clicking on the Authorize Token button below. You can then share the token with anyone you want to make admin.");
        });

        authorize.setOnClickListener(view -> {
            String tokenKey = token.getText().toString();
            if(!tokenKey.equals(getResources().getString(R.string.token_will_appear_here)))
            {
                DatabaseReference adminsRef = FirebaseDatabase.getInstance().getReference("admins");
                Admin admin = new Admin(tokenKey, "");
                adminsRef.child(tokenKey).setValue(admin).addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        alert("Token has been authorized and submitted to the cloud.");
                    }
                });

            }else
            {
               alert("Please generate token first.");
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void alert(String message)
    {
        AlertDialog dialog = new AlertDialog.Builder(AdminActivity.this)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                })
                .create();
        dialog.show();
    }
}