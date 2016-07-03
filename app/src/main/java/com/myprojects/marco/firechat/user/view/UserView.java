package com.myprojects.marco.firechat.user.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.myprojects.marco.firechat.R;
import com.myprojects.marco.firechat.Utils;
import com.myprojects.marco.firechat.user.data_model.User;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by marco on 19/08/16.
 */

public class UserView extends FrameLayout {

    private TextView nameTextView;
    private CircleImageView profileImageView;

    private int layoutResId;

    public UserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            int[] attrsArray = {
                    android.R.attr.layout
            };
            TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
            layoutResId = array.getResourceId(0, R.layout.merge_users_item_view);
            array.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), layoutResId, this);
        nameTextView = (TextView) this.findViewById(R.id.nameTextView);
        profileImageView = (CircleImageView) this.findViewById(R.id.profileImageView);
    }

    public void display(User user) {
        Utils.loadImageElseBlack(user.getImage(),profileImageView,getContext());
        nameTextView.setText(user.getName());
    }

}

