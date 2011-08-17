package edu.neumont.learningChess.controller;

import edu.neumont.chessModel.Move;
import edu.neumont.chessModel.Team;

public abstract class Player {
	
	protected Team team;
	
	public Player(Team team) {
		this.team = team;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public abstract Move getMove();
}
