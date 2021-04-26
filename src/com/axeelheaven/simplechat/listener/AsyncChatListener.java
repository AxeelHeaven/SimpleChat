package com.axeelheaven.simplechat.listener;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.axeelheaven.simplechat.Main;
import com.axeelheaven.simplechat.storage.PlayerData;

import me.clip.placeholderapi.PlaceholderAPI;

public class AsyncChatListener implements Listener {
	
	private HashMap<UUID, Long> cooldown;
	private boolean cooldown_enabled;
	private int cooldown_time;
	private Main plugin;

	private boolean antiCaps;
	private int maxCapsPercentage;
	private int minLength;
	
	public AsyncChatListener(final Main plugin) {
		this.plugin = plugin;
		this.cooldown = new HashMap<UUID, Long>();
		this.cooldown_enabled = plugin.getConfig().getBoolean("cooldown_settings.enabled");
		this.cooldown_time = plugin.getConfig().getInt("cooldown_settings.time");
		this.antiCaps = plugin.getConfig().getBoolean("message_settings.antiCaps");
		this.maxCapsPercentage = plugin.getConfig().getInt("message_settings.max-caps-percentage");
		this.minLength = plugin.getConfig().getInt("message_settings.min-message-length");
	}
	
	@EventHandler
	public void Chat(final AsyncPlayerChatEvent event) {
		final Player player = event.getPlayer();
		if(!player.hasPermission("simplechat.bypass.cooldown") && cooldown_enabled && this.cooldown.containsKey(player.getUniqueId()) && System.currentTimeMillis() < this.cooldown.get(player.getUniqueId())) {
			event.setCancelled(true);
			final Long time = Math.max(this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis(), 0L);
			player.sendMessage(this.text(player, this.plugin.getConfig().getString("cooldown_settings.message").replace("<seconds>", this.formatMilisecondsToSeconds(time))));
			return;
		}

		String message = event.getMessage();
		
		if(this.antiCaps && !player.hasPermission("simplechat.bypass.anticaps") && message.length() > this.minLength && this.getUppercasePercentage(message) > this.maxCapsPercentage) {
			message = String.valueOf(message.charAt(0) + message.substring(1).toLowerCase());
			
		}
		
		if(player.hasPermission("simplechat.chatcolour")) {
			message = ChatColor.translateAlternateColorCodes('&', message);
		}
		
		this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + cooldown_time * 1000);
		final PlayerData data = this.plugin.getManager().getData(player);
		final String format = this.text(player, this.plugin.getConfig().getString("format").replace("<symbol>", data.getSymbol().getSymbol()).replace("<message_color>", data.getMessageColor().getColorId()).replace("<name_color>", data.getNameColor().getColorId()).replace("<message>", message));
		event.setFormat(format);

	}
	
	private double getUppercasePercentage(String string) {
		double upperCase = 0.0D;
	    for (int i = 0; i < string.length(); i++) {
	    	if(String.valueOf("ABCDEFGHIJKLMNOPQRSTUVWXYZ").contains(string.substring(i, i + 1))) {
	    		upperCase++;
	    	}
	    } 
	    return upperCase / string.length() * 100.0D;
	}
	
	private String text(final Player player, String message) {
		if(this.plugin.isPlaceholderAPI()) {
			message = PlaceholderAPI.setPlaceholders(player, message);
		}
		return message.replace("<name>", player.getName());
	}

	private String formatMilisecondsToSeconds(final Long time) {
        final float seconds = (time + 0.0f) / 1000.0f;
        final String string = String.format("%1$.1f", seconds);
        return string;
    }
	
	
}
