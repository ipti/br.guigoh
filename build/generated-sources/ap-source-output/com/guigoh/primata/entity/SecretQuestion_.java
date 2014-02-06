package com.guigoh.primata.entity;

import com.guigoh.primata.entity.Users;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(SecretQuestion.class)
public class SecretQuestion_ { 

    public static volatile SingularAttribute<SecretQuestion, Integer> id;
    public static volatile SingularAttribute<SecretQuestion, String> name;
    public static volatile CollectionAttribute<SecretQuestion, Users> usersCollection;

}