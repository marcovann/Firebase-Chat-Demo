package com.myprojects.marco.firechat.global.view;

import android.support.v7.widget.RecyclerView;

import com.myprojects.marco.firechat.global.data_model.Message;
import com.myprojects.marco.firechat.user.data_model.User;

/**
 * Created by marco on 08/08/16.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {

    private final MessageView messageView;

    public MessageViewHolder(MessageView messageView) {
        super(messageView);
        this.messageView = messageView;
    }

    public void bind(User user, Message message) {
        messageView.display(user, message);
    }
}
