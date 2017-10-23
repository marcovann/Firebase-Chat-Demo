package com.myprojects.marco.firechat.global.presenter;

import android.util.Pair;

import com.myprojects.marco.firechat.global.data_model.Chat;
import com.myprojects.marco.firechat.global.data_model.Message;
import com.myprojects.marco.firechat.global.database.FirebaseGlobalDatabase;
import com.myprojects.marco.firechat.global.service.GlobalService;
import com.myprojects.marco.firechat.global.view.GlobalDisplayer;
import com.myprojects.marco.firechat.login.data_model.Authentication;
import com.myprojects.marco.firechat.login.service.LoginService;
import com.myprojects.marco.firechat.navigation.Navigator;
import com.myprojects.marco.firechat.user.data_model.User;
import com.myprojects.marco.firechat.user.data_model.Users;
import com.myprojects.marco.firechat.user.service.UserService;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by marco on 08/08/16.
 */

public class GlobalPresenter {

    private final LoginService loginService;
    private final GlobalService globalService;
    private final GlobalDisplayer globalDisplayer;
    private final UserService userService;
    private final Navigator navigator;

    private String currentKey;
    private User user;

    public GlobalPresenter(
            LoginService loginService,
            GlobalService globalService,
            GlobalDisplayer globalDisplayer,
            UserService userService,
            Navigator navigator
    ) {
        this.loginService = loginService;
        this.globalService = globalService;
        this.globalDisplayer = globalDisplayer;
        this.userService = userService;
        this.navigator = navigator;
    }

    public void startPresenting() {
        globalDisplayer.attach(actionListener);
        globalDisplayer.disableInteraction();

        loginService.getAuthentication()
                .flatMap(new Func1<Authentication, Observable<Chat>>() {
                    @Override
                    public Observable<Chat> call(Authentication authentication) {
                        user = authentication.getUser();
                        return globalService.getOldMessages(FirebaseGlobalDatabase.LAST_MESSAGE);
                    }
                })
                .flatMap(new Func1<Chat, Observable<Users>>() {
                             @Override
                             public Observable<Users> call(Chat chat) {
                                 return userService.getUsers();
                             }
                         }, new Func2<Chat, Users, Pair<Chat, Users>>() {
                            @Override
                            public Pair<Chat,Users> call(Chat chat, Users users) {
                                return new Pair<>(chat,users);
                            }
                         }
                )
                .flatMap(new Func1<Pair<Chat, Users>, Observable<Message>>() {
                    @Override
                    public Observable<Message> call(Pair<Chat, Users> pair) {
                        Chat chat = pair.first;
                        Users users = pair.second;
                        String key = chat.get(chat.size() - 1).getId();
                        currentKey = chat.getFirstKey();
                        globalDisplayer.display(chat,users,user);
                        return globalService.getNewMessages(key);
                    }
                })
                .flatMap(new Func1<Message, Observable<User>>() {
                             @Override
                             public Observable<User> call(Message message) {
                                 return userService.getUser(message.getUid());
                             }
                         }, new Func2<Message, User, Pair<Message, User>>() {
                            @Override
                            public  Pair<Message, User> call(Message message, User user) {
                                return new Pair<>(message,user);
                            }
                         }
                )
                .subscribe(new Subscriber<Pair<Message, User>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Pair<Message, User> pair) {
                        Message message = pair.first;
                        User sender = pair.second;
                        globalDisplayer.addToDisplay(message,sender,user);
                    }
                });
    }

    public void stopPresenting() {
        globalDisplayer.detach(actionListener);
    }

    private final GlobalDisplayer.GlobalActionListener actionListener = new GlobalDisplayer.GlobalActionListener() {

        @Override
        public void onPullMessages() {
            globalService.getOldMessages(currentKey)
                    .subscribe(new Subscriber<Chat>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Chat chat) {
                            currentKey = chat.getFirstKey();
                            if (chat.size() > 1)
                                globalDisplayer.displayOldMessages(chat,user);
                        }
                    });
        }

        @Override
        public void onUpPressed() {
            navigator.toParent();
        }

        @Override
        public void onMessageLengthChanged(int messageLength) {
            if (messageLength > 0) {
                globalDisplayer.enableInteraction();
            } else {
                globalDisplayer.disableInteraction();
            }
        }

        @Override
        public void onSubmitMessage(String message) {
            if (user != null)
                globalService.sendMessage(new Message(user.getUid(),message));
        }

    };

}
