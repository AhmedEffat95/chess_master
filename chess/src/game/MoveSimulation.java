package game;

import pieces.Piece;

public class MoveSimulation 
{
	RealBoard simulatedGame;
	
	private int spotStateBackUp[][] = new int[9][9]; //array to backup 64 board positions (will use starting from index 1,1)
	private int simFinalPosition[][] = new int[1][2];
	private Piece tempSelectedPiece;   // to hold the piece to be moved
	private Piece tempAttackedPiece;   //to hold the piece to be removed
	private int FinalvalidMovesCounter;
	private boolean reverseEnPassant = false;
	Piece capturedPawn;
	private String color;
	public MoveSimulation()  //constructor
	{  

	}
	
	public void updateSimulatedGame(RealBoard simulatedGameR, Piece piece)
	{
		simulatedGame = simulatedGameR;
		tempSelectedPiece = piece;
		color = tempSelectedPiece.getColour();
		
	}
	public void backUpSpotState()  //A function to back up a position while we are evaluating king safety after a move
	{
		for(int x=0;x<9;x++)
		{
			for(int y=0;y<9;y++)
			{
				spotStateBackUp[x][y] = simulatedGame.spotState[x][y];
			}
		}
		
	}
	public void retrieveSpotState()
	{
		for(int x=0;x<9;x++)
		{
			for(int y=0;y<9;y++)
			{
				simulatedGame.spotState[x][y] = spotStateBackUp[x][y];
			}
		}
		
	}
	public void reverseLastMove()  //move is being reversed because it affected our king's safety
	{
		int[][] tempPrevPos = new int[1][2];
		int[][] tempPrevPos2 = new int[1][2];
		
		if(tempAttackedPiece != null)
		{
			tempPrevPos = tempAttackedPiece.getPrevPosition();
			for(int b=0;b<2;b++)
			{
				tempPrevPos2[0][b]=tempPrevPos[0][b];
			}
			tempAttackedPiece.setCurrentPosition(tempPrevPos2); //set the position of the piece that was going to be taken to what it was
		}
		else if(reverseEnPassant)
		{
			reverseEnPassant();
			reverseEnPassant = false;
		}
		tempPrevPos = tempSelectedPiece.getPrevPosition();
		for(int b=0;b<2;b++)
		{
			tempPrevPos2[0][b]=tempPrevPos[0][b];
		}
		tempSelectedPiece.setCurrentPosition(tempPrevPos2); //set the position of the piece that was going to be moved to what it was
		
		//simulatedGame.spotState = spotStateBackUp; //reset spotState values to what it was
		
	}

	public boolean isKingSafe()
	{ 

		int position[][] = new int[1][2];
		
		switch (color)
		{
		case "white":
			position = simulatedGame.piece[13].getCurrentPosition();
			break;
		case "black":
			position = simulatedGame.piece[29].getCurrentPosition();
			break;		
		}
		
		if(simulatedGame.isCurrentlySafe(color,position)==true)
		{
			return true;
		}
		else
		{
			return false;
		}

	}
	public boolean simulateMove()
	{

		if(tempAttackedPiece != null)  //there is a piece to be removed
		{
			tempAttackedPiece.setCurrentPosition(RealBoard.takenPiece);
			
		}
		else if( (enPassant( tempSelectedPiece.getCurrentPosition(), simFinalPosition)) )
		{
			simulateEnpassant(simFinalPosition);
			reverseEnPassant = true;
			System.out.println("EN PASSANT SHOULD BE SIMULATED <<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>");
		}
		simulatedGame.updateSpotState( tempSelectedPiece.getCurrentPosition(), RealBoard.empty_spot);
		tempSelectedPiece.setCurrentPosition(simFinalPosition);	
		simulatedGame.updateSpotState(simFinalPosition, tempSelectedPiece.getValue());
		if(isKingSafe()==true)
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	public int[][] getFinalValidMoves(int initialValidMoves[][])  //Returns the actual valid moves after taking king safety into account 
	{
		backUpSpotState();
		FinalvalidMovesCounter = 0;
		int finalValidMoves[][] = new int[32][2];
		
		for(int x=0;x<finalValidMoves.length;x++)
		{	
			simFinalPosition[0][0] = initialValidMoves[x][0];
			simFinalPosition[0][1] = initialValidMoves[x][1];
			tempAttackedPiece = simulatedGame.determineSelectedPiece(simFinalPosition);
			if( (initialValidMoves[x][0]==0)  )  //End of moves
			{
				break;
			}
			if(simulateMove()) //Move is Okay, king is safe
			{
				finalValidMoves[FinalvalidMovesCounter][0]= simFinalPosition[0][0];
				finalValidMoves[FinalvalidMovesCounter][1]= simFinalPosition[0][1];
				FinalvalidMovesCounter++;
			}
			reverseLastMove();
			retrieveSpotState();
			if (initialValidMoves.length == x+1)  //To make sure that we do not get out of bound when initialvalidmoves is smaller than final valid moves
			{
				break;
			}
		}
		
		return finalValidMoves;
	}
	
	public void simulateEnpassant(int destR[][])   //Simulate the En passant process, remove the piece that would be captured and update its spot value on board
	{
		int dest[][] = new int [1][2];
		for(int h =0; h<2;h++)
		{
			dest[0][h] = destR[0][h];
		}
		String color = simulatedGame.getTurn();
		
		switch(color)
		{
			case "white":
					dest[0][0] += 1;  //The column value stays the same, the row number changes by 1 to select the captured pawn
					break;
			case "black":
					dest[0][0] -= 1;
					break;
		}
		capturedPawn = simulatedGame.determineSelectedPiece(dest);
		
		capturedPawn.setCurrentPosition(RealBoard.takenPiece);
		simulatedGame.updateSpotState(dest,RealBoard.empty_spot);	
	}
	public void reverseEnPassant()   //This function reverses the simulation of En passant, it returns the captured pawn back to live
	{
		int[][] PrevPos = new int[1][2];
		int[][] PrevPos2 = new int[1][2];
		PrevPos = capturedPawn.getPrevPosition();
		for(int b=0;b<2;b++)
		{
			PrevPos2[0][b]=PrevPos[0][b];
		}
		capturedPawn.setCurrentPosition(PrevPos2);
		
	}
	public boolean enPassant(int initial[][], int dest[][]) //can assume the piece is a pawn, detects if the move is enPassant
	{
		if ( (tempSelectedPiece.getValue()!= RealBoard.white_pawn) &&  (tempSelectedPiece.getValue()!=RealBoard.black_pawn) )
		{ //The move was not done by a pawn, no En passants available
			return false;
		}
		int colDsiplacement = Math.abs(initial[0][1] - dest[0][1]);
		if (  (colDsiplacement==1) && (simulatedGame.checkSpotValue(dest[0][0],dest[0][1])==0 ) )
		{
			return true;
		}
		return false;
	}
	
}
