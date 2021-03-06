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

package com.gufli.kingdomcraft.common.commands.general;

import com.gufli.kingdomcraft.api.domain.Kingdom;
import com.gufli.kingdomcraft.api.domain.KingdomInvite;
import com.gufli.kingdomcraft.api.domain.User;
import com.gufli.kingdomcraft.api.entity.PlatformPlayer;
import com.gufli.kingdomcraft.api.entity.PlatformSender;
import com.gufli.kingdomcraft.common.KingdomCraftImpl;
import com.gufli.kingdomcraft.common.command.CommandBase;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class JoinCommand extends CommandBase {

    public JoinCommand(KingdomCraftImpl kdc) {
        super(kdc, "join", 1, true);
        setArgumentsHint("<kingdom>");
        setExplanationMessage(() -> kdc.getMessages().getMessage("cmdJoinExplanation"));
        setPermissions("kingdom.join");
    }

    @Override
    public List<String> autocomplete(PlatformPlayer sender, String[] args) {
        return kdc.getKingdoms().stream().map(Kingdom::getName).collect(Collectors.toList());
    }

    @Override
    public void execute(PlatformSender sender, String[] args) {
        Kingdom kingdom = kdc.getKingdom(args[0]);
        if ( kingdom == null || kingdom.isTemplate() ) {
            kdc.getMessages().send(sender, "cmdErrorKingdomNotExist", args[0]);
            return;
        }

        User user = kdc.getUser((PlatformPlayer) sender);
        if ( user.getKingdom() != null ) {
            kdc.getMessages().send(sender, "cmdJoinAlready");
            return;
        }

        if ( kingdom.isInviteOnly() && !sender.hasPermission("kingdom.join." + kingdom.getName().toLowerCase())) {
            KingdomInvite invite = user.getInvite(kingdom);
            if ( invite == null || !invite.isValid() ) {
                kdc.getMessages().send(sender, "cmdJoinNoInvite", kingdom.getName());

                kdc.getOnlinePlayers().stream()
                        .filter(p -> p.getUser().getKingdom() == kingdom)
                        .filter(p -> p.hasPermission("kingdom.invite"))
                        .filter(p -> !p.has("INVITE_MEMBER_" + user.getName())
                                || p.get("INVITE_MEMBER_" + user.getName(), Long.class) < System.currentTimeMillis())
                        .forEach(p -> {
                            p.set("INVITE_MEMBER_" + user.getName(), System.currentTimeMillis() + 15000);
                            kdc.getMessages().send(p, "cmdJoinNoInviteMembers", user.getName());
                        });
                return;
            }
        }

        if ( kingdom.getMaxMembers() > 0 && kingdom.getMaxMembers() <= kingdom.getMemberCount() ) {
            kdc.getMessages().send(sender, "cmdJoinFull", kingdom.getName());
            return;
        }

        if ( kingdom.getDefaultRank() != null && kingdom.getDefaultRank().getMaxMembers() > 0
                && kingdom.getDefaultRank().getMaxMembers() <= kingdom.getDefaultRank().getMemberCount() ) {
            kdc.getMessages().send(sender, "cmdJoinFull", kingdom.getName());
            return;
        }

        user.setKingdom(kingdom);

        kdc.saveAsync(user).thenRun(user::clearInvites).exceptionally(ex -> {
            kdc.getPlugin().log(ex.getMessage(), Level.SEVERE);
            return null;
        });

        kdc.getMessages().send(sender, "cmdJoin", kingdom.getName());

        for ( PlatformPlayer p : kdc.getOnlinePlayers() ) {
            if ( p.equals(sender) || kdc.getUser(p).getKingdom() != kingdom ) continue;
            kdc.getMessages().send(p, "cmdJoinMembers", user.getName());
        }
    }
}
