package br.org.ipti.guigoh.util.websocket;

import br.org.ipti.guigoh.model.entity.MessengerMessages;
import br.org.ipti.guigoh.model.jpa.controller.MessengerMessagesJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UtilJpaController;
import br.org.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.websocket.DecodeException;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/socket/{user}/{friends}")
public class MessengerEndpoint {

    @OnOpen
    public void onOpen(final Session session, @PathParam("user") final String user, @PathParam("friends") final String friends) throws IOException, EncodeException, Exception {
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
        json = Json.createObjectBuilder().add("offlineMessages", loadOfflineMessages(user)).build().toString();
        session.getBasicRemote().sendObject(json);
        for (Session s : session.getOpenSessions()) {
            if (!s.getUserProperties().get("user").equals(user)) {
                for (String friendId : friendList) {
                    if (onlineUsers.contains(friendId)) {
                        json = Json.createObjectBuilder()
                                .add("status", "online")
                                .add("id", friendId).build().toString();
                        session.getBasicRemote().sendObject(json);
                    }
                }
            }
            if (s.isOpen() && friendList.contains((String) s.getUserProperties().get("user"))) {
                json = Json.createObjectBuilder()
                        .add("status", "online")
                        .add("id", user).build().toString();
                s.getBasicRemote().sendObject(json);
            }
        }
    }

    @OnMessage
    public void onMessage(final Session session, final String textMessage) throws Exception {
        MessengerMessagesJpaController messengerMessagesJpaController = new MessengerMessagesJpaController();
        SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
        UtilJpaController utilJpaController = new UtilJpaController();
        JsonObject obj = Json.createReader(new StringReader(textMessage))
                .readObject();
        if (obj.getString("type").equals("NEW_MSG")) {
            MessengerMessages messengerMessages = new MessengerMessages();
            messengerMessages.setMessage(obj.getString("message"));
            messengerMessages.setSocialProfileIdReceiver(Integer.parseInt(obj.getString("receiverId")));
            messengerMessages.setSocialProfileIdSender(Integer.parseInt(obj.getString("senderId")));
            Timestamp ts = utilJpaController.getTimestampServerTime();
            messengerMessages.setMessageDate(ts);
            messengerMessages.setMessageDelivered('N');
            messengerMessagesJpaController.create(messengerMessages);
            for (Session s : session.getOpenSessions()) {
                if (s.isOpen()
                        && obj.getString("receiverId").equals(s.getUserProperties().get("user"))) {
                    s.getBasicRemote().sendObject(Json.createObjectBuilder()
                            .add("message", obj.getString("message"))
                            .add("senderId", obj.getString("senderId"))
                            .add("senderName", socialProfileJpaController.findSocialProfileBySocialProfileId(Integer.parseInt(obj.getString("senderId"))).getName())
                            .add("receiverId", obj.getString("receiverId"))
                            .add("receiverName", socialProfileJpaController.findSocialProfileBySocialProfileId(Integer.parseInt(obj.getString("receiverId"))).getName())
                            .add("received", ts.toString())
                            .add("type", obj.getString("type")).build()
                            .toString());
                }
            }
        } else if (obj.getString("type").equals("MSG_SENT")) {
            List<MessengerMessages> messengerMessagesList = messengerMessagesJpaController.getNonReadMessages(Integer.parseInt(obj.getString("receiverId")));
            for (MessengerMessages messengerMessages : messengerMessagesList) {
                messengerMessages.setMessageDelivered('Y');
                messengerMessagesJpaController.edit(messengerMessages);
            }
        }
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

    private String loadOfflineMessages(String id) throws RollbackFailureException, Exception {
        MessengerMessagesJpaController messengerMessagesJpaController = new MessengerMessagesJpaController();
        SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
        List<MessengerMessages> messengerMessagesList = messengerMessagesJpaController.getNonReadMessages(Integer.parseInt(id));
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (MessengerMessages messengerMessages : messengerMessagesList) {
            jsonArrayBuilder.add(Json.createObjectBuilder()
                    .add("message", messengerMessages.getMessage())
                    .add("senderId", messengerMessages.getSocialProfileIdSender())
                    .add("senderName", socialProfileJpaController.findSocialProfileBySocialProfileId(messengerMessages.getSocialProfileIdSender()).getName())
                    .add("receiverId", messengerMessages.getSocialProfileIdReceiver())
                    .add("receiverName", socialProfileJpaController.findSocialProfileBySocialProfileId(messengerMessages.getSocialProfileIdReceiver()).getName())
                    .add("received", messengerMessages.getMessageDate().toString())
                    .add("type", "NEW_MSG"));
        }
        JsonArray jsonArray = jsonArrayBuilder.build();
        return jsonArray.toString();
    }
}
