package me.A5H73Y.Parkour;

import me.A5H73Y.Parkour.Other.Configurations;
import me.A5H73Y.Parkour.Other.StartPlugin;
import me.A5H73Y.Parkour.Other.Updater;
import me.A5H73Y.Parkour.Player.PlayerMethods;
import me.A5H73Y.Parkour.Utilities.Settings;
import me.A5H73Y.Parkour.Utilities.Static;
import me.A5H73Y.Parkour.Utilities.Utils;
import net.milkbowl.vault.economy.Economy;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import com.huskehhh.mysql.Database;

public class Parkour extends JavaPlugin {

	private static Parkour instance;
	private Configurations config;
	private static Database database;
	private static Economy economy;
	private static Settings settings;

	public void onEnable() {
		instance = this;
		StartPlugin.isFreshInstall();
		config = new Configurations();
		StartPlugin.run();
		settings = new Settings();

		getServer().getPluginManager().registerEvents(new ParkourListener(), this);
		getServer().getPluginManager().registerEvents(new ParkourSignListener(), this);
		getCommand("parkour").setExecutor(new ParkourCommands());

        new Metrics(this);
        updatePlugin();
	}

	public void onDisable() {
		Utils.saveAllPlaying(PlayerMethods.getPlaying(), Static.PATH);
		config.saveAll();
		getParkourConfig().reload();
		database.closeConnection();
		Utils.log("Disabled Parkour v" + Static.getVersion());
		instance = null;
	}

	public static void setDatabaseObj(Database databaseObj) {
		database = databaseObj;
	}

	public static void setEconomy(Economy newEconomy) {
		economy = newEconomy;
	}

	public static void setSettings(Settings newSettings) {
		settings = newSettings;
	}

	// Getters
	public static Parkour getPlugin() {
		return instance;
	}

	public static Configurations getParkourConfig() {
		return getPlugin().config;
	}

	public static Settings getSettings() {
		return settings;
	}

	public static Database getDatabaseObj() {
		return database;
	}

	public static Economy getEconomy() {
		return economy;
	}

	private void updatePlugin() {
		if (Parkour.getPlugin().getConfig().getBoolean("Other.CheckForUpdates"))
			new Updater(this, 42615, this.getFile(), Updater.UpdateType.DEFAULT, true);
	}
}