package com.axeelheaven.simplechat.commads;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.axeelheaven.simplechat.Main;

public class ColorsCommand implements CommandExecutor {
	
	private Main plugin;
	
	public ColorsCommand(final Main main) {
		this.plugin = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			return true;
		}
		final Player player = (Player) sender;
		if(args.length < 1) {
			sender.sendMessage(" ");
			sender.sendMessage(c("&fUse &e'/simplechat name' &fto change the color of the name"));
			sender.sendMessage(c("&fUse &e'/simplechat message' &fto change the color of the message."));
			sender.sendMessage(c("&fUse &e'/simplechat symbol' &fto change the name symbol."));
			sender.sendMessage(" ");
			return true;
		}
		if(args[0].equalsIgnoreCase("symbol")) {
			player.openInventory(this.plugin.getManager().SYMBOLS.create());
			return true;
		}
		if(args[0].equalsIgnoreCase("message")) {
			player.openInventory(this.plugin.getManager().MESSAGE.create());
			return true;
		}
		if(args[0].equalsIgnoreCase("name")) {
			player.openInventory(this.plugin.getManager().COLORS.create());
			return true;
		}
		return false;
	}

	private String c(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
}
