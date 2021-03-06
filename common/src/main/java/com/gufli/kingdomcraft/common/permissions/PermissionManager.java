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

package com.gufli.kingdomcraft.common.permissions;

import com.gufli.kingdomcraft.api.domain.Rank;
import com.gufli.kingdomcraft.api.domain.RankPermissionGroup;
import com.gufli.kingdomcraft.api.domain.User;
import com.gufli.kingdomcraft.api.entity.PlatformPlayer;
import com.gufli.kingdomcraft.common.KingdomCraftImpl;
import com.gufli.kingdomcraft.common.config.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PermissionManager {

    private final KingdomCraftImpl kdc;
    private final List<PermissionGroup> groups = new ArrayList<>();

    private PermissionGroup defaultGroup;

    public PermissionManager(KingdomCraftImpl kdc) {
        this.kdc = kdc;
    }

    public void addPermissionGroup(PermissionGroup group) {
        if ( groups.contains(group) ) {
            return;
        }
        groups.add(group);
    }

    public void removePermissionGroup(PermissionGroup group) {
        groups.remove(group);
    }

    public PermissionGroup getGroup(String name) {
        return groups.stream().filter(g -> g.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public List<PermissionGroup> getGroups(Rank rank) {
        return this.getGroups().stream().filter(group -> {
            RankPermissionGroup rpg = rank.getPermissionGroup(group.getName());
            return rpg != null || (!group.getRanks().isEmpty() && group.getRanks().stream()
                    .anyMatch(r -> r.equalsIgnoreCase(rank.getName())));
        }).collect(Collectors.toList());
    }

    public List<PermissionGroup> getGroups(Rank rank, String world) {
        return getGroups(rank).stream().filter(group ->
                group.getWorlds().isEmpty() || group.getWorlds().stream().anyMatch(w -> w.equalsIgnoreCase(world)))
                .collect(Collectors.toList());
    }

    public List<PermissionGroup> getGroups() {
        return new ArrayList<>(groups);
    }

    public PermissionGroup getDefaultGroup() {
        return defaultGroup;
    }

    public List<PermissionGroup> getGroups(PlatformPlayer player) {
        User user = kdc.getUser(player);
        List<PermissionGroup> groups = new ArrayList<>();

        if ( user.getRank() == null  ) {
            if ( defaultGroup != null ) {
                groups.add(defaultGroup);
            }
        } else {
            groups.addAll(getGroups(user.getRank(), player.getLocation().getWorldName()));
        }
        return groups;
    }

    public Map<String, Boolean> getTotalPermissions(PermissionGroup group) {
        Map<String, Boolean> perms = group.getPermissionsAsMap();
        fillPermissionsMap(group, new ArrayList<>(), perms);
        return perms;
    }

    public Map<String, Boolean> getTotalPermissions(PlatformPlayer player) {
        List<PermissionGroup> groups = getGroups(player);

        Map<String, Boolean> permissions = new HashMap<>();
        groups.forEach(group -> permissions.putAll(getTotalPermissions(group)));

        return permissions;
    }

    //

    private void fillPermissionsMap(PermissionGroup group, List<String> passed, Map<String, Boolean> perms) {
        perms.putAll(group.getPermissionsAsMap());
        passed.add(group.getName());

        for ( String inheritance : group.getInheritances() ) {
            if ( passed.contains(inheritance) ) {
                continue;
            }

            PermissionGroup g = getGroup(inheritance);
            if ( g == null ) {
                continue;
            }

            fillPermissionsMap(g, passed, perms);
        }
    }

    public void load(Configuration config) {
        groups.clear();

        for ( String key : config.getKeys(false) ) {
            Configuration cs = config.getConfigurationSection(key);

            PermissionGroup group = new PermissionGroup(key,
                    cs.contains("permissions") ? cs.getStringList("permissions") : new ArrayList<>(),
                    cs.contains("inheritances") ? cs.getStringList("inheritances") : new ArrayList<>(),
                    cs.contains("ranks") ? cs.getStringList("ranks") : new ArrayList<>(),
                    cs.contains("worlds") ? cs.getStringList("worlds") : new ArrayList<>(),
                    cs.contains("externals") ? cs.getStringList("externals") : new ArrayList<>());

            if ( key.equalsIgnoreCase("default") ) {
                defaultGroup = group;
                continue;
            }

            addPermissionGroup(group);
        }
    }

}
