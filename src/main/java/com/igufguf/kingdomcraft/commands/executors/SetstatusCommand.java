package com.igufguf.kingdomcraft.commands.executors;

import com.igufguf.kingdomcraft.commands.CommandBase;
import com.igufguf.kingdomcraft.commands.CommandHandler;
import com.igufguf.kingdomcraft.KingdomCraft;
import com.igufguf.kingdomcraft.objects.KingdomObject;
import com.igufguf.kingdomcraft.KingdomCraftMessages;
import com.igufguf.kingdomcraft.objects.KingdomUser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Copyrighted 2017 iGufGuf
 *
 * This file is part of KingdomCraft.
 *
 * Kingdomcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KingdomCraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with KingdomCraft.  If not, see <http://www.gnu.org/licenses/>.
 *
 **/
public class SetstatusCommand extends CommandBase {

	private final KingdomCraft plugin;
	
	public SetstatusCommand(KingdomCraft plugin) {
		super("setstatus", "kingdom.setstatus", true);
		addAliasses("settype");

		this.plugin = plugin;

		plugin.getCmdHandler().register(this);
	}
	
	@Override
	public ArrayList<String> tabcomplete(String[] args) {
		if ( args.length == 2 ) {
			if ( args[1].startsWith("c") ) return new ArrayList<>(Collections.singletonList("closed"));
			else if ( args[1].startsWith("o") ) return new ArrayList<>(Collections.singletonList("open"));
			return new ArrayList<>(Arrays.asList("closed", "open"));
		}
		return null;
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		Player p = (Player) sender;
		
		if ( args.length != 1 && args.length != 2 ) {
			plugin.getMsg().send(sender, "cmdDefaultUsage");
			return false;
		}

		KingdomUser user = plugin.getApi().getUserManager().getUser(p);

		KingdomObject kingdom;
		if (args.length == 2) {
			kingdom = plugin.getApi().getKingdomManager().getKingdom(args[0]);
			if ( kingdom == null ) {
				plugin.getMsg().send(sender, "cmdDefaultKingdomNotExist", args[0]);
				return false;
			}
		} else {
			kingdom = plugin.getApi().getUserManager().getKingdom(user);
		}

		if ( kingdom == null ) {
			plugin.getMsg().send(sender, "cmdDefaultTargetNoKingdom");
			return false;
		}

		String type = args[0];
		if ( args.length == 2 ) {
			type = args[1];
		}

		if ( type.equalsIgnoreCase("closed") ) {
			kingdom.addInList("flags", "closed");
			plugin.getMsg().send(sender, "cmdSetstatusClosed", kingdom.getName());
		} else if ( type.equalsIgnoreCase("open") ) {
			kingdom.delInList("flags", "closed");
			plugin.getMsg().send(sender, "cmdSetstatusOpen", kingdom.getName());
		} else {
			plugin.getMsg().send(sender, "cmdDefaultUsage");
			return false;
		}

		return false;
	}
}
