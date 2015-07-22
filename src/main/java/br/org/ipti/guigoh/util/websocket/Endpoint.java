package br.org.ipti.guigoh.util.websocket;

import br.org.ipti.guigoh.model.entity.MessengerMessages;
import br.org.ipti.guigoh.model.jpa.controller.MessengerMessagesJpaController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.json.Json;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.json.JSONObject;

@ServerEndpoint(value = "/socket/{user}/{friends}", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class Endpoint {

    @OnOpen
    public void onOpen(final Session session, @PathParam("user") final String user, @PathParam("friends") final String friends) throws IOException, EncodeException {
        session.getUserProperties().put("user", user);
        session.getUserProperties().put("friends", friends);
        List<String> friendList = new ArrayList<>();
        List<String> onlineUsers = new ArrayList<>();
        String sessionFriends = (String) session.getUserProperties().get("friends");
        friendList.addAll(Arrays.asList(sessionFriends.split(",")));
        for (Session s : session.getOpenSessions()) {
            onlineUsers.add((String) s.getUserProperties().get("user"));
        }
        String json;
        for (Session s : session.getOpenSessions()) {
            if (!s.getUserProperties().get("user").equals(user)) {
                for (String id : friendList) {
                    if (onlineUsers.contains(id)) {
                        json = Json.createObjectBuilder()
                                .add("status", "online")
                                .add("id", id).build().toString();
                        session.getBasicRemote().sendObject(json);
                    }
                }
            }
            if (s.isOpen() && friendList.contains((String) s.getUserProperties().get("user"))) {
                json = Json.createObjectBuilder()
                        .add("status", "online")
                        .add("id", session.getUserProperties().get("user").toString()).build().toString();
                s.getBasicRemote().sendObject(json);
            }
        }
    }

    @OnMessage
    public void onMessage(final Session session, final Message message) throws Exception {
        MessengerMessages messengerMessages = new MessengerMessages();
        messengerMessages.setMessage(message.getMessage());
        messengerMessages.setSocialProfileIdReceiver(Integer.parseInt(message.getReceiver()));
        messengerMessages.setSocialProfileIdSender(Integer.parseInt(message.getSender()));
        messengerMessages.setMessageDate(message.getReceived());
        messengerMessages.setMessageDelivered('N');
        MessengerMessagesJpaController messengerMessagesJpaController = new MessengerMessagesJpaController();
        messengerMessagesJpaController.create(messengerMessages);
        try {
            for (Session s : session.getOpenSessions()) {
                if (s.isOpen()
                        && message.getReceiver().equals(s.getUserProperties().get("user"))) {
                    messengerMessages.setMessageDelivered('Y');
                    messengerMessagesJpaController.edit(messengerMessages);
                    s.getBasicRemote().sendObject(message);
                }
            }
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Throwable t) {
    }

    @OnClose
    public void onClose(final Session session) throws IOException, EncodeException {
        String user = (String) session.getUserProperties().get("user");
        String friends = (String) session.getUserProperties().get("friends");
        List<String> friendList = new ArrayList<>();
        friendList.addAll(Arrays.asList(friends.split(",")));
        String json;
        int count = 0;
        for (Session s : session.getOpenSessions()) {
            if (s.getUserProperties().get("user").equals(user)) {
                count++;
            }
        }
        for (Session s : session.getOpenSessions()) {
            if (s.isOpen() && friendList.contains((String) s.getUserProperties().get("user"))) {
                if (count == 0) {
                    json = Json.createObjectBuilder()
                            .add("status", "offline")
                            .add("id", session.getUserProperties().get("user").toString()).build().toString();
                    s.getBasicRemote().sendObject(json);
                }
            }
        }
    }
}
