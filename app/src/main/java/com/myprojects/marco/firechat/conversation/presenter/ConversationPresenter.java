package com.myprojects.marco.firechat.conversation.presenter;

import com.myprojects.marco.firechat.conversation.data_model.Chat;
import com.myprojects.marco.firechat.conversation.data_model.Message;
import com.myprojects.marco.firechat.conversation.database.FirebaseConversationDatabase;
import com.myprojects.marco.firechat.conversation.service.ConversationService;
import com.myprojects.marco.firechat.conversation.view.ConversationDisplayer;
import com.myprojects.marco.firechat.login.service.LoginService;
import com.myprojects.marco.firechat.navigation.Navigator;
import com.myprojects.marco.firechat.user.data_model.User;
import com.myprojects.marco.firechat.user.service.UserService;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by marco on 29/07/16.
 */

public class ConversationPresenter {

    private final LoginService loginService;
    private final ConversationService conversationService;
    private final ConversationDisplayer conversationDisplayer;
    private final UserService userService;
    private final String self;
    private final String destination;
    private final Navigator navigator;

    private String firstKey = "";

    private Subscription subscription;
    private Subscription typingSubscription;

    public ConversationPresenter(
            LoginService loginService,
            ConversationService conversationService,
            ConversationDisplayer conversationDisplayer,
            UserService userService,
            String self,
            String destination,
            Navigator navigator
    ) {
        this.loginService = loginService;
        this.conversationService = conversationService;
        this.conversationDisplayer = conversationDisplayer;
        this.userService = userService;
        this.self = self;
        this.destination = destination;
        this.navigator = navigator;
    }

    public void startPresenting() {
        conversationDisplayer.attach(actionListener);
        conversationDisplayer.disableInteraction();

        subscription = userService.getUser(destination)
                .flatMap(getOldMessages())
                .flatMap(getNewMessages())
                .subscribe(newMessageSubscriber());

        typingSubscription = conversationService.getTyping(self,destination)
                .subscribe(isTypingSubscriber());
    }


    public void stopPresenting() {
        conversationDisplayer.detach(actionListener);
        conversationService.setTyping(self,destination,false);
        subscription.unsubscribe();
        typingSubscription.unsubscribe();
    }

    private boolean userIsAuthenticated() {
        return self != null;
    }

    private Func1<User, Observable<Chat>> getOldMessages() {
        return new Func1<User, Observable<Chat>>() {
            @Override
            public Observable<Chat> call(User user) {
                conversationDisplayer.setupToolbar(user.getName(),user.getImage(),user.getLastSeen());
                return conversationService.getOldMessages(self,destination,FirebaseConversationDatabase.LAST_MESSAGE);
            }
        };
    }

    private Func1<Chat, Observable<Message>> getNewMessages() {
        return new Func1<Chat, Observable<Message>>() {
            @Override
            public Observable<Message> call(Chat chat) {
                firstKey = chat.getFirstKey();
                conversationDisplayer.display(chat,self);
                return conversationService.getNewMessages(self,destination,chat.getLastKey());
            }
        };
    }

    private Subscriber<Message> newMessageSubscriber() {
        return new Subscriber<Message>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                conversationService.getNewMessages(self, destination, "")
                        .subscribe(newMessageSubscriber());
            }

            @Override
            public void onNext(Message message) {
                if (firstKey == null || firstKey.equals("")) firstKey = message.getId();
                conversationDisplayer.addToDisplay(message,self);
            }
        };
    }

    private Subscriber<Boolean> isTypingSubscriber() {
        return new Subscriber<Boolean>() {
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
        };
    }

    private Subscriber<Chat> oldMessagesSubscriber() {
        return new Subscriber<Chat>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Chat chat) {
                firstKey = chat.getFirstKey();
                conversationDisplayer.displayOldMessages(chat, self);
            }
        };
    }

    private final ConversationDisplayer.ConversationActionListener actionListener = new ConversationDisplayer.ConversationActionListener() {

        @Override
        public void onPullMessages() {
            if (firstKey != null)
                conversationService.getOldMessages(self,destination,firstKey)
                        .subscribe(oldMessagesSubscriber());
        }

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
        }

    };

}
