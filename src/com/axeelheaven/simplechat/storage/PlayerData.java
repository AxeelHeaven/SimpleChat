package com.axeelheaven.simplechat.storage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import com.axeelheaven.simplechat.Main;
import com.axeelheaven.simplechat.enums.ColorEnum;
import com.axeelheaven.simplechat.enums.SymbolsEnum;

public class PlayerData {

	private ColorEnum nameColor;
	private ColorEnum messageColor;
	private SymbolsEnum symbol;
	private Main plugin;
	final Player player;
	
	public PlayerData(final Player player, final Main plugin) {
		this.plugin = plugin;
		this.player = player;
		this.nameColor = ColorEnum.valueOf(plugin.getConfig().getString("settings.nameColor"));
		this.messageColor = ColorEnum.valueOf(plugin.getConfig().getString("settings.messageColor"));
		this.symbol = SymbolsEnum.valueOf(plugin.getConfig().getString("settings.symbol"));
		this.load();
	}
	
	
	public SymbolsEnum getSymbol() {
		return this.symbol;
	}
	
	public void setSymbol(final SymbolsEnum symbol) {
		this.symbol = symbol;
	}
	
	
	public ColorEnum getNameColor() {
		return this.nameColor;
	}
	
	public void setNameColor(final ColorEnum nameColor) {
		this.nameColor = nameColor;
	}
	
	public ColorEnum getMessageColor() {
		return this.messageColor;
	}
	
	public void setMessageColor(final ColorEnum messageColor) {
		this.messageColor = messageColor;
	}
	
	private void load() {
		this.plugin.getDatabaseManager().createUUID(this.player.getPlayer().getUniqueId());

		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		try {
			preparedStatement = plugin.getDatabaseManager().getDatabase().getConnection().prepareStatement("SELECT * FROM `" + plugin.getDatabaseManager().getTable() + "` WHERE `uuid` =?;");
			preparedStatement.setString(1, this.player.getUniqueId().toString());
			result = preparedStatement.executeQuery();
			if (result.isBeforeFirst()) {
				while (result.next()) {
					this.nameColor = ColorEnum.valueOf(result.getString("name").toUpperCase());
					this.messageColor = ColorEnum.valueOf(result.getString("message").toUpperCase());
					this.symbol = SymbolsEnum.valueOf(result.getString("symbol").toUpperCase());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
            if (result != null) {
                try {
                	result.close();
                } catch (final SQLException ignored) {
                }
            }
            if (preparedStatement != null) {
                try {
                	preparedStatement.close();
                } catch (final SQLException ignored) {
                }
            }
        }
	}
	
	public void save() {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = plugin.getDatabaseManager().getDatabase().getConnection().prepareStatement("UPDATE `" + plugin.getDatabaseManager().getTable() + "` SET `name` =?, `message` =?, `symbol` =? WHERE `uuid` =?;");
			preparedStatement.setString(1, this.getNameColor().toString());
			preparedStatement.setString(2,  this.getMessageColor().toString());
			preparedStatement.setString(3, this.symbol.toString());
			preparedStatement.setString(4, this.player.getUniqueId().toString());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
            if (preparedStatement != null) {
                try {
                	preparedStatement.close();
                } catch (final SQLException ignored) {
                }
            }
        }
	}
	
	
}
