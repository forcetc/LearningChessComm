package edu.neumont.learningChess.controller;

import edu.neumont.chessModel.ICheckChecker;
import edu.neumont.chessModel.Move;
import edu.neumont.chessModel.Team;
import edu.neumont.learningChess.comm.ChessBoardInterface;

public class TeamLocal extends Team {

	
	public TeamLocal(Color color, ChessBoardInterface board, ICheckChecker checkChecker) {
		super(color,board,checkChecker);
	}
	//for testing
	public TeamLocal(Color color){
		super(color);
	}
	@Override
	public synchronized Move getMove() {
		awaitingMove = true;
		try {
			wait();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		Move tempMove = awaitedMove;
		awaitedMove = null;
		awaitingMove = false;
		return tempMove;
	}

	@Override
	public synchronized boolean handleMove(Move move) {
		boolean canHandleMove = true;
		if (awaitingMove) {
			if (isLegalMove(move)) {
				canHandleMove = true;
				awaitedMove = move;
				notify();
			} else {
				canHandleMove = false;
			}
		}
		return canHandleMove;
	}	
}
