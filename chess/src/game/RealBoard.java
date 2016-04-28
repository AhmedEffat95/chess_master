package game;

import java.util.ArrayList;

import gui.UI;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;


/*
 * This class provides a communication link between the GUI VIEW and the different pieces(calculations). It will
 * always hold the absolute real board position. It recieves a potentially moved piece (a piece that has been clicked)
 * from mouseListener and carries out some calculations in a background thread(determine where this piece can go for example and whether 
 * this piece can actually be moved) while we are waiting for the player to propose a move, then further calculations are done, also in thread,
 *  before giving permission to the move to be played.
 * It will also hold all the pieces instances which represent all 32 pieces on board
 * 
*/

public class RealBoard implements PieceValues

{
	int movesCounter=0;
	int validMoves[][] = new int[32][2];
	 
	protected Piece piece[]= new Piece[33];  //Create instances for 32 pieces
	protected int spotState[][] = new int[9][9]; //array to hold 64 board positions (will use starting from index 1,1)
	protected static ArrayList<int[][]> allSpotStates = new ArrayList<int[][]>();
	private static int [][] initialSpotState = new int[9][9];
	
	protected Piece selectedPiece;   // to hold the piece to be moved
	protected Piece attackedPiece;   //to hold the piece to be removed
	private Piece potentialSelectedPiece;
	

	private String Turn = "white"; //to be set to  white or black
	private boolean awaitingDestination = false;
	private int selectedPosition[][] = new int[1][2];
	private int finalPosition[][] = new int [1][2];
	private int potSelectedPosition[][] = new int [1][2];
	
	UI GUI; //reference to hold GUI Object
	AdvancedGame AdvGame = new AdvancedGame(this);
	private BoardHighlight highlight;
	private static MovesHistoryDisplay displayPosition;
	GameControl NewGameRequest;
	
	public RealBoard(UI GUIr)
	{
		// initialise a 2D array which holds the initial position of all pieces, white pieces have values from 1-6 and black pieces from 7-12
		GUI = GUIr;		
		
	}

	public void clickRecieved(int clickedX, int clickedY)
	{	
		System.out.println((awaitingDestination));
		
		if(awaitingDestination == false)
		{	 			
			selectedPosition[0][0] = clickedX;
			selectedPosition[0][1] = clickedY;
			selectedPiece=determineSelectedPiece(selectedPosition);
			if (selectedPiece!=null)
			{	
				System.out.println(("piece is not  null!"));
				if(selectedPiece.getColour() == getTurn() )  //check if a piece owned by the player who has the turn is being played
				{
					awaitingDestination = true;
					AdvGame.updateCastleOpt();
					CreateHighlightThread();
				}
			}
			else
			{
				System.out.println(("pice is null!"));
			}
		}
		else 		
		{
			awaitingDestination = false;
			finalPosition[0][0] = clickedX;
			finalPosition[0][1] = clickedY;
			manageGame();
		}
	}
	
	public void setTurn(String turn)
	{
		this.Turn = turn;	
	}
	
	public String getTurn()
	{
		return Turn;
	}
	
	public void initGame()
	{	
		int initY;
		//initialise values of white pieces 
		for (int i=1; i<9; i++)  //8 white pawns
		{
			spotState[7][i] = white_pawn;
			initY = i;
			piece[i] = new Pawn(this, "white", 7  , initY);  //piece[1 to 9] reserved for white pawns
		}
		spotState[8][5] = white_king;
		piece[13] = new King(this, "white", 8,5);  
		
		spotState[8][4] = white_queen;
		piece[12] = new Queen(this, "white", 8 , 4);
		spotState[8][1] = white_rook;
		piece[9] = new Rook(this, "white", 8 , 1);
		spotState[8][8] = white_rook;
		piece[16] = new Rook(this, "white", 8, 8);
		spotState[8][2] = white_knight;
		piece[10] = new Knight(this, "white", 8 , 2);
		spotState[8][7] = white_knight;
		piece[15] = new Knight(this, "white", 8 , 7);
		spotState[8][3] = white_bishop;
		piece[11] = new Bishop(this, "white", 8 , 3); //white bishop on coordinates (8,3)
		spotState[8][6] = white_bishop;
		piece[14] = new Bishop(this, "white", 8 , 6); //white bishop on coordinates (8,6)
	
		//initialise values of black pieces
		for (int i=1; i<9; i++)  //8 black pawns
		{
			spotState[2][i] = black_pawn;
			initY = i;
			piece[i+16] = new Pawn(this, "black", 2  , initY);  //piece[17 to 24] reserved for black pawns
		}
		spotState[1][5] = black_king;
		piece[29] = new King(this, "black", 1,5);  //piece[10] for white king
		spotState[1][4] = black_queen;
		piece[28] = new Queen(this, "black", 1 , 4);
		spotState[1][1] = black_rook;
		piece[25] = new Rook(this, "black", 1 , 1);
		spotState[1][8] = black_rook;
		piece[32] = new Rook(this, "black", 1 , 8);
		spotState[1][2] = black_knight;
		piece[26] = new Knight(this, "black", 1 , 2);
		spotState[1][7] = black_knight;
		piece[31] = new Knight(this, "black", 1 , 7);
		spotState[1][3] = black_bishop;
		piece[27] = new Bishop(this, "black", 1 , 3); //white bishop on coordinates (1,3)
		spotState[1][6] = black_bishop;
		piece[30] = new Bishop(this, "black", 1 , 6); //white bishop on coordinates (1,6)
		
		for (int x=1; x<9; x++)
		{
			for (int y=1; y<9; y++)
			{
				initialSpotState[x][y] = spotState[x][y];
			}
		}
	}
	
