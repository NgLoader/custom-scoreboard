package de.ngloader.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.dependency.LoadBefore;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import de.ngloader.scoreboard.api.ScoreboardManager;
import de.ngloader.scoreboard.api.ScoreboardTeam;

@Plugin(name = "CustomScoreboard", version = "1.0.0")
@Author(name = "NgLoader")
@LogPrefix(prefix = "Custom Scoreboard")
@LoadBefore(plugin = "ProtocolLib")
@Dependency(plugin = "ProtocolLib")
public class ScoreboardPlugin extends JavaPlugin implements Listener {

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
		Bukkit.getPluginManager().registerEvents(this, this);

		Bukkit.getServicesManager().register(ScoreboardManager.class, this.scoreboardManager, this, ServicePriority.Normal);
	}

	@Override
	public void onDisable() {
		if (this.scoreboardManager != null) {
			Bukkit.getScheduler().cancelTasks(this);
			HandlerList.unregisterAll((JavaPlugin) this);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		this.scoreboardManager.getGlobalScoreboard().addToPlayers(player);
		
		ScoreboardTeam team = this.scoreboardManager.getGlobalScoreboard().getTeam("xD");
		if (team == null) {
			team = this.scoreboardManager.getGlobalScoreboard().createTeam("xD");
			team.setPrefix("§4Moin ");
			team.setColor(ChatColor.RED);
		}
		team.addEntry(player);
		
		SharedSidebar sidebar = this.scoreboardManager.createSidebar(player.getName());
		sidebar.fillLines(0, 10);
		sidebar.setLine(2, player.getName());
		
		sidebar.addToPlayers(player);
	}

	public SharedScoreboardManager getScoreboardManager() {
		return this.scoreboardManager;
	}
}
