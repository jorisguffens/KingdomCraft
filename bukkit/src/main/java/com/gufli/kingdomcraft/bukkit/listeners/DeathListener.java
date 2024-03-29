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

package com.gufli.kingdomcraft.bukkit.listeners;

import com.gufli.kingdomcraft.api.entity.PlatformPlayer;
import com.gufli.kingdomcraft.bukkit.KingdomCraftBukkitPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DeathListener implements Listener {

    private final KingdomCraftBukkitPlugin plugin;

    public DeathListener(KingdomCraftBukkitPlugin plugin) {
        this.plugin = plugin;
    }

    // respawn

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent event) {
        if ( event.getEntity().getKiller() != null && event.getEntity().getKiller() != event.getEntity() ) {
            PlatformPlayer p = plugin.getKdc().getPlayer(event.getEntity().getUniqueId());
            PlatformPlayer k = plugin.getKdc().getPlayer(event.getEntity().getKiller().getUniqueId());

            String msg = plugin.getKdc().getConfig().getOnKillMessage();

            ItemStack weapon = event.getEntity().getKiller().getItemInHand();
            if ( weapon != null && weapon.getType() != Material.AIR
                    && plugin.getKdc().getConfig().getOnKillWeaponMessage() != null
                    && !plugin.getKdc().getConfig().getOnKillWeaponMessage().equals("") ) {

                ItemMeta meta = weapon.getItemMeta();
                if ( meta.getDisplayName() != null && !meta.getDisplayName().equals("") ) {
                    msg = plugin.getKdc().getConfig().getOnKillWeaponMessage();
                    msg = msg.replace("{weapon}", plugin.colorify(meta.getDisplayName()));
                }
            }

            if ( msg == null ) {
                return;
            }

            event.setDeathMessage(null);
            if ( msg.equals("") || p == null || k == null ) {
                return;
            }

            msg = plugin.getKdc().getPlaceholderManager().handle(p, msg);
            msg = plugin.getKdc().getPlaceholderManager().handle(k, msg, "killer_");
            msg = plugin.getKdc().getMessages().colorify(msg);
            event.setDeathMessage(msg);
            return;
        }

        if ( plugin.getKdc().getConfig().getOnDeathMessage() == null ) {
            return;
        }

        event.setDeathMessage(null);

        PlatformPlayer p = plugin.getKdc().getPlayer(event.getEntity().getUniqueId());
        if ( plugin.getKdc().getConfig().getOnDeathMessage().equals("") || p == null ) {
            return;
        }

        String msg = plugin.getKdc().getConfig().getOnDeathMessage();
        msg = plugin.getKdc().getPlaceholderManager().handle(p, msg);
        msg = plugin.getKdc().getMessages().colorify(msg);
        event.setDeathMessage(msg);
    }

//    @EventHandler(priority = EventPriority.MONITOR)
//    public void onDeath2(PlayerDeathEvent event) {
//        if ( event.getDeathMessage() == null || event.getDeathMessage().equals("") ) {
//            return;
//        }
//
//        plugin.getKdc().getOnlinePlayers().forEach(p -> p.sendMessage(event.getDeathMessage()));
//        event.setDeathMessage(null);
//    }
}
