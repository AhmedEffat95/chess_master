package game;

import gui.UI;

public class BoardHighlight implements Runnable 
{
	UI GUI;
	private volatile Thread th;
	private String threadName;
	
	int validMoves[][];
	
	public BoardHighlight(String name, UI GUI, int[][] tempValidMoves)
	{
		threadName = name;
		this.GUI = GUI;
		validMoves = tempValidMoves;
		
	}

	@Override
	public void run()
	{	
		displayValidMoves(validMoves);

		killThread();
	}
	
	public void displayValidMoves(int validMoves[][])
	{
		
		int[][] tempPosition = new int[1][2];
		for(int x=0; x<validMoves.length; x++)
		{
			if(validMoves[x][0] == 0)
			{
				return;    //end of moves
			}
			tempPosition[0][0] = validMoves[x][0];
			tempPosition[0][1] = validMoves[x][1];
			UI.updateButton(tempPosition, 13);
		}
	}
	

	
	public void start()
	{
		if(th==null)
		{
			th = new Thread(this, threadName);
			th.start();
		}
	}
	
	public void killThread()
	{
		th = null;
	}
}
