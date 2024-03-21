package de.ngloader.scoreboard.api;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public interface ScoreboardSidebar extends ScoreboardObjective {

	ScoreboardSidebar fillLines(int from, int to);

	ScoreboardSidebar setLine(int line, String text);

	ScoreboardSidebar setLine(int line, BaseComponent... text);

	default ScoreboardSidebar setLine(int line, ComponentBuilder text) {
		return this.setLine(line, text.create());
	}

	ScoreboardSidebar deleteLine(int line);

}