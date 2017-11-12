package com.myprojects.marco.firechat.profile.presenter;

import android.graphics.Bitmap;
import android.support.v4.util.Pair;
import android.widget.TextView;

import com.myprojects.marco.firechat.login.data_model.Authentication;
import com.myprojects.marco.firechat.login.service.LoginService;
import com.myprojects.marco.firechat.navigation.ProfileNavigator;
import com.myprojects.marco.firechat.profile.service.ProfileService;
import com.myprojects.marco.firechat.profile.view.ProfileDisplayer;
import com.myprojects.marco.firechat.storage.StorageService;
import com.myprojects.marco.firechat.user.data_model.User;
import com.myprojects.marco.firechat.user.service.UserService;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by marco on 09/09/16.
 */

public class ProfilePresenter {

    private final LoginService loginService;
    private final UserService userService;
    private final ProfileService profileService;
    private final StorageService storageService;
    private final ProfileDisplayer profileDisplayer;
    private final ProfileNavigator navigator;

    private User self;
    private Subscription loginSubscription;

    public ProfilePresenter(LoginService loginService,
                            UserService userService,
                            ProfileService profileService,
                            StorageService storageService,
                            ProfileDisplayer loginDisplayer,
                            ProfileNavigator navigator) {
        this.loginService = loginService;
        this.userService = userService;
        this.profileService = profileService;
        this.storageService = storageService;
        this.profileDisplayer = loginDisplayer;
        this.navigator = navigator;
    }

    public void startPresenting() {
        navigator.attach(dialogListener);
        profileDisplayer.attach(actionListener);
        loginSubscription = loginService.getAuthentication()
                .flatMap(getUser())
                .subscribe(userSubscriber());
    }

    public void stopPresenting() {
        navigator.detach(dialogListener);
        profileDisplayer.detach(actionListener);
        loginSubscription.unsubscribe();
    }

    private Func1<Authentication, Observable<User>> getUser() {
        return new Func1<Authentication, Observable<User>>() {
            @Override
            public Observable<User> call(Authentication authentication) {
                return userService.getUser(authentication.getUser().getUid());
            }
        };
    }

    private Subscriber<User> userSubscriber() {
        return new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(User user) {
                self = user;
                profileDisplayer.display(user);
            }
        };
    }

    private Func1<String, Observable<User>> getCurrentUser() {
        return new Func1<String, Observable<User>>() {
            @Override
            public Observable<User> call(String s) {
                profileDisplayer.onFinishUpload();
                return userService.getUser(self.getUid());
            }
        };
    }

    private Func2<String, User, Pair<String, User>> asPairImageUser() {
        return new Func2<String, User, Pair<String, User>>() {
            @Override
            public Pair<String, User> call(String s, User user) {
                return new Pair<>(s, user);
            }
        };
    }

    private Subscriber<Pair<String, User>> imageSubscriber(final Bitmap bitmap) {
        return new Subscriber<Pair<String, User>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Pair<String, User> pair) {
                String image = pair.first;
                User user = pair.second;
                if (user.getImage() != null)
                    storageService.removeImage(user.getImage());
                userService.setProfileImage(self, image);
                profileDisplayer.updateProfileImage(bitmap);
            }
        };
    }


    private ProfileDisplayer.ProfileActionListener actionListener = new ProfileDisplayer.ProfileActionListener() {

        @Override
        public void onUpPressed() {
            navigator.toParent();
        }

        @Override
        public void onNamePressed(String hint, TextView textView) {
            if (self != null)
                navigator.showInputTextDialog(hint,textView,self);
        }

        @Override
        public void onPasswordPressed(String hint) {
            navigator.showInputPasswordDialog(hint,self);
        }

        @Override
        public void onImagePressed() {
            navigator.showImagePicker();
        }

        @Override
        public void onRemovePressed() {
            navigator.showRemoveDialog();
        }

        @Override
        public void onStartUpload() {
            navigator.showProgressDialog();
        }

        @Override
        public void onFinishUpload() {
            navigator.dismissProgressDialog();
        }
    };

    private ProfileNavigator.ProfileDialogListener dialogListener = new ProfileNavigator.ProfileDialogListener() {

        @Override
        public void onNameSelected(String text, User user) {
            userService.setName(user,text);
        }

        @Override
        public void onPasswordSelected(String text) {
            profileService.setPassword(text);
        }

        @Override
        public void onRemoveSelected() {
            profileService.removeUser();
        }

        @Override
        public void onImageSelected(final Bitmap bitmap) {
            profileDisplayer.onStartUpload();
            storageService.uploadImage(bitmap)
                    .flatMap(getCurrentUser(), asPairImageUser())
                    .subscribe(imageSubscriber(bitmap));
        }

    };

}
