package de.ngloader.scoreboard.api;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

public interface ScoreboardManager {

	ScoreboardManager unassignDisplaySlot(DisplaySlot slot, Player... players);

	ScoreboardManager unassignDisplaySlot(DisplaySlot slot, DisplaySlot slot2, Player... players);

	ScoreboardManager unassignDisplaySlot(DisplaySlot slot, DisplaySlot slot2, DisplaySlot slot3,
			Player... players);

	Scoreboard createScoreboard();

	ScoreboardTeam createTeam(String name);

	ScoreboardSidebar createSidebar(String name);

	ScoreboardObjective createObjective(String name);

	ScoreboardManager removePlayers(Player player, Player... players);

	ScoreboardManager delete(ScoreboardBase base);

	Scoreboard getGlobalScoreboard();
}