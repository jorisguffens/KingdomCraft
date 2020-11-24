package com.guflan.kingdomcraft.api.domain;

import java.util.Date;

public interface Mail extends Model {

    int getMailID();

    void setMailID(int mailID);

    String getSubject();

    void setSubject(String subject);

    String getSender();

    void setSender(String sender);

    String getReceiver();

    void setReceiver(String receiver);

    int getPriority();

    void setPriority(int priority);

    boolean isRead();

    void setRead(boolean read);

    String getContext();

    void setContext(String context);

    void getCreatedAt(Date createdAt);
}
