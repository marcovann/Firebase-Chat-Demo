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

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.FuncN;

/**
 * Created by marco on 08/08/16.
 */

public class GlobalPresenter {

    private final LoginService loginService;
    private final GlobalService globalService;
    private final GlobalDisplayer globalDisplayer;
    private final UserService userService;
    private final Navigator navigator;

    private String firstKey = "";
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
                .flatMap(getOldMessages())
                .flatMap(getUsers(), asPairChatUsers())
                .subscribe(new Subscriber<Pair<Chat, Users>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        globalService.getNewMessages("")
                                .flatMap(getUser(), asPairMessageUser())
                                .subscribe(newMessagesSubscriber());
                    }

                    @Override
                    public void onNext(Pair<Chat, Users> pair) {
                        Chat chat = pair.first;
                        Users users = pair.second;
                        globalDisplayer.display(chat, users, user);
                        globalService.getNewMessages(chat.getLastKey())
                                .flatMap(getUser(), asPairMessageUser())
                                .subscribe(newMessagesSubscriber());
                    }
                });

    }

    public void stopPresenting() {
        globalDisplayer.detach(actionListener);
    }

    private Func1<Authentication, Observable<Chat>> getOldMessages() {
        return new Func1<Authentication, Observable<Chat>>() {
            @Override
            public Observable<Chat> call(Authentication authentication) {
                user = authentication.getUser();
                return globalService.getOldMessages(FirebaseGlobalDatabase.LAST_MESSAGE);
            }
        };
    }

    private Func1<Chat, Observable<Users>> getUsers() {
        return new Func1<Chat, Observable<Users>>() {
            @Override
            public Observable<Users> call(Chat chat) {
                firstKey = chat.getFirstKey();
                List<Observable<User>> list = new ArrayList<>();
                for (Message m : chat.getMessages())
                    list.add(userService.getUser(m.getUid()));

                return Observable.zip(list, new FuncN<Users>() {
                    @Override
                    public Users call(Object... args) {
                        ArrayList<User> users = new ArrayList<>();
                        for (Object o: args) users.add((User)o);
                        return new Users(users);
                    }
                });
            }
        };
    }

    private Func2<Chat, Users, Pair<Chat, Users>> asPairChatUsers() {
        return new Func2<Chat, Users, Pair<Chat, Users>>() {
            @Override
            public Pair<Chat, Users> call(Chat chat, Users users) {
                return new Pair<>(chat, users);
            }
        };
    }

    private Func1<Message, Observable<User>> getUser() {
        return new Func1<Message, Observable<User>>() {
            @Override
            public Observable<User> call(Message message) {
                return userService.getUser(message.getUid());
            }
        };
    }

    private Func2<Message, User, Pair<Message,User>> asPairMessageUser() {
        return new Func2<Message, User, Pair<Message,User>>() {
            @Override
            public Pair<Message, User> call(Message message, User user) {
                return new Pair<>(message, user);
            }
        };
    }

    private Subscriber<Pair<Message, User>> newMessagesSubscriber() {
        return new Subscriber<Pair<Message, User>>() {
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

                if (firstKey == null || firstKey.equals(""))
                    firstKey = message.getId();

                globalDisplayer.addToDisplay(message, sender, user);
            }
        };
    }

    private Subscriber<Pair<Chat, Users>> oldMessagesSubscriber() {
        return new Subscriber<Pair<Chat, Users>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Pair<Chat, Users> pair) {
                Chat chat = pair.first;
                Users users = pair.second;
                firstKey = chat.getFirstKey();
                globalDisplayer.displayOldMessages(chat, users, user);
            }
        };
    }

    private final GlobalDisplayer.GlobalActionListener actionListener = new GlobalDisplayer.GlobalActionListener() {

        @Override
        public void onPullMessages() {
            if (firstKey != null)
                globalService.getOldMessages(firstKey)
                        .flatMap(getUsers(), asPairChatUsers())
                        .subscribe(oldMessagesSubscriber());
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
