package com.axeelheaven.simplechat.storage;

import java.sql.*;
import java.util.UUID;

import com.axeelheaven.simplechat.Main;
import com.axeelheaven.simplechat.storage.types.*;

public class DatabaseManager {

	private Main plugin;
	private Database database;
	private String table;
	
	public DatabaseManager(final Main plugin) {
		this.plugin = plugin;
    	this.table = plugin.getConfig().getString("mysql.table_name");
		
    	if(plugin.getConfig().getBoolean("mysql.enabled")) {
    		this.database = (Database)new MySQL(plugin.getConfig().getString("mysql.settings.host"), 
    				plugin.getConfig().getInt("mysql.settings.port"), 
    				plugin.getConfig().getString("mysql.settings.database"), 
    				plugin.getConfig().getString("mysql.settings.username"), 
    				plugin.getConfig().getString("mysql.settings.password"));
    	} else {
    		this.database = (Database)new SQLite(plugin, plugin.getConfig().getString("mysql.settings.host"), plugin.getConfig().getInt("mysql.settings.port"), plugin.getConfig().getString("mysql.settings.database"), plugin.getConfig().getString("mysql.settings.user"), plugin.getConfig().getString("mysql.settings.password"));
    	}
	}
	
	public Database getDatabase() {
		return this.database;
	}

	public String getTable() {
		return this.table;
	}
	
	public void createUUID(final UUID uuid) {
		final PreparedStatement preparedStatement;
		try{
			preparedStatement = this.getDatabase().getConnection().prepareStatement("SELECT * FROM `" + this.getTable() + "` WHERE `uuid` =? LIMIT 1;");
			preparedStatement.setString(1, uuid.toString());
			final ResultSet result = preparedStatement.executeQuery();
			if(!result.next()) {
				result.close();
				preparedStatement.close();
				this.createPlayer(uuid);
			}
		}catch (final SQLException e){
			System.out.println(e.getMessage());
		}
	}
	
	public void createTables() {
		Statement preparedStatement = null;
		try {
			preparedStatement = this.getDatabase().getConnection().createStatement();
			final StringBuilder message = new StringBuilder();
			message.append("CREATE TABLE IF NOT EXISTS `" + this.getTable() + "` ");
			message.append("(`uuid` TEXT, ");
			message.append("`name` TEXT, ");
			message.append("`message` TEXT, ");
			message.append("`symbol` TEXT)");
			preparedStatement.executeUpdate(message.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try{
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			}catch(SQLException e01){ }
		}
	}

	private void createPlayer(final UUID uuid) {
		PreparedStatement preparedStatement = null;
		try {
			final StringBuilder message = new StringBuilder();
			message.append("INSERT INTO `" + this.getTable() + "` ");
			message.append("(uuid, name, message, symbol) ");
			message.append("VALUES ");
			message.append("(?, ?, ?, ?);");
			preparedStatement = this.getDatabase().getConnection().prepareStatement(message.toString());
			preparedStatement.setString(1, uuid.toString());
			preparedStatement.setString(2, plugin.getConfig().getString("settings.nameColor"));
			preparedStatement.setString(3, plugin.getConfig().getString("settings.messageColor"));
			preparedStatement.setString(4, plugin.getConfig().getString("settings.symbol"));
			preparedStatement.executeUpdate();
		} catch (final SQLException e) {
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
