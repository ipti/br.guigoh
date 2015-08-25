package br.org.ipti.guigoh.util.websocket;

import br.org.ipti.guigoh.model.entity.MessengerMessages;
import br.org.ipti.guigoh.model.entity.UserAuthorization;
import br.org.ipti.guigoh.model.jpa.controller.MessengerMessagesJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UserAuthorizationJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UtilJpaController;
import br.org.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/socket/{user}/{friends}")
public class MessengerEndpoint {

    private static final String ADMIN = "AD";
    private final UserAuthorizationJpaController userAuthorizationJpaController;
    private final SocialProfileJpaController socialProfileJpaController;
    private final MessengerMessagesJpaController messengerMessagesJpaController;

    public MessengerEndpoint() {
        userAuthorizationJpaController = new UserAuthorizationJpaController();
        socialProfileJpaController = new SocialProfileJpaController();
        messengerMessagesJpaController = new MessengerMessagesJpaController();
    }

    @OnOpen
    public void onOpen(final Session session, @PathParam("user") final String user, @PathParam("friends") final String friends) throws IOException, EncodeException, Exception {
        session.getUserProperties().put("user", user);
        session.getUserProperties().put("friends", friends);
        List<String> friendList = new ArrayList<>();
        List<String> onlineUsers = new ArrayList<>();
        String sessionFriends = (String) session.getUserProperties().get("friends");
        friendList.addAll(Arrays.asList(sessionFriends.split(",")));
        String json;
        for (Session s : session.getOpenSessions()) {
            if (!onlineUsers.contains((String) s.getUserProperties().get("user"))) {
                onlineUsers.add((String) s.getUserProperties().get("user"));
            }
        }
        String offlineMessages = loadAllFriendIdFromOfflineMessages(user);
        if (offlineMessages != null) {
            json = Json.createObjectBuilder().add("offlineMessages", offlineMessages).build().toString();
            session.getBasicRemote().sendObject(json);
        }
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
            UserAuthorization userAuthorization = userAuthorizationJpaController.findUserAuthorization(socialProfileJpaController.findSocialProfileBySocialProfileId(Integer.parseInt((String) s.getUserProperties().get("user"))).getTokenId());
            if (userAuthorization.getRoles().equals(ADMIN)) {
                json = Json.createObjectBuilder()
                        .add("onlineUsers", onlineUsers.size()).build().toString();
                s.getBasicRemote().sendObject(json);
            }
        }
    }

    @OnMessage
    public void onMessage(final Session session, final String textMessage) throws Exception {
        UtilJpaController utilJpaController = new UtilJpaController();
        JsonObject obj = Json.createReader(new StringReader(textMessage))
                .readObject();
        switch (obj.getString("type")) {
            case "NEW_MSG":
                MessengerMessages messengerMessages = new MessengerMessages();
                messengerMessages.setMessage(obj.getString("message"));
                messengerMessages.setSocialProfileIdReceiver(Integer.parseInt(obj.getString("receiverId")));
                messengerMessages.setSocialProfileIdSender(Integer.parseInt(obj.getString("senderId")));
                Timestamp ts = utilJpaController.getTimestampServerTime();
                String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(ts);
                messengerMessages.setMessageDate(ts);
                messengerMessages.setMessageDelivered('N');
                messengerMessagesJpaController.create(messengerMessages);
                for (Session s : session.getOpenSessions()) {
                    if (s.isOpen() && obj.getString("receiverId").equals(s.getUserProperties().get("user"))) {
                        sendObject(s, obj, false, date);
                    } else if (s.isOpen() && obj.getString("senderId").equals(s.getUserProperties().get("user"))) {
                        sendObject(s, obj, true, date);
                    }
                }
                break;
            case "MSG_SENT":
                List<MessengerMessages> messengerMessagesList = messengerMessagesJpaController.getFriendNonReadMessages(Integer.parseInt(obj.getString("receiverId")), Integer.parseInt(obj.getString("senderId")));
                for (MessengerMessages mm : messengerMessagesList) {
                    mm.setMessageDelivered('Y');
                    messengerMessagesJpaController.edit(mm);
                }
                for (Session s : session.getOpenSessions()) {
                    if (s.isOpen() && obj.getString("receiverId").equals(s.getUserProperties().get("user"))) {
                        String json = Json.createObjectBuilder()
                                .add("saw", obj.getString("senderId")).build().toString();
                        s.getBasicRemote().sendObject(json);
                    }
                }
                break;
            case "LAST_MSGS":
                String json = Json.createObjectBuilder().add("lastMessages", loadLastMessages(obj.getString("senderId"), obj.getString("receiverId"))).build().toString();
                session.getBasicRemote().sendObject(json);
                break;
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
            UserAuthorization authorization = userAuthorizationJpaController.findUserAuthorization(socialProfileJpaController.findSocialProfileBySocialProfileId(Integer.parseInt((String) s.getUserProperties().get("user"))).getTokenId());
            if (authorization.getRoles().equals(ADMIN)) {
                json = Json.createObjectBuilder()
                        .add("onlineUsers", session.getOpenSessions().size()).build().toString();
                s.getBasicRemote().sendObject(json);
            }
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

    private String loadAllFriendIdFromOfflineMessages(String receiverId) throws RollbackFailureException, Exception {
        List<Integer> idList = messengerMessagesJpaController.getAllIdFromOfflineMessages(Integer.parseInt(receiverId));
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        idList.stream().forEach((senderId) -> {
            jsonArrayBuilder.add(Json.createObjectBuilder()
                    .add("id", senderId)
                    .add("name", socialProfileJpaController.findSocialProfileBySocialProfileId(senderId).getName()));
        });
        JsonArray jsonArray = jsonArrayBuilder.build();
        if (jsonArray.isEmpty()) {
            return null;
        } else {
            return jsonArray.toString();
        }
    }

    private String loadLastMessages(String senderId, String receiverId) throws RollbackFailureException, Exception {
        List<MessengerMessages> lastTenMessagesList = messengerMessagesJpaController.getLastTenMessages(Integer.parseInt(receiverId), Integer.parseInt(senderId));
        List<MessengerMessages> messengerMessagesList = new ArrayList<>();
        for (int i = lastTenMessagesList.size(); i > 0; i--) {
            messengerMessagesList.add(lastTenMessagesList.get(i - 1));
        }
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        messengerMessagesList.stream().forEach((messengerMessages) -> {
            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(messengerMessages.getMessageDate());
            jsonArrayBuilder.add(Json.createObjectBuilder()
                    .add("message", messengerMessages.getMessage())
                    .add("senderId", messengerMessages.getSocialProfileIdSender())
                    .add("senderName", socialProfileJpaController.findSocialProfileBySocialProfileId(messengerMessages.getSocialProfileIdSender()).getName())
                    .add("receiverId", messengerMessages.getSocialProfileIdReceiver())
                    .add("receiverName", socialProfileJpaController.findSocialProfileBySocialProfileId(messengerMessages.getSocialProfileIdReceiver()).getName())
                    .add("date", date)
                    .add("read", String.valueOf(messengerMessages.getMessageDelivered())));
        });
        JsonArray jsonArray = jsonArrayBuilder.build();
        return jsonArray.toString();
    }

    private void sendObject(Session session, JsonObject obj, boolean himself, String date) throws IOException, EncodeException {
        session.getBasicRemote().sendObject(Json.createObjectBuilder()
                .add("message", obj.getString("message"))
                .add("senderId", obj.getString("senderId"))
                .add("senderName", socialProfileJpaController.findSocialProfileBySocialProfileId(Integer.parseInt(obj.getString("senderId"))).getName())
                .add("receiverId", obj.getString("receiverId"))
                .add("receiverName", socialProfileJpaController.findSocialProfileBySocialProfileId(Integer.parseInt(obj.getString("receiverId"))).getName())
                .add("date", date)
                .add("himself", himself)
                .add("read", "N").build()
                .toString());
    }
}
