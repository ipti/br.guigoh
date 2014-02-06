package com.guigoh.primata.entity;

import com.guigoh.primata.entity.ContactType;
import com.guigoh.primata.entity.UserContactInfoPK;
import com.guigoh.primata.entity.Users;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(UserContactInfo.class)
public class UserContactInfo_ { 

    public static volatile SingularAttribute<UserContactInfo, Users> users;
    public static volatile SingularAttribute<UserContactInfo, UserContactInfoPK> userContactInfoPK;
    public static volatile SingularAttribute<UserContactInfo, String> value;
    public static volatile SingularAttribute<UserContactInfo, ContactType> contactType;

}