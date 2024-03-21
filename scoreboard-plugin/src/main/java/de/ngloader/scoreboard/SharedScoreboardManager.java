package de.ngloader.scoreboard;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

import de.ngloader.scoreboard.api.Scoreboard;
import de.ngloader.scoreboard.api.ScoreboardBase;
import de.ngloader.scoreboard.api.ScoreboardManager;

public class SharedScoreboardManager implements ScoreboardManager, Runnable {

	private final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

	private final List<SharedBase<?>> entries = new CopyOnWriteArrayList<>();

	private final SharedScoreboard globalScoreboard = new SharedScoreboard(this);

	@Override
	public void run() {
		for (SharedBase<?> base : this.entries) {
			base.run();
		}
	}

	void initialize() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			this.globalScoreboard.addToPlayers(player);
		}
	}

	public void destory() {
		this.entries.forEach(this::delete);
	}

	void sendPacket(Set<Player> players, List<PacketContainer> packets) {
		for (Player player : players) {
			for (PacketContainer packet : packets) {
				this.protocolManager.sendServerPacket(player, packet, false);
			}
		}
	}

	void sendPacket(Player player, List<PacketContainer> packets) {
		for (PacketContainer packet : packets) {
			this.protocolManager.sendServerPacket(player, packet, false);
		}
	}

	void sendPacket(Player[] players, PacketContainer... packets) {
		for (Player player : players) {
			for (PacketContainer packet : packets) {
				this.protocolManager.sendServerPacket(player, packet, false);
			}
		}
	}

	void sendPacket(Set<Player> players, PacketContainer... packets) {
		this.sendPacket(players, Arrays.asList(packets));
	}

	void addPlayer(SharedBase<?> base, Player player, Player... players) {
		base.addPlayer(player);
		for (Player entry : players) {
			base.addPlayer(entry);
		}
	}

	void removePlayer(SharedBase<?> base, Player player, Player... players) {
		base.removePlayer(player);
		for (Player entry : players) {
			base.removePlayer(entry);
		}
	}

	private SharedScoreboardManager unassignDisplaySlot(DisplaySlot[] slots, Player[] players) {
		for (DisplaySlot slot : slots) {
			if (slot != null) {
				this.sendPacket(players, ScoreboardProtocol.setObjectiveDisplayPacket(null, slot));
			}
		}
		return this;
	}

	@Override
	public SharedScoreboardManager unassignDisplaySlot(DisplaySlot slot, Player... players) {
		return this.unassignDisplaySlot(slot, null, null, players);
	}

	@Override
	public SharedScoreboardManager unassignDisplaySlot(DisplaySlot slot, DisplaySlot slot2, Player... players) {
		return this.unassignDisplaySlot(slot, slot2, null, players);
	}

	@Override
	public SharedScoreboardManager unassignDisplaySlot(DisplaySlot slot, DisplaySlot slot2, DisplaySlot slot3, Player... players) {
		return this.unassignDisplaySlot(new DisplaySlot[] { slot, slot2, slot3 }, players);
	}

	@Override
	public Scoreboard createScoreboard() {
		return new SharedScoreboard(this);
	}

	@Override
	public SharedTeam createTeam(String name) {
		SharedTeam team = new SharedTeam(this, name);
		this.entries.add(team);
		return team;
	}

	@Override
	public SharedSidebar createSidebar(String name) {
		SharedSidebar sidebar = new SharedSidebar(this, name);
		this.entries.add(sidebar);
		return sidebar;
	}

	@Override
	public SharedObjective createObjective(String name) {
		SharedObjective objective = new SharedObjective(this, name);
		this.entries.add(objective);
		return objective;
	}

	@Override
	public SharedScoreboardManager removePlayers(Player player, Player... players) {
		this.globalScoreboard.removeFromPlayers(player, players);
		this.entries.forEach(entry -> entry.removeFromPlayers(player, players));
		return this;
	}

	@Override
	public SharedScoreboardManager delete(ScoreboardBase base) {
		this.entries.remove(base);
		if (base instanceof SharedBase<?> realBase) {
			realBase.delete();
		}
		return this;
	}

	@Override
	public Scoreboard getGlobalScoreboard() {
		return this.globalScoreboard;
	}
}