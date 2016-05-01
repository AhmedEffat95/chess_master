package pieces;

import game.MoveSimulation;
import game.RealBoard;

public class King implements Piece
{
	private int[][] position = new int[1][2]; //to hold the current position of a knight
	private int[][] PreviousPosition = new int[1][2]; //to hold the previous position of a knight
	
	RealBoard Game;
	MoveSimulation simulateKing;
	private String myColor;
	private int myValue;
	final int initialPositionX;
	final int initialPositionY;
	boolean moved = false;
	boolean shortCastle;
	boolean longCastle;
	public King (RealBoard GameR, String color, int initialXr, int initialYr)  //constructor
	{
		Game = GameR;
		simulateKing = new MoveSimulation();
		this.myColor = color;	
		// set the initial position for the knight
		position[0][0] = initialXr;
		position[0][1] = initialYr;
		
		initialPositionX = initialXr;  //Holds its initial position forever, can not be changed
		initialPositionY = initialYr;
		
		PreviousPosition[0][0] = position[0][0];  //To avoid null pointer
		PreviousPosition[0][1] = position[0][1];
		switch (myColor)    
		{		
			case "white": 
				          myValue = RealBoard.white_king;
						  break;
			case "black": 		
		          		  myValue = RealBoard.black_king;
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
	public int[][] getAttackedSpots()
	{
		int attackedSpots[][] = new int[8][2]; // A king can have a maximum of 6 valid moves at a time in theory
		int tempSpotValue; //to hold a calculated spot value
		
		//counter to maintain size of validMoves array
		int counter = 0;	
		int xStep = 1; //will start with moving up and down the x axis 
		int yStep = 0; //stay on the same y axis for now
		
		for(int x=0;x<2;x++)   // get 4 moves along x and y axis
		{
			for(int y=0;y<2;y++)
			{
					tempSpotValue = Game.checkSpotValue(position[0][0]+xStep, position[0][1]+yStep);
					if( (tempSpotValue != -1)  )  
					{
						attackedSpots[counter][0] = position[0][0]+xStep;
						attackedSpots[counter][1] = position[0][1]+yStep;
						counter++;
					}
					xStep = -xStep;
					yStep = -yStep;
			 }
				//At this point we will have moved straight along the x axis, time to do the same along the y axis
				xStep = 0;
				yStep = 1;
		}
		
		// now get the diagonal moves of a king
		xStep = 1;
		yStep = -1;
		
		for(int x=0;x<2;x++)   // get 4 diagonal moves
		{
			for(int y=0;y<2;y++)
			{	
				tempSpotValue = Game.checkSpotValue(position[0][0]+xStep, position[0][1]+yStep);
				if( (tempSpotValue != -1)  )  
				{
					attackedSpots[counter][0] = position[0][0]+xStep;
					attackedSpots[counter][1] = position[0][1]+yStep;
					counter++;
				}
					xStep = -xStep;
					yStep = -yStep;
			 }
				//At this point we will have moved straight along the x axis, time to do the same along the y axis
				xStep = 1;
				yStep = 1;
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
		simulateKing.updateSimulatedGame(Game, this);
		int validMoves[][] = new int [8][2];
		int attackedSpots[][] = new int[8][2];
		int counter = 0;
		attackedSpots = getAttackedSpots();
		
		for(int x=0;x<attackedSpots.length;x++)
		{
			if( 
					( (Game.checkSpotValue(attackedSpots[x][0], attackedSpots[x][1]) > 7) && (myColor=="white") )
					|| ( (Game.checkSpotValue(attackedSpots[x][0], attackedSpots[x][1]) < 7) && (myColor=="black") && (Game.checkSpotValue(attackedSpots[x][0], attackedSpots[x][1]) > 1) )
				    || (Game.checkSpotValue(attackedSpots[x][0], attackedSpots[x][1])==0)
			 )
			{
				validMoves[counter][0] = attackedSpots[x][0];
				validMoves[counter][1] = attackedSpots[x][1];
				counter++;
			}
		}
		if(shortCastle)
		{
			validMoves[counter][0] = position[0][0];
			validMoves[counter][1] = position[0][1] + 2;
			counter++;
		}
		if(longCastle)
		{
			validMoves[counter][0] = position[0][0];
			validMoves[counter][1] = position[0][1] - 2;
			counter++;
		}
		validMoves = simulateKing.getFinalValidMoves(validMoves);
		for(int g=0;g<validMoves.length;g++)
		{
			//System.out.println("king valid move "+g+" is "+validMoves[g][0]+" and " + validMoves[g][1]);
		}
		return validMoves;
	}
	
	public boolean moved()
	{
		if (moved == true)
		{
			return true;
		}
		if( (position[0][0] == initialPositionX) && (position[0][1]==initialPositionY) )
		{
			return false;
		}
		else 
		{
			moved = true;
			return true;
		}
		
	}
	public void setShortCastle(boolean shortCastleR)
	{
		shortCastle = shortCastleR;
	}
	public void setLongCastle(boolean longCastleR)
	{
		longCastle = longCastleR;
	}
}
