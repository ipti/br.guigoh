package com.guigoh.primata.entity;

import com.guigoh.primata.entity.DiscussionTopic;
import com.guigoh.primata.entity.DiscussionTopicFiles;
import com.guigoh.primata.entity.SocialProfile;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(DiscussionTopicMsg.class)
public class DiscussionTopicMsg_ { 

    public static volatile SingularAttribute<DiscussionTopicMsg, Integer> id;
    public static volatile SingularAttribute<DiscussionTopicMsg, SocialProfile> socialProfileId;
    public static volatile SingularAttribute<DiscussionTopicMsg, Character> status;
    public static volatile SingularAttribute<DiscussionTopicMsg, Date> data;
    public static volatile SingularAttribute<DiscussionTopicMsg, String> reply;
    public static volatile SingularAttribute<DiscussionTopicMsg, DiscussionTopic> discussionTopicId;
    public static volatile ListAttribute<DiscussionTopicMsg, DiscussionTopicFiles> discussionTopicFilesList;

}