package br.org.ipti.guigoh.util.websocket;

import br.org.ipti.guigoh.model.jpa.controller.UtilJpaController;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<Message> {

    @Override
    public void init(final EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public Message decode(final String textMessage) throws DecodeException {
        Message message = new Message();
        JsonObject obj = Json.createReader(new StringReader(textMessage))
                .readObject();
        message.setMessage(obj.getString("message"));
        message.setSender(obj.getString("sender"));
        message.setReceiver(obj.getString("receiver"));
        UtilJpaController utilJpaController = new UtilJpaController();
        Timestamp ts = utilJpaController.getTimestampServerTime();
        message.setReceived(ts);
        return message;
    }

    @Override
    public boolean willDecode(final String s) {
        return true;
    }
}
