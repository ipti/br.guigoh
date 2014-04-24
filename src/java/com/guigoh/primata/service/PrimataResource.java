/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.service;

import com.guigoh.primata.bo.FriendsBO;
import com.guigoh.primata.bo.InterestsBO;
import com.guigoh.primata.bo.MessengerMessagesBO;
import com.guigoh.primata.bo.MessengerStatusBO;
import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.bo.TagsBO;
import com.guigoh.primata.bo.util.JsonConverter;
import com.guigoh.primata.dao.exceptions.NonexistentEntityException;
import com.guigoh.primata.dao.exceptions.RollbackFailureException;
import com.guigoh.primata.entity.Friends;
import com.guigoh.primata.entity.Interests;
import com.guigoh.primata.entity.MessengerMessages;
import com.guigoh.primata.entity.MessengerStatus;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.Tags;
import com.guigoh.primata.entity.Users;
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
import org.codehaus.jettison.json.*;

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
    @Path("/getReviewObject/{reviewId}")
    @Produces("application/json")
    public String getReviewObject(@PathParam("reviewId") final String reviewID) throws SQLException {
        try {
            JSONObject reviewObject = new JSONObject();
            Connection Conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mandril", "postgres", "");
            Statement Stmt1 = Conn.createStatement();
            Statement Stmt2 = Conn.createStatement();
            Statement Stmt3 = Conn.createStatement();
            ResultSet rsobjectData = Stmt1.executeQuery("select pri.comunity as mainTheme,mro.tags,mro.abstract,mro.id,mro.title,mro.submitter_login as submitterNick,mro.editor_login,to_char(mroe.event_date, 'DD/MM/YYYY'),mro.community_id as mainThemeId,"
                    + "(select count(mc.id) from mandril_comment mc where mc.review_object_fk=mro.id) as content_quantity,"
                    + "(select mm.filename from mandril_media mm where mst.picture_fk=mm.id) as picture_filename,"
                    + "(select mm.type from mandril_media mm where mst.picture_fk=mm.id) as picture_type, "
                    + "(select 'http://cdn.guigoh.com/mandrildata/medias/'||mro.id||'/'||filename||'.'||type from mandril_media mx where mst.picture_fk=mx.id ) as pictureURL "
                    + "from mandril_review_object mro"
                    + "     join mandril_review_object_event mroe on(mroe.review_object_fk = mro.id)"
                    + "     join mandril_social_technology mst on(mro.reviewable_fk = mst.id)"
                    + "     join dblink('dbname=primata', 'select name,id from community') as pri(comunity character varying(255), id bigint) on (mro.community_id=pri.id)"
                    + "     where mroe.status = 'PUBLISHED' and mro.id =" + reviewID
                    + "     order by mroe.event_date DESC "
                    + "limit 10");
            ResultSet rsauthorsData = Stmt2.executeQuery("select * from mandril_social_tech_author a join mandril_author b on(b.id=a.author_fk) "
                    + "where social_tech_fk = " + reviewID);
            ResultSet rsmediaData = Stmt3.executeQuery("select *,(select 'http://cdn.guigoh.com/mandrildata/medias/'||filename||'.'||type) as url from mandril_social_tech_media a join mandril_media b on(a.media_fk=b.id) "
                    + "where social_tech_fk = " + reviewID);
            JSONArray robjectData = JsonConverter.convert(rsobjectData);
            JSONArray rauthorsData = JsonConverter.convert(rsauthorsData);
            JSONArray rmediaData = JsonConverter.convert(rsmediaData);

            reviewObject.put("info", robjectData);
            reviewObject.put("authors", rauthorsData);
            reviewObject.put("medias", rmediaData);

            return reviewObject.toString();
        } catch (JSONException ex) {
            Logger.getLogger(PrimataResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PrimataResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("/listThemes")
    @Produces("application/json")
    public String getThemes() throws SQLException {
        try {
            Connection Conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/primata", "postgres", "");
            Statement Stmt = Conn.createStatement();
            ResultSet RS = Stmt.executeQuery("select * from community where parent_fk = '21' and has_social_tech IS TRUE");
            JSONArray jsona = JsonConverter.convert(RS);
            return jsona.toString(2);
        } catch (JSONException ex) {
            Logger.getLogger(PrimataResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PrimataResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("/lastreviewobjects")
    @Produces("application/json")
    public String getLastReviewObjects() throws SQLException {
        try {
            Connection Conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mandril", "postgres", "postgres");
            Statement Stmt = Conn.createStatement();
            ResultSet RS = Stmt.executeQuery("select pri.comunity,mro.abstract,mro.tags,mro.id,mro.title,mro.submitter_login,mro.editor_login,to_char(mroe.event_date, 'DD/MM/YYYY'),mro.community_id,"
                    + "(select count(mc.id) from mandril_comment mc where mc.review_object_fk=mro.id) as content_quantity,"
                    + "(select mm.filename from mandril_media mm where mst.picture_fk=mm.id) as picture_filename,"
                    + "(select mm.type from mandril_media mm where mst.picture_fk=mm.id) as picture_type, "
                    + "(select 'http://cdn.guigoh.com/mandrildata/medias/'||mro.id||'/'||mx.filename||'.'||mx.type from mandril_media mx where mst.picture_fk=mx.id) as picture_url "
                    + "from mandril_review_object mro"
                    + "     join mandril_review_object_event mroe on(mroe.review_object_fk = mro.id)"
                    + "     join mandril_social_technology mst on(mro.reviewable_fk = mst.id)"
                    + "     join dblink('dbname=primata hostaddr=127.0.0.1 user=postgres password=postgres port=5432', 'select name,id from community') as pri(comunity character varying(255), id bigint) on (mro.community_id=pri.id)"
                    + "     where mroe.status = 'PUBLISHED'"
                    + "     order by mroe.event_date DESC ");
            JSONArray jsona = JsonConverter.convert(RS);
            return jsona.toString(2);
        } catch (JSONException ex) {
            Logger.getLogger(PrimataResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PrimataResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("/publishreviewobjects")
    @Produces("application/json")
    public String publishReviewObjects(@QueryParam("publishObjectId") Integer id) throws SQLException {
        try {
            Connection Conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mandril", "postgres", "postgres");
            Statement Stmt = Conn.createStatement();
            ResultSet RS = Stmt.executeQuery("INSERT INTO mandril_review_object_event(index,event_date,status,review_object_fk) "
                    + "VALUES(0, (select now()), 'PUBLISHED', " + id + ")");
            JSONObject reviewObject = new JSONObject();
            return reviewObject.toString(2);
        } catch (JSONException ex) {
            Logger.getLogger(PrimataResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PrimataResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("/submittedreviewobjects")
    @Produces("application/json")
    public String getSubmittedReviewObjects() throws SQLException {
        try {
            Connection Conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mandril", "postgres", "postgres");
            Statement Stmt = Conn.createStatement();
            ResultSet RS = Stmt.executeQuery("select pri.comunity,mro.abstract,mro.tags,mro.id,mro.title,mro.submitter_login,mro.editor_login,to_char(mroe.event_date, 'DD/MM/YYYY'),mro.community_id,"
                    + "(select count(mc.id) from mandril_comment mc where mc.review_object_fk=mro.id) as content_quantity,"
                    + "(select mm.filename from mandril_media mm where mst.picture_fk=mm.id) as picture_filename,"
                    + "(select mm.type from mandril_media mm where mst.picture_fk=mm.id) as picture_type, "
                    + "(select 'http://cdn.guigoh.com/mandrildata/medias/'||mro.id||'/'||mx.filename||'.'||mx.type from mandril_media mx where mst.picture_fk=mx.id) as picture_url "
                    + "from mandril_review_object mro"
                    + "     join mandril_review_object_event mroe on(mroe.review_object_fk = mro.id)"
                    + "     join mandril_social_technology mst on(mro.reviewable_fk = mst.id)"
                    + "     join dblink('dbname=primata hostaddr=127.0.0.1 user=postgres password=postgres port=5432', 'select name,id from community') as pri(comunity character varying(255), id bigint) on (mro.community_id=pri.id)"
                    + "     where mroe.status = 'SUBMITTED' and"
                    + "     mroe.review_object_fk not in (select mr.review_object_fk from mandril_review_object_event mr where mr.status = 'PUBLISHED' and mr.review_object_fk = mroe.review_object_fk)"
                    + "     order by mroe.event_date DESC ");
            JSONArray jsona = JsonConverter.convert(RS);
            return jsona.toString(2);
        } catch (JSONException ex) {
            Logger.getLogger(PrimataResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PrimataResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("/feednews")
    @Produces("application/json")
    public String getFeedNews() throws SQLException {
        try {
            Connection Conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/primata", "postgres", "");
            Statement Stmt = Conn.createStatement();
            ResultSet RS = Stmt.executeQuery("select g1.name, "
                    + "(CASE WHEN c.parent_fk = 21 or c.parent_fk = 1 THEN c.name ELSE (select c2.name from community c2 where c.parent_fk = c2.id) END) as theme, "
                    + "g1.event_date, "
                    + "(select coalesce((select ud.nick from user_data ud where g1.submitter_login = ud.login), g1.submitter_login)) as nick, "
                    + "(select coalesce((select m.name from media m join profile p on (p.picture_fk = m.id) where g1.submitter_login = p.login),'default_filename')) as picname, "
                    + "(select coalesce((select m.type from media m join profile p on (p.picture_fk = m.id) where g1.submitter_login = p.login),'default_filetype')) as pictype, "
                    + "g1.submitter_login, 'Doc' as type, (select coalesce((select p.id from profile p where g1.submitter_login = p.login), 0)) as profile_id, "
                    + "g1.id as event_id, (CASE WHEN c.parent_fk = 21 or c.parent_fk = 1 THEN c.id ELSE (select c2.id from community c2 where c.parent_fk = c2.id) END) as theme_id "
                    + "from dblink('dbname=guigoh hostaddr=127.0.0.1 user=guigoh password=guigoh port=5432', "
                    + "'select d.id, d.name, dv.author, d.community_id, dv.date "
                    + "from teo2_doc d join teo2_doc_version dv on (dv.doc_fk = d.id)' "
                    + ") as g1(id bigint, name character varying(255), submitter_login text, community_id bigint, event_date timestamp without time zone) "
                    + "join community c on (community_id = c.id) "
                    + "union "
                    + "select t2.title as name,c2.name as theme,c1.submit_date as event_date,coalesce(u1.nick,u1.login),coalesce(m1.name,'default_filename') as picname, "
                    + "coalesce(m1.type,'default_filetype') as pictype,u1.login as submitter_login,'cTopic' as type,coalesce(p1.id,0) as profile_id,t2.id as event_id,c2.id as theme_id  from topic_comments t1 "
                    + "join topic t2 on(t1.topic_fk=t2.id) "
                    + "join comment c1 on(t1.comment_fk=c1.id) "
                    + "join profile p1 on(p1.id=c1.profile_fk) "
                    + "join media m1 on(p1.picture_fk = m1.id) "
                    + "join user_data u1 on(u1.login=p1.login) "
                    + "join community c2 on(c2.id=t2.community_fk) "
                    + "union "
                    + "select m1.name, c.name as theme, m1.event_date, "
                    + "(select coalesce((select ud.nick from user_data ud where m1.submitter_login = ud.login), m1.submitter_login)) as nick, "
                    + "(select coalesce((select m.name from media m join profile p on (p.picture_fk = m.id) where m1.submitter_login = p.login),'default_filename')) as picname, "
                    + "(select coalesce((select m.type from media m join profile p on (p.picture_fk = m.id) where m1.submitter_login = p.login),'default_filetype')) as pictype, "
                    + "m1.submitter_login, 'ST' as type, (select coalesce((select p.id from profile p where m1.submitter_login = p.login), 0)) as profile_id, "
                    + "m1.id as event_id, c.id as theme_id "
                    + "from dblink('dbname=mandril hostaddr=127.0.0.1 user=mandril password=mandril port=5432', "
                    + "'select ro.id, ro.title,ro.submitter_login,ro.community_id,mroe.event_date "
                    + "from mandril_review_object ro join mandril_review_object_event mroe on (mroe.review_object_fk = ro.id) "
                    + "where mroe.status = $$PUBLISHED$$' "
                    + ") as m1(id bigint, name character varying(255), submitter_login text, community_id bigint, event_date timestamp without time zone) "
                    + "join community c on (community_id = c.id) ");
            JSONArray jsona = JsonConverter.convert(RS);
            return jsona.toString(2);
        } catch (JSONException ex) {
            Logger.getLogger(PrimataResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PrimataResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("/lastreviewobjectsbytheme")
    @Produces("application/json")
    public String getLastReviewObjectsByTheme(@QueryParam("theme_id") Integer theme_id) throws SQLException {
        try {
            InterestsBO iBO = new InterestsBO();
            Interests theme = iBO.findInterestsByID(theme_id);
            Connection Conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mandril", "postgres", "postgres");
            Statement Stmt = Conn.createStatement();
            ResultSet RS = Stmt.executeQuery("select pri.comunity,mro.abstract,mro.tags,mro.id,mro.title,mro.submitter_login,mro.editor_login,to_char(mroe.event_date, 'DD/MM/YYYY'),mro.community_id,"
                    + "(select count(mc.id) from mandril_comment mc where mc.review_object_fk=mro.id) as content_quantity,"
                    + "(select mm.filename from mandril_media mm where mst.picture_fk=mm.id) as picture_filename,"
                    + "(select mm.type from mandril_media mm where mst.picture_fk=mm.id) as picture_type, "
                    + "(select 'http://cdn.guigoh.com/mandrildata/medias/'||mro.id||'/'||mx.filename||'.'||mx.type from mandril_media mx where mst.picture_fk=mx.id) as picture_url "
                    + "from mandril_review_object mro"
                    + "     join mandril_review_object_event mroe on(mroe.review_object_fk = mro.id)"
                    + "     join mandril_social_technology mst on(mro.reviewable_fk = mst.id)"
                    + "     join dblink('dbname=primata hostaddr=127.0.0.1 user=postgres password=postgres port=5432', 'select name,id from community') as pri(comunity character varying(255), id bigint) on (mro.community_id=pri.id)"
                    + "     where mroe.status = 'PUBLISHED' and UPPER(comunity) like '%" + theme.getName().toUpperCase() + "%'"
                    + "     order by mroe.event_date DESC ");
            JSONArray jsona = JsonConverter.convert(RS);
            return jsona.toString(2);
        } catch (JSONException ex) {
            Logger.getLogger(PrimataResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PrimataResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("/searchreviewobjects")
    @Produces("application/json")
    public String searchReviewObjects(@QueryParam("theme_id") Integer theme_id, @QueryParam("generalSearchInput") String generalSearchInput) throws JSONException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            InterestsBO iBO = new InterestsBO();
            Interests theme = iBO.findInterestsByID(theme_id);
            Connection Conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mandril", "postgres", "postgres");
            Statement Stmt = Conn.createStatement();
            ResultSet RS = Stmt.executeQuery("select pri.comunity,mro.abstract,mro.tags,mro.id,mro.title,mro.submitter_login,mro.editor_login,to_char(mroe.event_date, 'DD/MM/YYYY'),mro.community_id,"
                    + "(select count(mc.id) from mandril_comment mc where mc.review_object_fk=mro.id) as content_quantity,"
                    + "(select mm.filename from mandril_media mm where mst.picture_fk=mm.id) as picture_filename,"
                    + "(select mm.type from mandril_media mm where mst.picture_fk=mm.id) as picture_type, "
                    + "(select 'http://cdn.guigoh.com/mandrildata/medias/'||mro.id||'/'||mx.filename||'.'||mx.type from mandril_media mx where mst.picture_fk=mx.id) as picture_url "
                    + "from mandril_review_object mro"
                    + "     join mandril_review_object_event mroe on(mroe.review_object_fk = mro.id)"
                    + "     join mandril_social_technology mst on(mro.reviewable_fk = mst.id)"
                    + "     join dblink('dbname=primata hostaddr=127.0.0.1 user=postgres password=postgres port=5432', 'select name,id from community') as pri(comunity character varying(255), id bigint) on (mro.community_id=pri.id)"
                    + "     where mroe.status = 'PUBLISHED' and UPPER(comunity) like '%" + theme.getName().toUpperCase() + "%' and (UPPER(mro.title) like '%" + generalSearchInput.toUpperCase() + "%')"
                    + "     order by mroe.event_date DESC ");
            JSONArray jsona = JsonConverter.convert(RS);
            return jsona.toString(2);
        } catch (JSONException ex) {
            Logger.getLogger(PrimataResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PrimataResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("/searchreviewobjectsbytag")
    @Produces("application/json")
    public String searchReviewObjectsByTag(@QueryParam("theme_id") Integer theme_id, @QueryParam("tag") String tag) throws JSONException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            InterestsBO iBO = new InterestsBO();
            Interests theme = iBO.findInterestsByID(theme_id);
            Connection Conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mandril", "postgres", "postgres");
            Statement Stmt = Conn.createStatement();
            ResultSet RS = Stmt.executeQuery("select pri.comunity,mro.abstract,mro.tags,mro.id,mro.title,mro.submitter_login,mro.editor_login,to_char(mroe.event_date, 'DD/MM/YYYY'),mro.community_id,"
                    + "(select count(mc.id) from mandril_comment mc where mc.review_object_fk=mro.id) as content_quantity,"
                    + "(select mm.filename from mandril_media mm where mst.picture_fk=mm.id) as picture_filename,"
                    + "(select mm.type from mandril_media mm where mst.picture_fk=mm.id) as picture_type, "
                    + "(select 'http://cdn.guigoh.com/mandrildata/medias/'||mro.id||'/'||mx.filename||'.'||mx.type from mandril_media mx where mst.picture_fk=mx.id) as picture_url "
                    + "from mandril_review_object mro"
                    + "     join mandril_review_object_event mroe on(mroe.review_object_fk = mro.id)"
                    + "     join mandril_social_technology mst on(mro.reviewable_fk = mst.id)"
                    + "     join dblink('dbname=primata hostaddr=127.0.0.1 user=postgres password=postgres port=5432', 'select name,id from community') as pri(comunity character varying(255), id bigint) on (mro.community_id=pri.id)"
                    + "     where mroe.status = 'PUBLISHED' and UPPER(comunity) like '%" + theme.getName().toUpperCase() + "%' and (UPPER(mro.tags) like '%" + tag.toUpperCase() + "%')"
                    + "     order by mroe.event_date DESC ");
            JSONArray jsona = JsonConverter.convert(RS);
            return jsona.toString(2);
        } catch (JSONException ex) {
            Logger.getLogger(PrimataResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PrimataResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
        TagsBO tagsBO = new TagsBO();
        List<Tags> tagslist = tagsBO.findTagsByText(text);
        JSONArray tagsArray = new JSONArray();
        for (int i = 0; i < tagslist.size(); i++) {
            JSONObject tag = new JSONObject();
            tag.put("name", tagslist.get(i).getName());
            tag.put("id", tagslist.get(i).getId());
            tagsArray.put(i, tag);
        }
//        InterestsBO iBO = new InterestsBO();
//        Interests theme = iBO.findInterestsByID(theme_id);
//        Connection Conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mandril", "postgres", "postgres");
//        Statement Stmt = Conn.createStatement();
//        ResultSet RS = Stmt.executeQuery("select pri.comunity,mro.abstract,mro.tags,mro.id,mro.title,mro.submitter_login,mro.editor_login,to_char(mroe.event_date, 'DD/MM/YYYY'),mro.community_id,"
//                + "(select count(mc.id) from mandril_comment mc where mc.review_object_fk=mro.id) as content_quantity,"
//                + "(select mm.filename from mandril_media mm where mst.picture_fk=mm.id) as picture_filename,"
//                + "(select mm.type from mandril_media mm where mst.picture_fk=mm.id) as picture_type, "
//                + "(select 'http://cdn.guigoh.com/mandrildata/medias/'||mro.id||'/'||mx.filename||'.'||mx.type from mandril_media mx where mst.picture_fk=mx.id) as picture_url "
//                + "from mandril_review_object mro"
//                + "     join mandril_review_object_event mroe on(mroe.review_object_fk = mro.id)"
//                + "     join mandril_social_technology mst on(mro.reviewable_fk = mst.id)"
//                + "     join dblink('dbname=primata hostaddr=127.0.0.1 user=postgres password=postgres port=5432', 'select name,id from community') as pri(comunity character varying(255), id bigint) on (mro.community_id=pri.id)"
//                + "     where mroe.status = 'PUBLISHED' and UPPER(comunity) like '%" + theme.getName().toUpperCase() + "%' and (UPPER(mro.tags) like '%" + text.toUpperCase() + "%')"
//                + "     order by mroe.event_date DESC ");
//        JSONArray jsona = JsonConverter.convert(RS);
//        JSONArray jsona2 = jsona.getJSONArray(2);
//        for (int i = 0; i < jsona2.length(); i++) {
//            JSONObject jObject = jsona.getJSONObject(i);
//
//        }

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
