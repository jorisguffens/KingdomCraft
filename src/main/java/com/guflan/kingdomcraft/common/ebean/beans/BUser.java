package com.guflan.kingdomcraft.common.ebean.beans;

import com.guflan.kingdomcraft.api.domain.Kingdom;
import com.guflan.kingdomcraft.api.domain.User;
import com.guflan.kingdomcraft.api.domain.Rank;
import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class BUser extends Model implements User {

    @Id
    public String id;

    @Column(unique=true)
    public String name;

    public BRank rank;

    public BKingdom kingdom;

    @OneToMany(mappedBy = "user")
    public Set<BKingdomInvite> kingdomInvites;

    @WhenCreated
    public Instant createdAt;

    @WhenModified
    public Instant updatedAt;

    // interface

    @Override
    public UUID getUniqueId() {
        return UUID.fromString(id);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Rank getRank() {
        return rank;
    }

    @Override
    public Kingdom getKingdom() {
        return rank != null ? rank.getKingdom() : null;
    }

    @Override
    public void setKingdom(Kingdom kingdom) {
        if ( kingdom == null ) {
            this.kingdom = null;
            this.rank = null;
        } else {
            this.kingdom = (BKingdom) kingdom;
            this.rank = (BRank) kingdom.getDefaultRank();
        }
    }

    @Override
    public void setRank(Rank rank) {
        if ( rank.getKingdom() != kingdom ) {
            return; // TODO throw exception
        }
        this.rank = (BRank) rank;
    }

    @Override
    public boolean hasInvite(Kingdom kingdom) {
        for ( BKingdomInvite invite : kingdomInvites ) {
            if ( invite.kingdom == kingdom && invite.user.getKingdom() == kingdom ) {
                return true; // TODO time check
            }
        }
        return false;
    }

    @Override
    public void addInvite(User sender) {
        if ( sender.getKingdom() == null ) {
            return;
        }

        BKingdomInvite invite = new BKingdomInvite();
        invite.kingdom = (BKingdom) sender.getKingdom();
        invite.sender = (BUser) sender;
        invite.user = this;

        this.kingdomInvites.add(invite);
    }

}