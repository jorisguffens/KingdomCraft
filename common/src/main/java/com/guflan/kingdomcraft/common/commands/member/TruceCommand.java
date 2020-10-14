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

package com.guflan.kingdomcraft.common.commands.member;

import com.guflan.kingdomcraft.api.KingdomCraftHandler;
import com.guflan.kingdomcraft.api.domain.models.Kingdom;
import com.guflan.kingdomcraft.api.domain.models.Relation;
import com.guflan.kingdomcraft.api.domain.models.RelationType;
import com.guflan.kingdomcraft.api.domain.models.User;
import com.guflan.kingdomcraft.api.entity.CommandSender;
import com.guflan.kingdomcraft.api.entity.Player;
import com.guflan.kingdomcraft.common.command.DefaultCommandBase;

public class TruceCommand extends DefaultCommandBase {

    public TruceCommand(KingdomCraftHandler kdc) {
        super(kdc, "truce", 1, true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if ( !sender.hasPermission("kingdom.truce") ) {
            kdc.getMessageManager().send(sender, "noPermissionCmd");
            return;
        }

        User user = kdc.getUser((Player) sender);
        Kingdom kingdom = user.getKingdom();
        if ( kingdom == null ) {
            kdc.getMessageManager().send(sender, "cmdDefaultSenderNoKingdom");
            return;
        }

        Kingdom target = kdc.getKingdom(args[0]);
        if ( target == null ) {
            kdc.getMessageManager().send(sender, "cmdDefaultKingdomNotExist", args[0]);
            return;
        }

        Relation existing = kdc.getRelation(kingdom, target);
        if ( existing != null && existing.getType() == RelationType.TRUCE ) {
            kdc.getMessageManager().send(sender, "cmdTruceAlready", target.getName());
            return;
        }

        if ( existing == null || existing.getType() != RelationType.ENEMY ) {
            kdc.getMessageManager().send(sender, "cmdTruceNotEnemies", target.getName());
            return;
        }

        Relation request = kdc.getRelationRequest(target, kingdom);
        if ( request == null || request.getType() != RelationType.TRUCE ) {

            request = kdc.getRelationRequest(kingdom, target);
            if ( request != null && request.getType() == RelationType.TRUCE ) {
                kdc.getMessageManager().send(sender, "cmdTruceRequestAlready", target.getName());
                return;
            }

            kdc.addRelationRequest(kingdom, target, RelationType.TRUCE);
            kdc.getMessageManager().send(sender, "cmdTruceRequest", target.getName());

            for ( Player member : kdc.getOnlinePlayers() ) {
                if ( kdc.getUser(member).getKingdom() != target ) continue;
                kdc.getMessageManager().send(sender, "cmdTruceRequestTarget", kingdom.getName());
            }
            return;
        }

        kdc.removeRelationRequest(target, kingdom);
        kdc.setRelation(target, kingdom, RelationType.TRUCE);
        kdc.getMessageManager().send(sender, "cmdTruceAccepted", target.getName());

        for ( Player member : kdc.getOnlinePlayers() ) {
            if ( kdc.getUser(member).getKingdom() != target ) continue;
            kdc.getMessageManager().send(sender, "cmdTruceAccepted", kingdom.getName());
        }
    }
}
