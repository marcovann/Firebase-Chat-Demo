package com.myprojects.marco.firechat.conversation.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.myprojects.marco.firechat.R;
import com.myprojects.marco.firechat.Utils;
import com.myprojects.marco.firechat.analytics.DeveloperError;
import com.myprojects.marco.firechat.conversation.data_model.Chat;
import com.myprojects.marco.firechat.conversation.data_model.Message;

import java.util.ArrayList;

/**
 * Created by marco on 29/07/16.
 */

public class ConversationMessageAdapter extends RecyclerView.Adapter<ConversationMessageViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_THIS_USER = 0;
    private static final int VIEW_TYPE_MESSAGE_OTHER_USERS = 1;
    private static final int VIEW_TYPE_MESSAGE_THIS_USER_OTHER_DATE = 3;
    private static final int VIEW_TYPE_MESSAGE_OTHER_USERS_OTHER_DATE = 4;
    private Chat chat = new Chat(new ArrayList<Message>());
    private String self;
    private final LayoutInflater inflater;

    ConversationMessageAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        setHasStableIds(true);
    }

    public void update(Chat chat, String user) {
        this.chat = chat;
        this.self = user;
        notifyDataSetChanged();
    }

    public void add(Message message, String user) {
        this.self = user;
        this.chat.addMessage(message);
        notifyDataSetChanged();
    }

    @Override
    public ConversationMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConversationMessageView messageView;
        if (viewType == VIEW_TYPE_MESSAGE_THIS_USER) {
            messageView = (ConversationMessageView) inflater.inflate(R.layout.conversation_message_item_destination_view, parent, false);
        } else if (viewType == VIEW_TYPE_MESSAGE_OTHER_USERS) {
            messageView = (ConversationMessageView) inflater.inflate(R.layout.conversation_message_item_sender_view, parent, false);
        } else if (viewType == VIEW_TYPE_MESSAGE_THIS_USER_OTHER_DATE) {
            messageView = (ConversationMessageView) inflater.inflate(R.layout.conversation_message_item_destination_other_date_view, parent, false);
        } else if (viewType == VIEW_TYPE_MESSAGE_OTHER_USERS_OTHER_DATE) {
            messageView = (ConversationMessageView) inflater.inflate(R.layout.conversation_message_item_sender_other_date_view, parent, false);
        } else {
            throw new DeveloperError("chat message error");
        }
        return new ConversationMessageViewHolder(messageView);
    }

    @Override
    public void onBindViewHolder(ConversationMessageViewHolder holder, int position) {
        holder.bind(chat.get(position));
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
                return chat.get(position).getDestination().equals(self) ? VIEW_TYPE_MESSAGE_THIS_USER_OTHER_DATE : VIEW_TYPE_MESSAGE_OTHER_USERS_OTHER_DATE;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return chat.get(position).getDestination().equals(self) ? VIEW_TYPE_MESSAGE_THIS_USER_OTHER_DATE : VIEW_TYPE_MESSAGE_OTHER_USERS_OTHER_DATE;
        }

        return chat.get(position).getDestination().equals(self) ? VIEW_TYPE_MESSAGE_THIS_USER : VIEW_TYPE_MESSAGE_OTHER_USERS;
    }

}
