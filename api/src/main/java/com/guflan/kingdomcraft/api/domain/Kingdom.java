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

package com.guflan.kingdomcraft.api.domain;

import com.guflan.kingdomcraft.api.entity.PlatformLocation;

import java.util.Date;
import java.util.List;

public interface Kingdom extends Model {

    String getName();

    void renameTo(String name);

    String getDisplay();

    void setDisplay(String display);

    String getPrefix();

    void setPrefix(String prefix);

    String getSuffix();

    void setSuffix(String suffix);

    boolean isInviteOnly();

    void setInviteOnly(boolean inviteOnly);

    int getMaxMembers();

    void setMaxMembers(int maxMembers);

    Rank getDefaultRank();

    void setDefaultRank(Rank defaultRank);

    List<Rank> getRanks();

    Rank getRank(String name);

    Rank createRank(String name);

    KingdomAttribute getAttribute(String name);

    KingdomAttribute createAttribute(String name);

    Mail getMail(int mailId);

    List<Mail> getMails();

    void createMail(Mail mail);

    PlatformLocation getSpawn();

    void setSpawn(PlatformLocation location);

    int getMemberCount();

    List<String> getMembers();

    Date getCreatedAt();

}
