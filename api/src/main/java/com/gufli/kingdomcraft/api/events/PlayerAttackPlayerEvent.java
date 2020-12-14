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

package com.gufli.kingdomcraft.api.events;

import com.gufli.kingdomcraft.api.entity.PlatformPlayer;

public class PlayerAttackPlayerEvent {

    private final PlatformPlayer player;
    private final PlatformPlayer attacker;

    private Result result = Result.NEUTRAL;

    public PlayerAttackPlayerEvent(PlatformPlayer player, PlatformPlayer attacker) {
        this.player = player;
        this.attacker = attacker;
    }

    public PlatformPlayer getPlayer() {
        return player;
    }

    public PlatformPlayer getAttacker() {
        return attacker;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public enum Result {
        ALLOW, DENY, NEUTRAL;
    }
}
