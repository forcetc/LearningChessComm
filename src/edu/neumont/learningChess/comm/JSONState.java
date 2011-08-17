package edu.neumont.learningChess.comm;

import edu.neumont.chessModel.Bishop;
import edu.neumont.chessModel.ChessBoardInterface;
import edu.neumont.chessModel.ChessPiece;
import edu.neumont.chessModel.King;
import edu.neumont.chessModel.Knight;
import edu.neumont.chessModel.Location;
import edu.neumont.chessModel.Pawn;
import edu.neumont.chessModel.Queen;
import edu.neumont.chessModel.Rook;
import edu.neumont.chessModel.Team;
import edu.neumont.learningChess.controller.TeamLocal;
import edu.neumont.learningChess.controller.TeamServer;

public class JSONState 
{

	public static final int CHESS_BOARD_SIZE = 8;
	
	private PieceState[] pieceStates = new PieceState[32];
	private boolean isWhiteTurn;
	private Location pawnMovedTwoLocation = null;

	public JSONState(PieceState[] pieces, boolean isWhiteTurn, boolean pawnMovedTwo, Location pawnMovedTwoLocation)
	{
		this.pieceStates = pieces;
		this.isWhiteTurn = isWhiteTurn;
		if(pawnMovedTwo)
			this.pawnMovedTwoLocation = pawnMovedTwoLocation;
	}
	
	public JSONState(ChessBoardInterface board, boolean isWhiteTurn)
	{
		this.isWhiteTurn = isWhiteTurn;
		if(board.hasEnPassantMove())
		{
			this.pawnMovedTwoLocation = board.getEnPassantablePawnLocation();
		}
		int currentArrayPosition=0;
		for (int row = 0; row < CHESS_BOARD_SIZE; row++) 
		{
			for (int col = 0; col < CHESS_BOARD_SIZE; col++) 
			{
				addPieceToPieceStates(board.getPiece( new Location(row,col)), new Location(row,col),currentArrayPosition++);
			}
		}
	}
	
	public GameState getGameState()
	{
		GameState generatedGameState = new GameState();
		
		//We made our own constructor for these teams...is this bad?
		Team white = new TeamLocal(Team.Color.LIGHT);
		Team black = new TeamServer(Team.Color.DARK);
		
		
		
		for (int i = 0; i < pieceStates.length; i++) 
		{
			PieceState currentPiece = pieceStates[i];
			if(currentPiece != null)
			{
				ChessPiece piece = null;
				if(currentPiece.isWhite())
				{
					piece = makeChessPieceFromPieceState(currentPiece);
					piece.setTeam(white);
					if(piece instanceof King)
					{
						generatedGameState.setKingMoved(true, piece.hasMoved());
					}
				}
				else
				{
					piece = makeChessPieceFromPieceState(currentPiece);
					piece.setTeam(black);
					if(piece instanceof King)
					{
						generatedGameState.setKingMoved(false, piece.hasMoved());
					}
				}
				generatedGameState.setPiece(piece.getLocation(), piece);
			}
		}
		
		setRooksMoved(generatedGameState, new Location(0, 0), 0);
		setRooksMoved(generatedGameState, new Location(0, 7), 1);
		setRooksMoved(generatedGameState, new Location(7, 0), 2);
		setRooksMoved(generatedGameState, new Location(7, 7), 3);
		
		generatedGameState.setTeamsMove(this.isWhiteTurn);
		
		//Set pawn metadata
		generatedGameState.setPawnMovedTwo(isPawnMovedTwo());
		if(isPawnMovedTwo())
		{
			generatedGameState.setPawnMovedTwoRow(this.pawnMovedTwoLocation.getRow());
			generatedGameState.setPawnMovedTwoCol(this.pawnMovedTwoLocation.getColumn());
		}
		
		return generatedGameState;
	}
	
