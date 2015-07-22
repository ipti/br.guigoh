package br.org.ipti.guigoh.util.websocket;

import java.sql.Timestamp;

public class Message {

    private String message;
    private String sender;
    private String receiver;
    private Timestamp received;

    public final String getMessage() {
        return message;
    }

    public final void setMessage(final String message) {
        this.message = message;
    }

    public final String getSender() {
        return sender;
    }

    public final void setSender(final String sender) {
        this.sender = sender;
    }

    public final Timestamp getReceived() {
        return received;
    }

    public final void setReceived(final Timestamp received) {
        this.received = received;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Override
    public String toString() {
        return "Message [message=" + message + ", sender=" + sender
                + ", receiver=" + receiver + ", received=" + received + "]";
    }
}