	public int checkSpotValue(int x, int y)
	{
		if ( ( (x<9)  && (x>0) ) && (y<9)  && (y>0)  )  //x and y are not out of bound 
		{
			return spotState[x][y];
		}
		else
			return -1;
	}
	
	public Piece determineSelectedPiece(int selectedPositionR[][] )
	{
		int i = 0;
		int[][] tempPosition = new int[1][2];
		for (i = 1; i<33; i++ )
		{
			if (piece[i]!=null)
			{
				tempPosition = piece[i].getCurrentPosition();				
				if ( (tempPosition[0][0] == selectedPositionR[0][0]) && (tempPosition[0][1] == selectedPositionR[0][1]) )
				{
					return piece[i];
				}
			}
		}  
		return null;
	}
	
	public void manageGame()
	{
		
		System.out.println("we got to manageGame!");
		
		if (verifyMove(finalPosition)==0)//move fully verified
		{	
			printBoard();
			System.out.println("This is a valid move");
			if(getTurn() == "white") //if it is white's turn
			{
				System.out.println("Turn from white to black!");
				setTurn("black");
				MaintainMovesHistory.addRow(selectedPiece.getValue(),(attackedPiece==null)?false:true,selectedPosition, finalPosition);
			}
			else  //if it is black's turn
			{
				
				setTurn("white");
				MaintainMovesHistory.editRow(selectedPiece.getValue(),(attackedPiece==null)?false:true,selectedPosition, finalPosition);
				System.out.println("Turn from black to white!");
			}
			if (AdvGame.Checkmate())
			{
				KillGameControlThread("checkmate");
				System.out.println(" CHECKMATE >>>>>>>>>>");
				
			}
			else if(AdvGame.Stalemate())
			{
				KillGameControlThread("stalemate");
				System.out.println("STALEMATE <<<<<<<<<<<<");
			}
		}
		else 
		{
			potSelectedPosition[0][0] = finalPosition[0][0];
			potSelectedPosition[0][1] = finalPosition[0][1];
			potentialSelectedPiece = determineSelectedPiece(potSelectedPosition);
			if(potentialSelectedPiece!=null)
			{
				
				if (potentialSelectedPiece.getColour()==getTurn())
				{					
					selectedPiece = potentialSelectedPiece;		
					CreateHighlightThread();
					selectedPosition = potSelectedPosition;
					awaitingDestination = true;
				}
			}				
		}
	}
	
	public int verifyMove(int finalPositionR[][]) //To verify the validity of a Generic move
	{
	   
		GUI.redrawBoard();
		
		for(int x = 0; x<validMoves.length; x++ )
		{
			if(validMoves[x][0] == 0)  //end of actual moves
			{
				break;
			}
			if( (validMoves[x][0] == finalPositionR[0][0]) && (validMoves[x][1] == finalPositionR[0][1])  )
			{
				// first check if there is a piece to be removed
				attackedPiece=determineSelectedPiece(finalPosition);
				if(attackedPiece != null)  //there is a piece to be removed
				{
					attackedPiece.setCurrentPosition(takenPiece);
				}
				else if( (AdvGame.enPassant(selectedPosition, finalPosition)) ) //if en passant, the attacked piece is not where we will be moving
				{
					AdvGame.processEnPassant(finalPosition);
				}
				else if(AdvGame.isCastle(finalPosition))
				{
					AdvGame.processCastle(finalPosition);
				}
	
		        selectedPiece.setCurrentPosition(finalPositionR); //update the move's real position
		        
		        updateSpotState(selectedPosition,empty_spot); //set the square from which the piece was moved to zero.
		        updateSpotState(finalPosition,selectedPiece.getValue()); //set the value of the square we just moved to.
		        
			    updateView(selectedPosition,empty_spot);  // update view to user
			    updateView(finalPosition,selectedPiece.getValue());	
			    
				if(AdvGame.isPromotion(finalPosition))
				{
					AdvGame.processPromotion(finalPosition);
				}
			    
			    AdvGame.removePotentialEnPassant();  //remove Enpassant flags from last move whether they were used or not
			    if( (selectedPiece.getValue()== 2) || (selectedPiece.getValue()== 8) ) //if a pawn was just moved
			    {
			    	AdvGame.checkPotentialEnPassant();  //look for new EnPassant opportunites
			    }
			    updateAllSpotStates();  //Add position to history
			    return 0;
			}
		}
		return -1;  //-1 indicates invalid move
	}
	
