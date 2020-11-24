/*
 * This file is part of KingdomCraft.
 *
 * KingdomCraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KingdomCraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with KingdomCraft. If not, see <https://www.gnu.org/licenses/>.
 */

package com.guflan.kingdomcraft.common.ebean.beans;

import com.guflan.kingdomcraft.api.domain.*;
import com.guflan.kingdomcraft.api.entity.PlatformLocation;
import com.guflan.kingdomcraft.common.ebean.StorageContext;
import com.guflan.kingdomcraft.common.ebean.beans.query.QBUser;
import io.ebean.Model;
import io.ebean.annotation.*;
import io.ebean.annotation.ConstraintMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "kingdoms")
public class BKingdom extends Model implements Kingdom {

    @Id
    public long id;

    @Column(unique=true)
    public String name;

    public String display;
    public String prefix;
    public String suffix;

    public PlatformLocation spawn;

    public boolean inviteOnly;
    public int maxMembers;

    @OneToOne
    @DbForeignKey(onDelete = ConstraintMode.SET_NULL)
    public BRank defaultRank;

    @OneToMany(mappedBy = "kingdom", fetch = FetchType.EAGER)
    public List<BRank> ranks;

    @OneToMany(mappedBy = "kingdom", fetch = FetchType.EAGER)
    public List<BKingdomAttribute> attributes;

    @OneToMany(mappedBy = "kingdom", fetch = FetchType.EAGER)
    public List<BMail> mails;

    @Formula(select = "(select count(id) from users where kingdom_id = t0.id)")
    public int memberCount;

    @WhenCreated
    Date createdAt;

    @WhenModified
    Date updatedAt;

    //

    @Override
    public boolean delete() {
        StorageContext.kingdoms.remove(this);
        return super.delete();
    }

    @Override
    public void save() {
        super.save();
    }

    // interface

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void renameTo(String name) {
        if (StorageContext.kingdoms.stream().anyMatch(k -> k.getName().equalsIgnoreCase(name))) {
            throw new IllegalArgumentException("A kingdom with that name already exists.");
        }
        this.name = name;
    }

    @Override
    public String getDisplay() {
        return display != null ? display : name;
    }

    @Override
    public void setDisplay(String display) {
        this.display = display;
    }

    @Override
    public String getPrefix() {
        return prefix != null ? prefix : "";
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String getSuffix() {
        return suffix != null ? suffix : "";
    }

    @Override
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public boolean isInviteOnly() {
        return inviteOnly;
    }

    @Override
    public void setInviteOnly(boolean inviteOnly) {
        this.inviteOnly = inviteOnly;
    }

    @Override
    public int getMaxMembers() {
        return maxMembers;
    }

    @Override
    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    @Override
    public Rank getDefaultRank() {
        return defaultRank;
    }

    @Override
    public void setDefaultRank(Rank defaultRank) {
        if ( !this.ranks.contains(defaultRank) ) {
            throw new IllegalArgumentException("The given defaultRank does not belong to this kingdom.");
        }
        this.defaultRank = (BRank) defaultRank;
    }

    @Override
    public List<Rank> getRanks() {
        return new ArrayList<>(ranks);
    }

    @Override
    public Rank getRank(String name) {
        return ranks.stream().filter(r -> r.name.equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public Rank createRank(String name) {
        if ( getRank(name) != null ) {
            throw new IllegalArgumentException("A rank with that name already exists.");
        }
        BRank rank = new BRank();
        rank.name = name;
        rank.kingdom = this;

        ranks.add(rank);
        return rank;
    }

    @Override
    public KingdomAttribute getAttribute(String name) {
        return attributes.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public KingdomAttribute createAttribute(String name) {
        return attributes.stream().filter(p -> p.getName().equals(name)).findFirst().orElseGet(() -> {
            BKingdomAttribute attribute = new BKingdomAttribute();
            attribute.kingdom = this;
            attribute.name = name;

            attributes.add(attribute);
            return attribute;
        });
    }


    @Override
    public Mail getMail(int mailId){
        return mails.get(mailId);
    }

    @Override
    public List<Mail> getMails(){
        return new ArrayList<>(mails);
    }

    @Override
    public void createMail(Mail mail) {
        mails.add((BMail) mail);
        //Not sure if this cast works with ebeans.

    }

    @Override
    public PlatformLocation getSpawn() {
        return this.spawn;
    }

    @Override
    public void setSpawn(PlatformLocation location) {
        this.spawn = location;
    }

    @Override
    public int getMemberCount() {
        return memberCount;
    }

    @Override
    public List<String> getMembers() {
        return new QBUser().kingdom.eq(this).select("name").findSingleAttributeList();
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }
}
