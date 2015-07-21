package br.org.ipti.guigoh.util.websocket;

import java.io.StringReader;
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
                message.setSender(obj.getString("receiver"));
		message.setReceived(new Date());
		return message;
	}

	@Override
	public boolean willDecode(final String s) {
		return true;
	}
}