	public void updateSpotState(int PositionR[][],int spotValueR)
	{
		spotState[PositionR[0][0]][PositionR[0][1]]= spotValueR;
		// System.out.println("New spot State "+PositionR[0][0]+" and "+PositionR[0][1]+" is "+spotState[PositionR[0][0]][PositionR[0][1]]);
	}
	
	public static void updateView(int PositionR[][], int SpotValueR) //to update the view of a generic move
	{
		UI.updateButton(PositionR, SpotValueR);
	}
	
	public void printBoard()
	{
		for(int x=1; x<9;x++)
		{
			for(int y=1; y<9;y++)
			{
				System.out.print(spotState[x][y]+ "  ");
			}
			System.out.println(" ");	
		}
	}
	
	public boolean isCurrentlySafe(String myColor, int positionR[][])
	{
		
		int subAttackedSpots[][] = new int [36][2];
		
		int tempPosition[][] = new int[1][2];
		
		int x;
		
		switch (myColor)
		{
		case "white":
			for( x=17;x<33;x++)
			{
				tempPosition = piece[x].getCurrentPosition();
				if(tempPosition[0][0] != 0)  //Piece is still alive
				{
					subAttackedSpots = piece[x].getAttackedSpots();
					for(int h=0; h < subAttackedSpots.length;h++)
					{
						if ( (subAttackedSpots[h][0] == positionR[0][0]) && (subAttackedSpots[h][1] == positionR[0][1]) )
						{
							return false; //The passed position is being attacked, return false to indicate unsafe 
						}
					}	
				}	
			}
			break;
			
		case "black":
			for( x=1;x<17;x++)
			{
				tempPosition = piece[x].getCurrentPosition();  
				if(tempPosition[0][0] != 0)    //check if piece is still alive
				{
					subAttackedSpots = piece[x].getAttackedSpots();
					for(int h=0;h<subAttackedSpots.length;h++)
					{
						if ( (subAttackedSpots[h][0] == positionR[0][0]) && (subAttackedSpots[h][1] == positionR[0][1]) )
						{
							return false; //The passed position is being attacked, return false to indicate unsafe 
						}
					}
				}	
			}
			break;
		}

		return true;  //Safe position
	}
	
	public void printCurrentPositions()
	{
		for(int c=1;c<33;c++)
		{
			int temp[][] = new int[1][2];
			temp=piece[c].getCurrentPosition();
			System.out.println(" value " +piece[c].getValue() +" position " + temp[0][0]+ " and " + temp[0][1]);
		}
	}
	
	public void CreateHighlightThread()
	{
		validMoves = selectedPiece.getValidMoves();
		highlight = new BoardHighlight("HighlightTHread", GUI, validMoves);
		highlight.start();
	}
	public void CreateGameControlThread(int timeControlIndex)
	{
		NewGameRequest = new GameControl ( GUI,this,timeControlIndex);
	}
	public void KillGameControlThread(String endCause)
	{
		NewGameRequest.endGame(endCause);
	}
	public void updateAllSpotStates()
	{
		int [][] tempSpotState = new int[9][9];
		for (int i=1; i<9; i++ )
		{
			for (int j=1; j<9; j++ )
			{
				tempSpotState[i][j] = spotState[i][j];
			}
		}
		allSpotStates.add(tempSpotState);		
		movesCounter++;
	}
	public static void createMovesHistoryThread(int row, int col)
	{
		displayPosition = new MovesHistoryDisplay("SpecifiedPositionDisplay", row, col);
		displayPosition.start();
	}
	public static int[][] getInitialSpotState() { return initialSpotState; }
}
