package com.guigoh.primata.entity;

import com.guigoh.primata.entity.SocialProfile;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(DiscussionTopicWarnings.class)
public class DiscussionTopicWarnings_ { 

    public static volatile SingularAttribute<DiscussionTopicWarnings, Integer> id;
    public static volatile SingularAttribute<DiscussionTopicWarnings, SocialProfile> socialProfileId;
    public static volatile SingularAttribute<DiscussionTopicWarnings, Character> fkType;
    public static volatile SingularAttribute<DiscussionTopicWarnings, Integer> fkId;
    public static volatile SingularAttribute<DiscussionTopicWarnings, String> warnings;

}