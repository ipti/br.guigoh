package com.guigoh.primata.entity;

import com.guigoh.primata.entity.DiscussionTopic;
import com.guigoh.primata.entity.InterestsType;
import com.guigoh.primata.entity.SocialProfile;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(Interests.class)
public class Interests_ { 

    public static volatile SingularAttribute<Interests, Integer> id;
    public static volatile CollectionAttribute<Interests, SocialProfile> socialProfileCollection;
    public static volatile CollectionAttribute<Interests, DiscussionTopic> discussionTopicCollection;
    public static volatile SingularAttribute<Interests, String> name;
    public static volatile SingularAttribute<Interests, InterestsType> typeId;

}