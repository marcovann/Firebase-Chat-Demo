package com.myprojects.marco.firechat.global.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myprojects.marco.firechat.R;
import com.myprojects.marco.firechat.Utils;
import com.myprojects.marco.firechat.global.data_model.Message;
import com.myprojects.marco.firechat.user.data_model.User;

import de.hdodenhof.circleimageview.CircleImageView;
import github.ankushsachdeva.emojicon.EmojiconTextView;

/**
 * Created by marco on 08/08/16.
 */

public class MessageView extends LinearLayout {

    private CircleImageView profileImageView;
    private TextView dateTextView;
    private EmojiconTextView messageTextView;
    private TextView messengerTextView;
    private TextView timestampTextView;

    private int layoutResId;

    public MessageView(Context context, AttributeSet attrs) {
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
            layoutResId = array.getResourceId(0, R.layout.merge_global_message_item_destination);
            array.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), layoutResId, this);
        this.profileImageView = (CircleImageView) this.findViewById(R.id.profileImageView);
        this.dateTextView = (TextView) this.findViewById(R.id.dateTextView);
        this.messageTextView = (EmojiconTextView) this.findViewById(R.id.messageTextView);
        this.messengerTextView = (TextView) this.findViewById(R.id.messengerTextView);
        this.timestampTextView = (TextView) this.findViewById(R.id.timeTextView);
    }

    public void display(User user, final Message message) {

        Utils.loadImageElseBlack(user.getImage(),profileImageView,getContext());

        String timestamp = message.getTimestamp();
        if (dateTextView != null)
            dateTextView.setText(Utils.getDate(timestamp));
        messageTextView.setText(message.getText());

        messengerTextView.setText(user.getName());
        timestampTextView.setText(Utils.getTimestamp(timestamp));
    }

}
