package com.guflan.kingdomcraft.common.ebean.beans;

import com.guflan.kingdomcraft.api.domain.Kingdom;
import com.guflan.kingdomcraft.api.domain.Mail;
import io.ebean.Model;
import io.ebean.annotation.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mail")
@Index(columnNames = {"mailId", "reciever"}, unique = true)
public class BMail extends Model implements Mail {

    @Id
    public long id;

    public int mailId;

    public String subject;

    public Kingdom sender;

    public Kingdom receiver;

    public int priority;

    public boolean read;

    public String context;

    @Override
    public int getMailId() {
        return mailId;
    }

    public void setMailId(int mailId) {
        this.mailId = mailId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Kingdom getSender() {
        return sender;
    }

    public void setSender(Kingdom sender) {
        this.sender = sender;
    }

    public Kingdom getReceiver() {
        return receiver;
    }

    public void setReceiver(Kingdom receiver) {
        this.receiver = receiver;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
