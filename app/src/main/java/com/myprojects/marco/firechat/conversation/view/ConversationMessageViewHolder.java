package com.myprojects.marco.firechat.conversation.view;

import android.support.v7.widget.RecyclerView;

import com.myprojects.marco.firechat.conversation.data_model.Message;

/**
 * Created by marco on 29/07/16.
 */

class ConversationMessageViewHolder extends RecyclerView.ViewHolder {

    private final ConversationMessageView conversationMessageView;

    public ConversationMessageViewHolder(ConversationMessageView messageView) {
        super(messageView);
        this.conversationMessageView = messageView;
    }

    public void bind(Message message) {
        conversationMessageView.display(message);
    }
}
