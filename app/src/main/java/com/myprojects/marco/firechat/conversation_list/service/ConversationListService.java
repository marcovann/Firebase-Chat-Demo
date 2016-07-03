package com.myprojects.marco.firechat.conversation_list.service;

import com.myprojects.marco.firechat.conversation.data_model.Message;
import com.myprojects.marco.firechat.user.data_model.User;
import com.myprojects.marco.firechat.user.data_model.Users;

import java.util.List;

import rx.Observable;

/**
 * Created by marco on 29/07/16.
 */

public interface ConversationListService {

    Observable<Message> getLastMessageFor(User self, User destination);

    Observable<List<String>> getConversationsFor(User user);

    Observable<Users> getUsers(List<String> usersId);

}
