/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipti.guigoh.controller.service;

import com.guigoh.bo.FriendsBO;
import com.guigoh.bo.InterestsBO;
import com.guigoh.bo.MessengerMessagesBO;
import com.guigoh.bo.MessengerStatusBO;
import com.guigoh.bo.SocialProfileBO;
import com.ipti.guigoh.model.jpa.exceptions.NonexistentEntityException;
import com.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import com.ipti.guigoh.model.entity.Friends;
import com.ipti.guigoh.model.entity.Interests;
import com.ipti.guigoh.model.entity.MessengerMessages;
import com.ipti.guigoh.model.entity.MessengerStatus;
import com.ipti.guigoh.model.entity.SocialProfile;
import com.ipti.guigoh.model.entity.Tags;
import com.ipti.guigoh.model.entity.Users;
import com.ipti.guigoh.model.jpa.controller.TagsJpaController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.json.*;
/**
 * REST Web Service
 *
 * @author IPTI
 */
@Path("primata")
public class PrimataResource extends Thread {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of PrimataResource
     */
    public PrimataResource() {
    }

    /**
     * Retrieves representation of an instance of
     * com.guigoh.primata.service.PrimataResource
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
        MessengerMessagesBO messengerMessagesBO = new MessengerMessagesBO();
        List<MessengerMessages> messengerMessagesList = messengerMessagesBO.getAllMessages(loggedSocialProfileId, socialProfileId);
        SocialProfileBO socialProfileBO = new SocialProfileBO();
        JSONArray messagesList = new JSONArray();
        for (int i = 0; i < messengerMessagesList.size(); i++) {
            JSONObject message = new JSONObject();
            SocialProfile socialProfile = socialProfileBO.findSocialProfileBySocialProfileId(messengerMessagesList.get(i).getSocialProfileIdSender());
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
        MessengerMessagesBO messengerMessagesBO = new MessengerMessagesBO();
        List<MessengerMessages> lastTenMessagesList = messengerMessagesBO.getLastTenMessages(loggedSocialProfileId, socialProfileId);
        List<MessengerMessages> messengerMessagesList = new ArrayList<MessengerMessages>();
        SocialProfileBO socialProfileBO = new SocialProfileBO();
        for (int i = lastTenMessagesList.size(); i > 0; i--) {
            messengerMessagesList.add(lastTenMessagesList.get(i - 1));
        }
        JSONArray messagesList = new JSONArray();
        for (int i = 0; i < messengerMessagesList.size(); i++) {
            JSONObject message = new JSONObject();
            SocialProfile socialProfile = socialProfileBO.findSocialProfileBySocialProfileId(messengerMessagesList.get(i).getSocialProfileIdSender());
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
        MessengerMessagesBO messengerMessagesBO = new MessengerMessagesBO();
        List<MessengerMessages> nonReadMessagesList = messengerMessagesBO.getNonReadMessages(socialProfileId);
        List<MessengerMessages> messengerMessagesList = new ArrayList<MessengerMessages>();
        for (MessengerMessages mm : nonReadMessagesList) {
            messengerMessagesList.add(mm);
        }
        SocialProfileBO socialProfileBO = new SocialProfileBO();
        JSONArray messagesList = new JSONArray();
        for (int i = 0; i < messengerMessagesList.size(); i++) {
            SocialProfile socialProfile = socialProfileBO.findSocialProfileBySocialProfileId(messengerMessagesList.get(i).getSocialProfileIdSender());
            JSONObject message = new JSONObject();
            message.put("id", messengerMessagesList.get(i).getSocialProfileIdSender());
            message.put("name", socialProfile.getName());
            message.put("message", messengerMessagesList.get(i).getMessage());
            message.put("messageType", "N");
            messagesList.put(i, message);
            messengerMessagesList.get(i).setMessageDelivered('Y');
            messengerMessagesBO.editMessage(messengerMessagesList.get(i));
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
        MessengerMessagesBO mmBO = new MessengerMessagesBO();
        Timestamp ts = mmBO.getServerTime();
        mm.setMessageDate(ts);
        mm.setMessageDelivered('N');
        mmBO.createMessage(mm);
        JSONObject object = new JSONObject();
        return object.toString();
    }

    @GET
    @Path("/messengerFriends")
    @Produces("application/json")
    public String getMessengerFriends(@QueryParam("socialProfileId") Integer socialProfileId) throws JSONException, Exception, RollbackFailureException {
        MessengerStatusBO messengerStatusBO = new MessengerStatusBO();
        Double serverTime = messengerStatusBO.getServerTime();
        MessengerStatus ms = new MessengerStatus();
        ms.setSocialProfileId(socialProfileId);
        ms.setLastPing(serverTime);
        messengerStatusBO.pingUser(ms);
        SocialProfileBO socialProfileBO = new SocialProfileBO();
        SocialProfile socialProfile = socialProfileBO.findSocialProfileBySocialProfileId(socialProfileId);
        FriendsBO friendsBO = new FriendsBO();
        List<SocialProfile> friendsOfflineList = friendsBO.findFriendsOfflineByToken(socialProfile.getTokenId());
        List<SocialProfile> friendsOnlineList = friendsBO.findFriendsOnlineByToken(socialProfile.getTokenId());
        JSONArray friendList = new JSONArray();
        for (int i = 0; i < friendsOnlineList.size(); i++) {
            JSONObject friend = new JSONObject();
            friend.put("id", friendsOnlineList.get(i).getSocialProfileId());
            friend.put("name", friendsOnlineList.get(i).getName());
            friend.put("photo", socialProfileBO.findSocialProfileBySocialProfileId(friendsOnlineList.get(i).getSocialProfileId()).getPhoto());
            friend.put("online", "true");
            friendList.put(i, friend);
        }
        for (int i = 0; i < friendsOfflineList.size(); i++) {
            JSONObject friend = new JSONObject();
            friend.put("id", friendsOfflineList.get(i).getSocialProfileId());
            friend.put("name", friendsOfflineList.get(i).getName());
            friend.put("photo", socialProfileBO.findSocialProfileBySocialProfileId(friendsOfflineList.get(i).getSocialProfileId()).getPhoto());
            friend.put("online", "false");
            friendList.put(friendsOnlineList.size() + i, friend);
        }
        return friendList.toString();
    }

    @GET
    @Path("/sendMessageCurriculum")
    @Produces("application/json")
    public String sendMessageCurriculum(@QueryParam("socialProfileId") Integer socialProfileId, @QueryParam("businessName") String businessName, @QueryParam("email") String email, @QueryParam("phone") String phone, @QueryParam("message") String message) throws JSONException, Exception, RollbackFailureException {
        MessengerMessages mm = new MessengerMessages();
        String messageConcatenated = businessName + ";" + email + ";" + phone + ";" + message;
        mm.setMessage(messageConcatenated);
        mm.setSocialProfileIdSender(0);
        mm.setSocialProfileIdReceiver(socialProfileId);
        mm.setMessageDelivered('U');
        MessengerMessagesBO mmBO = new MessengerMessagesBO();
        Timestamp ts = mmBO.getServerTime();
        mm.setMessageDate(ts);
        mmBO.createMessage(mm);
        JSONObject jsonObject = new JSONObject();
        return jsonObject.toString();
    }

    @GET
    @Path("/getCurriculumMessages")
    @Produces("application/json")
    public String getCurriculumMessages(@QueryParam("socialProfileId") Integer socialProfileId) throws JSONException, Exception, RollbackFailureException {
        MessengerMessagesBO mmBO = new MessengerMessagesBO();
        List<MessengerMessages> curriculumMessagesList = mmBO.getCurriculumMessages(socialProfileId);
        JSONArray messagesList = new JSONArray();
        for (int i = 0; i < curriculumMessagesList.size(); i++) {
            JSONObject message = new JSONObject();
            message.put("businessName", curriculumMessagesList.get(i).getMessage().split(";")[0]);
            message.put("email", curriculumMessagesList.get(i).getMessage().split(";")[1]);
            message.put("phone", curriculumMessagesList.get(i).getMessage().split(";")[2]);
            message.put("message", curriculumMessagesList.get(i).getMessage().split(";")[3]);
            message.put("date", curriculumMessagesList.get(i).getMessageDate());
            messagesList.put(i, message);
            curriculumMessagesList.get(i).setMessageDelivered('S');
            mmBO.editMessage(curriculumMessagesList.get(i));
        }
        return messagesList.toString();
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
     * PUT method for updating or creating an instance of PrimataResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
