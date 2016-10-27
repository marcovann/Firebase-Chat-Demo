package com.myprojects.marco.firechat.global.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myprojects.marco.firechat.R;
import com.myprojects.marco.firechat.global.data_model.Chat;
import com.myprojects.marco.firechat.global.data_model.Message;
import com.myprojects.marco.firechat.user.data_model.User;
import com.myprojects.marco.firechat.user.data_model.Users;

import java.util.List;

/**
 * Created by marco on 08/08/16.
 */

public class GlobalView extends LinearLayout implements GlobalDisplayer {

    private final MessageAdapter messageAdapter;
    private TextView messageEditText;
    private ImageButton sendButton;
    private RecyclerView messageRecyclerView;

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

        messageEditText = (TextView) this.findViewById(R.id.messageEditText);
        sendButton = (ImageButton) this.findViewById(R.id.sendButton);

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
    }

    @Override
    public void detach(GlobalActionListener globalActionListener) {
        sendButton.setOnClickListener(null);
        messageEditText.removeTextChangedListener(textWatcher);
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

}


