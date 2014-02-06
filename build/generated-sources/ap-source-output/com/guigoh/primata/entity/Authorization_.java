package com.guigoh.primata.entity;

import com.guigoh.primata.entity.Users;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(Authorization.class)
public class Authorization_ { 

    public static volatile SingularAttribute<Authorization, Users> users;
    public static volatile SingularAttribute<Authorization, String> tokenId;
    public static volatile SingularAttribute<Authorization, String> status;
    public static volatile SingularAttribute<Authorization, String> roles;
    public static volatile SingularAttribute<Authorization, String> network;

}