package edu.neumont.learningChess.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.neumont.chessModel.ChessBoard;
import edu.neumont.chessModel.ICheckChecker;
import edu.neumont.chessModel.Move;
import edu.neumont.chessModel.Team;
import edu.neumont.learningChess.comm.ChessBoardInterface;
import edu.neumont.learningChess.comm.JSONFactory;
import edu.neumont.learningChess.comm.JSONState;

public class TeamServer extends Team {

	public TeamServer(Color color, ChessBoardInterface board, ICheckChecker checkChecker) {
		super(color, board, checkChecker);
	}

	// for testing
	public TeamServer(Color color) {
		super(color);
	}

	@Override
	public synchronized Move getMove() {
		//TODO fix this so user can retry if connection fails
		Move rtnValue= null; 
		
		JSONState state = new JSONState(super.board, color.equals(Color.LIGHT));
		String json = JSONFactory.createJSONFromJSONState(state);
		try {
			
			URL url = ServerUtil.getGetMoveUrl();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			out.write(json);
			out.flush();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder jsonResponse = new StringBuilder();
			 for (;;) {
				 String line = in.readLine();
				 if (line == null) break;
				 jsonResponse.append(line);
			 }
			 out.close();
			 in.close();
			 rtnValue = JSONFactory.createMoveFromJSON(jsonResponse.toString());
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return rtnValue;
	}

	@Override
	public synchronized boolean handleMove(Move move) {
		return true;
	}
}
