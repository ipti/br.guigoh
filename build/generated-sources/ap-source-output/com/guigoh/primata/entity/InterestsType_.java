package com.guigoh.primata.entity;

import com.guigoh.primata.entity.Interests;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(InterestsType.class)
public class InterestsType_ { 

    public static volatile SingularAttribute<InterestsType, Integer> id;
    public static volatile SingularAttribute<InterestsType, String> name;
    public static volatile SingularAttribute<InterestsType, String> type;
    public static volatile CollectionAttribute<InterestsType, Interests> interestsCollection;

}