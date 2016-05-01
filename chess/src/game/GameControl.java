package game;

import javax.swing.JOptionPane;

import gui.UI;

public class GameControl 
{
	private RealBoard currentGame;
	private TimeManagement time;
	private UI GUI;
	private int timeControlIndex;
	private int timeControl;
	
	public GameControl( UI GUI,RealBoard currentGame, int TimeControlIndex) //constructor
	{
		this.currentGame = currentGame;
		this.GUI = GUI;
		this.timeControlIndex = TimeControlIndex;
		startNewGame();
	}
	public void startNewGame()
	{
		GUI.setGameOn(true);
		GUI.setInitialBoard();
		currentGame.initGame();
		timeControl = determineTimeControl(timeControlIndex);
		time = new TimeManagement("TimeThread", currentGame, GUI,  this);
		MaintainMovesHistory.resetMovesHistory();
		FlipView.resetFlipView();
		time.start();

	}
	public void endGame(String endCause)
	{
		GUI.setGameOn(false);
		time.killThread();
		UI.NewGame.setText("New Game");
		switch (endCause)
		{
		case "user":
			System.out.println("GAME ENDED BY USER **********************");
			JOptionPane.showMessageDialog(null, "Game ended upon user's request", " Game ended ", JOptionPane.INFORMATION_MESSAGE);
			break;
		case "checkmate":
			System.out.println("GAME ENDED BY checkmate **********************");
			JOptionPane.showMessageDialog(null, ""+ ( (currentGame.getTurn() == "white")? "Black":"White" )+ " won by Checkmate", " Game ended ", JOptionPane.INFORMATION_MESSAGE);
			break;
		case "stalemate":
			System.out.println("GAME ENDED BY stalemate **********************");
			JOptionPane.showMessageDialog(null, "Game drawn by stalemate", " Game ended ", JOptionPane.INFORMATION_MESSAGE);
			break;
		case "time" :
			System.out.println("GAME ENDED BY time out **********************");
			JOptionPane.showMessageDialog(null, ""+ ( (currentGame.getTurn() == "white")? "Black":"White" )+ " won on time", " Game ended ", JOptionPane.INFORMATION_MESSAGE);
			break;
		
		}
		

	}
	public int determineTimeControl(int Index)  //returns time control in seconds*10
	{
		switch(Index)
		{
		case 0:
			return 10*60;
		case 1:
			return 20*60;			
		case 2:
			return 30*60;		
		case 3:
			return 60*60;		
		default:
			return 10*60;		
		}
	}
	public int getTimeControl()
	{
		return timeControl;
	}
	

}
