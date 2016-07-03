package com.myprojects.marco.firechat.conversation_list.data_model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by marco on 29/07/16.
 */

public class Conversations {

    private final List<Conversation> conversations;

    public Conversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    public Conversation getConversationAt(int position) {
        return conversations.get(position);
    }

    public int size() {
        return conversations.size();
    }

    public void add(Conversation conversation) {
        if (!conversations.contains(conversation))
            conversations.add(conversation);
        else
            conversations.set(conversations.indexOf(conversation),conversation);
    }

    public Conversations sortedByDate() {
        List<Conversation> sortedList = new ArrayList<>(conversations);
        Collections.sort(sortedList,byDate());
        return new Conversations(sortedList);
    }

    private static Comparator<? super Conversation> byDate() {
        return new Comparator<Conversation>() {
            @Override
            public int compare(Conversation o1, Conversation o2) {
                return o2.getTime().compareTo(o1.getTime());
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Conversations conversations1 = (Conversations) o;

        return conversations != null ? conversations.equals(conversations1.conversations) : conversations1.conversations == null;
    }

    @Override
    public int hashCode() {
        return conversations != null ? conversations.hashCode() : 0;
    }
    
}
