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

package com.gufli.kingdomcraft.bukkit.placeholders;

import com.gufli.kingdomcraft.api.placeholders.PlaceholderManager;
import com.gufli.kingdomcraft.bukkit.KingdomCraftBukkitPlugin;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultPlaceholderReplacer {

    public VaultPlaceholderReplacer(KingdomCraftBukkitPlugin plugin) {
        if ( !plugin.getServer().getPluginManager().isPluginEnabled("Vault") ) {
            return;
        }

        RegisteredServiceProvider<Chat> chatProvider = plugin.getServer().getServicesManager().getRegistration(Chat.class);
        if (chatProvider == null) {
            return;
        }

        Chat chat = chatProvider.getProvider();
        PlaceholderManager pm = plugin.getKdc().getPlaceholderManager();

        pm.addPlaceholderReplacer((user, placeholder) -> {
            OfflinePlayer p = Bukkit.getOfflinePlayer(user.getUniqueId());
            if ( p == null ) return null;

            return chat.getPlayerPrefix(null, p);
        }, "prefix");

        pm.addPlaceholderReplacer((user, placeholder) -> {
            OfflinePlayer p = Bukkit.getOfflinePlayer(user.getUniqueId());
            if ( p == null ) return null;

            return chat.getPlayerSuffix(null, p);
        }, "suffix");

    }

}
