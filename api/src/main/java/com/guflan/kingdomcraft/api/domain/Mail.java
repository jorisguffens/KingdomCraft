package com.guflan.kingdomcraft.api.domain;

public interface Mail extends Model {

    int getMailId();

    void setMailId(int mailId);

    String getSubject();

    void setSubject(String subject);

    void setSender(Kingdom sender);

    Kingdom getReceiver();

    void setReceiver(Kingdom receiver);

    int getPriority();

    void setPriority(int priority);

    boolean isRead();

    void setRead(boolean read);

    String getContext();

    void setContext(String context);
}
