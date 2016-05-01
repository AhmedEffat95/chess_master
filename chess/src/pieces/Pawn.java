package pieces;

import game.MoveSimulation;
import game.RealBoard;

public class Pawn implements Piece 
{
	private int[][] position = new int[1][2]; //to hold the current position of a pawn
	private int[][] PreviousPosition = new int[1][2]; //to hold the previous position of a pawn
	RealBoard Game;
	MoveSimulation simulate;
	private String myColor;
	private int myValue;
	private int stepDirection;
	private boolean enPassant = false;
	private int enPassantDirection = 0;
	public Pawn(RealBoard GameR, String color, int initialXr, int initialYr) //constructor
	{		
		Game = GameR;
		simulate = new MoveSimulation();
		 
		this.myColor = color;	
		// set the initial position for the pawn
		position[0][0] = initialXr;
		position[0][1] = initialYr;
		
		PreviousPosition[0][0] = position[0][0];  //To avoid null pointer
		PreviousPosition[0][1] = position[0][1];
		
		switch (myColor)
		{		
			case "white": 
				          stepDirection = -1;   //white pieces move from 8 to 1 (backwards in the eyes of the board)
				          myValue = RealBoard.white_pawn;
						  break;
			case "black": 
		          		  stepDirection = 1;  //black pieces move forward in the eyes of board		
		          		  myValue = RealBoard.black_pawn;
		          		  break;				  
		}				  
		//System.out.println(myColor + " Pawn created at " + position[0][0] + " and " +position[0][1] );
	}
	@Override
	public int[][] getCurrentPosition()
	{	
		return position;
	}
	@Override
	public void setCurrentPosition(int updatedPosition1[][])
	{
		//backup previous position first
		PreviousPosition[0][0] = position[0][0];
		PreviousPosition[0][1] = position[0][1];
		
		//Then set the new position as requested
		position[0][0] = updatedPosition1[0][0];
		position[0][1] = updatedPosition1[0][1];	
		
		//System.out.println("pawn new position set to "+updatedPosition1[0][0]+" and "+ updatedPosition1[0][1] );
	}

	@Override
	public int[][] getAttackedSpots() 
	{
		//pawn attacks diagonally to the left and right by 1 square
		int attackedSpots[][] = new int[2][2]; //pawn can attack a maximum of two squares at a time	

		if(Game.checkSpotValue(position[0][0]+stepDirection, position[0][1]-stepDirection)!=-1)  //make sure it is in range 
		{
			attackedSpots[0][0] = position[0][0] + stepDirection; //pawn always attack one square forward (diagonally)
			attackedSpots[0][1] = position[0][1] - stepDirection;  
		}

		if(Game.checkSpotValue(position[0][0]+stepDirection, position[0][1]+stepDirection)!=-1)
		{
			attackedSpots[1][0] = position[0][0] + stepDirection; //pawn always attack one square forward (diagonally)
			attackedSpots[1][1] = position[0][1] + stepDirection;
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
	public int[][] getValidMoves() //function defining general moving logic for a pawn (own king not yet taken into account)
	{
		simulate.updateSimulatedGame(Game, this);
		int validMoves[][] = new int[4][4]; // A pawn can have a maximum of 4 valid moves at a time in theory
		//counter to maintain size of validMoves array
		int x = 0;	 
		
		if (Game.checkSpotValue(position[0][0]+ stepDirection, position[0][1]) == 0) //check if pawn can move one square forward
		{
			validMoves[x][0] = position[0][0] + stepDirection; //pawn can move forward 
			validMoves[x][1] = position[0][1];
			x++;
			
		}
		if ( (Game.checkSpotValue(position[0][0]+ (2*stepDirection), position[0][1]) == 0)  //check if pawn can move two squares forward
				&&
				( ( (myColor=="white") && (position[0][0]==7) ) || ( (myColor=="black") && (position[0][0]==2) ) )
				&&
				(Game.checkSpotValue(position[0][0]+ (stepDirection), position[0][1]) == 0)
			)
		{
			validMoves[x][0] = position[0][0] + (2*stepDirection); //pawn can move 2 steps forward 
			validMoves[x][1] = position[0][1];
			x++;
			
		}
		// check if we are white and there is a black piece that we can take
		if ( 
				( (Game.checkSpotValue(position[0][0]+ stepDirection, position[0][1] + stepDirection) > 7) && (myColor == "white") )
				||
				( 
				  (Game.checkSpotValue(position[0][0]+ stepDirection, position[0][1] + stepDirection) < 7) && (myColor == "black") 
				  && ( Game.checkSpotValue(position[0][0]+ stepDirection, position[0][1] + stepDirection) > 1)
				)
			) // Take a piece diagonally(take to the right) 
		{
			
			validMoves[x][0] = position[0][0] + stepDirection; //pawn can take diagonally
			validMoves[x][1] = position[0][1] + stepDirection;
			x++;
		}
		if ( 
				( (Game.checkSpotValue(position[0][0]+ stepDirection, position[0][1] - stepDirection) > 7) && (myColor == "white") )
				||
				( 
				  (Game.checkSpotValue(position[0][0]+ stepDirection, position[0][1] - stepDirection) < 7) && (myColor == "black") 
				  && ( Game.checkSpotValue(position[0][0]+ stepDirection, position[0][1] - stepDirection) > 1)
				)
			) // Take a piece diagonally(take to the left) 
		{
			
			validMoves[x][0] = position[0][0] + stepDirection; //pawn can take diagonally
			validMoves[x][1] = position[0][1] - stepDirection;
			x++;
		}
		if(enPassant)
		{
			switch(myColor)
			{
				case "white":
					
						if ( (Game.checkSpotValue(position[0][0], position[0][1]+1) == 8) && (enPassantDirection == 1) )
						{
							validMoves[x][0] = position[0][0] + stepDirection; //pawn can take through enPassant
							validMoves[x][1] = position[0][1] + 1;
							x++;
						}
						if ( (Game.checkSpotValue(position[0][0], position[0][1]-1) == 8) && (enPassantDirection == -1) )
						{
							validMoves[x][0] = position[0][0] + stepDirection; //pawn can take through enPassant
							validMoves[x][1] = position[0][1] - 1;
							x++;
						}
						break;
				case "black":
						if ( (Game.checkSpotValue(position[0][0], position[0][1]+1) == 2) && (enPassantDirection == 1) )
						{
							validMoves[x][0] = position[0][0] + stepDirection; //pawn can take through enPassant
							validMoves[x][1] = position[0][1] + 1;
							x++;
						}
						if ( (Game.checkSpotValue(position[0][0], position[0][1]-1) == 2) &&  (enPassantDirection == -1) )
						{
							validMoves[x][0] = position[0][0] + stepDirection; //pawn can take through enPassant
							validMoves[x][1] = position[0][1] - 1;
							x++;
						}
						break;	
			}
			
		}
		validMoves = simulate.getFinalValidMoves(validMoves);
		for(int g=0;g<validMoves.length;g++)
		{
			//System.out.println("pawn valid move "+g+" is "+validMoves[g][0]+" and " + validMoves[g][1]);
		}
		return validMoves;
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
	public int[][] getPrevPosition()
	{
		return PreviousPosition;
	}
    public void setEnPassant(boolean enPassantR, int direction)
    {
    	enPassant = enPassantR;
    	enPassantDirection = direction;
    }

}
