package com.guflan.kingdomcraft.common.ebean.beans;

import com.guflan.kingdomcraft.api.domain.Kingdom;
import com.guflan.kingdomcraft.api.domain.Mail;
import io.ebean.Model;
import io.ebean.annotation.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "mail")
@Index(columnNames = {"mailId", "receiver"}, unique = true)
public class BMail extends Model implements Mail {

    @Id
    public long id;

    public int mailID;

    public String subject;

    public String sender;

    @ManyToOne
    @DbForeignKey(onDelete = ConstraintMode.CASCADE)
    public String receiver;

    public int priority;

    public boolean read;

    public String context;

    @WhenCreated
    public Date createdAt;

    @Override
    public int getMailID() {
        return mailID;
    }

    @Override
    public void setMailID(int mailID) {
        this.mailID = mailID;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String getSender() {
        return sender;
    }

    @Override
    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public String getReceiver() {
        return receiver;
    }

    @Override
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean isRead() {
        return read;
    }

    @Override
    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public String getContext() {
        return context;
    }

    @Override
    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public void getCreatedAt(Date createdAt){
        this.createdAt = createdAt;
    }

}
