package com.guflan.kingdomcraft.common.commands.management;

import com.guflan.kingdomcraft.api.KingdomCraftPlugin;
import com.guflan.kingdomcraft.api.domain.Kingdom;
import com.guflan.kingdomcraft.api.entity.CommandSender;
import com.guflan.kingdomcraft.common.command.DefaultCommandBase;

import java.util.List;
import java.util.stream.Collectors;

public class EditDisplayOtherCommand extends DefaultCommandBase {

    public EditDisplayOtherCommand(KingdomCraftPlugin plugin) {
        super(plugin, "edit display", 2);
    }

    @Override
    public List<String> autocomplete(CommandSender sender, String[] args) {
        if ( args.length == 1 ) {
            return plugin.getKingdomManager().getKingdoms().stream().map(Kingdom::getName).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if ( !sender.hasPermission("kingdom.edit.display.other") ) {
            plugin.getMessageManager().send(sender, "noPermission");
            return;
        }

        Kingdom kingdom = plugin.getKingdomManager().getKingdom(args[0]);
        if ( kingdom == null ) {
            plugin.getMessageManager().send(sender, "cmdDefaultKingdomNotExist", args[0]);
            return;
        }

        kingdom.setDisplay(args[1]);
        plugin.getKingdomManager().saveKingdom(kingdom);

        plugin.getMessageManager().send(sender, "cmdEditSuccess", "display", args[1], kingdom.getName());
    }
}
