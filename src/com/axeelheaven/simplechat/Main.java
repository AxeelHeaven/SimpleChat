package com.axeelheaven.simplechat;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.axeelheaven.simplechat.commads.ColorsCommand;
import com.axeelheaven.simplechat.commads.MessageCommand;
import com.axeelheaven.simplechat.listener.AsyncChatListener;
import com.axeelheaven.simplechat.listener.PlayerListener;
import com.axeelheaven.simplechat.manager.Manager;
import com.axeelheaven.simplechat.storage.DatabaseManager;
import com.axeelheaven.simplechat.storage.PlayerData;
import com.axeelheaven.simplechat.util.FileConfig;
import com.axeelheaven.simplechat.util.PlaceholderAPI;

public class Main extends JavaPlugin{

	private boolean placeholderapi = false;
	private Manager manager;
	private DatabaseManager databaseManager;
	public PluginDescriptionFile pdffile = getDescription();
	public String version = pdffile.getVersion();
	
	@Override
	public void onEnable() {
		FileConfig.getInstance().save("menus.yml");
		FileConfig.getInstance().load("menus.yml");
		this.getConfig().options().copyDefaults(true);
		this.saveDefaultConfig();
		
		this.manager = new Manager(this);
		this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		this.getServer().getPluginManager().registerEvents(new AsyncChatListener(this), this);
		
		this.loadCommands();
		
		this.databaseManager = new DatabaseManager(this);
		try {
			this.databaseManager.getDatabase().connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if(!this.databaseManager.getDatabase().hasConnection(true, true, 0)) {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage("§6HPermissions: Your database is not working correctly,");
				Bukkit.getConsoleSender().sendMessage("§6HPermissions: please check your database connection.");
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getServer().getPluginManager().disablePlugins();return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		};
		this.databaseManager.createTables();
		this.updateConnection();
		new BukkitRunnable() {
			@Override
			public void run() {
				if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI").isEnabled()) {
					placeholderapi = true;
					new PlaceholderAPI(Main.this).register();
				}
			}
		}.runTaskLater(this, 50L);
	}
	
	private void loadCommands() {
		this.getCommand("simplechat").setExecutor(new ColorsCommand(this));
		final MessageCommand messgeCommand = new MessageCommand(this);
		this.getCommand("clearchat").setExecutor(messgeCommand);
		this.getCommand("msg").setExecutor(messgeCommand);
		this.getCommand("reply").setExecutor(messgeCommand);
	}

	@Override
	public void onDisable() {
		for(final PlayerData users : this.getManager().getDatas().values()) {
			users.save();
		}
		try {
			this.databaseManager.getDatabase().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Manager getManager() {
		return this.manager;
	}
	
	public boolean isPlaceholderAPI() {
		return this.placeholderapi;
	}
	
	public FileConfiguration getMenus() {
		return FileConfig.getInstance().get("menus.yml");
	}

	public DatabaseManager getDatabaseManager() {
		return this.databaseManager;
	}
	
	private void updateConnection() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
		      public void run() {
		    	  try {
		    		  final PreparedStatement keepAlive = getDatabaseManager().getDatabase().getConnection().prepareStatement("SELECT 1 FROM `" + getDatabaseManager().getTable() + "`");
		    		  keepAlive.executeQuery();
		    	  } catch (SQLException e) {
		    		  Bukkit.getConsoleSender().sendMessage("MYSQL '" + e.getSQLState() + "': " + e.getMessage());
		    	  }
		      }
			}, (long)1200L, (long)1200L);
	}
	
}
