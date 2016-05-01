package pieces;

import game.MoveSimulation;
import game.RealBoard;

public class Knight implements Piece 
{
	private int[][] position = new int[1][2]; //to hold the current position of a knight
	private int[][] PreviousPosition = new int[1][2]; //to hold the previous position of a knight
	
	RealBoard Game;
	MoveSimulation simulateKnight;
	private String myColor;
	private int myValue;

	public Knight (RealBoard GameR, String color, int initialXr, int initialYr)  //constructor
	{
		Game = GameR;
		simulateKnight = new MoveSimulation();
		this.myColor = color;	
		// set the initial position for the knight
		position[0][0] = initialXr;
		position[0][1] = initialYr;
		PreviousPosition[0][0] = position[0][0];  //To avoid null pointer
		PreviousPosition[0][1] = position[0][1];
		switch (myColor)    
		{		
			case "white": 
				          myValue = RealBoard.white_knight;
						  break;
			case "black": 		
		          		  myValue = RealBoard.black_knight;
		          		  break;				  
		}	
	}

	@Override
	public int[][] getCurrentPosition() 
	{
		return position;
	}

	@Override
	public void setCurrentPosition(int[][] updatedPosition)
	{
		//backup previous position first
		PreviousPosition[0][0] = position[0][0];
		PreviousPosition[0][1] = position[0][1];
		
		//Then set the new position as requested
		position[0][0] = updatedPosition[0][0];
		position[0][1] = updatedPosition[0][1];	
		
	}

	@Override
	public int[][] getPrevPosition()
	{
		return PreviousPosition;
	}

	@Override
	public String getColour() 
	{
		return myColor;
	}

	@Override
	public int getValue() 
	{
		return myValue;
	}


	@Override
	public int[][] getAttackedSpots() {
		int attackedSpots[][] = new int [9][9];  //An array to hold spots attacked by a knight
		
		int xStepSize = 1;
		int yStepSize = 2; 
		int counter = 0; //To count array elements
		//Knights will always move with x and y magnitudes of (1,2) or (2,1) where each
		for(int x=0; x<2; x++)
		{
			for(int y=0; y<2; y++)
			{
				for(int z=0; z<2; z++)
				{
					if( 
							( (position[0][0]+ (xStepSize) ) > 0) && ( (position[0][0]+(xStepSize)) < 9) 
							&& ( (position[0][1]+(yStepSize)) > 0) && ( (position[0][1]+(yStepSize)) < 9)
					  )  //check if the spot actually exists
						{
							attackedSpots[counter][0] = position[0][0]+(xStepSize);
							attackedSpots[counter][1] = position[0][1]+(yStepSize);
							counter++;
							
						} 
					yStepSize = -yStepSize;  //
				}
				xStepSize = -xStepSize;
			}
			xStepSize = 2;  //reverse initial conditions to get the rest of knight moves
			yStepSize = 1;
		}
		return attackedSpots;
	}

	@Override
	public boolean canMove() 
	{
		int validMovesCounter = 0;
		int validMovesL[][] = new int[32][2];
		validMovesL = getValidMoves();
		for(int x = 0; x<validMovesL.length; x++)
		{
			if(validMovesL[x][0] == 0)  //if we ran out of moves break
			{
				break;
			}
			validMovesCounter++;
		}
		
		if(validMovesCounter > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public int[][] getValidMoves() 
	{
		simulateKnight.updateSimulatedGame(Game, this);
		// same as attacked spots except when there is a piece on a particular spot
		int attackedSpots[][] = new int[16][2];
		int validMoves[][] = new int[16][2];
		attackedSpots=getAttackedSpots();
		int tempSpotValue;
		int counter=0;
		//filter attacked spots to provide valid moves only
		for (int x=0; x<attackedSpots.length; x++)
		{
			tempSpotValue = Game.checkSpotValue(attackedSpots[x][0],attackedSpots[x][1]);
			if( 
					(tempSpotValue==0)  //if spot is empty
					|| ( (myColor=="black") && (tempSpotValue> 1) && (tempSpotValue<7)  )//or spot has a white piece + we are black
					|| ( (myColor=="white") && (tempSpotValue> 7) ) //or spot has a black piece + we are white    
			  )
			    
			{  //consider move to be a valid move
				validMoves[counter][0] = attackedSpots[x][0];
				validMoves[counter][1] = attackedSpots[x][1];
				counter++;
			} //else do not consider the move as a valid move
	   
		}

		validMoves = simulateKnight.getFinalValidMoves(validMoves);
		for(int g=0;g<validMoves.length;g++)
		{
			//System.out.println("knight valid move "+g+" is "+validMoves[g][0]+" and " + validMoves[g][1]);
		}
		return validMoves;
	}


}
