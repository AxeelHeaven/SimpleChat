 package com.axeelheaven.simplechat.listener;
 
import java.util.UUID;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import com.axeelheaven.simplechat.Main;

public class PlayerListener implements Listener {

	private Main plugin;
	
	public PlayerListener(final Main main) {
		this.plugin = main;
	}
	
	@EventHandler (priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void AsyncPlayerPreLogin(final AsyncPlayerPreLoginEvent event) {
		final UUID uuid = event.getUniqueId();
		this.plugin.getDatabaseManager().createUUID(uuid);
	}
	
	@EventHandler (priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void PlayerQuit(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		if(this.plugin.getManager().getDatas().containsKey(player.getUniqueId())) {
			this.plugin.getManager().getData(player).save();
			this.plugin.getManager().getDatas().remove(player.getUniqueId());
		}
	}
	
	@EventHandler (priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void PlayerKick(final PlayerKickEvent event) {
		final Player player = event.getPlayer();
		if(this.plugin.getManager().getDatas().containsKey(player.getUniqueId())) {
			this.plugin.getManager().getData(player).save();
			this.plugin.getManager().getDatas().remove(player.getUniqueId());
		}
	}

}
