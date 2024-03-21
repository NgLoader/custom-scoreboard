package de.ngloader.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.dependency.LoadBefore;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import de.ngloader.scoreboard.api.ScoreboardManager;

@Plugin(name = "CustomScoreboard", version = "1.0.0")
@Author(name = "NgLoader")
@LogPrefix(prefix = "Custom Scoreboard")
@LoadBefore(plugin = "ProtocolLib")
@Dependency(plugin = "ProtocolLib")
public class ScoreboardPlugin extends JavaPlugin {

	private SharedScoreboardManager scoreboardManager;

	@Override
	public void onEnable() {
		if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
			this.getLogger().warning("Unable to find plugin ProtocolLib.");
			return;
		}

		this.scoreboardManager = new SharedScoreboardManager();
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, this.scoreboardManager, 0, 0);
		Bukkit.getPluginManager().registerEvents(new ScoreboardListener(this), this);

		Bukkit.getServicesManager().register(ScoreboardManager.class, this.scoreboardManager, this, ServicePriority.Normal);
	}

	@Override
	public void onDisable() {
		if (this.scoreboardManager != null) {
			Bukkit.getScheduler().cancelTasks(this);
			HandlerList.unregisterAll(this);
		}
	}

	public SharedScoreboardManager getScoreboardManager() {
		return this.scoreboardManager;
	}
}
