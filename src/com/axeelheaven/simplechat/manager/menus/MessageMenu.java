package com.axeelheaven.simplechat.manager.menus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.axeelheaven.simplechat.Main;
import com.axeelheaven.simplechat.enums.ColorEnum;
import com.axeelheaven.simplechat.storage.PlayerData;

public class MessageMenu implements Listener {

	private Main plugin;
	private Inventory inv = null;
	
	public MessageMenu(final Main plugin) {
		this.plugin = plugin;
	}
	
	public Inventory create() {
		if(this.inv == null) {
			final Inventory inv = Bukkit.createInventory(null, plugin.getMenus().getInt("message_colors.size"), ChatColor.translateAlternateColorCodes('&', plugin.getMenus().getString("message_colors.title")));
			
			for(final String path : this.plugin.getMenus().getConfigurationSection("message_colors").getKeys(false)) {
				if(!(path.equals("size") || path.equals("title"))) {

					ItemStack item;
					if(this.isNumeric(this.plugin.getMenus().getString("message_colors." + path + ".material"))) {
						item = new ItemStack(Material.getMaterial(Integer.valueOf(this.plugin.getMenus().getInt("message_colors." + path + ".material"))), 1, (short)this.plugin.getMenus().getInt("message_colors." + path + ".data-value"));
					} else {
						item = new ItemStack(Material.getMaterial(this.plugin.getMenus().getString("message_colors." + path + ".material").toUpperCase()), 1, (short)this.plugin.getMenus().getInt("message_colors." + path + ".data-value"));
					}
					final ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.plugin.getMenus().getString("message_colors." + path + ".name")));
					final List<String> lore = new ArrayList<String>();
					for(final String list : this.plugin.getMenus().getStringList("message_colors." + path + ".lore")) {
						lore.add(ChatColor.translateAlternateColorCodes('&', list));
					}
					meta.setLore(lore);
					item.setItemMeta(meta);
					inv.setItem((this.plugin.getMenus().getInt("message_colors." + path + ".slot") - 1), item);
				}
			}
			this.inv = inv;
		}

		return this.inv;
	}
	
	@EventHandler
	public void Click(InventoryClickEvent event) {
		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', plugin.getMenus().getString("message_colors.title")))) {
			event.setCancelled(true);
			final Player player = (Player) event.getWhoClicked();
			for(final String path : this.plugin.getMenus().getConfigurationSection("message_colors").getKeys(false)) {
				if(event.getSlot() == (this.plugin.getMenus().getInt("message_colors." + path + ".slot") - 1)) {
					if(!player.hasPermission(this.plugin.getMenus().getString("message_colors." + path + ".permission"))) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getMenus().getString("nopermission").replace("<permission>", this.plugin.getMenus().getString("message_colors." + path + ".permission"))));
						break;
					}
					final PlayerData data = this.plugin.getManager().getData(player);
					if(data.getMessageColor().toString().equals(path.toUpperCase())) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getMenus().getString("already_selected")));
						break;
					}
					player.closeInventory();
					data.setMessageColor(ColorEnum.valueOf(path.toUpperCase()));
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getMenus().getString("selected").replace("<color>", data.getMessageColor().toString())));
					break;
				}
			}
		}
	}

	private boolean isNumeric(String i) {
		try {
			Integer.parseInt(i);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

}
