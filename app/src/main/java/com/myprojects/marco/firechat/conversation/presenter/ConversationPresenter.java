package com.myprojects.marco.firechat.conversation.presenter;

import com.myprojects.marco.firechat.conversation.data_model.Message;
import com.myprojects.marco.firechat.conversation.service.ConversationService;
import com.myprojects.marco.firechat.conversation.view.ConversationDisplayer;
import com.myprojects.marco.firechat.login.service.LoginService;
import com.myprojects.marco.firechat.navigation.Navigator;
import com.myprojects.marco.firechat.user.data_model.User;
import com.myprojects.marco.firechat.user.service.UserService;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by marco on 29/07/16.
 */

public class ConversationPresenter {

    private final LoginService loginService;
    private final ConversationService conversationService;
    private final ConversationDisplayer conversationDisplayer;
    private final UserService userService;
    //private final Analytics analytics;
    private final String self;
    private final String destination;
    private final Navigator navigator;

    private Subscription subscription;
    private Subscription chatSubscription;
    private Subscription typingSubscription;

    public ConversationPresenter(
            LoginService loginService,
            ConversationService conversationService,
            ConversationDisplayer conversationDisplayer,
            UserService userService,
            String self,
            String destination,
            //Analytics analytics,
            Navigator navigator//,
    ) {
        this.loginService = loginService;
        this.conversationService = conversationService;
        this.conversationDisplayer = conversationDisplayer;
        this.userService = userService;
        //this.analytics = analytics;
        this.self = self;
        this.destination = destination;
        this.navigator = navigator;
    }

    public void startPresenting() {
        conversationDisplayer.attach(actionListener);
        conversationDisplayer.disableInteraction();

        Subscriber conversationSubscriber = new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final User user) {
                conversationDisplayer.setupToolbar(user.getName(),user.getImage(),user.getLastSeen());
                chatSubscription = conversationService.syncMessages(self,destination)
                        .subscribe(new Subscriber<Message>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Message message) {
                                conversationDisplayer.addToDisplay(message,self);
                            }
                        });
            }
        };

        subscription = userService.getUser(destination)
                .subscribe(conversationSubscriber);

        typingSubscription = conversationService.getTyping(self,destination)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        conversationService.setTyping(self,destination,false);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean)
                            conversationDisplayer.showTyping();
                        else
                            conversationDisplayer.hideTyping();
                    }
                });
    }


    public void stopPresenting() {
        conversationDisplayer.detach(actionListener);
        conversationService.setTyping(self,destination,false);
        subscription.unsubscribe();
        if (typingSubscription != null)
            typingSubscription.unsubscribe();
        if (chatSubscription != null)
            chatSubscription.unsubscribe();
    }

    private boolean userIsAuthenticated() {
        return self != null;
    }

    private final ConversationDisplayer.ConversationActionListener actionListener = new ConversationDisplayer.ConversationActionListener() {

        @Override
        public void onUpPressed() {
            navigator.toParent();
        }

        @Override
        public void onMessageLengthChanged(int messageLength) {
            if (userIsAuthenticated() && messageLength > 0) {
                conversationDisplayer.enableInteraction();
                conversationService.setTyping(self,destination,true);
            } else {
                conversationDisplayer.disableInteraction();
                conversationService.setTyping(self,destination,false);
            }
        }

        @Override
        public void onSubmitMessage(String message) {
            conversationService.sendMessage(self, new Message(self, destination, message));
            //analytics.trackMessageLength(message.length(), self.getId(), channel.getName());
        }

    };

}
