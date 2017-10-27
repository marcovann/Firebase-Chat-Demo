package com.myprojects.marco.firechat.user.service;

import com.myprojects.marco.firechat.user.data_model.User;
import com.myprojects.marco.firechat.user.data_model.Users;

import java.util.List;

import rx.Observable;

/**
 * Created by marco on 31/07/16.
 */

public interface UserService {

    Observable<Users> syncUsers();

    Observable<User> getUser(String userId);

    Observable<Users> getUsers();

    void setName(User user, String name);

    void setProfileImage(User user, String image);

}