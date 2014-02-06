package com.guigoh.primata.entity;

import com.guigoh.primata.entity.Educations;
import com.guigoh.primata.entity.Scholarity;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(EducationsName.class)
public class EducationsName_ { 

    public static volatile SingularAttribute<EducationsName, Integer> id;
    public static volatile CollectionAttribute<EducationsName, Educations> educationsCollection;
    public static volatile SingularAttribute<EducationsName, String> name;
    public static volatile SingularAttribute<EducationsName, Scholarity> scholarityId;

}