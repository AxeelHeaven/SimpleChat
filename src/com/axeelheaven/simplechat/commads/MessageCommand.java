package com.axeelheaven.simplechat.commads;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import com.axeelheaven.simplechat.Main;

public class MessageCommand implements CommandExecutor {
	
	private HashMap<String, String> msg = new HashMap<>();
	private Main plugin;
	
	public MessageCommand(final Main main) {
		this.plugin = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("clearchat") && sender.hasPermission("targannian.clearchat")) {
			for(final Player p : Bukkit.getOnlinePlayers()) {
				for(int i = 0; i < 100; i++) {
					p.sendMessage(" ");
				}
			}
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("msg")) {
			if (args.length < 1) {
				sender.sendMessage(c("&c/msg <player> <message>"));
				return true;
			} 
			Player reciverPlayer = Bukkit.getPlayer(args[0]);
			if (reciverPlayer == null) {
				sender.sendMessage(c("&cEl jugador " + args[0] + " no esta conectado."));
				return true;
			}
			final String message = this.getMessage(1, args);
			reciverPlayer.sendMessage(c("&dDe &5&l» &7" + sender.getName() + "&f: " + message));
			sender.sendMessage(c("&dPara &5&l» &7" + reciverPlayer.getName() + "&f: " + message));
		      
			this.msg.put(sender.getName(), reciverPlayer.getName());
			this.msg.put(reciverPlayer.getName(), sender.getName());
		      
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("reply")) {
			if (args.length == 0) {
				sender.sendMessage(c("&c/reply <message>"));
				return true;
			} 
		      
			if (!this.msg.containsKey(sender.getName())) {
				sender.sendMessage(c("&cNo tienes a nadie a quien enviarle una respuesta."));
		        return true;
			} 

			Player reciverPlayer = Bukkit.getPlayer(this.msg.get(sender.getName()));
			if (reciverPlayer == null) {
				sender.sendMessage(c("&cEl jugador " + args[0] + " no esta conectado."));
		        return true;
			}
			final String message = this.getMessage(0, args);
			reciverPlayer.sendMessage(c("&dDe &5&l» &7" + sender.getName() + "&f: " + message));
			sender.sendMessage(c("&dPara &5&l» &7" + reciverPlayer.getName() + "&f: " + message));
			this.msg.put(sender.getName(), reciverPlayer.getName());
			this.msg.put(reciverPlayer.getName(), sender.getName());
		      
			return true;
		} 
		return false;
	}
	
	private String c(final String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	private String getMessage(final int init, final String[] strings) {
		final StringBuilder builder = new StringBuilder();
		for (int i = init; i < strings.length; i++) {
			if (i < strings.length - 1) {
				builder.append(strings[i]).append(" ");
			} else {
				builder.append(strings[i]);
	        } 
		} 
		return builder.toString();
	}
	
}
