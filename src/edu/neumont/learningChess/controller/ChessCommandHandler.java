package edu.neumont.learningChess.controller;

import edu.neumont.chessModel.Location;

public interface ChessCommandHandler {

	public void handlePlacement(String pieceId, boolean isWhite, Location location);
	public void handleMovement(Location from, Location to, boolean takesPiece);
}
