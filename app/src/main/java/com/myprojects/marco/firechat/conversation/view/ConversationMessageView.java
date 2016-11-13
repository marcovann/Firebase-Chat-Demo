package com.myprojects.marco.firechat.conversation.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myprojects.marco.firechat.R;
import com.myprojects.marco.firechat.Utils;
import com.myprojects.marco.firechat.conversation.data_model.Message;

import github.ankushsachdeva.emojicon.EmojiconTextView;

/**
 * Created by marco on 29/07/16.
 */

public class ConversationMessageView extends LinearLayout {

    private TextView dateTextView;
    private EmojiconTextView messageTextView;
    private TextView timestampTextView;

    private int layoutResId;

    public ConversationMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            int[] attrsArray = {
                    android.R.attr.layout
            };
            TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
            layoutResId = array.getResourceId(0, R.layout.merge_conversation_message_item_destination);
            array.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), layoutResId, this);
        this.dateTextView = (TextView) this.findViewById(R.id.dateTextView);
        this.messageTextView = (EmojiconTextView) this.findViewById(R.id.message);
        this.timestampTextView = (TextView) this.findViewById(R.id.time);
    }

    public void display(final Message message) {
        final String timestamp = message.getTimestamp();
        if (dateTextView != null)
            dateTextView.setText(Utils.getDate(timestamp));
        messageTextView.setText(message.getMessage());
        timestampTextView.setText(Utils.getTimestamp(timestamp));
    }

}
