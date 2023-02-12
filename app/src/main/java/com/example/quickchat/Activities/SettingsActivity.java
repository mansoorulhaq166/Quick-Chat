package com.example.quickchat.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.quickchat.R;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);

        logout = findViewById(R.id.logout_button);

        // setting toolbar
        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        logout.setOnClickListener(view -> {
            Dialog dialog = new Dialog(SettingsActivity.this, R.style.Dialog);
            dialog.setContentView(R.layout.dialog_logout);

            Button yes, no;

            yes = dialog.findViewById(R.id.dialog_yes);
            no = dialog.findViewById(R.id.dialog_no);

            yes.setOnClickListener(view1 -> {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                finish();
            });
            no.setOnClickListener(view12 -> dialog.dismiss());
            dialog.show();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Intent main = new Intent(SettingsActivity.this, HomeActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(main);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent main = new Intent(SettingsActivity.this, HomeActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(main);
        finish();
        super.onBackPressed();
    }
}