package com.mustapha.fishingsystemgs.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.mustapha.fishingsystemgs.R;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

        Button authorize = findViewById(R.id.submit);

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
        });

        authorize.setOnClickListener(view -> {
            if(!token.getText().toString().equals(getResources().getString(R.string.token_will_appear_here)))
            {
                copy.setVisibility(View.VISIBLE);
                share.setVisibility(View.VISIBLE);
                //TODO

                AlertDialog dialog = new AlertDialog.Builder(AdminActivity.this)
                        .setMessage("Token has been submitted and authorized, you can copy and share as you wish.")
                        .setCancelable(true)
                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                        })
                        .create();
                dialog.show();
            }else
            {
                AlertDialog dialog = new AlertDialog.Builder(AdminActivity.this)
                        .setMessage("Please generate token first.")
                        .setCancelable(true)
                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                        })
                        .create();
                dialog.show();
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
}