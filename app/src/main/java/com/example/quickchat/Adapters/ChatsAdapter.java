package com.example.quickchat.Adapters;

import static com.example.quickchat.Activities.ChatActivity.rImage;
import static com.example.quickchat.Activities.ChatActivity.receivedEmail;
import static com.example.quickchat.Activities.HomeActivity.loggedUser;
import static com.example.quickchat.Activities.HomeActivity.sendImage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickchat.Models.Messages;
import com.example.quickchat.Models.Seen_upd;
import com.example.quickchat.R;
import com.example.quickchat.Retrofit.ApiController;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsAdapter extends RecyclerView.Adapter {

    Context context;
    final static String url = "http://192.168.43.231/ChatappApis/images/";
    List<Messages> messagesList;
    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;
    public static int ls_message_status;
    public static String ls_message_email;
    public static String message_id;

    public ChatsAdapter(List<Messages> messagesList, Context context) {
        this.context = context;
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_layout_item, parent, false);
            return new SenderHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reciever_layout_item, parent, false);
            return new ReceiverHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int adapterPosition = holder.getAdapterPosition();
        Messages messages = messagesList.get(adapterPosition);
        ls_message_status = messages.getSeen_status();
        ls_message_email = messages.getReceiver_email();
        message_id = messages.getMessage_id();

        if (loggedUser.equals(ls_message_email)) {
            if (ls_message_status == 0) {
                getSeenUpdated();
            }
            if (holder.getClass() == SenderHolder.class) {
                SenderHolder sviewHolder = (SenderHolder) holder;
                sviewHolder.messageText.setText(messages.getMessage());
                Picasso.get().load(url + sendImage).into(sviewHolder.imageView);
            } else {
                ReceiverHolder rviewHolder = (ReceiverHolder) holder;
                rviewHolder.messageText.setText(messages.getMessage());
                Picasso.get().load(rImage).into(rviewHolder.imageView);
            }
        } else {
            if (holder.getClass() == SenderHolder.class) {
                SenderHolder sviewHolder = (SenderHolder) holder;
                sviewHolder.messageText.setText(messages.getMessage());
                Picasso.get().load(url + sendImage).into(sviewHolder.imageView);

                if (ls_message_status == 1) {
                    sviewHolder.messageText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sent_done_all_24, 0, 0, 0);
                }
            } else {
                ReceiverHolder rviewHolder = (ReceiverHolder) holder;
                rviewHolder.messageText.setText(messages.getMessage());
                Picasso.get().load(rImage).into(rviewHolder.imageView);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesList.get(position);
        if (messages.getReceiver_email().equals(receivedEmail)) {
            return ITEM_SEND;
        } else
            return ITEM_RECEIVE;
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public static class ReceiverHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView messageText;

        public ReceiverHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profile_image);
            messageText = itemView.findViewById(R.id.chat_text);
        }
    }

    public static class SenderHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView messageText;

        public SenderHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profile_image);
            messageText = itemView.findViewById(R.id.chat_text);
        }
    }

    public void getSeenUpdated() {

        Call<Seen_upd> call = ApiController.getInstance().getApi().updateSeenStatus(message_id);
        call.enqueue(new Callback<Seen_upd>() {
            @Override
            public void onResponse(@NonNull Call<Seen_upd> call, @NonNull Response<Seen_upd> response) {
                Seen_upd obj = response.body();
                assert obj != null;
                String result = obj.getMessage();
                if (result.equals("success")) {
                } else if (result.equals("fail")) {
                }
            }

            @Override
            public void onFailure(@NonNull Call<Seen_upd> call, @NonNull Throwable t) {
                Log.d("Seen Failure", "onFailure: " + t.getMessage());
                Toast.makeText(context.getApplicationContext(), "Failure: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}