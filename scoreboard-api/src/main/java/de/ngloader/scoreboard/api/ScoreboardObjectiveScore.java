package de.ngloader.scoreboard.api;

public interface ScoreboardObjectiveScore extends ScoreboardBase {

	String getName();

	int getScore();

	ScoreboardObjectiveScore setScore(int score);

	ScoreboardObjective getObjective();

}