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

package com.gufli.kingdomcraft.api.event;

import com.gufli.kingdomcraft.api.events.Event;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface EventManager {

    <T extends Event> EventSubscription<T> addListener(Class<T> type, Consumer<T> consumer);

    <T extends Event> EventSubscription<T> addListener(Class<T> type, BiConsumer<EventSubscription<T>, T> consumer);

    <T extends Event> void dispatch(T event);

}
