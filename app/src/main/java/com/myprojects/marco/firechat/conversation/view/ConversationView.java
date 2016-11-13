package com.myprojects.marco.firechat.conversation.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import com.mikhaellopez.circularimageview.CircularImageView;
import com.myprojects.marco.firechat.R;
import com.myprojects.marco.firechat.Utils;
import com.myprojects.marco.firechat.conversation.data_model.Chat;
import com.myprojects.marco.firechat.conversation.data_model.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

/**
 * Created by marco on 29/07/16.
 */

public class ConversationView extends LinearLayout implements ConversationDisplayer {

    private final ConversationMessageAdapter conversationMessageAdapter;
    private EmojiconEditText messageEditText;
    private ImageButton sendButton;
    private RecyclerView messageRecyclerView;

    private EmojiconsPopup popup;
    private ImageButton emojiconButton;

    private Toolbar toolbar;
    private CircularImageView profileImageView;
    private TextView nameTextView;
    private TextView lastSeenTextView;
    private TextView typingTextView;

    private ConversationActionListener actionListener;

    public ConversationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        conversationMessageAdapter = new ConversationMessageAdapter(LayoutInflater.from(context));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_conversation_view, this);

        View rootView = this.getRootView();
        popup = new EmojiconsPopup(rootView, getContext());
        popup.setSizeForSoftKeyboard();

        messageEditText = (EmojiconEditText) this.findViewById(R.id.messageEditText);
        sendButton = (ImageButton) this.findViewById(R.id.sendButton);
        emojiconButton = (ImageButton) this.findViewById(R.id.emoticonButton);

        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        profileImageView = (CircularImageView) toolbar.findViewById(R.id.profileImageView);
        nameTextView = (TextView) toolbar.findViewById(R.id.nameTextView);
        lastSeenTextView = (TextView) toolbar.findViewById(R.id.lastSeenTextView);
        typingTextView = (TextView) this.findViewById(R.id.typingTextView);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        messageRecyclerView = (RecyclerView) this.findViewById(R.id.messageRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        messageRecyclerView.setLayoutManager(layoutManager);
        messageRecyclerView.setAdapter(conversationMessageAdapter);

    }

    @Override
    public void display(Chat chat, String user) {
        conversationMessageAdapter.update(chat, user);
        int lastMessagePosition = conversationMessageAdapter.getItemCount() == 0 ? 0 : conversationMessageAdapter.getItemCount() - 1;
        messageRecyclerView.scrollToPosition(lastMessagePosition);
    }

    @Override
    public void addToDisplay(Message message, String user) {
        conversationMessageAdapter.add(message,user);
        int lastMessagePosition = conversationMessageAdapter.getItemCount() == 0 ? 0 : conversationMessageAdapter.getItemCount() - 1;
        messageRecyclerView.scrollToPosition(lastMessagePosition);
    }

    @Override
    public void setupToolbar(String user, String image, long lastSeen) {

        Utils.loadImageElseWhite(image,profileImageView,getContext());

        nameTextView.setText(user);
        if (lastSeen == 0)
            lastSeenTextView.setText(R.string.chat_toolbar_lastseen_online);
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
            Date resultdate = new Date(lastSeen);
            String timestamp = sdf.format(resultdate);
            String today = Utils.getCurrentTimestamp();
            String[] time1 = timestamp.split("/");
            String[] time2 = today.split("/");
            if ((time1[0]+time1[1]+time1[2]).equals(time2[0]+time2[1]+time2[2])) {
                String s = getResources().getString(R.string.chat_toolbar_lastseen_offline_today);
                lastSeenTextView.setText(s.replace("time",time1[3] + ":" + time1[4]));
            } else {
                String s = getResources().getString(R.string.chat_toolbar_lastseen_offline);
                s = s.replace("date",time1[2] + "/" + time1[1]);
                s = s.replace("time",time1[3] + ":" + time1[4]);
                lastSeenTextView.setText(s);
            }
        }

        typingTextView.setText(user + getResources().getString(R.string.chat_textview_typing));
    }

    @Override
    public void showTyping() {
        typingTextView.setVisibility(VISIBLE);
    }

    @Override
    public void hideTyping() {
        typingTextView.setVisibility(GONE);
    }

    @Override
    public void attach(ConversationActionListener conversationInteractionListener) {
        this.actionListener = conversationInteractionListener;
        messageEditText.addTextChangedListener(textWatcher);
        sendButton.setOnClickListener(submitClickListener);
        toolbar.setNavigationOnClickListener(navigationClickListener);
        popup.setOnSoftKeyboardOpenCloseListener(softKeyboardOpenCloseListener);
        popup.setOnEmojiconClickedListener(emojiconClickedListener);
        popup.setOnEmojiconBackspaceClickedListener(emojiconBackspaceClickedListener);
        popup.setOnDismissListener(emojiDismissListener);
        emojiconButton.setOnClickListener(emojiClickListener);
    }

    @Override
    public void detach(ConversationActionListener conversationInteractionListener) {
        sendButton.setOnClickListener(null);
        messageEditText.removeTextChangedListener(textWatcher);
        toolbar.setOnMenuItemClickListener(null);
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

    private final OnClickListener navigationClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            actionListener.onUpPressed();
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

            if(!popup.isShowing()){
                if(popup.isKeyBoardOpen()){
                    popup.showAtBottom();
                    changeEmojiKeyboardIcon(emojiconButton, R.drawable.ic_menu_keyboard);
                }

                else{
                    messageEditText.setFocusableInTouchMode(true);
                    messageEditText.requestFocus();
                    popup.showAtBottomPending();
                    final InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(messageEditText, InputMethodManager.SHOW_IMPLICIT);
                    changeEmojiKeyboardIcon(emojiconButton, R.drawable.ic_menu_keyboard);
                }
            } else{
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
