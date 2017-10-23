package com.myprojects.marco.firechat.conversation.service;

import com.myprojects.marco.firechat.conversation.data_model.Chat;
import com.myprojects.marco.firechat.conversation.data_model.Message;
import com.myprojects.marco.firechat.conversation.database.ConversationDatabase;
import com.myprojects.marco.firechat.database.DatabaseResult;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by marco on 29/07/16.
 */

public class PersistedConversationService implements ConversationService {

    private final ConversationDatabase conversationDatabase;

    public PersistedConversationService(ConversationDatabase conversationDatabase) {
        this.conversationDatabase = conversationDatabase;
    }

    @Override
    public Observable<Chat> getOldMessages(String self, String destination, String key) {
        return conversationDatabase.observeOldMessages(self,destination,key);
    }

    @Override
    public Observable<Message> getNewMessages(String self, String destination, String key) {
        return conversationDatabase.observeNewMessages(self,destination,key);
    }

    @Override
    public Observable<DatabaseResult<Chat>> getChat(String self, String destination) {
        return conversationDatabase.observeChat(self,destination)
                .map(asDatabaseResult())
                .onErrorReturn(DatabaseResult.<Chat>errorAsDatabaseResult());
    }

    private static Func1<Chat, DatabaseResult<Chat>> asDatabaseResult() {
        return new Func1<Chat, DatabaseResult<Chat>>() {
            @Override
            public DatabaseResult<Chat> call(Chat chat) {
                return new DatabaseResult<>(chat);
            }
        };
    }

    @Override
    public void sendMessage(String user, Message message) {
        conversationDatabase.sendMessage(user, message);
    }

    @Override
    public Observable<Boolean> getTyping(String self, String destination) {
        return conversationDatabase.observeTyping(self,destination);
    }

    @Override
    public void setTyping(String self, String destination, Boolean value) {
        conversationDatabase.setTyping(self,destination,value);
    }
}