	private void setRooksMoved(GameState gameState, Location location, int rookNumber)
	{
		ChessPiece cornerPiece = gameState.getPiece(location);
		if(cornerPiece == null) {
			gameState.setRookMoved(rookNumber, true);
		} else {
//			if(cornerPiece instanceof Rook)
//			{	
				gameState.setRookMoved(rookNumber, cornerPiece.hasMoved());
//			}
		}
	}
	
	private ChessPiece makeChessPieceFromPieceState(PieceState pieceState)
	{
		ChessPiece madeChessPiece = null;
		String pieceName = pieceState.getPieceName();
		
		if("Pawn".equals(pieceName))
		{
			madeChessPiece = new Pawn();
			//generatedGameState.setPawnMovedTwo(pieceState.specialMoveInformation);
		}
		else if("Rook".equals(pieceName))
		{
			madeChessPiece = new Rook();
			madeChessPiece.setHasMoved(pieceState.isSpecialMoveInformation());
		}
		else if("Knight".equals(pieceName))
		{
			madeChessPiece = new Knight();
		}
		else if("Bishop".equals(pieceName))
		{
			madeChessPiece = new Bishop();
		}
		else if("Queen".equals(pieceName))
		{
			madeChessPiece = new Queen();
		}
		else if("King".equals(pieceName))
		{
			madeChessPiece = new King();
			madeChessPiece.setHasMoved(pieceState.isSpecialMoveInformation());
		}
		else
		{
			System.out.println("JSONState could not make a piece with the piece name " + pieceName);
		}
		if(madeChessPiece!=null)
			madeChessPiece.setLocation(pieceState.getLocation());
		
		return madeChessPiece;
	}
	
	
	private void addPieceToPieceStates(ChessPiece pieceToBeAddedToArray, Location pieceLocation, int currentArrayPosition)
	{
		if(pieceToBeAddedToArray != null)
		{
			addPieceToPieceStates(pieceToBeAddedToArray, pieceLocation, pieceToBeAddedToArray.getTeam().isWhite(), currentArrayPosition);
		}
	}
	
	private void addPieceToPieceStates(ChessPiece pieceToBeAddedToArray, Location pieceLocation, boolean isWhite,int currentArrayPosition )
	{
		String name = pieceToBeAddedToArray.getName();
		
		if("Pawn".equals(name))
		{
			this.pieceStates[currentArrayPosition] = new PieceState(pieceToBeAddedToArray.getName(), pieceLocation, false, isWhite);//Will need to be the boolean saying if the pawn has jumped or not.
		}
		else if("Rook".equals(name))
		{
			this.pieceStates[currentArrayPosition] = new PieceState(pieceToBeAddedToArray.getName(), pieceLocation, pieceToBeAddedToArray.hasMoved(), isWhite);
		}
		else if("Bishop".equals(name))
		{
			this.pieceStates[currentArrayPosition] = new PieceState(pieceToBeAddedToArray.getName(), pieceLocation, false, isWhite);//boolean will not be used, since I know the piece type	
		}
		else if("Knight".equals(name))
		{
			this.pieceStates[currentArrayPosition] = new PieceState(pieceToBeAddedToArray.getName(), pieceLocation, false, isWhite);//boolean will not be used, since I know the piece type
		}
		else if("Queen".equals(name))
		{
			this.pieceStates[currentArrayPosition] = new PieceState(pieceToBeAddedToArray.getName(), pieceLocation, false, isWhite);//boolean will not be used, since I know the piece type
		}
		else if("King".equals(name))
		{
			this.pieceStates[currentArrayPosition] = new PieceState(pieceToBeAddedToArray.getName(), pieceLocation, pieceToBeAddedToArray.hasMoved(), isWhite);
		}

	}

	public PieceState[] getPieceStates() {
		return pieceStates;
	}

	public boolean isWhiteTurn() {
		return isWhiteTurn;
	}

	public boolean isPawnMovedTwo() {
		return pawnMovedTwoLocation!=null;
	}

	public Location getPawnMovedTwoLocation() {
		return pawnMovedTwoLocation;
	}

}
