package com.myprojects.marco.firechat.conversation_list.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.myprojects.marco.firechat.R;
import com.myprojects.marco.firechat.conversation_list.data_model.Conversation;
import com.myprojects.marco.firechat.conversation_list.data_model.Conversations;

import java.util.ArrayList;

/**
 * Created by marco on 29/07/16.
 */

public class ConversationListAdapter extends RecyclerView.Adapter<ConversationViewHolder> {

    private Conversations conversations = new Conversations(new ArrayList<Conversation>());
    private ConversationListDisplayer.ConversationInteractionListener conversationInteractionListener;
    private final LayoutInflater inflater;

    ConversationListAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void update(Conversations conversations){
        this.conversations = conversations.sortedByDate();
        notifyDataSetChanged();
    }

    public void add(Conversation conversation) {
        this.conversations.add(conversation);
        this.conversations = this.conversations.sortedByDate();
        notifyDataSetChanged();
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConversationViewHolder((ConversationView) inflater.inflate(R.layout.conversation_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, int position) {
        holder.bind(conversations.getConversationAt(position), clickListener);
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    @Override
    public long getItemId(int position) {
        return conversations.getConversationAt(position).hashCode();
    }

    public void attach(ConversationListDisplayer.ConversationInteractionListener conversationInteractionListener) {
        this.conversationInteractionListener = conversationInteractionListener;
    }

    public void detach(ConversationListDisplayer.ConversationInteractionListener conversationInteractionListener) {
        this.conversationInteractionListener = null;
    }

    private final ConversationViewHolder.ConversationSelectionListener clickListener = new ConversationViewHolder.ConversationSelectionListener() {
        @Override
        public void onConversationSelected(Conversation conversation) {
            ConversationListAdapter.this.conversationInteractionListener.onConversationSelected(conversation);
        }
    };

}
