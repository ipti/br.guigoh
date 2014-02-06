package com.guigoh.primata.entity;

import com.guigoh.primata.entity.UserContactInfo;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(ContactType.class)
public class ContactType_ { 

    public static volatile SingularAttribute<ContactType, Integer> id;
    public static volatile CollectionAttribute<ContactType, UserContactInfo> userContactInfoCollection;
    public static volatile SingularAttribute<ContactType, String> logo;
    public static volatile SingularAttribute<ContactType, String> name;

}