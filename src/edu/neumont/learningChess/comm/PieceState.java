package edu.neumont.learningChess.comm;

import edu.neumont.chessModel.Location;


public class PieceState 
{
	private Location location;
	private String pieceName;
	private boolean specialMoveInformation = false;
	private boolean isWhite = false;
	
	public PieceState(String pieceName, Location location, boolean specialMoveInformation, boolean isWhite)
	{
		this.location = location;
		this.pieceName = pieceName;
		this.specialMoveInformation = specialMoveInformation;
		this.isWhite = isWhite;
	}
	
	public Location getLocation() {
		return location;
	}


	public String getPieceName() {
		return pieceName;
	}


	public boolean isSpecialMoveInformation() {
		return specialMoveInformation;
	}


	public boolean isWhite() {
		return isWhite;
	}


}
