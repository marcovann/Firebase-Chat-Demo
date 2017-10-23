package com.myprojects.marco.firechat.global.data_model;

import com.myprojects.marco.firechat.Utils;

/**
 * Created by marco on 08/08/16.
 */

public class Message {

    private String uid;
    private String text;
    private String timestamp;
    private String id;

    public Message() {
    }

    public Message(String uid, String text) {
        this.uid = uid;
        this.text = text;
        this.timestamp = Utils.getCurrentTimestamp();
    }

    public String getUid() {
        return uid;
    }

    public String getText() {
        return text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Message message = (Message) o;

        return uid != null && uid.equals(message.uid) && text != null && text.equals(message.text) && timestamp != null && timestamp.equals(message.timestamp);
    }

    @Override
    public int hashCode() {
        int result = uid != null ? uid.hashCode() + text.hashCode() + timestamp.hashCode() : 0;
        result = 31 * result;
        return result;
    }

}
