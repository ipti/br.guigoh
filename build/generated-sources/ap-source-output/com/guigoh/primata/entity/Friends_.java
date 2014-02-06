package com.guigoh.primata.entity;

import com.guigoh.primata.entity.FriendsPK;
import com.guigoh.primata.entity.Users;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(Friends.class)
public class Friends_ { 

    public static volatile SingularAttribute<Friends, String> message;
    public static volatile SingularAttribute<Friends, Integer> recommenders;
    public static volatile SingularAttribute<Friends, Users> tokenRecommender;
    public static volatile SingularAttribute<Friends, String> status;
    public static volatile SingularAttribute<Friends, Users> tokenFriend1;
    public static volatile SingularAttribute<Friends, Users> tokenFriend2;
    public static volatile SingularAttribute<Friends, FriendsPK> friendsPK;

}