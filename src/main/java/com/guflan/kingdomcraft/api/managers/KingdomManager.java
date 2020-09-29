package com.guflan.kingdomcraft.api.managers;


import com.guflan.kingdomcraft.api.domain.Kingdom;
import com.guflan.kingdomcraft.api.domain.Player;
import com.guflan.kingdomcraft.api.entity.EntityPlayer;

import java.util.List;

public interface KingdomManager {

    List<Kingdom> getKingdoms();

    Kingdom getKingdom(String name);

    Kingdom createKingdom(String name);

    void deleteKingdom(Kingdom kingdom);

    void saveKingdom(Kingdom kingdom);

    List<EntityPlayer> getOnlineMembers(Kingdom kingdom);

}
