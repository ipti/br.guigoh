package com.guigoh.primata.entity;

import com.guigoh.primata.entity.SocialProfile;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(Language.class)
public class Language_ { 

    public static volatile SingularAttribute<Language, Integer> id;
    public static volatile SingularAttribute<Language, String> acronym;
    public static volatile CollectionAttribute<Language, SocialProfile> socialProfileCollection;
    public static volatile SingularAttribute<Language, String> description;

}