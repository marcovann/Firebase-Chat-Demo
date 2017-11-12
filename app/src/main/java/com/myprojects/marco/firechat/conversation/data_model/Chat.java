package com.myprojects.marco.firechat.conversation.data_model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by marco on 07/08/16.
 */

public class Chat {

    private final List<Message> messages;

    public Chat(List<Message> messages) {
        this.messages = messages;
    }

    public int size() {
        return messages.size();
    }

    public Message getMessage(int position) {
        return messages.get(position);
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public int addMessages(List<Message> messages) {
        int count;
        for (count = 0; count < messages.size() - 1; count++)
            this.messages.add(count, messages.get(count));
        return count;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public String getFirstKey() {
        return messages.get(0).getId();
    }

    public String getLastKey() {
        return messages.get(messages.size()-1).getId();
    }

    public Chat sortedByDate() {
        List<Message> sortedList = new ArrayList<>(messages);
        Collections.sort(sortedList,byDate());
        return new Chat(sortedList);
    }

    private static Comparator<? super Message> byDate() {
        return new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                long time1 = Long.parseLong(o1.getTimestamp().replace("/",""));
                long time2 = Long.parseLong(o2.getTimestamp().replace("/",""));
                return time1 < time2 ? -1: time1 > time2 ? 1: 0;
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

        Chat chat = (Chat) o;

        return messages != null ? messages.equals(chat.messages) : chat.messages == null;

    }

    @Override
    public int hashCode() {
        return messages != null ? messages.hashCode() : 0;
    }

}
