package com.myprojects.marco.firechat.conversation_list.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.myprojects.marco.firechat.R;
import com.myprojects.marco.firechat.Utils;
import com.myprojects.marco.firechat.conversation_list.data_model.Conversation;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by marco on 29/07/16.
 */

public class ConversationView extends FrameLayout {

    private TextView nameTextView;
    private TextView messageTextView;
    private TextView timeTextView;
    private CircleImageView profileImageView;

    private int layoutResId;

    public ConversationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            int[] attrsArray = {
                    android.R.attr.layout
            };
            TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
            layoutResId = array.getResourceId(0, R.layout.merge_conversation_list_item_view);
            array.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), layoutResId, this);
        nameTextView = (TextView) this.findViewById(R.id.nameTextView);
        messageTextView = (TextView) this.findViewById(R.id.messageTextView);
        timeTextView = (TextView) this.findViewById(R.id.timeTextView);
        profileImageView = (CircleImageView) this.findViewById(R.id.profileImageView);
    }

    public void display(Conversation conversation) {
        Utils.loadImageElseBlack(conversation.getImage(),profileImageView,getContext());
        nameTextView.setText(conversation.getName());
        messageTextView.setText(conversation.getMessage());

        String time = Utils.getTimestamp(conversation.getTime());
        String date = Utils.getDate(conversation.getTime());
        String today = Utils.getCurrentTimestamp();
        String[] time1 = date.split("/");
        String[] time2 = today.split("/");
        if ((time1[2]+time1[1]+time1[0]).equals(time2[0]+time2[1]+time2[2])) {
            timeTextView.setText(time + "\n\n" + getContext().getString(R.string.conversations_conversation_item_today));
        } else {
            timeTextView.setText(date);
        }
    }

}