package com.myprojects.marco.firechat.global.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.myprojects.marco.firechat.R;
import com.myprojects.marco.firechat.global.data_model.Chat;
import com.myprojects.marco.firechat.global.data_model.Message;
import com.myprojects.marco.firechat.user.data_model.User;
import com.myprojects.marco.firechat.user.data_model.Users;

import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

/**
 * Created by marco on 08/08/16.
 */

public class GlobalView extends LinearLayout implements GlobalDisplayer {

    private final MessageAdapter messageAdapter;
    private EmojiconEditText messageEditText;
    private ImageButton sendButton;
    private RecyclerView messageRecyclerView;

    private EmojiconsPopup popup;
    private ImageButton emojiconButton;

    private GlobalActionListener actionListener;

    public GlobalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        messageAdapter = new MessageAdapter(LayoutInflater.from(context));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_global_view, this);

        View rootView = this.getRootView();
        popup = new EmojiconsPopup(rootView, getContext());
        popup.setSizeForSoftKeyboard();

        messageEditText = (EmojiconEditText) this.findViewById(R.id.messageEditText);
        sendButton = (ImageButton) this.findViewById(R.id.sendButton);
        emojiconButton = (ImageButton) this.findViewById(R.id.emoticonButton);

        messageRecyclerView = (RecyclerView) this.findViewById(R.id.messageRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        messageRecyclerView.setLayoutManager(layoutManager);
        messageRecyclerView.setAdapter(messageAdapter);

    }

    @Override
    public void display(Chat chat, Users users, User user) {
        messageAdapter.update(chat, users, user);
        int lastMessagePosition = messageAdapter.getItemCount() == 0 ? 0 : messageAdapter.getItemCount() - 1;
        messageRecyclerView.smoothScrollToPosition(lastMessagePosition);
    }

    @Override
    public void addToDisplay(Message message, User sender, User user) {
        messageAdapter.add(message, sender, user);
        int lastMessagePosition = messageAdapter.getItemCount() == 0 ? 0 : messageAdapter.getItemCount() - 1;
        messageRecyclerView.smoothScrollToPosition(lastMessagePosition);
    }

    @Override
    public void attach(GlobalActionListener globalActionListener) {
        this.actionListener = globalActionListener;
        messageEditText.addTextChangedListener(textWatcher);
        sendButton.setOnClickListener(submitClickListener);
        popup.setOnSoftKeyboardOpenCloseListener(softKeyboardOpenCloseListener);
        popup.setOnEmojiconClickedListener(emojiconClickedListener);
        popup.setOnEmojiconBackspaceClickedListener(emojiconBackspaceClickedListener);
        popup.setOnDismissListener(emojiDismissListener);
        emojiconButton.setOnClickListener(emojiClickListener);
    }

    @Override
    public void detach(GlobalActionListener globalActionListener) {
        sendButton.setOnClickListener(null);
        messageEditText.removeTextChangedListener(textWatcher);
        popup.setOnSoftKeyboardOpenCloseListener(null);
        popup.setOnEmojiconClickedListener(null);
        popup.setOnEmojiconBackspaceClickedListener(null);
        popup.setOnDismissListener(null);
        emojiconButton.setOnClickListener(null);
        this.actionListener = null;
    }

    @Override
    public void enableInteraction() {
        sendButton.setEnabled(true);
    }

    @Override
    public void disableInteraction() {
        sendButton.setEnabled(false);
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            actionListener.onMessageLengthChanged(s.toString().trim().length());
        }
    };

    private final OnClickListener submitClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            actionListener.onSubmitMessage(messageEditText.getText().toString().trim());
            messageEditText.setText("");
        }
    };

    private final EmojiconsPopup.OnSoftKeyboardOpenCloseListener softKeyboardOpenCloseListener = new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

        @Override
        public void onKeyboardOpen(int keyBoardHeight) {

        }

        @Override
        public void onKeyboardClose() {
            if(popup.isShowing())
                popup.dismiss();
        }
    };

    private final EmojiconGridView.OnEmojiconClickedListener emojiconClickedListener = new EmojiconGridView.OnEmojiconClickedListener() {

        @Override
        public void onEmojiconClicked(Emojicon emojicon) {
            if (messageEditText == null || emojicon == null) {
                return;
            }

            int start = messageEditText.getSelectionStart();
            int end = messageEditText.getSelectionEnd();
            if (start < 0) {
                messageEditText.append(emojicon.getEmoji());
            } else {
                messageEditText.getText().replace(Math.min(start, end),
                        Math.max(start, end), emojicon.getEmoji(), 0,
                        emojicon.getEmoji().length());
            }
        }
    };

    private final EmojiconsPopup.OnEmojiconBackspaceClickedListener emojiconBackspaceClickedListener = new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

        @Override
        public void onEmojiconBackspaceClicked(View v) {
            KeyEvent event = new KeyEvent(
                    0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
            messageEditText.dispatchKeyEvent(event);
        }
    };

    private final OnClickListener emojiClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            //If popup is not showing => emoji keyboard is not visible, we need to show it
            if(!popup.isShowing()){

                //If keyboard is visible, simply show the emoji popup
                if(popup.isKeyBoardOpen()){
                    popup.showAtBottom();
                    changeEmojiKeyboardIcon(emojiconButton, R.drawable.ic_menu_keyboard);
                }

                //else, open the text keyboard first and immediately after that show the emoji popup
                else{
                    messageEditText.setFocusableInTouchMode(true);
                    messageEditText.requestFocus();
                    popup.showAtBottomPending();
                    final InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(messageEditText, InputMethodManager.SHOW_IMPLICIT);
                    changeEmojiKeyboardIcon(emojiconButton, R.drawable.ic_menu_keyboard);
                }
            }

            //If popup is showing, simply dismiss it to show the undelying text keyboard
            else{
                popup.dismiss();
            }
        }
    };

    private final PopupWindow.OnDismissListener emojiDismissListener = new PopupWindow.OnDismissListener() {

        @Override
        public void onDismiss() {
            changeEmojiKeyboardIcon(emojiconButton, R.drawable.ic_menu_emoticon);
        }
    };

    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId){
        iconToBeChanged.setImageResource(drawableResourceId);
    }

}


