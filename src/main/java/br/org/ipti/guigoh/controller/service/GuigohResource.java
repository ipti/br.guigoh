/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.service;

import br.org.ipti.guigoh.model.entity.MessengerMessages;
import br.org.ipti.guigoh.model.entity.MessengerStatus;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.Tags;
import br.org.ipti.guigoh.model.jpa.controller.FriendsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.MessengerMessagesJpaController;
import br.org.ipti.guigoh.model.jpa.controller.MessengerStatusJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.TagsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.UtilJpaController;
import br.org.ipti.guigoh.model.jpa.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.json.*;
/**
 * REST Web Service
 *
 * @author IPTI
 */
@Path("/")
public class GuigohResource extends Thread {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GuigohResource
     */
    public GuigohResource() {
    }

    /**
     * Retrieves representation of an instance of
 br.org.ipti.guigoh.controller.service.GuigohResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    

    @GET
    @Path("/allMessagesHistory")
    @Produces("application/json")
    public String getAllMessagesHistory(@QueryParam("loggedSocialProfileId") Integer loggedSocialProfileId, @QueryParam("friendSocialProfileId") Integer socialProfileId) throws JSONException, NonexistentEntityException, RollbackFailureException, Exception {
        MessengerMessagesJpaController messengerMessagesJpaController = new MessengerMessagesJpaController();
        List<MessengerMessages> messengerMessagesList = messengerMessagesJpaController.getAllMessages(loggedSocialProfileId, socialProfileId);
        SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
        JSONArray messagesList = new JSONArray();
        for (int i = 0; i < messengerMessagesList.size(); i++) {
            JSONObject message = new JSONObject();
            SocialProfile socialProfile = socialProfileJpaController.findSocialProfileBySocialProfileId(messengerMessagesList.get(i).getSocialProfileIdSender());
            message.put("id", messengerMessagesList.get(i).getSocialProfileIdSender());
            message.put("name", socialProfile.getName());
            message.put("message", messengerMessagesList.get(i).getMessage());
            message.put("messageType", messengerMessagesList.get(i).getMessageDelivered());
            messagesList.put(i, message);
        }

        return messagesList.toString();
    }

    @GET
    @Path("/messagesHistory")
    @Produces("application/json")
    public String getMessagesHistory(@QueryParam("loggedSocialProfileId") Integer loggedSocialProfileId, @QueryParam("friendSocialProfileId") Integer socialProfileId) throws JSONException, NonexistentEntityException, RollbackFailureException, Exception {
        MessengerMessagesJpaController messengerMessagesJpaController = new MessengerMessagesJpaController();
        List<MessengerMessages> lastTenMessagesList = messengerMessagesJpaController.getLastTenMessages(loggedSocialProfileId, socialProfileId);
        List<MessengerMessages> messengerMessagesList = new ArrayList<MessengerMessages>();
        SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
        for (int i = lastTenMessagesList.size(); i > 0; i--) {
            messengerMessagesList.add(lastTenMessagesList.get(i - 1));
        }
        JSONArray messagesList = new JSONArray();
        for (int i = 0; i < messengerMessagesList.size(); i++) {
            JSONObject message = new JSONObject();
            SocialProfile socialProfile = socialProfileJpaController.findSocialProfileBySocialProfileId(messengerMessagesList.get(i).getSocialProfileIdSender());
            message.put("id", messengerMessagesList.get(i).getSocialProfileIdSender());
            message.put("name", socialProfile.getName());
            message.put("message", messengerMessagesList.get(i).getMessage());
            message.put("messageType", messengerMessagesList.get(i).getMessageDelivered());
            messagesList.put(i, message);
        }

        return messagesList.toString();
    }

    @GET
    @Path("/deliverMessages")
    @Produces("application/json")
    public String getMessagesDelivered(@QueryParam("socialProfileId") Integer socialProfileId) throws JSONException, NonexistentEntityException, RollbackFailureException, Exception {
        MessengerMessagesJpaController messengerMessagesJpaController = new MessengerMessagesJpaController();
        List<MessengerMessages> nonReadMessagesList = messengerMessagesJpaController.getNonReadMessages(socialProfileId);
        List<MessengerMessages> messengerMessagesList = new ArrayList<>();
        for (MessengerMessages mm : nonReadMessagesList) {
            messengerMessagesList.add(mm);
        }
        SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
        JSONArray messagesList = new JSONArray();
        for (int i = 0; i < messengerMessagesList.size(); i++) {
            SocialProfile socialProfile = socialProfileJpaController.findSocialProfileBySocialProfileId(messengerMessagesList.get(i).getSocialProfileIdSender());
            JSONObject message = new JSONObject();
            message.put("id", messengerMessagesList.get(i).getSocialProfileIdSender());
            message.put("name", socialProfile.getName());
            message.put("message", messengerMessagesList.get(i).getMessage());
            message.put("messageType", "N");
            messagesList.put(i, message);
            messengerMessagesList.get(i).setMessageDelivered('Y');
            messengerMessagesJpaController.edit(messengerMessagesList.get(i));
        }

        return messagesList.toString();
    }

    @GET
    @Path("/sendMessage")
    @Produces("application/json")
    public String sendMessage(@QueryParam("socialProfileIdSender") Integer idSender, @QueryParam("socialProfileIdReceiver") Integer idReceiver, @QueryParam("message") String message) throws NonexistentEntityException, RollbackFailureException, Exception {
        MessengerMessages mm = new MessengerMessages();
        mm.setMessage(message);
        mm.setSocialProfileIdReceiver(idReceiver);
        mm.setSocialProfileIdSender(idSender);
        MessengerMessagesJpaController messengerMessagesJpaController = new MessengerMessagesJpaController();
        UtilJpaController utilJpaController = new UtilJpaController();
        Timestamp ts = utilJpaController.getTimestampServerTime();
        mm.setMessageDate(ts);
        mm.setMessageDelivered('N');
        messengerMessagesJpaController.create(mm);
        JSONObject object = new JSONObject();
        return object.toString();
    }

    @GET
    @Path("/messengerFriends")
    @Produces("application/json")
    public String getMessengerFriends(@QueryParam("socialProfileId") Integer socialProfileId) throws JSONException, Exception, RollbackFailureException {
        MessengerStatusJpaController messengerStatusJpaController = new MessengerStatusJpaController();
        UtilJpaController utilJpaController = new UtilJpaController();
        Double serverTime = utilJpaController.getEpochServerTime();
        MessengerStatus ms = new MessengerStatus();
        ms.setSocialProfileId(socialProfileId);
        ms.setLastPing(serverTime);
        messengerStatusJpaController.pingUser(ms);
        SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
        SocialProfile socialProfile = socialProfileJpaController.findSocialProfileBySocialProfileId(socialProfileId);
        FriendsJpaController friendsJpaController = new FriendsJpaController();
        List<SocialProfile> friendsOfflineList = friendsJpaController.findFriendsOfflineByToken(socialProfile.getTokenId());
        List<SocialProfile> friendsOnlineList = friendsJpaController.findFriendsOnlineByToken(socialProfile.getTokenId());
        JSONArray friendList = new JSONArray();
        for (int i = 0; i < friendsOnlineList.size(); i++) {
            JSONObject friend = new JSONObject();
            friend.put("id", friendsOnlineList.get(i).getSocialProfileId());
            friend.put("name", friendsOnlineList.get(i).getName());
            friend.put("photo", socialProfileJpaController.findSocialProfileBySocialProfileId(friendsOnlineList.get(i).getSocialProfileId()).getPhoto());
            friend.put("online", "true");
            friendList.put(i, friend);
        }
        for (int i = 0; i < friendsOfflineList.size(); i++) {
            JSONObject friend = new JSONObject();
            friend.put("id", friendsOfflineList.get(i).getSocialProfileId());
            friend.put("name", friendsOfflineList.get(i).getName());
            friend.put("photo", socialProfileJpaController.findSocialProfileBySocialProfileId(friendsOfflineList.get(i).getSocialProfileId()).getPhoto());
            friend.put("online", "false");
            friendList.put(friendsOnlineList.size() + i, friend);
        }
        return friendList.toString();
    }

    @GET
    @Path("/tags")
    @Produces("application/json")
    public String getTagss(@QueryParam("text") String text, @QueryParam("theme_id") Integer theme_id) throws JSONException, Exception, RollbackFailureException {
        TagsJpaController tagsJpaController = new TagsJpaController();
        List<Tags> tagslist = tagsJpaController.findTagsByText(text);
        JSONArray tagsArray = new JSONArray();
        for (int i = 0; i < tagslist.size(); i++) {
            JSONObject tag = new JSONObject();
            tag.put("name", tagslist.get(i).getName());
            tag.put("id", tagslist.get(i).getId());
            tagsArray.put(i, tag);
        }
        return tagsArray.toString();
    }

    /**
     * PUT method for updating or creating an instance of GuigohResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
