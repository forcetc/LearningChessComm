package edu.neumont.learningChess.controller;

import java.net.MalformedURLException;
import java.net.URL;

public class ServerUtil {

	public static URL getGetMoveUrl() throws MalformedURLException {
		// TODO Auto-generated method stub
		return new URL("HTTP","chess.neumont.edu",8081,"/ChessGame/getmove");
	}
	public static URL getSaveHistoryUrl() throws MalformedURLException {
		// TODO Auto-generated method stub
		return new URL("HTTP","chess.neumont.edu",8081,"/ChessGame/analyzehistory");
	}

}
