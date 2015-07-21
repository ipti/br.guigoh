package br.org.ipti.guigoh.util.websocket;

import java.util.Date;

public class Message {

    private String message;
    private String sender;
    private String receiver;
    private Date received;

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

    public final Date getReceived() {
        return received;
    }

    public final void setReceived(final Date received) {
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
                + ", received=" + received + "]";
    }
}
