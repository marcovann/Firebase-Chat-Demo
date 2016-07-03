package com.myprojects.marco.firechat.conversation.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myprojects.marco.firechat.R;
import com.myprojects.marco.firechat.Utils;
import com.myprojects.marco.firechat.conversation.data_model.Message;

/**
 * Created by marco on 29/07/16.
 */

public class ConversationMessageView extends LinearLayout {

    private TextView dateTextView;
    private TextView messageTextView;
    private TextView timestampTextView;

    private int layoutResId;

    public ConversationMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        super.setOrientation(VERTICAL);
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
        this.messageTextView = (TextView) this.findViewById(R.id.message);
        this.timestampTextView = (TextView) this.findViewById(R.id.time);
    }

    public void display(final Message message) {
        String timestamp = message.getTimestamp();
        if (dateTextView != null)
            dateTextView.setText(Utils.getDate(timestamp));
        messageTextView.setText(message.getMessage());
        messageTextView.post(new Runnable() {
            @Override
            public void run() {
                if (messageTextView.getLineCount() == 1) {
                    if (message.getMessage().length() > 33) {
                        messageTextView.setText(message.getMessage() + "\n");
                    } else {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) timestampTextView.getLayoutParams();
                        params.addRule(RelativeLayout.END_OF, R.id.message);
                        params.removeRule(RelativeLayout.ALIGN_END);
                        timestampTextView.setLayoutParams(params);
                    }
                } else {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) timestampTextView.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_END, R.id.message);
                    params.removeRule(RelativeLayout.END_OF);
                    timestampTextView.setLayoutParams(params);
                }
            }
        });
        timestampTextView.setText(Utils.getTimestamp(timestamp));
    }

}
