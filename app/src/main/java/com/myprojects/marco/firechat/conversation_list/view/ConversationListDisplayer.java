package com.myprojects.marco.firechat.conversation_list.view;

import com.myprojects.marco.firechat.conversation_list.data_model.Conversation;
import com.myprojects.marco.firechat.conversation_list.data_model.Conversations;

/**
 * Created by marco on 29/07/16.
 */

public interface ConversationListDisplayer {

    void display(Conversations conversations);

    void addToDisplay(Conversation conversation);

    void attach(ConversationInteractionListener conversationInteractionListener);

    void detach(ConversationInteractionListener conversationInteractionListener);

    interface ConversationInteractionListener {

        void onConversationSelected(Conversation conversation);

    }

}
