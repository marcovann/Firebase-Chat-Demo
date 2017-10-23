package com.myprojects.marco.firechat.conversation.database;

import com.myprojects.marco.firechat.conversation.data_model.Chat;
import com.myprojects.marco.firechat.conversation.data_model.Message;

import rx.Observable;

/**
 * Created by marco on 29/07/16.
 */

public interface ConversationDatabase {

    Observable<Chat> observeOldMessages(String self, String destination, String key);

    Observable<Message> observeNewMessages(String self, String destination, String key);

    Observable<Message> observeLastMessage(String self, String destination);

    Observable<Chat> observeChat(String self, String destination);

    void sendMessage(String user, Message message);

    Observable<Boolean> observeTyping(String self, String destination);

    void setTyping(String self, String destination, Boolean value);

}
