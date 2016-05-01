package pieces;

import game.MoveSimulation;
import game.RealBoard;

public class Bishop implements Piece
{
	private int[][] position = new int[1][2]; //to hold the current position of a bishop
	private int[][] PreviousPosition = new int[1][2]; //to hold the previous position of a bishop
	
	RealBoard Game;
	MoveSimulation simulateBishop;
	private String myColor;
	private int myValue;
	
	public Bishop(RealBoard GameR, String color, int initialXr, int initialYr) //constructor
	{
		Game = GameR;
		simulateBishop = new MoveSimulation();
		this.myColor = color;	
		// set the initial position for the bishop
		position[0][0] = initialXr;
		position[0][1] = initialYr;
		PreviousPosition[0][0] = position[0][0];  //To avoid null pointer
		PreviousPosition[0][1] = position[0][1];
		switch (myColor)
		{		
			case "white": 
				          myValue = RealBoard.white_bishop;
						  break;
			case "black": 		
		          		  myValue = RealBoard.black_bishop;
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
		
		System.out.println("bishop new position set to "+updatedPosition[0][0]+" and "+ updatedPosition[0][1] );	
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
	public int[][] getAttackedSpots() 
	{
		int attackedSpots[][] = new int[16][2]; // An array to hold positions attacked by a bishop
				
		int xStepDirection = 1;  //To hold the direction of motion ( + and -) while searching for spots being attacked by us
		int yStepDirection = 1;
		int stepCounter = 0; //to count how many steps we are currently away from initial position
		int counter = 0; //To count array elements
		
		// A bishop can move diagonally in a maximum of 4 directions;All the free squares in each direction
		//are considered to be attacked up to and including the first square on its way that has a piece
		for(int x=0; x<2; x++)                 
		{
			for (int y=0; y<2; y++)
			{
				do   //do while loop to make sure we do this at least once in each direction since this is only the squares
					//we attack and not necessarily the ones we can move to
				{
					stepCounter++;
					if( 
						( (position[0][0]+ (xStepDirection*stepCounter) ) > 0) && ( (position[0][0]+(xStepDirection*stepCounter)) < 9) 
						&& ( (position[0][1]+(yStepDirection*stepCounter)) > 0) && ( (position[0][1]+(yStepDirection*stepCounter)) < 9)
					  )  //check if the spot actually exists
					{
						attackedSpots[counter][0] = position[0][0]+(xStepDirection*stepCounter);
						attackedSpots[counter][1] = position[0][1]+(yStepDirection*stepCounter);
						counter++;
						
					} //else we are out of board range anyway, while loop will terminate here
					
				}
				while(Game.checkSpotValue(position[0][0]+(xStepDirection*(stepCounter)),position[0][1]+(yStepDirection*(stepCounter)))==0);
				//while loop is used to ensure that the last  spot we checked was not occupied(breaking path) 
				stepCounter = 0;  //we just finished a direction, reset counter
				yStepDirection = -yStepDirection;    
			}
			xStepDirection = -xStepDirection;    // xStepDirection and YstepDirection are be reversed as necessary 
												// to ensure that we cover all 4 directions a bishop can move in
												////  (1  1)  // (1  -1)  // (-1 -1)    //(-1  1)
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
		simulateBishop.updateSimulatedGame(Game, this);
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

		validMoves = simulateBishop.getFinalValidMoves(validMoves);
		for(int g=0;g<validMoves.length;g++)
		{
			//System.out.println("bishop valid move "+g+" is "+validMoves[g][0]+" and " + validMoves[g][1]);
		}
		return validMoves;
	}
	@Override
	public int[][] getPrevPosition() 
	{	
		return PreviousPosition;
	}


}
