package com.guigoh.primata.entity;

import com.guigoh.primata.entity.DiscussionTopicFiles;
import com.guigoh.primata.entity.DiscussionTopicMsg;
import com.guigoh.primata.entity.Interests;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.Tags;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(DiscussionTopic.class)
public class DiscussionTopic_ { 

    public static volatile SingularAttribute<DiscussionTopic, Integer> id;
    public static volatile SingularAttribute<DiscussionTopic, String> body;
    public static volatile SingularAttribute<DiscussionTopic, String> title;
    public static volatile SingularAttribute<DiscussionTopic, SocialProfile> socialProfileId;
    public static volatile SingularAttribute<DiscussionTopic, Interests> themeId;
    public static volatile SingularAttribute<DiscussionTopic, Character> status;
    public static volatile SingularAttribute<DiscussionTopic, Date> data;
    public static volatile CollectionAttribute<DiscussionTopic, Tags> tagsCollection;
    public static volatile CollectionAttribute<DiscussionTopic, DiscussionTopicMsg> discussionTopicMsgCollection;
    public static volatile ListAttribute<DiscussionTopic, DiscussionTopicFiles> discussionTopicFilesList;

}