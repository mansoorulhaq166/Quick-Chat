package com.example.quickchat.Adapters;

import static com.example.quickchat.Activities.ChatActivity.rImage;
import static com.example.quickchat.Activities.ChatActivity.receivedEmail;
import static com.example.quickchat.Activities.HomeActivity.sendImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quickchat.Models.Messages;
import com.example.quickchat.R;
import com.squareup.picasso.Picasso;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends RecyclerView.Adapter {

    Context context;
    final static String url = "http://192.168.43.231/ChatAppApis/images/";
    List<Messages> messagesList;
    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;

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

        if (holder.getClass() == SenderHolder.class) {
            SenderHolder sviewHolder = (SenderHolder) holder;
            sviewHolder.messageText.setText(messages.getMessage());
            Picasso.get().load(url + sendImage).into(sviewHolder.imageView);
        } else {
            ReceiverHolder rviewHolder = (ReceiverHolder) holder;
            rviewHolder.messageText.setText(messages.getMessage());
            Picasso.get().load(rImage).into(rviewHolder.imageView);
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
}
