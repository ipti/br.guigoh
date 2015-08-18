/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.controller.service;

import br.org.ipti.guigoh.model.entity.Friends;
import br.org.ipti.guigoh.model.entity.Users;
import br.org.ipti.guigoh.model.jpa.controller.FriendsJpaController;
import br.org.ipti.guigoh.model.jpa.controller.SocialProfileJpaController;
import br.org.ipti.guigoh.model.jpa.exceptions.RollbackFailureException;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.json.*;
/**
 * REST Web Service
 *
 * @author IPTI
 */
@Path("/")
public class GuigohResource extends Thread {

    public GuigohResource() {
    }

    @GET
    @Produces("application/json")
    public String getJson() {
        throw new UnsupportedOperationException();
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

    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
