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

package com.gufli.kingdomcraft.common.commands.spawn;

import com.gufli.kingdomcraft.api.domain.Kingdom;
import com.gufli.kingdomcraft.api.entity.PlatformLocation;
import com.gufli.kingdomcraft.api.entity.PlatformPlayer;
import com.gufli.kingdomcraft.api.entity.PlatformSender;
import com.gufli.kingdomcraft.common.KingdomCraftImpl;
import com.gufli.kingdomcraft.common.command.CommandBase;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

public class TpAllSpawnCommand extends CommandBase {

    public TpAllSpawnCommand(KingdomCraftImpl kdc) {
        super(kdc, "tpallspawn", 0);
        setExplanationMessage(() -> kdc.getMessages().getMessage("cmdTpAllSpawnExplanation"));
        setPermissions("kingdom.tpallspawn");
    }

    @Override
    public List<String> autocomplete(PlatformPlayer sender, String[] args) {
        return null;
    }

    @Override
    public void execute(PlatformSender sender, String[] args) {
        Kingdom kingdom = ((PlatformPlayer) sender).getUser().getKingdom();
        if ( kingdom == null ) {
            kdc.getMessages().send(sender, "cmdErrorSenderNoKingdom");
            return;
        }

        PlatformLocation loc = kingdom.getSpawn();
        if ( loc == null ) {
            kdc.getMessages().send(sender, "cmdSpawnNotExists");
            return;
        }

        kdc.getMessages().send(sender, "cmdTpAllSpawn", kingdom.getName());

        for ( PlatformPlayer pp : kdc.getOnlinePlayers() ) {
            if ( !pp.getUser().getKingdom().equals(kingdom) ) {
                continue;
            }

            pp.teleport(loc);
            kdc.getMessages().send(pp, "cmdTpAllSpawnTarget", kingdom.getName());
        }
    }
}
