/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.util.websocket;

import java.io.IOException;
import java.util.Arrays;
import javax.json.Json;
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
    }

    @OnMessage
    public void onMessage(final Session session, final String jsonString) throws Exception {
        String[] collaborators = session.getUserProperties().get("collaborators").toString().split(",");
        for (Session s : session.getOpenSessions()) {
            if (s.isOpen() && Arrays.asList(collaborators).contains(s.getUserProperties().get("user").toString())) {
                s.getBasicRemote().sendObject(jsonString);
            }
        }
    }

    @OnClose
    public void onClose(final Session session) throws IOException, EncodeException {
        for (Session s : session.getOpenSessions()) {
            if (s.isOpen() && session.getUserProperties().get("doc").equals(s.getUserProperties().get("doc"))
                    && !session.getUserProperties().get("user").equals(s.getUserProperties().get("user"))) {
                String json = Json.createObjectBuilder()
                        .add("status", "offline")
                        .add("id", session.getUserProperties().get("user").toString()).build().toString();
                s.getBasicRemote().sendObject(json);
            }
        }
    }
}
