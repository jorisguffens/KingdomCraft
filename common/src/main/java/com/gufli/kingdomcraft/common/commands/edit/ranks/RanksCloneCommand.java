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

package com.gufli.kingdomcraft.common.commands.edit.ranks;

import com.gufli.kingdomcraft.api.domain.Kingdom;
import com.gufli.kingdomcraft.api.domain.Model;
import com.gufli.kingdomcraft.api.domain.Rank;
import com.gufli.kingdomcraft.api.entity.PlatformPlayer;
import com.gufli.kingdomcraft.api.entity.PlatformSender;
import com.gufli.kingdomcraft.common.KingdomCraftImpl;
import com.gufli.kingdomcraft.common.command.CommandBase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RanksCloneCommand extends CommandBase {

    public RanksCloneCommand(KingdomCraftImpl kdc) {
        super(kdc, "ranks clone", 3, true);
        setArgumentsHint("<from-kingdom> <to-kingdom> <rank>");
        setExplanationMessage(() -> kdc.getMessages().getMessage("cmdRanksCloneExplanation"));
        setPermissions("kingdom.ranks.clone");
    }

    @Override
    public List<String> autocomplete(PlatformPlayer sender, String[] args) {
        if ( args.length == 1 ) {
            return kdc.getKingdoms().stream().map(Kingdom::getName).collect(Collectors.toList());
        }
        if ( args.length == 2 ) {
            Kingdom kingdom = kdc.getKingdom(args[0]);
            if ( kingdom == null ) {
                return null;
            }
            return kdc.getKingdoms().stream().filter(k -> k != kingdom).map(Kingdom::getName).collect(Collectors.toList());
        }
        if ( args.length == 3 ) {
            Kingdom from = kdc.getKingdom(args[0]);
            if ( from == null || kdc.getKingdom(args[1]) == null ) {
                return null;
            }
            return from.getRanks().stream().map(Rank::getName).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void execute(PlatformSender sender, String[] args) {
        Kingdom from = kdc.getKingdom(args[0]);
        if ( from == null ) {
            kdc.getMessages().send(sender, "cmdErrorKingdomNotExist", args[0]);
            return;
        }

        Kingdom to = kdc.getKingdom(args[1]);
        if ( to == null ) {
            kdc.getMessages().send(sender, "cmdErrorKingdomNotExist", args[1]);
            return;
        }

        Rank rank = from.getRank(args[2]);
        if ( rank == null ) {
            kdc.getMessages().send(sender, "cmdErrorRankNotExist", args[2]);
            return;
        }

        Rank clone = rank.clone(to, true);

        List<Model> models = new ArrayList<>();
        models.add(clone);
        models.addAll(rank.getPermissionGroups());
        models.addAll(rank.getAttributes());
        kdc.saveAsync(models);

        kdc.getMessages().send(sender, "cmdRanksClone", rank.getName(), from.getName(), to.getName());
    }
}
