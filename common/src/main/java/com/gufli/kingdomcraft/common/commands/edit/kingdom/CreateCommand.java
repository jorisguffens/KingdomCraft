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
import com.gufli.kingdomcraft.api.domain.Rank;
import com.gufli.kingdomcraft.api.domain.User;
import com.gufli.kingdomcraft.api.entity.PlatformSender;
import com.gufli.kingdomcraft.api.entity.PlatformPlayer;
import com.gufli.kingdomcraft.common.KingdomCraftImpl;
import com.gufli.kingdomcraft.common.command.CommandBase;

import java.util.Comparator;
import java.util.concurrent.CompletableFuture;

public class CreateCommand extends CommandBase {

    public CreateCommand(KingdomCraftImpl kdc) {
        super(kdc, "create", 1);
        setArgumentsHint("<name>");
        setExplanationMessage(() -> kdc.getMessages().getMessage("cmdCreateExplanation"));
        setPermissions("kingdom.create", "kingdom.create.other");
    }

    @Override
    public void execute(PlatformSender sender, String[] args) {
        if (!args[0].matches("[a-zA-Z0-9]+") || args[0].length() > 24) {
            kdc.getMessages().send(sender, "cmdErrorInvalidName");
            return;
        }

        if (kdc.getKingdom(args[0]) != null) {
            kdc.getMessages().send(sender, "cmdErrorKingdomAlreadyExists", args[0]);
            return;
        }

        if (sender instanceof PlatformPlayer && kdc.getUser((PlatformPlayer) sender).getKingdom() != null
                && !sender.hasPermission("kingdom.create.other")) {
            kdc.getMessages().send(sender, "cmdErrorNoPermission");
            return;
        }

        Kingdom template = kdc.getTemplateKingdom();

        CompletableFuture<Kingdom> kdf = kdc.createKingdom(args[0]);

        kdf.thenAccept((kingdom) -> {
            kdc.getMessages().send(sender, "cmdCreate", kingdom.getName());
        });

        CompletableFuture<Kingdom> tf = kdf.thenCompose((kingdom) -> {
            // copy attributes from template
            template.copyTo(kingdom, true);
            return kdc.saveAsync(kingdom).thenApply(unused -> kingdom);
        }).thenCompose(kingdom -> {
            // copy ranks from template
            for (Rank rank : template.getRanks()) {
                rank.clone(kingdom, true);
            }
            return kdc.saveAsync(kingdom.getRanks()).thenApply(unused -> kingdom);
        }).thenCompose(kingdom -> {
            // set default rank from template
            if (template.getDefaultRank() != null) {
                kingdom.setDefaultRank(kingdom.getRank(template.getDefaultRank().getName()));
            }
            return kdc.saveAsync(kingdom).thenApply(unused -> kingdom);
        });

        if ( !(sender instanceof PlatformPlayer) ) {
            return;
        }

        User user = kdc.getUser((PlatformPlayer) sender);
        if ( user.getKingdom() != null ) {
            return;
        }

        // set kingdom of user
        tf.thenAccept((kingdom) -> {
            user.setKingdom(kingdom);

            if ( kingdom.getRanks().isEmpty() ) {
                return;
            }

            user.setRank(kingdom.getRanks().stream().max(Comparator.comparingInt(Rank::getLevel)).orElse(null));
            kdc.saveAsync(user);
        });
    }
}
