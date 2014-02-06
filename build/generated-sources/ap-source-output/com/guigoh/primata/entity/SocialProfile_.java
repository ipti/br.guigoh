package com.guigoh.primata.entity;

import com.guigoh.primata.entity.Availability;
import com.guigoh.primata.entity.City;
import com.guigoh.primata.entity.Country;
import com.guigoh.primata.entity.DiscussionTopic;
import com.guigoh.primata.entity.DiscussionTopicMsg;
import com.guigoh.primata.entity.DiscussionTopicWarnings;
import com.guigoh.primata.entity.Educations;
import com.guigoh.primata.entity.Experiences;
import com.guigoh.primata.entity.Interests;
import com.guigoh.primata.entity.Language;
import com.guigoh.primata.entity.Occupations;
import com.guigoh.primata.entity.Scholarity;
import com.guigoh.primata.entity.SocialProfileVisibility;
import com.guigoh.primata.entity.State;
import com.guigoh.primata.entity.Subnetwork;
import com.guigoh.primata.entity.Users;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(SocialProfile.class)
public class SocialProfile_ { 

    public static volatile SingularAttribute<SocialProfile, Users> users;
    public static volatile SingularAttribute<SocialProfile, String> phone;
    public static volatile SingularAttribute<SocialProfile, Subnetwork> subnetworkId;
    public static volatile SingularAttribute<SocialProfile, City> cityId;
    public static volatile SingularAttribute<SocialProfile, String> complement;
    public static volatile SingularAttribute<SocialProfile, String> tokenId;
    public static volatile SingularAttribute<SocialProfile, String> description;
    public static volatile CollectionAttribute<SocialProfile, Experiences> experiencesCollection;
    public static volatile SingularAttribute<SocialProfile, String> name;
    public static volatile SingularAttribute<SocialProfile, Availability> availabilityId;
    public static volatile SingularAttribute<SocialProfile, Character> gender;
    public static volatile CollectionAttribute<SocialProfile, DiscussionTopicWarnings> discussionTopicWarningsCollection;
    public static volatile CollectionAttribute<SocialProfile, Interests> interestsCollection;
    public static volatile SingularAttribute<SocialProfile, Country> countryId;
    public static volatile SingularAttribute<SocialProfile, Integer> socialProfileId;
    public static volatile SingularAttribute<SocialProfile, String> zipcode;
    public static volatile SingularAttribute<SocialProfile, String> phoneAlternative;
    public static volatile SingularAttribute<SocialProfile, String> neighborhood;
    public static volatile SingularAttribute<SocialProfile, String> number;
    public static volatile SingularAttribute<SocialProfile, Scholarity> scholarityId;
    public static volatile SingularAttribute<SocialProfile, Occupations> occupationsId;
    public static volatile SingularAttribute<SocialProfile, Language> languageId;
    public static volatile SingularAttribute<SocialProfile, String> photo;
    public static volatile CollectionAttribute<SocialProfile, Educations> educationsCollection;
    public static volatile SingularAttribute<SocialProfile, String> address;
    public static volatile SingularAttribute<SocialProfile, State> stateId;
    public static volatile SingularAttribute<SocialProfile, SocialProfileVisibility> socialProfileVisibility;
    public static volatile CollectionAttribute<SocialProfile, DiscussionTopic> discussionTopicCollection;
    public static volatile CollectionAttribute<SocialProfile, DiscussionTopicMsg> discussionTopicMsgCollection;

}