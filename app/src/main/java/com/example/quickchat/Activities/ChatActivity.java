package com.example.quickchat.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.quickchat.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    CircleImageView profileImage;
    TextView profile_name;
    EditText messageText;
    ImageButton messageButton;
    ImageView button_back;
    RecyclerView messageRecycler;
    String receivedName, receivedImage, receivedId, senderUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        profileImage = findViewById(R.id.chat_profile_image);
        profile_name = findViewById(R.id.user_name_chat);

        messageText = findViewById(R.id.message_text);
        messageButton = findViewById(R.id.message_button);
        button_back = findViewById(R.id.chat_back);
        messageRecycler = findViewById(R.id.chat_recyclerView);

        AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.message_text, RegexTemplate.NOT_EMPTY, R.string.message_error);

        //setting recycler layout from end
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageRecycler.setLayoutManager(linearLayoutManager);

        receivedId = getIntent().getStringExtra("userId");
        receivedName = getIntent().getStringExtra("userName");
        receivedImage = getIntent().getStringExtra("userImage");

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatActivity.this, HomeActivity.class));
                finish();
            }
        });
        profile_name.setText(receivedName);
        Picasso.get().load(receivedImage).into(profileImage);

    }
}