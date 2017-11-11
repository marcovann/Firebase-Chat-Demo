package com.myprojects.marco.firechat.conversation.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myprojects.marco.firechat.Constants;
import com.myprojects.marco.firechat.conversation.data_model.Chat;
import com.myprojects.marco.firechat.conversation.data_model.Message;
import com.myprojects.marco.firechat.rx.FirebaseObservableListeners;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by marco on 29/07/16.
 */

public class FirebaseConversationDatabase implements ConversationDatabase {

    private static final int DEFAULT_LIMIT = 1000;
    private static final int PULL_LIMIT = 30;

    public static final String LAST_MESSAGE = "LAST";

    private final DatabaseReference userChat;
    private final FirebaseObservableListeners firebaseObservableListeners;

    public FirebaseConversationDatabase(FirebaseDatabase firebaseDatabase, FirebaseObservableListeners firebaseObservableListeners) {
        userChat = firebaseDatabase.getReference(Constants.FIREBASE_CHAT);
        this.firebaseObservableListeners = firebaseObservableListeners;
    }

    private DatabaseReference messagesOfUser(String self, String destination) {
        return userChat.child(self).child(destination).child(Constants.FIREBASE_CHAT_MESSAGES);
    }

    @Override
    public Observable<Chat> observeOldMessages(String self, String destination, String key) {
        if (key.equals(LAST_MESSAGE))
            return firebaseObservableListeners.listenToSingleValueEvents(messagesOfUser(self,destination).limitToLast(PULL_LIMIT), toChat());
        return firebaseObservableListeners.listenToSingleValueEvents(messagesOfUser(self,destination).endAt(null,key).limitToLast(PULL_LIMIT), toChat());
    }

    @Override
    public Observable<Message> observeNewMessages(String self, String destination, String key) {
        if (key.equals(""))
            return firebaseObservableListeners.listenToAddChildEvents(messagesOfUser(self,destination).limitToLast(1), toMessage());
        return firebaseObservableListeners.listenToAddChildEvents(messagesOfUser(self,destination).startAt(null,key).limitToFirst(DEFAULT_LIMIT), toMessage());
    }

    @Override
    public Observable<Message> observeLastMessage(String self, String destination) {
        return firebaseObservableListeners.listenToSingleValueEvents(messagesOfUser(self,destination).limitToLast(1), toLastMessage());
    }

    @Override
    public Observable<Chat> observeChat(String self, String destination) {
        return firebaseObservableListeners.listenToValueEvents(messagesOfUser(self,destination).limitToLast(DEFAULT_LIMIT), toChat());
    }

    private Func1<DataSnapshot, Chat> toChat() {
        return new Func1<DataSnapshot, Chat>() {
            @Override
            public Chat call(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                List<Message> messages = new ArrayList<>();
                for (DataSnapshot child : children) {
                    Message message = child.getValue(Message.class);
                    message.setId(child.getKey());
                    messages.add(message);
                }
                return new Chat(messages);
            }
        };
    }

    private Func1<DataSnapshot, Message> toLastMessage() {
        return new Func1<DataSnapshot, Message>() {
            @Override
            public Message call(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                List<Message> messages = new ArrayList<>();
                for (DataSnapshot child : children) {
                    Message message = child.getValue(Message.class);
                    messages.add(message);
                }
                return messages.get(0);
            }
        };
    }

    private Func1<DataSnapshot, Message> toMessage() {
        return new Func1<DataSnapshot, Message>() {
            @Override
            public Message call(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(Message.class);
            }
        };
    }

    @Override
    public void sendMessage(final String user, final Message message) {
        userChat.child(user).child(message.getDestination()).child(Constants.FIREBASE_CHAT_MESSAGES).push().setValue(message);
        userChat.child(message.getDestination()).child(user).child(Constants.FIREBASE_CHAT_MESSAGES).push().setValue(message);
    }

    @Override
    public Observable<Boolean> observeTyping(String self, String destination) {
        return firebaseObservableListeners.listenToValueEvents(userChat.child(destination).child(self).child(Constants.FIREBASE_CHAT_TYPING), asBoolean());
    }

    @Override
    public void setTyping(String self, String destination, Boolean value) {
        userChat.child(self).child(destination).child(Constants.FIREBASE_CHAT_TYPING).setValue(value);
    }

    private Func1<DataSnapshot,Boolean> asBoolean() {
        return new Func1<DataSnapshot, Boolean>() {
            @Override
            public Boolean call(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(Boolean.class);
            }
        };
    }
}
