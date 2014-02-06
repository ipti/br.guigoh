package com.guigoh.primata.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(MessengerMessages.class)
public class MessengerMessages_ { 

    public static volatile SingularAttribute<MessengerMessages, Integer> id;
    public static volatile SingularAttribute<MessengerMessages, String> message;
    public static volatile SingularAttribute<MessengerMessages, Integer> socialProfileIdReceiver;
    public static volatile SingularAttribute<MessengerMessages, Character> messageDelivered;
    public static volatile SingularAttribute<MessengerMessages, Integer> socialProfileIdSender;
    public static volatile SingularAttribute<MessengerMessages, Date> messageDate;

}