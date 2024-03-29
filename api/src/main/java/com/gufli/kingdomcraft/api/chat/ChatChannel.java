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

package com.gufli.kingdomcraft.api.chat;

import java.util.Arrays;

public interface ChatChannel {

    String getName();

    String getPrefix();

    void setPrefix(String prefix);

    String getFormat();

    void setFormat(String format);

    RestrictMode getRestrictMode();

    void setRestrictMode(RestrictMode restrictMode);

    String getPermission();

    boolean isToggleable();

    void setToggleable(boolean toggleable);

    int getRange();

    void setRange(int range);

    int getCooldown();

    void setCooldown(int cooldown);

    boolean isEnabled();

    void setEnabled(boolean enabled);

    enum RestrictMode {
        NONE, TALK, READ;

        public static RestrictMode get(String name) {
            return Arrays.stream(RestrictMode.values())
                    .filter(val -> val.name().equalsIgnoreCase(name))
                    .findFirst().orElse(null);
        }
    }

}
