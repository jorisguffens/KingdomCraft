package com.igufguf.kingdomcraft.commands;

import com.igufguf.kingdomcraft.KingdomCraft;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
public class CommandHandler implements CommandExecutor, TabCompleter {

	private final KingdomCraft plugin;
	private List<CommandBase> commands = new ArrayList<>();
	
	public CommandHandler(KingdomCraft plugin) {
		this.plugin = plugin;
	}

	public void register(CommandBase base) {
		commands.add(0, base);
	}

	public void unregister(CommandBase base) {
		commands.remove(base);
	}

	public CommandBase getByCommand(String cmd) {
		for ( CommandBase cb : commands ) {
			if ( cb.cmd.equalsIgnoreCase(cmd) ) return cb;
		}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		boolean player = sender instanceof Player;
		if ( player && !plugin.getApi().isWorldEnabled(((Player) sender).getWorld()) ) {
			plugin.getMsg().send(sender, "noWorld");
			return false;
		}
		
		if ( args.length >= 1 ) {

			for ( CommandBase cb : commands ) {
				if ( args[0].equalsIgnoreCase(cb.cmd) || cb.hasAlias(args[0]) ) {
					if ( !player && cb.playeronly ) {
						sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "This command can only be executed by players!");
						return true;
					} else {
						if ( cb.permission != null && player && !sender.hasPermission(cb.permission)) {
							plugin.getMsg().send(sender, "noPermissionCmd");
							return true;
						} else {
							return cb.execute(sender, Arrays.copyOfRange(args, 1, args.length));
						}
					}
				}
			}

		} else {
			String prefix = ChatColor.RED + ChatColor.BOLD.toString() + "KingdomCraft" + ChatColor.DARK_GRAY + ChatColor.BOLD + " > " + ChatColor.GRAY;
			sender.sendMessage(prefix + "v" + plugin.getDescription().getVersion() + " | Created by iGufGuf");
			sender.sendMessage(prefix + "https://www.igufguf.com");
			sender.sendMessage(prefix + ChatColor.GREEN + "For help type /k help");
			return true;
		}

		plugin.getMsg().send(sender, "noCommand", args[0]);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if ( !plugin.getApi().isWorldEnabled(((Player) sender).getWorld()) ) {
			return null;
		}
		List<CommandBase> cmds = new ArrayList<>(commands);
		Collections.reverse(cmds);

		if ( args.length >= 1 ) {
			for ( CommandBase cb : cmds ) {
				if ( args[0].equalsIgnoreCase(cb.cmd) || cb.hasAlias(args[0]) ) {
					return cb.tabcomplete(sender, args);
				}
			}
		} 
		if ( args.length == 1 ) {
			ArrayList<String> list = new ArrayList<>();
			for ( CommandBase cb : cmds ) {
				list.add(cb.cmd);
			}
			for ( String c : new ArrayList<>(list) ) {
				if ( !c.toLowerCase().startsWith(args[0].toLowerCase()) ) list.remove(c);
			}
			return list;
		}
		return null;
	}
}
