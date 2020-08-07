package com.igufguf.kingdomcraft.common.commands.admin;

import com.igufguf.kingdomcraft.api.KingdomCraftPlugin;
import com.igufguf.kingdomcraft.api.commands.CommandSender;
import com.igufguf.kingdomcraft.api.domain.Kingdom;
import com.igufguf.kingdomcraft.api.domain.Player;
import com.igufguf.kingdomcraft.common.commands.DefaultCommandBase;

public class SetKingdomCommand extends DefaultCommandBase {

    public SetKingdomCommand(KingdomCraftPlugin plugin) {
        super(plugin, "kick", 1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player target = plugin.getPlayerManager().getPlayer(args[0]);
        if ( target == null ) {
            plugin.getMessageManager().send(sender, "cmdDefaultNoPlayer");
            return;
        }

        Kingdom kingdom = plugin.getKingdomManager().getKingdom(args[1]);
        if ( kingdom == null ) {
            plugin.getMessageManager().send(sender, "cmdDefaultKingdomNotExist", args[0]);
            return;
        }

        plugin.getPlayerManager().leaveKingdom(target);
        plugin.getPlayerManager().joinKingdom(target, kingdom);

        plugin.getMessageManager().send(target, "cmdSetKingdomTarget", kingdom.getName());
        plugin.getMessageManager().send(sender, "cmdSetKingdomSender", target.getName(), kingdom.getName());
    }
}
