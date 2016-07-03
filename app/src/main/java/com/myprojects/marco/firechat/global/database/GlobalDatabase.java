package com.myprojects.marco.firechat.global.database;

import com.myprojects.marco.firechat.global.data_model.Chat;
import com.myprojects.marco.firechat.global.data_model.Message;

import rx.Observable;

/**
 * Created by marco on 08/08/16.
 */

public interface GlobalDatabase {

    Observable<Message> observeAddMessage();

    Observable<Chat> observeChat();

    void sendMessage(Message message);

}

