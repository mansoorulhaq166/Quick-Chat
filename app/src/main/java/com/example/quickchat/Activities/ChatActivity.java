package com.example.quickchat.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.quickchat.Adapters.ChatsAdapter;
import com.example.quickchat.Models.Messages;
import com.example.quickchat.Models.Save_msg_resp;
import com.example.quickchat.R;
import com.example.quickchat.Retrofit.ApiController;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    CircleImageView profileImage;
    TextView profile_name;
    EditText messageText;
    ImageButton messageButton;
    ImageView button_back;
    RecyclerView messageRecycler;
    ArrayList<Messages> messagesArrayList;
    SharedPreferences sharedPreferences;
    String receivedName, receivedImage, receivedId, sendImage;
    String message;
    public static String currentUserEmail;
    public static String rImage;
    public static String receivedEmail;

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

        messagesArrayList = new ArrayList<>();

          ChatsAdapter chatsAdapter = new ChatsAdapter(messagesArrayList, ChatActivity.this);
          messageRecycler.setAdapter(chatsAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        messageRecycler.setLayoutManager(linearLayoutManager);

        AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.message_text, RegexTemplate.NOT_EMPTY, R.string.message_error);

        receivedId = getIntent().getStringExtra("userId");
        receivedName = getIntent().getStringExtra("userName");
        receivedImage = getIntent().getStringExtra("userImage");

        // getting images for chatAdapter
        rImage = receivedImage;

        // receiving messages
        getUserMessages();

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatActivity.this, HomeActivity.class));
                finish();
            }
        });
        profile_name.setText(receivedName);
        Picasso.get().load(receivedImage).into(profileImage);

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (awesomeValidation.validate()) {
                    message = messageText.getText().toString();
                    currentUserEmail = getSharedPreferences("credentials", MODE_PRIVATE)
                            .getString("user", "");
                    receivedEmail = getIntent().getStringExtra("userEmail");
                    sendMessage(message, currentUserEmail, receivedEmail);
                }
                //  getUserMessages();
            }
        });
    }

    public void getUserMessages() {
        Call<ArrayList<Messages>> call = ApiController.getInstance().getApi().
                getMessages();
        call.enqueue(new Callback<ArrayList<Messages>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Messages>> call, @NonNull Response<ArrayList<Messages>> response) {
                ArrayList<Messages> messageData = response.body();
                if (response.isSuccessful()) {
                    messagesArrayList = messageData;
                    ArrayList<Messages> otherMsgs = getOtherMsgs();
                    ChatsAdapter chatsAdapter = new ChatsAdapter(otherMsgs, ChatActivity.this);

                    // checking if layout is loaded
                    final ViewTreeObserver observer = messageRecycler.getViewTreeObserver();
                    observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (chatsAdapter.getItemCount() > 0) {
                                // scrolling to last message
                                messageRecycler.smoothScrollToPosition(chatsAdapter.getItemCount() - 1);
                            }
                            // removing listener to avoid multiple calls
                            messageRecycler.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });
                    messageRecycler.setAdapter(chatsAdapter);
                    chatsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Messages>> call, @NonNull Throwable t) {
                Toast.makeText(ChatActivity.this, "Cause: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("myError", "onFailure: " + t.getMessage());
            }
        });
    }

    public ArrayList<Messages> getOtherMsgs() {
        ArrayList<Messages> otherMsgs = new ArrayList<>();
        currentUserEmail = getSharedPreferences("credentials", MODE_PRIVATE)
                .getString("user", "");
        receivedEmail = getIntent().getStringExtra("userEmail");
        for (Messages msgs : messagesArrayList) {
            if ((msgs.getSender_email().equals(currentUserEmail) &&
                    msgs.getReceiver_email().equals(receivedEmail)) ||
                    (msgs.getSender_email().equals(receivedEmail) &&
                            msgs.getReceiver_email().equals(currentUserEmail))) {
                otherMsgs.add(msgs);
            }
        }
        return otherMsgs;
    }

    private void sendMessage(String message, String currentUserEmail, String receivedEmail) {
        Call<Save_msg_resp> call = ApiController.getInstance().getApi()
                .saveMsgs(message, currentUserEmail, receivedEmail);
        call.enqueue(new Callback<Save_msg_resp>() {
            @Override
            public void onResponse(@NonNull Call<Save_msg_resp> call, @NonNull Response<Save_msg_resp> response) {
                Save_msg_resp msg_resp = response.body();
                assert msg_resp != null;
                String result = msg_resp.getStatus();
                if (result.equals("success")) {
                    messageText.setText("");
                    getUserMessages();
                } else if (result.equals("fail")) {
                    Toast.makeText(ChatActivity.this, "Not Sent", Toast.LENGTH_SHORT).show();
                    messageText.setText("");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Save_msg_resp> call, @NonNull Throwable t) {
                Log.e("SavingMsg", "onFailure: " + t.getMessage());
                Toast.makeText(ChatActivity.this, "Cause: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}