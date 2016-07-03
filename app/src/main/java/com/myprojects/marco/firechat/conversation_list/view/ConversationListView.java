package com.myprojects.marco.firechat.conversation_list.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.myprojects.marco.firechat.R;
import com.myprojects.marco.firechat.conversation_list.data_model.Conversation;
import com.myprojects.marco.firechat.conversation_list.data_model.Conversations;

/**
 * Created by marco on 29/07/16.
 */

public class ConversationListView extends LinearLayout implements ConversationListDisplayer {

    private final ConversationListAdapter conversationListAdapter;
    private Toolbar toolbar;
    private ConversationInteractionListener conversationInteractionListener;

    public ConversationListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        conversationListAdapter = new ConversationListAdapter(LayoutInflater.from(context));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_conversation_list_view, this);
        RecyclerView conversations = (RecyclerView) this.findViewById(R.id.conversationsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        conversations.setLayoutManager(layoutManager);
        conversations.setAdapter(conversationListAdapter);
    }

    @Override
    public void display(Conversations conversations) {
        conversationListAdapter.update(conversations);
    }

    @Override
    public void addToDisplay(Conversation conversation) {
        conversationListAdapter.add(conversation);
    }

    @Override
    public void attach(ConversationInteractionListener conversationInteractionListener) {
        this.conversationInteractionListener = conversationInteractionListener;
        conversationListAdapter.attach(conversationInteractionListener);
    }

    @Override
    public void detach(ConversationInteractionListener conversationInteractionListener) {
        conversationListAdapter.detach(conversationInteractionListener);
        this.conversationInteractionListener = null;
    }

}