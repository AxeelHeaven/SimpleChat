package com.axeelheaven.simplechat.util;

import org.bukkit.entity.Player;

import com.axeelheaven.simplechat.Main;
import com.axeelheaven.simplechat.storage.PlayerData;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceholderAPI extends PlaceholderExpansion {
	
	private Main plugin;

	public PlaceholderAPI(final Main plugin) {
		this.plugin = plugin;
	}
	
    @Override
    public String getIdentifier() {
        return "simplechat";
    }

    @Override
    public String getAuthor() {
        return "AxeelHeaven";
    }

    @Override
    public String getVersion() {
        return this.plugin.pdffile.getVersion();
    }

	@Override
	public String onPlaceholderRequest(Player p, String id) {
		final PlayerData pd = this.plugin.getManager().getData(p);
		if(id.equalsIgnoreCase("name")) {
			return String.valueOf(pd.getNameColor().getColorId());
		}
		if(id.equalsIgnoreCase("message")) {
			return String.valueOf(pd.getMessageColor().getColorId());
		}
		if(id.equalsIgnoreCase("symbol")) {
			return String.valueOf(pd.getSymbol().getSymbol());
		}
		return null;
	}

}
