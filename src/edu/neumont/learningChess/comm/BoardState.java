package edu.neumont.learningChess.comm;

import edu.neumont.chessModel.ChessBoardInterface;
import edu.neumont.chessModel.ChessPiece;
import edu.neumont.chessModel.Location;
import edu.neumont.chessModel.Team;

public class BoardState {

	private static final int BOARD_SIZE = 8;
	private ChessPiece board[][] = new ChessPiece[BOARD_SIZE][BOARD_SIZE];
	private boolean whitesTurn;
	
	public BoardState(Team currentTeam, ChessBoardInterface board){
		getPeicesFromBoard(board);
		whitesTurn = (currentTeam.isWhite());
	}
	
	public BoardState(){
		
	}	
	
	private void getPeicesFromBoard(ChessBoardInterface board){
		for(int x =0; x<BOARD_SIZE; x++){
			for(int y= 0; y<BOARD_SIZE; y++){
				this.board[x][y] = board.getPiece(new Location(x,y));
			}
		}
	}
	
	public boolean wasWhiteTeamsTurn(){
		return whitesTurn;
	}
	
	//returns a piece  user that is using this will have to get to color from the piece's team or call isWhitePiece.
	public ChessPiece getPiece(Location location)
	{
		return board[location.getRow()][location.getColumn()];
	}
	
	public boolean isWhitePiece(Location location)
	{
		return board[location.getRow()][location.getColumn()].getTeam().isWhite();
	}
	
	public void setPiece(ChessPiece piece)
	{
		// TODO: the piece shouldn't have a location associated with it
		Location pieceLocation = piece.getLocation();
		board[pieceLocation.getRow()][pieceLocation.getColumn()] = piece;
	}
	
	public void setEmptyPiece(Location location)
	{	
		// TODO: the piece shouldn't have a location associated with it
		board[location.getRow()][location.getColumn()] = null;
	}
}
