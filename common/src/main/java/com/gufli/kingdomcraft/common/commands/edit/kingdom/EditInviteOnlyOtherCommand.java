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

package com.gufli.kingdomcraft.common.commands.edit.kingdom;

import com.gufli.kingdomcraft.api.domain.Kingdom;
import com.gufli.kingdomcraft.api.entity.PlatformPlayer;
import com.gufli.kingdomcraft.api.entity.PlatformSender;
import com.gufli.kingdomcraft.common.KingdomCraftImpl;
import com.gufli.kingdomcraft.common.command.CommandBase;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EditInviteOnlyOtherCommand extends CommandBase {

    public EditInviteOnlyOtherCommand(KingdomCraftImpl kdc) {
        super(kdc, "edit invite-only", 2);
        setArgumentsHint("<kingdom> [true/false]");
        setExplanationMessage(() -> kdc.getMessages().getMessage("cmdInviteOnlyOtherExplanation"));
        setPermissions("kingdom.edit.invite-only.other");
    }

    @Override
    public List<String> autocomplete(PlatformPlayer player, String[] args) {
        if ( args.length == 1 ) {
            return kdc.getKingdoms(true).stream().map(Kingdom::getName).collect(Collectors.toList());
        }
        if ( args.length == 2 ) {
            return Arrays.asList("true", "false");
        }
        return null;
    }

    @Override
    public void execute(PlatformSender sender, String[] args) {
        Kingdom kingdom = kdc.getKingdom(args[0]);
        if ( kingdom == null ) {
            kdc.getMessages().send(sender, "cmdErrorKingdomNotExist", args[0]);
            return;
        }

        if ( !args[1].equalsIgnoreCase("true")
                && !args[1].equalsIgnoreCase("false") ) {
            kdc.getMessages().send(sender, "errorInvalidBoolean", args[1]);
            return;
        }

        kingdom.setInviteOnly(Boolean.parseBoolean(args[1]));
        kdc.saveAsync(kingdom);

        kdc.getMessages().send(sender, "cmdEditOther", "invite-only", kingdom.getName(), kingdom.isInviteOnly() + "");
    }
}
