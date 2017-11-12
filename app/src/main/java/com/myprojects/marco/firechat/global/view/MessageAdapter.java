package com.myprojects.marco.firechat.global.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.myprojects.marco.firechat.R;
import com.myprojects.marco.firechat.Utils;
import com.myprojects.marco.firechat.analytics.DeveloperError;
import com.myprojects.marco.firechat.global.data_model.Chat;
import com.myprojects.marco.firechat.global.data_model.Message;
import com.myprojects.marco.firechat.user.data_model.User;
import com.myprojects.marco.firechat.user.data_model.Users;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 08/08/16.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_THIS_USER = 0;
    private static final int VIEW_TYPE_MESSAGE_OTHER_USERS = 1;
    private static final int VIEW_TYPE_MESSAGE_THIS_USER_OTHER_DATE = 3;
    private static final int VIEW_TYPE_MESSAGE_OTHER_USERS_OTHER_DATE = 4;
    private Chat chat = new Chat(new ArrayList<Message>());
    private User self;
    private final LayoutInflater inflater;

    MessageAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        setHasStableIds(true);
    }

    public void update(Chat chat, Users users, User user) {
        this.chat = chat;
        this.chat.addUsers(users.getUsers());
        this.self = user;
        notifyDataSetChanged();
    }

    public void add(Chat chat, Users users, User user) {
        this.chat.addUsers(users.getUsers());
        this.self = user;
        int count = this.chat.addMessages(chat.getMessages());
        notifyItemRangeInserted(0, count);
    }

    public void add(Message message, User sender, User user) {
        if (this.chat.size() == 0 || !this.chat.get(this.chat.size()-1).equals(message)) {
            this.chat.addMessage(message);
            this.chat.addUser(sender);
            this.self = user;
            notifyDataSetChanged();
        }
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MessageView messageView;
        if (viewType == VIEW_TYPE_MESSAGE_THIS_USER) {
            messageView = (MessageView) inflater.inflate(R.layout.global_message_item_sender_view, parent, false);
        } else if (viewType == VIEW_TYPE_MESSAGE_OTHER_USERS) {
            messageView = (MessageView) inflater.inflate(R.layout.global_message_item_destination_view, parent, false);
        } else if (viewType == VIEW_TYPE_MESSAGE_THIS_USER_OTHER_DATE) {
            messageView = (MessageView) inflater.inflate(R.layout.global_message_item_sender_other_date_view, parent, false);
        } else if (viewType == VIEW_TYPE_MESSAGE_OTHER_USERS_OTHER_DATE) {
            messageView = (MessageView) inflater.inflate(R.layout.global_message_item_destination_other_date_view, parent, false);
        } else {
            throw new DeveloperError("global message error");
        }
        return new MessageViewHolder(messageView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = chat.get(position);
        if (chat.getUser(message.getUid()) != null)
            holder.bind(chat.getUser(message.getUid()),message);
    }

    @Override
    public int getItemCount() {
        return chat.size();
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(chat.get(position).getTimestamp().replace("/",""));
    }

    @Override
    public int getItemViewType(int position) {
        try {
            String[] date1 = Utils.getDate(chat.get(position - 1).getTimestamp()).split("/");
            String[] date2 = Utils.getDate(chat.get(position).getTimestamp()).split("/");
            String concatDate1 = date1[0] + date1[1] + date1[2];
            String concatDate2 = date2[0] + date2[1] + date2[2];
            if (!concatDate1.equals(concatDate2)) {
                return chat.get(position).getUid().equals(self.getUid()) ? VIEW_TYPE_MESSAGE_THIS_USER_OTHER_DATE : VIEW_TYPE_MESSAGE_OTHER_USERS_OTHER_DATE;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return chat.get(position).getUid().equals(self.getUid()) ? VIEW_TYPE_MESSAGE_THIS_USER_OTHER_DATE : VIEW_TYPE_MESSAGE_OTHER_USERS_OTHER_DATE;
        }

        return chat.get(position).getUid().equals(self.getUid()) ? VIEW_TYPE_MESSAGE_THIS_USER : VIEW_TYPE_MESSAGE_OTHER_USERS;
    }

}
