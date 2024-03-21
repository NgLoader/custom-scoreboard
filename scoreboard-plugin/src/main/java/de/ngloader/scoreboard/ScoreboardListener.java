package de.ngloader.scoreboard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.ngloader.scoreboard.api.Scoreboard;

public class ScoreboardListener implements Listener {

	private final SharedScoreboardManager scoreboardManager;
	private final Scoreboard globalScoreboard;

	public ScoreboardListener(ScoreboardPlugin plugin) {
		this.scoreboardManager = plugin.getScoreboardManager();
		this.globalScoreboard = this.scoreboardManager.getGlobalScoreboard();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		this.globalScoreboard.addToPlayers(event.getPlayer());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		this.scoreboardManager.removePlayers(event.getPlayer());
	}
}