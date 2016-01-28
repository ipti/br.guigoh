/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.util.websocket;

import br.org.ipti.guigoh.model.entity.DocMessage;
import br.org.ipti.guigoh.model.jpa.controller.DocJpaController;
import br.org.ipti.guigoh.model.jpa.controller.DocMessageJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UtilJpaController;
import br.org.ipti.guigoh.model.jpa.controller.exceptions.RollbackFailureException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

/**
 *
 * @author iptipc008
 */
@ServerEndpoint(value = "/socket/docs/{user}/{doc}")
public class DocsEndpoint {

    @OnOpen
    public void onOpen(final Session session, @PathParam("user") final String user, @PathParam("doc") final String doc) throws IOException, EncodeException, Exception {
        session.getUserProperties().put("user", user);
        session.getUserProperties().put("doc", doc);
        String json;
        for (Session s : session.getOpenSessions()) {
            if (s.isOpen() && session.getUserProperties().get("doc").equals(s.getUserProperties().get("doc"))) {
                json = Json.createObjectBuilder()
                        .add("status", "online")
                        .add("id", s.getUserProperties().get("user").toString()).build().toString();
                session.getBasicRemote().sendObject(json);
                if (!s.getUserProperties().get("user").equals(session.getUserProperties().get("user"))) {
                    json = Json.createObjectBuilder()
                            .add("status", "online")
                            .add("id", session.getUserProperties().get("user").toString()).build().toString();
                    s.getBasicRemote().sendObject(json);
                }
            }
        }
        json = Json.createObjectBuilder().add("previousMessages", loadPreviousMessages(Integer.parseInt((String) session.getUserProperties().get("doc")))).build().toString();
        session.getBasicRemote().sendObject(json);
    }

    @OnMessage
    public void onMessage(final Session session, final String jsonString) throws Exception {
        JsonObject obj = Json.createReader(new StringReader(jsonString))
                .readObject();
        switch (obj.getString("action")) {
            case "UPDATE":
                for (Session s : session.getOpenSessions()) {
                    if (s.isOpen() && s.getUserProperties().get("doc").equals(session.getUserProperties().get("doc")) && !s.equals(session)) {
                        s.getBasicRemote().sendObject(jsonString);
                    }
                }
                break;
            case "PERMISSION":
            case "KICK":
                for (Session s : session.getOpenSessions()) {
                    if (s.isOpen() && s.getUserProperties().get("doc").equals(obj.getString("doc"))
                            && !session.getUserProperties().get("user").equals(s.getUserProperties().get("user"))) {
                        s.getBasicRemote().sendObject(jsonString);
                    }
                }
                break;
            case "MESSAGE":
                DocJpaController docJpaController = new DocJpaController();
                SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
                DocMessageJpaController docMessageJpaController = new DocMessageJpaController();
                UtilJpaController utilJpaController = new UtilJpaController();
                DocMessage docMessage = new DocMessage();
                docMessage.setMessage(obj.getString("message"));
                docMessage.setDocFk(docJpaController.findDoc(Integer.parseInt(obj.getString("doc"))));
                docMessage.setSocialProfileFk(socialProfileJpaController.findSocialProfileBySocialProfileId(Integer.parseInt(obj.getString("user"))));
                Timestamp ts = utilJpaController.getTimestampServerTime();
                String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(ts);
                docMessage.setDate(ts);
                docMessageJpaController.create(docMessage);
                for (Session s : session.getOpenSessions()) {
                    if (s.isOpen() && s.getUserProperties().get("doc").equals(obj.getString("doc"))) {
                        if (!session.equals(s)) {
                            s.getBasicRemote().sendObject(Json.createObjectBuilder()
                                    .add("user", obj.getString("user"))
                                    .add("chatMessage", obj.getString("message"))
                                    .add("userName", socialProfileJpaController.findSocialProfileBySocialProfileId(Integer.parseInt(obj.getString("user"))).getName())
                                    .add("date", date).build()
                                    .toString());
                        } else {
                            session.getBasicRemote().sendObject(Json.createObjectBuilder()
                                    .add("dateToHimself", date).build()
                                    .toString());
                        }
                    }
                }
                break;
            case "EDITING_ON":
            case "EDITING_OFF":
                for (Session s : session.getOpenSessions()) {
                    if (s.isOpen() && s.getUserProperties().get("doc").equals(obj.getString("doc")) && !session.equals(s)) {
                        s.getBasicRemote().sendObject(jsonString);
                    }
                }
                break;
        }
    }

    @OnClose
    public void onClose(final Session session) throws IOException, EncodeException {
        int count = 0;
        for (Session s : session.getOpenSessions()) {
            if (s.isOpen() && session.getUserProperties().get("doc").equals(s.getUserProperties().get("doc"))
                    && s.getUserProperties().get("user").equals(session.getUserProperties().get("user"))) {
                count++;
            }
        }
        for (Session s : session.getOpenSessions()) {
            if (s.isOpen() && session.getUserProperties().get("doc").equals(s.getUserProperties().get("doc"))
                    && !session.getUserProperties().get("user").equals(s.getUserProperties().get("user"))) {
                if (count == 0) {
                    String json = Json.createObjectBuilder()
                            .add("status", "offline")
                            .add("id", session.getUserProperties().get("user").toString()).build().toString();
                    s.getBasicRemote().sendObject(json);
                }
            }
        }
    }

    private String loadPreviousMessages(Integer docId) throws RollbackFailureException, Exception {
        DocMessageJpaController docMessageJpaController = new DocMessageJpaController();
        List<DocMessage> previousMessagesList = docMessageJpaController.getPreviousMessages(docId);
        List<DocMessage> docMessageList = new ArrayList<>();
        for (int i = previousMessagesList.size(); i > 0; i--) {
            docMessageList.add(previousMessagesList.get(i - 1));
        }
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        docMessageList.stream().forEach((docMessage) -> {
            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(docMessage.getDate());
            jsonArrayBuilder.add(Json.createObjectBuilder()
                    .add("user", docMessage.getSocialProfileFk().getSocialProfileId())
                    .add("chatMessage", docMessage.getMessage())
                    .add("userName", docMessage.getSocialProfileFk().getName())
                    .add("date", date)
                    .add("action", "PREVIOUS_MESSAGES").build());
        });
        JsonArray jsonArray = jsonArrayBuilder.build();
        return jsonArray.toString();
    }
}
