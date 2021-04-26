package com.axeelheaven.simplechat.manager;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import com.axeelheaven.simplechat.Main;
import com.axeelheaven.simplechat.manager.menus.ColorsMenu;
import com.axeelheaven.simplechat.manager.menus.MessageMenu;
import com.axeelheaven.simplechat.manager.menus.SymbolsMenu;
import com.axeelheaven.simplechat.storage.PlayerData;

public class Manager {
	
	private HashMap<UUID, PlayerData> data;
	private Main plugin;
	public ColorsMenu COLORS;
	public MessageMenu MESSAGE;
	public SymbolsMenu SYMBOLS;
	
	public Manager(final Main plugin) {
		this.data = new HashMap<UUID, PlayerData>();
		this.plugin = plugin;
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this.COLORS = new ColorsMenu(plugin), plugin);;
		pm.registerEvents(this.MESSAGE = new MessageMenu(plugin), plugin);;
		pm.registerEvents(this.SYMBOLS = new SymbolsMenu(plugin), plugin);;
	}
	
	
	public PlayerData getData(final Player player) {
		if(!this.data.containsKey(player.getUniqueId())) {
			this.data.put(player.getUniqueId(), new PlayerData(player, this.plugin));
		}
		return this.data.get(player.getUniqueId());
	}
	
	public HashMap<UUID, PlayerData> getDatas() {
		return this.data;
	}
	
	
	
}
