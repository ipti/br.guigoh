package com.guigoh.primata.entity;

import com.guigoh.primata.entity.EducationsName;
import com.guigoh.primata.entity.SocialProfile;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(Scholarity.class)
public class Scholarity_ { 

    public static volatile SingularAttribute<Scholarity, Integer> id;
    public static volatile CollectionAttribute<Scholarity, SocialProfile> socialProfileCollection;
    public static volatile SingularAttribute<Scholarity, String> description;
    public static volatile CollectionAttribute<Scholarity, EducationsName> educationsNameCollection;

}