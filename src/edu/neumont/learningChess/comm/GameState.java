package edu.neumont.learningChess.comm;

import java.util.ArrayList;
import java.util.List;

import edu.neumont.chessModel.Bishop;
import edu.neumont.chessModel.ChessPiece;
import edu.neumont.chessModel.King;
import edu.neumont.chessModel.Knight;
import edu.neumont.chessModel.Location;
import edu.neumont.chessModel.Pawn;
import edu.neumont.chessModel.Queen;
import edu.neumont.chessModel.Rook;
import edu.neumont.chessModel.Team;

public class GameState {

	private static final int PAWN_VALUE = 4;
	private static final int ROOK_VALUE = 6;
	private static final int KNIGHT_VALUE = 8;
	private static final int BISHOP_VALUE = 10;
	private static final int QUEEN_VALUE = 12;
	private static final int KING_VALUE = 14;
	private static final int BOARD_SIZE = 8;
	private static final int BUFFER_SIZE = 32;
	private BoardState boardState;
	private MetaBoard  metaBoard;
	
	public GameState()
	{
		boardState = new BoardState();
		metaBoard = new MetaBoard();
	}
	
	//32 byte buffer
	public GameState(byte[] buffer, Team white, Team black) 
	{
		//is the byte[] that is passed in going to be like the serialized byte[]?
		this();
		List<Integer> metaValues = new ArrayList<Integer>();
		
		NibbleBoard board = new NibbleBoard(buffer);
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				int value = board.get(row, col);
				// if the value is 1 or 2 then its a MetaBoard value.
				if(value == 1 || value == 2)
				{
					metaValues.add(value);
				}
				ChessPiece piece = getPiece(value, white, black);
				Location location = new Location(row,col);
				if(piece != null) {
					piece.setLocation(location);
				}
				setPiece(location,piece);
			}
		}
		setMetaBoardValues(metaValues);
		
		
	}
	
	private void setMetaBoardValues(List<Integer> metaValues)
	{
		for (int i = 0; i < 4; i++) {
			metaBoard.setRookMoved(i, getBooleanValue(metaValues.get(i)));
		}
		metaBoard.setKingMoved(true, getBooleanValue(metaValues.get(4)));
		metaBoard.setKingMoved(true, getBooleanValue(metaValues.get(5)));
		metaBoard.setTeamsMove(getBooleanValue(metaValues.get(6)));
		metaBoard.setPawnMovedTwo(getBooleanValue(metaValues.get(7)));
		
		if(metaValues.size() == 14)
		{
			String row = "0";
			row += getBoardValue(metaValues.get(8));
			row += getBoardValue(metaValues.get(9));
			row += getBoardValue(metaValues.get(10));
			metaBoard.setPawnMovedTwoRow(Integer.parseInt(row, 2));
			String col = "0";
			col += getBoardValue(metaValues.get(11));
			col += getBoardValue(metaValues.get(12));
			col += getBoardValue(metaValues.get(13));
			metaBoard.setPawnMovedTwoCol(Integer.parseInt(col, 2));
		}
	}
	
	private int getBoardValue(int value)
	{
		int boardValue = 0;
		if(value == 1)
		{
			boardValue = 0;
		}
		else if(value == 2)
		{
			boardValue = 1;
		}
		return boardValue;	
	}
	
	private boolean getBooleanValue(int value)
	{
		boolean isTrue = true;
		if(value == 1)
		{
			isTrue = true;
		}
		else if(value == 2)
		{
			isTrue = false;
		}
		return isTrue;
	}
	
	public byte[] serialize()
	{
		byte[] buffer = new byte[BUFFER_SIZE];
		NibbleBoard board = new NibbleBoard(buffer);
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				ChessPiece piece = boardState.getPiece(new Location(row,col));
				int value = getPieceValue(piece);
				board.set(row, col, value);
			}
		}
		for (int i = 0; i < 4; i++) {
			setEmptyValue(board,metaBoard.getRookMoved(i));
		}
		
		setEmptyValue(board,metaBoard.getKingMoved(true));
		setEmptyValue(board,metaBoard.getKingMoved(false));

		setEmptyValue(board,metaBoard.isWhiteTeamsMove());

		if (metaBoard.getPawnMovedTwo()) {
			setEmptyValue(board,true);
			
			int row = metaBoard.getPawnMovedTwoRow();
			for (int i = 0; i < 3; i++){
				int mask = 1 << i;
				setEmptyValue(board, (row & mask) != 0);
			}
			
			int col = metaBoard.getPawnMovedTwoCol();
			for (int i = 0; i < 3; i++){
				int mask = 1 << i;
				setEmptyValue(board, (col & mask) != 0);
			}
		} else {
			setEmptyValue(board,false);
			
		}
		return buffer;
	}
	
	private void setEmptyValue(NibbleBoard board, boolean isSet) {
		Location location = getFirstUnusedLocation(board);
		int value = isSet? 1: 2;
		board.set(location.getRow(), location.getColumn(), value);
	}

	private Location getFirstUnusedLocation(NibbleBoard board) {
		Location location = null;
		for (int row = 0; (location == null) && (row < BOARD_SIZE); row++) {
			for (int col = 0; (location == null) && (col < BOARD_SIZE); col++) {
				int value = board.get(row, col);
				if (value == 0) {
					location = new Location(row, col);
				}
			}
		}
		return location;
	}
	
	private ChessPiece getPiece(int pieceValue, Team white, Team black)
	{
		ChessPiece piece = null;
		switch(pieceValue)
		{
		case 0:
			piece = null;
			break;
		case 1:
			piece = null;
			break;
		case 2: 
			piece = null;
			break;
		case 3: 
			piece = null;
			break;
		case 4: //W pawn
			piece = new Pawn();
			piece.setTeam(white);
			break;
		case 5: //B pawn
			piece = new Pawn();
			piece.setTeam(black);
			break;
		case 6: //W rook
			piece = new Rook();
			piece.setTeam(white);
			break;
		case 7: //B rook
			piece = new Rook();
			piece.setTeam(black);
			break;
		case 8: //W knight
			piece = new Knight();
			piece.setTeam(white);
			break;
		case 9: //B knight
			piece = new Knight();
			piece.setTeam(black);
			break;
		case 10: //W bishop
			piece = new Bishop();
			piece.setTeam(white);
			break;
		case 11: //B bishop
			piece = new Bishop();
			piece.setTeam(black);
			break;
		case 12: //W queen
			piece = new Queen();
			piece.setTeam(white);
			break;
		case 13: //B queen
			piece = new Queen();
			piece.setTeam(black);
			break;
		case 14: //W king
			piece = new King();
			piece.setTeam(white);
			break;
		case 15: //B king
			piece = new King();
			piece.setTeam(black);
			break;
		}
		return piece;
	}
	
	public int getPieceValue(ChessPiece piece)
	{
		int pieceValue = 0;
		if(piece instanceof Pawn)
		{
			pieceValue = PAWN_VALUE;
		}
		else if(piece instanceof Rook)
		{
			pieceValue = ROOK_VALUE;
		}
		else if(piece instanceof Knight)
		{
			pieceValue = KNIGHT_VALUE;
		}
		else if(piece instanceof Bishop)
		{
			pieceValue = BISHOP_VALUE;
		}
		else if(piece instanceof Queen)
		{
			pieceValue = QUEEN_VALUE;
		}
		else if(piece instanceof King)
		{
			pieceValue = KING_VALUE;
		}
		else
		{
			pieceValue = 0;
		}
		
		if((piece != null) && (!piece.getTeam().isWhite())) {
			pieceValue |= 0x01;
		}
		return pieceValue;
	}
	
	public ChessPiece getPiece(Location location)
	{
		return boardState.getPiece(location);
	}
	
	public void setPiece(Location location, ChessPiece piece)
	{
		if(piece == null)
		{
			boardState.setEmptyPiece(location);
		}
		else 
		{
			boardState.setPiece(piece);
		}
	}
	
	public boolean getRookMoved(int rookNum)
	{
		return metaBoard.getRookMoved(rookNum);
	}
	
	public void setRookMoved(int rookNum, boolean hasMoved)
	{
		metaBoard.setRookMoved(rookNum, hasMoved);
	}
	
	public boolean getKingMoved(boolean isWhite)
	{
		return metaBoard.getKingMoved(isWhite);
	}
	
	public void setKingMoved(boolean isWhite, boolean hasMoved)
	{
		metaBoard.setKingMoved(isWhite, hasMoved);
	}
	
	public boolean isWhiteTeamsMove()
	{
		return metaBoard.isWhiteTeamsMove();
	}
	
	public void setTeamsMove(boolean isWhiteTeam)
	{
		metaBoard.setTeamsMove(isWhiteTeam);
	}
	
	public boolean getPawnMovedTwo()
	{
		return metaBoard.getPawnMovedTwo();
	}
	
	public void setPawnMovedTwo(boolean hasMoved)
	{
		metaBoard.setPawnMovedTwo(hasMoved);
	}
	
	public int getPawnMovedTwoRow()
	{
		return metaBoard.getPawnMovedTwoRow();
	}
	
	public void setPawnMovedTwoRow(int row)
	{
		metaBoard.setPawnMovedTwoRow(row);
	}
	
	public int getPawnMovedTwoCol()
	{
		return metaBoard.getPawnMovedTwoCol();
	}
	
	public void setPawnMovedTwoCol(int col)
	{
		metaBoard.setPawnMovedTwoCol(col);
	}
}

