package com.igufguf.kingdomcraft.api.managers;


import com.igufguf.kingdomcraft.api.domain.Kingdom;
import com.igufguf.kingdomcraft.api.domain.Player;

import java.util.List;

public interface KingdomManager {

    List<Kingdom> getKingdoms();

    Kingdom getKingdom(String name);

    Kingdom createKingdom(String name);

    void deleteKingdom(Kingdom kingdom);

    void saveKingdom(Kingdom kingdom);

    List<Player> getOnlineMembers(Kingdom kingdom);

}
