package com.guigoh.primata.entity;

import com.guigoh.primata.entity.Authorization;
import com.guigoh.primata.entity.EmailActivation;
import com.guigoh.primata.entity.Friends;
import com.guigoh.primata.entity.SecretQuestion;
import com.guigoh.primata.entity.SocialProfile;
import com.guigoh.primata.entity.UserContactInfo;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(Users.class)
public class Users_ { 

    public static volatile CollectionAttribute<Users, UserContactInfo> userContactInfoCollection;
    public static volatile SingularAttribute<Users, EmailActivation> emailActivation;
    public static volatile SingularAttribute<Users, String> secretAnswer;
    public static volatile SingularAttribute<Users, String> alias;
    public static volatile SingularAttribute<Users, String> status;
    public static volatile SingularAttribute<Users, SocialProfile> socialProfile;
    public static volatile CollectionAttribute<Users, Friends> friendsCollection;
    public static volatile SingularAttribute<Users, Authorization> authorization;
    public static volatile SingularAttribute<Users, String> password;
    public static volatile SingularAttribute<Users, String> username;
    public static volatile CollectionAttribute<Users, Friends> friendsCollection1;
    public static volatile CollectionAttribute<Users, Friends> friendsCollection2;
    public static volatile SingularAttribute<Users, String> token;
    public static volatile SingularAttribute<Users, SecretQuestion> secretQuestionId;

}