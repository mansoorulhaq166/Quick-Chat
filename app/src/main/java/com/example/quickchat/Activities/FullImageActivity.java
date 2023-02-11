package com.example.quickchat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.quickchat.R;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class FullImageActivity extends AppCompatActivity {

    String receivedClickedImage, receivedClickedName;
    ImageView fullImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        receivedClickedImage = getIntent().getStringExtra("clickedImage");
        receivedClickedName = getIntent().getStringExtra("clickedName");

        // setting toolbar
        Toolbar toolbar = findViewById(R.id.full_image_toolbar);
        toolbar.setTitle(receivedClickedName);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        fullImage = findViewById(R.id.clicked_image);
        Picasso.get().load(receivedClickedImage).into(fullImage);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Intent main = new Intent(FullImageActivity.this, HomeActivity.class);
      //  main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(main);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent main = new Intent(FullImageActivity.this, HomeActivity.class);
        startActivity(main);
        finish();
        super.onBackPressed();
    }
}