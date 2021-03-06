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

package com.gufli.kingdomcraft.common.commands.admin;

import com.gufli.kingdomcraft.api.domain.Kingdom;
import com.gufli.kingdomcraft.api.domain.User;
import com.gufli.kingdomcraft.api.entity.PlatformSender;
import com.gufli.kingdomcraft.api.entity.PlatformPlayer;
import com.gufli.kingdomcraft.common.KingdomCraftImpl;
import com.gufli.kingdomcraft.common.command.CommandBase;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class SetKingdomCommand extends CommandBase {

    public SetKingdomCommand(KingdomCraftImpl kdc) {
        super(kdc, "setkingdom", 2);
        setArgumentsHint("<player> <kingdom>");
        setExplanationMessage(() -> kdc.getMessages().getMessage("cmdSetKingdomExplanation"));
        setPermissions("kingdom.setkingdom");
    }

    @Override
    public List<String> autocomplete(PlatformPlayer sender, String[] args) {
        if ( args.length == 1 ) {
            return kdc.getOnlinePlayers().stream().map(PlatformPlayer::getName).collect(Collectors.toList());
        }
        if ( args.length == 2 ) {
            return kdc.getKingdoms().stream().map(Kingdom::getName).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void execute(PlatformSender sender, String[] args) {
        kdc.getPlugin().getScheduler().executeAsync(() -> {
            try {
                User target = kdc.getUser(args[0]).get();
                if ( target == null ) {
                    kdc.getMessages().send(sender, "cmdErrorPlayerNotExist", args[0]);
                    return;
                }

                Kingdom kingdom = kdc.getKingdom(args[1]);
                if ( kingdom == null || kingdom.isTemplate() ) {
                    kdc.getMessages().send(sender, "cmdErrorKingdomNotExist", args[1]);
                    return;
                }

                if ( target.getKingdom() == kingdom ) {
                    kdc.getMessages().send(sender, "cmdSetKingdomAlready", target.getName(), kingdom.getName());
                    return;
                }

                target.setKingdom(kingdom);
                kdc.saveAsync(target);

                PlatformPlayer tplayer = kdc.getPlayer(target);
                if ( tplayer != null ) {
                    kdc.getMessages().send(tplayer, "cmdSetKingdomTarget", kingdom.getName());
                }

                kdc.getMessages().send(sender, "cmdSetKingdomSender", target.getName(), kingdom.getName());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }
}
