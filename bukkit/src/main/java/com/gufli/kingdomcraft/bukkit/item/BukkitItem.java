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

package com.gufli.kingdomcraft.bukkit.item;

import com.gufli.kingdomcraft.api.item.Item;
import org.bukkit.inventory.ItemStack;

public class BukkitItem implements Item {

    private final ItemStack handle;

    public BukkitItem(ItemStack handle) {
        this.handle = handle;
    }

    @Override
    public Object getHandle() {
        if ( handle == null ) {
            return null;
        }
        return handle.clone();
    }
}
