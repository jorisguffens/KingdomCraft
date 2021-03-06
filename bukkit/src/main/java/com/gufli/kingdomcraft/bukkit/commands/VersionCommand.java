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

package com.gufli.kingdomcraft.bukkit.commands;

import com.gufli.kingdomcraft.api.entity.PlatformPlayer;
import com.gufli.kingdomcraft.api.entity.PlatformSender;
import com.gufli.kingdomcraft.bukkit.KingdomCraftBukkitPlugin;
import com.gufli.kingdomcraft.common.command.CommandBase;

import java.util.List;

public class VersionCommand extends CommandBase {

    private final KingdomCraftBukkitPlugin plugin;

    public VersionCommand(KingdomCraftBukkitPlugin plugin) {
        super(plugin.getKdc(), "version", 0);
        this.plugin = plugin;
    }

    @Override
    public List<String> autocomplete(PlatformPlayer player, String[] args) {
        return null;
    }

    @Override
    public void execute(PlatformSender sender, String[] args) {
        String prefix = kdc.getMessages().colorify(kdc.getMessages().getPrefix());
        sender.sendMessage(prefix + plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion());
        sender.sendMessage(prefix + "Created by " + String.join(", ", plugin.getDescription().getAuthors()));
        sender.sendMessage(prefix + "https://kingdomcraft.be");
    }

}
