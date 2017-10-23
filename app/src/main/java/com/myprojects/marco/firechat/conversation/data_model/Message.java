package com.myprojects.marco.firechat.conversation.data_model;

import com.myprojects.marco.firechat.Utils;

/**
 * Created by marco on 03/07/16.
 */

public class Message {

    private String sender;
    private String destination;
    private String message;
    private String timestamp;
    private String id;

    public Message() {}

    public Message(String sender, String destination, String message) {
        this.sender = sender;
        this.destination = destination;
        this.message = message;
        this.timestamp = Utils.getCurrentTimestamp();
    }

    public String getSender() {
        return sender;
    }

    public String getDestination() {
        return destination;
    }

    public String getMessage() {
        return message;
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

        return this.message != null && this.message.equals(message.message)
                && timestamp != null && timestamp.equals(message.timestamp);
    }
}
