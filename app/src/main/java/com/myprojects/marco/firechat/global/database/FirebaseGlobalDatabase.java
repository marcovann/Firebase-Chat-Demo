package com.myprojects.marco.firechat.global.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myprojects.marco.firechat.Constants;
import com.myprojects.marco.firechat.global.data_model.Chat;
import com.myprojects.marco.firechat.global.data_model.Message;
import com.myprojects.marco.firechat.rx.FirebaseObservableListeners;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by marco on 08/08/16.
 */

public class FirebaseGlobalDatabase implements GlobalDatabase {

    private static final int DEFAULT_LIMIT = 1000;
    private static final int PULL_LIMIT = 30;

    public static final String LAST_MESSAGE = "LAST";

    private final DatabaseReference globalMessages;
    private final FirebaseObservableListeners firebaseObservableListeners;

    public FirebaseGlobalDatabase(FirebaseDatabase firebaseDatabase, FirebaseObservableListeners firebaseObservableListeners) {
        globalMessages = firebaseDatabase.getReference(Constants.FIREBASE_GLOBALMESSAGES);
        this.firebaseObservableListeners = firebaseObservableListeners;
    }

    @Override
    public Observable<Chat> observeOldMessages(String key) {
        if (key.equals(LAST_MESSAGE))
            return firebaseObservableListeners.listenToSingleValueEvents(globalMessages.limitToLast(PULL_LIMIT), toChat());
        return firebaseObservableListeners.listenToSingleValueEvents(globalMessages.endAt(null,key).limitToLast(PULL_LIMIT), toChat());
    }

    @Override
    public Observable<Message> observeNewMessages(String key) {
        if (key.equals(""))
            return firebaseObservableListeners.listenToAddChildEvents(globalMessages.limitToLast(DEFAULT_LIMIT), toMessage());
        return firebaseObservableListeners.listenToAddChildEvents(globalMessages.startAt(null,key).limitToLast(DEFAULT_LIMIT), toMessage());
    }

    @Override
    public Observable<Chat> observeChat() {
        return firebaseObservableListeners.listenToSingleValueEvents(globalMessages.limitToLast(DEFAULT_LIMIT), toChat());
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

    private Func1<DataSnapshot, Message> toMessage() {
        return new Func1<DataSnapshot, Message>() {
            @Override
            public Message call(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(Message.class);
            }
        };
    }

    @Override
    public void sendMessage(Message message) {
        globalMessages.push().setValue(message);
    }
}
