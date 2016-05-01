package game;


public class MovesHistoryDisplay implements Runnable
{
	
	private volatile Thread th;
	String threadName;
	boolean GameOn = true;
	int row, col;
	public MovesHistoryDisplay(String threadName, int row, int col)
	{
		
		this.threadName = threadName;
		this.row = row;
		this.col = col;
		
	}

	@Override
	public void run() 
	{
		//GameInstance.GUI.redrawBoard();
		displayOldSpotState();
		killThread();
		
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

	public void displayOldSpotState()
	{
		int index = (2 * row) + col;	
		
		if (index>=MaintainMovesHistory.movesCounter) 
		{
			return;
		}
		if (index==(MaintainMovesHistory.movesCounter-1))
		{
			MaintainMovesHistory.setDisplayUptoDate(true);
		}
		else
		{
			MaintainMovesHistory.setDisplayUptoDate(false);
		}
		int [][] tempSpotState = new int[9][9];		
		tempSpotState = RealBoard.allSpotStates.get(index);
		int tempPos[][] = new int[1][2];
		for (int i=1; i<9; i++ )
		{
			for (int j=1; j<9; j++ )
			{
				tempPos[0][0] = i;
				tempPos[0][1] = j;
				RealBoard.updateView(tempPos,tempSpotState[i][j]);
			}
		}
		FlipView.setCurrentDisplayIndex(index);   //let the FlipView class know which position is currently being displayed
	}

}
