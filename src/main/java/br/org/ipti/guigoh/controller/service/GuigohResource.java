/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.service;

import br.org.ipti.guigoh.model.entity.Friends;
import br.org.ipti.guigoh.model.entity.MessengerMessages;
import br.org.ipti.guigoh.model.entity.SocialProfile;
import br.org.ipti.guigoh.model.entity.Tags;
import br.org.ipti.guigoh.model.entity.Users;
import br.org.ipti.guigoh.model.jpa.controller.FriendsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.MessengerMessagesJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.controller.TagsJpaController;
import br.org.ipti.guigoh.model.jpa.exceptions.NonexistentEntityException;
import br.org.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
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
    @Path("/messagesHistory")
    @Produces("application/json")
    public String getMessagesHistory(@QueryParam("loggedSocialProfileId") Integer loggedSocialProfileId, @QueryParam("friendSocialProfileId") Integer socialProfileId) throws JSONException, NonexistentEntityException, RollbackFailureException, Exception {
        MessengerMessagesJpaController messengerMessagesJpaController = new MessengerMessagesJpaController();
        List<MessengerMessages> lastTenMessagesList = messengerMessagesJpaController.getLastTenMessages(loggedSocialProfileId, socialProfileId);
        List<MessengerMessages> messengerMessagesList = new ArrayList<>();
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
            messagesList.put(i, message);
            messengerMessagesList.get(i).setMessageDelivered('Y');
            messengerMessagesJpaController.edit(messengerMessagesList.get(i));
        }

        return messagesList.toString();
    }

    @GET
    @Path("/messengerFriends")
    @Produces("application/json")
    public String getMessengerFriends(@QueryParam("socialProfileId") Integer socialProfileId) throws JSONException, Exception, RollbackFailureException {
        SocialProfileJpaController socialProfileJpaController = new SocialProfileJpaController();
        FriendsJpaController friendsJpaController = new FriendsJpaController();
        List<Friends> friendList = friendsJpaController.findFriendsByToken(socialProfileJpaController.findSocialProfileBySocialProfileId(socialProfileId).getTokenId());
        JSONArray jsonFriendList = new JSONArray();
        for (int i = 0; i < friendList.size(); i++) {
            Users friendUser = (friendList.get(i).getTokenFriend1().getSocialProfile().getSocialProfileId() == socialProfileId) ? 
                    friendList.get(i).getTokenFriend2() : friendList.get(i).getTokenFriend1();
            JSONObject jsonFriend = new JSONObject();
            jsonFriend.put("id", friendUser.getSocialProfile().getSocialProfileId());
            jsonFriend.put("name", friendUser.getSocialProfile().getName());
            jsonFriend.put("photo", friendUser.getSocialProfile().getPhoto());
            jsonFriendList.put(i, jsonFriend);
        }
        return jsonFriendList.toString();
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
