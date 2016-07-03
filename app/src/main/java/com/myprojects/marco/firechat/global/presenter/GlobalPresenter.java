package com.myprojects.marco.firechat.global.presenter;

import com.myprojects.marco.firechat.global.data_model.Message;
import com.myprojects.marco.firechat.global.service.GlobalService;
import com.myprojects.marco.firechat.global.view.GlobalDisplayer;
import com.myprojects.marco.firechat.login.data_model.Authentication;
import com.myprojects.marco.firechat.login.service.LoginService;
import com.myprojects.marco.firechat.navigation.Navigator;
import com.myprojects.marco.firechat.user.data_model.User;
import com.myprojects.marco.firechat.user.service.UserService;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by marco on 08/08/16.
 */

public class GlobalPresenter {

    private final LoginService loginService;
    private final GlobalService globalService;
    private final GlobalDisplayer globalDisplayer;
    private final UserService userService;
    private final Navigator navigator;

    private User user;

    private Subscription subscription;

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

        final Subscriber messagesSubscriber = new Subscriber<Message>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final Message message) {
                userService.getUser(message.getUid())
                        .subscribe(new Subscriber<User>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(User sender) {
                                if (sender != null)
                                    globalDisplayer.addToDisplay(message,sender,user);
                            }
                        });
            }
        };

        loginService.getAuthentication()
                .subscribe(new Subscriber<Authentication>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Authentication authentication) {
                        if (authentication.isSuccess()) {
                            user = authentication.getUser();
                            subscription = globalService.syncMessages()
                                    .subscribe(messagesSubscriber);
                        }
                    }
                });

    }

    public void stopPresenting() {
        globalDisplayer.detach(actionListener);
        subscription.unsubscribe();
    }

    private final GlobalDisplayer.GlobalActionListener actionListener = new GlobalDisplayer.GlobalActionListener() {

        @Override
        public void onUpPressed() {
            navigator.toParent();
        }

        @Override
        public void onMessageLengthChanged(int messageLength) {
            if (/*userIsAuthenticated() && */messageLength > 0) {
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
