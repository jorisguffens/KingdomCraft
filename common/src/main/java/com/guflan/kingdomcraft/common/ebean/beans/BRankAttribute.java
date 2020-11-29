/*
 * This file is part of KingdomCraft.
 *
 * KingdomCraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KingdomCraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with KingdomCraft. If not, see <https://www.gnu.org/licenses/>.
 */

package com.guflan.kingdomcraft.common.ebean.beans;

import com.guflan.kingdomcraft.api.domain.Rank;
import com.guflan.kingdomcraft.api.domain.RankAttribute;
import io.ebean.Model;
import io.ebean.annotation.ConstraintMode;
import io.ebean.annotation.DbForeignKey;
import io.ebean.annotation.DbName;
import io.ebean.annotation.Index;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Index(unique = true, columnNames = {"rank_id", "name"})
@Table(name = "rank_attributes")
public class BRankAttribute extends BaseModel implements RankAttribute {

    @Id
    public int id;

    @ManyToOne
    @DbForeignKey(onDelete = ConstraintMode.CASCADE)
    public BRank rank;

    public String name;
    public String value;

    //

    public BRankAttribute() {
        super();
    }

    @Override
    public boolean delete() {
        rank.attributes.remove(this);
        return super.delete();
    }

    @Override
    public void save() {
        super.save();
    }

    // interface

    @Override
    public Rank getRank() {
        return rank;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
