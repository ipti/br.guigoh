/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.util.websocket;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Arrays;
import javax.json.Json;
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
@ServerEndpoint(value = "/socket/docs/{user}/{collaborators}")
public class DocsEndpoint {

    @OnOpen
    public void onOpen(final Session session, @PathParam("user") final String user, @PathParam("collaborators") final String collaborators) throws IOException, EncodeException, Exception {
        session.getUserProperties().put("user", user);
        session.getUserProperties().put("collaborators", collaborators);
    }

    @OnMessage
    public void onMessage(final Session session, final String jsonString) throws Exception {
        JsonObject obj = Json.createReader(new StringReader(jsonString))
                .readObject();
        String[] collaborators = session.getUserProperties().get("collaborators").toString().split(",");
        switch (obj.getString("type")) {
            case "NEW_CODE":
            case "FAKE_CODE":
                for (Session s : session.getOpenSessions()) {
                    if (s.isOpen() && Arrays.asList(collaborators).contains(s.getUserProperties().get("user").toString())) {
                        s.getBasicRemote().sendObject(jsonString);
                    }
                }
                break;
        }
    }

    @OnClose
    public void onClose(final Session session) throws IOException, EncodeException {

    }
}
