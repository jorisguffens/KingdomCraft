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

package com.gufli.kingdomcraft.common.commands.relations;

import com.gufli.kingdomcraft.api.domain.Kingdom;
import com.gufli.kingdomcraft.api.domain.Relation;
import com.gufli.kingdomcraft.api.domain.RelationType;
import com.gufli.kingdomcraft.api.domain.User;
import com.gufli.kingdomcraft.api.entity.PlatformSender;
import com.gufli.kingdomcraft.api.entity.PlatformPlayer;
import com.gufli.kingdomcraft.common.KingdomCraftImpl;
import com.gufli.kingdomcraft.common.command.CommandBase;

import java.util.List;
import java.util.stream.Collectors;

public class TruceCommand extends CommandBase {

    public TruceCommand(KingdomCraftImpl kdc) {
        super(kdc, "truce", 1, true);
        setArgumentsHint("<kingdom>");
        setExplanationMessage(() -> kdc.getMessages().getMessage("cmdTruceExplanation"));
        setPermissions("kingdom.truce");
    }

    @Override
    public List<String> autocomplete(PlatformPlayer player, String[] args) {
        if ( args.length == 1 ) {
            User user = kdc.getUser(player);
            Kingdom kingdom = user.getKingdom();
            if ( kingdom != null ) {
                return kdc.getKingdoms().stream().filter(k -> k != kingdom)
                        .filter(k -> {
                            Relation rel = kdc.getRelation(kingdom, k);
                            return rel != null && rel.getType() == RelationType.ENEMY;
                        })
                        .map(Kingdom::getName).collect(Collectors.toList());
            }
        }
        return null;
    }

    @Override
    public void execute(PlatformSender sender, String[] args) {
        User user = kdc.getUser((PlatformPlayer) sender);
        Kingdom kingdom = user.getKingdom();
        if ( kingdom == null ) {
            kdc.getMessages().send(sender, "cmdErrorSenderNoKingdom");
            return;
        }

        Kingdom target = kdc.getKingdom(args[0]);
        if ( target == null ) {
            kdc.getMessages().send(sender, "cmdErrorKingdomNotExist", args[0]);
            return;
        }

        if ( target == kingdom ) {
            kdc.getMessages().send(sender, "cmdErrorSameKingdom");
            return;
        }

        Relation existing = kdc.getRelation(kingdom, target);
        if ( existing != null && existing.getType() == RelationType.TRUCE ) {
            kdc.getMessages().send(sender, "cmdTruceAlready", target.getName());
            return;
        }

        if ( existing == null || existing.getType() != RelationType.ENEMY ) {
            kdc.getMessages().send(sender, "cmdTruceNotEnemies", target.getName());
            return;
        }

        Relation request = kdc.getRelationRequest(target, kingdom);
        if ( request == null || request.getType() != RelationType.TRUCE ) {

            request = kdc.getRelationRequest(kingdom, target);
            if ( request != null && request.getType() == RelationType.TRUCE ) {
                kdc.getMessages().send(sender, "cmdTruceRequestAlready", target.getName());
                return;
            }

            kdc.addRelationRequest(kingdom, target, RelationType.TRUCE);
            kdc.getMessages().send(sender, "cmdTruceRequest", target.getName());

            for ( PlatformPlayer member : kdc.getOnlinePlayers() ) {
                if ( kdc.getUser(member).getKingdom() != target || !member.has("kingdom.truce")) continue;
                kdc.getMessages().send(member, "cmdTruceRequestTarget", kingdom.getName());
            }
            return;
        }

        kdc.setRelation(target, kingdom, RelationType.TRUCE);

        for ( PlatformPlayer member : kdc.getOnlinePlayers() ) {
            Kingdom kd = member.getUser().getKingdom();
            if ( kd == kingdom ) {
                kdc.getMessages().send(member, "cmdTruce", target.getName());
            } else if ( kd == target ) {
                kdc.getMessages().send(member, "cmdTruce", kingdom.getName());
            }
        }
    }
}
