package com.gufli.kingdomcraft.api.config;

import com.gufli.kingdomcraft.api.domain.RelationType;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface KingdomCraftConfig {

    List<String> getCommandAliases();

    int getTeleportDelay();

    List<String> getWorlds();

    String getLanguage();

    ZoneId getTimeZone();

    DateTimeFormatter getDateFormat();

    DateTimeFormatter getTimeFormat();

    DateTimeFormatter getDateTimeFormat();

    boolean isWorldEnabled(String world);

    List<RelationType> getFriendlyFireRelationTypes();

    boolean isFriendlyFireEnabled();

    List<String> getOnKingdomJoinCommands();

    List<String> getOnKingdomLeaveCommands();

    boolean respawnAtKingdom();

    String getOnJoinMessage();

    String getOnLeaveMessage();

    String getOnDeathMessage();

    String getOnKillMessage();

    String getOnKillWeaponMessage();

    String getNoKingdomPrefix();

    String getNoKingdomSuffix();

    String getNoKingdomDisplay();

    boolean isChatEnabledInDisabledWorlds();

    boolean showJoinAndLeaveKingdomOnly();

    boolean updateDisplayNames();
}
