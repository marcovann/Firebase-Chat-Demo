package com.myprojects.marco.firechat.global.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.myprojects.marco.firechat.R;
import com.myprojects.marco.firechat.Utils;
import com.myprojects.marco.firechat.global.data_model.Message;
import com.myprojects.marco.firechat.user.data_model.User;

import github.ankushsachdeva.emojicon.EmojiconTextView;

/**
 * Created by marco on 08/08/16.
 */

public class MessageView extends LinearLayout {

    private CircularImageView profileImageView;
    private TextView dateTextView;
    private EmojiconTextView messageTextView;
    private TextView messengerTextView;
    private TextView timestampTextView;
    private RelativeLayout timeLayout;

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
        this.profileImageView = (CircularImageView) this.findViewById(R.id.profileImageView);
        this.dateTextView = (TextView) this.findViewById(R.id.dateTextView);
        this.messageTextView = (EmojiconTextView) this.findViewById(R.id.messageTextView);
        this.messengerTextView = (TextView) this.findViewById(R.id.messengerTextView);
        this.timestampTextView = (TextView) this.findViewById(R.id.timeTextView);
        //this.timeLayout = (RelativeLayout) this.findViewById(R.id.timeLayout);
    }

    public void display(User user, final Message message) {

        Utils.loadImageElseBlack(user.getImage(),profileImageView,getContext());

        String timestamp = message.getTimestamp();
        if (dateTextView != null)
            dateTextView.setText(Utils.getDate(timestamp));
        messageTextView.setText(message.getText());
//        messageTextView.post(new Runnable() {
//            @Override
//            public void run() {
//                if (message.getText().length() > 33) {
//                    RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
//                            new RelativeLayout.LayoutParams(
//                                    RelativeLayout.LayoutParams.MATCH_PARENT,
//                                    RelativeLayout.LayoutParams.WRAP_CONTENT));
//                    timeLayout.setLayoutParams(relativeParams);
//                    timeLayout.requestLayout();
//                } else {
//                    RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
//                            new RelativeLayout.LayoutParams(
//                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
//                                    RelativeLayout.LayoutParams.WRAP_CONTENT));
//                    timeLayout.setLayoutParams(relativeParams);
//                    timeLayout.requestLayout();
//                }
//            }
//        });

        messengerTextView.setText(user.getName());
        timestampTextView.setText(Utils.getTimestamp(timestamp));
    }

}
