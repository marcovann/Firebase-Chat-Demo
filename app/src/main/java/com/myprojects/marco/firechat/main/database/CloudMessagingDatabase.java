package com.myprojects.marco.firechat.main.database;

import com.myprojects.marco.firechat.user.data_model.User;

import rx.Observable;

/**
 * Created by marco on 17/08/16.
 */

public interface CloudMessagingDatabase {

    Observable<String> readToken(User user);

    void setToken(User user);

    void enableToken(String userId);

    void disableToken(String userId);

}
