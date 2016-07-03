package com.myprojects.marco.firechat.conversation_list.presenter;

import android.os.Bundle;

import com.myprojects.marco.firechat.conversation.data_model.Message;
import com.myprojects.marco.firechat.conversation_list.data_model.Conversation;
import com.myprojects.marco.firechat.conversation_list.service.ConversationListService;
import com.myprojects.marco.firechat.conversation_list.view.ConversationListDisplayer;
import com.myprojects.marco.firechat.login.data_model.Authentication;
import com.myprojects.marco.firechat.login.service.LoginService;
import com.myprojects.marco.firechat.navigation.AndroidConversationsNavigator;
import com.myprojects.marco.firechat.user.data_model.User;
import com.myprojects.marco.firechat.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by marco on 29/07/16.
 */

public class ConversationListPresenter {

    private static final String SENDER = "sender";
    private static final String DESTINATION = "destination";

    private ConversationListDisplayer conversationListDisplayer;
    private ConversationListService conversationListService;
    private AndroidConversationsNavigator navigator;
    private LoginService loginService;
    private UserService userService;

    private Subscription loginSubscription;
    private Subscription userSubscription;
    private Subscription messageSubscription;

    private List<String> uids;
    private User self;

    public ConversationListPresenter(
            ConversationListDisplayer conversationListDisplayer,
            ConversationListService conversationListService,
            AndroidConversationsNavigator navigator,
            LoginService loginService,
            UserService userService) {
        this.conversationListDisplayer = conversationListDisplayer;
        this.conversationListService = conversationListService;
        this.navigator = navigator;
        this.loginService = loginService;
        this.userService = userService;
    }

    public void startPresenting() {
        conversationListDisplayer.attach(conversationInteractionListener);

        final Subscriber userSubscriber = new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final User user) {
                messageSubscription = conversationListService.getLastMessageFor(self,user)
                        .subscribe(new Action1<Message>() {
                            @Override
                            public void call(Message message) {
                                conversationListDisplayer.addToDisplay(
                                        new Conversation(user.getUid(),user.getName(),user.getImage(),message.getMessage(),message.getTimestamp()));
                            }
                        });
            }

        };
        Subscriber usersSubscriber = new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<String> strings) {
                uids = new ArrayList<>(strings);
                for (String uid: uids) {
                    userSubscription = userService.getUser(uid)
                            .subscribe(userSubscriber);
                }
            }

        };

        loginSubscription = loginService.getAuthentication()
                .filter(successfullyAuthenticated())
                .doOnNext(new Action1<Authentication>() {
                    @Override
                    public void call(Authentication authentication) {
                        self = authentication.getUser();
                    }
                })
                .flatMap(conversationsForUser())
                .subscribe(usersSubscriber);
    }

    public void stopPresenting() {
        conversationListDisplayer.detach(conversationInteractionListener);
        loginSubscription.unsubscribe();
        if (userSubscription != null)
            userSubscription.unsubscribe();
        if (messageSubscription != null)
            messageSubscription.unsubscribe();
    }

    private Func1<Authentication, Observable<List<String>>> conversationsForUser() {
        return new Func1<Authentication, Observable<List<String>>>() {
            @Override
            public Observable<List<String>> call(Authentication authentication) {
                return conversationListService.getConversationsFor(self);
            }
        };
    }

    private Func1<Authentication, Boolean> successfullyAuthenticated() {
        return new Func1<Authentication, Boolean>() {
            @Override
            public Boolean call(Authentication authentication) {
                return authentication.isSuccess();
            }
        };
    }

    private final ConversationListDisplayer.ConversationInteractionListener conversationInteractionListener = new ConversationListDisplayer.ConversationInteractionListener() {

        @Override
        public void onConversationSelected(Conversation conversation) {
            Bundle bundle = new Bundle();
            bundle.putString(SENDER, self.getUid());
            bundle.putString(DESTINATION,conversation.getUid());
            navigator.toSelectedConversation(bundle);
        }

    };

}
