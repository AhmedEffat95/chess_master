package game;

public class FlipView 
{
	private static boolean boardFlipped = false;
	private static int currentDisplayIndex;

	public static boolean isBoardFlipped() 
	{
		return boardFlipped;
	}

	public static void setBoardFlipped(boolean boardFlipped)
	{
		FlipView.boardFlipped = boardFlipped;
	}
	
	public static void flipBoard()
	{
		
		int [][] tempSpotState = new int[9][9];
		int tempPos[][] = new int[1][2];
		
		if(MaintainMovesHistory.movesCounter==0)
		{
			tempSpotState = RealBoard.getInitialSpotState();
		}
		else if(MaintainMovesHistory.getDisplayUptoDate()==true)  //if display is up to date, display the flipped position of the uptodate position
		{	
			tempSpotState = RealBoard.allSpotStates.get(RealBoard.allSpotStates.size()-1);  //get the last position
		}
		else  //display is not up to date
		{
			tempSpotState = RealBoard.allSpotStates.get(currentDisplayIndex);
		}
		for (int i=1; i<9; i++ )
		{
			for (int j=1; j<9; j++ )
			{
				tempPos[0][0] = 9-i;    //flip the ranks display
				tempPos[0][1] = 9-j;
				RealBoard.updateView(tempPos,tempSpotState[i][j]);
			}
		
		}
		boardFlipped = true;
	}
	
	public static void unflipBoard()
	{
		int [][] tempSpotState = new int[9][9];
		int tempPos[][] = new int[1][2];
		
		boardFlipped = false;
		
		if(MaintainMovesHistory.movesCounter==0)
		{
			tempSpotState = RealBoard.getInitialSpotState();
		}
		else if(MaintainMovesHistory.getDisplayUptoDate()==true)  //if display is up to date, display the flipped position of the uptodate position
		{	
			tempSpotState = RealBoard.allSpotStates.get(RealBoard.allSpotStates.size()-1);  //get the last position
		}
		else  //display is not up to date
		{
			tempSpotState = RealBoard.allSpotStates.get(currentDisplayIndex);
		}
		for (int i=1; i<9; i++ )
		{
			for (int j=1; j<9; j++ )
			{
				tempPos[0][0] = i;    //unflip the ranks display
				tempPos[0][1] = j;
				RealBoard.updateView(tempPos,tempSpotState[i][j]);
			}
		
		}
		
	}
	
	public static void setCurrentDisplayIndex(int val)
	{
		currentDisplayIndex = val;
	}
	
	public static int getCurrentDisplayIndex()
	{
		return currentDisplayIndex;
	}
	public static void resetFlipView()
	{
		boardFlipped = false;
	}
}
