package com.igufguf.kingdomcraft.commands.executors;

import com.igufguf.kingdomcraft.commands.CommandBase;
import com.igufguf.kingdomcraft.commands.CommandHandler;
import com.igufguf.kingdomcraft.KingdomCraft;
import com.igufguf.kingdomcraft.objects.KingdomObject;
import com.igufguf.kingdomcraft.objects.KingdomRelation;
import com.igufguf.kingdomcraft.KingdomCraftMessages;
import com.igufguf.kingdomcraft.objects.KingdomUser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Copyrighted 2018 iGufGuf
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
public class FriendlyCommand extends CommandBase {

	private final KingdomCraft plugin;

	public FriendlyCommand(KingdomCraft plugin) {
		super("friendly", "kingdom.friendly", true);

		this.plugin = plugin;

		plugin.getCmdHandler().register(this);
	}
	
	@Override
	public ArrayList<String> tabcomplete(String[] args) {
		if ( args.length == 2 ) {
			ArrayList<String> kingdoms = new ArrayList<>();
			for ( KingdomObject kd : plugin.getApi().getKingdomManager().getKingdoms() ) {
				if ( kd.getName().toLowerCase().startsWith(args[1].toLowerCase()) ) kingdoms.add(kd.getName());
			}
			return kingdoms;
		}
		return null;
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		Player p = (Player) sender;
		
		if ( args.length != 1) {
			plugin.getMsg().send(sender, "cmdDefaultUsage");
			return false;
		}
		if ( plugin.getApi().getUserManager().getUser(p).getKingdom() == null ) {
			plugin.getMsg().send(sender, "cmdDefaultSenderNoKingdom");
			return false;
		}
		if ( plugin.getApi().getKingdomManager().getKingdom(args[0]) == null ) {
			plugin.getMsg().send(sender, "cmdDefaultKingdomNotExist", args[0]);
			return false;
		}

		KingdomUser user = plugin.getApi().getUserManager().getUser(p);
		KingdomObject senderkd = plugin.getApi().getUserManager().getKingdom(user);
		KingdomObject targetkd = plugin.getApi().getKingdomManager().getKingdom(args[0]);

		if ( plugin.getApi().getRelationManager().getRelation(senderkd, targetkd) == KingdomRelation.FRIENDLY ) {
			plugin.getMsg().send(sender, "cmdFriendlyAlready", targetkd.getName());
			return false;
		}

		plugin.getApi().getRelationManager().setRelation(senderkd, targetkd, KingdomRelation.FRIENDLY);

		plugin.getMsg().send(sender, "cmdFriendlySuccess", targetkd.getName());

		return false;
	}

}
