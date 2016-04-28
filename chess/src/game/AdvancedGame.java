package game;

import javax.swing.JOptionPane;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

/*
 *  This class involves methods that are used to perform advance-High level operations such as 
 *  checking for a Checkmate or a Stalemate and supporting advanced moves such as En passant, 
 *  castling and promotions.
 */
public class    AdvancedGame 
{
	RealBoard Game;  //To hold the game's Object
	
	public AdvancedGame(RealBoard GameR) //constructor
	{
		Game = GameR;
	}
	//The check for stalemate will happen at the end of each move, if the last move was played by white, the checks will apply to black
	public boolean Stalemate()  //Can assume there is no checkmate on
	{
		String Color = Game.getTurn();
		
		int tempPosition[][] = new int[1][2];

		switch(Color)     
		{
		case "white":
					for( int x=1; x<17; x++ )   //Make sure that we have at least one valid moves to make
					{
						tempPosition = Game.piece[x].getCurrentPosition();
						if( (tempPosition[0][0] != 0 )  && (Game.piece[x].canMove()) ) //if piece is still alive and it has one valid move at least
						{
							return false;
						}
					}
					break;
		case "black":
					for( int x=17; x<32; x++) 
					{
						tempPosition = Game.piece[x].getCurrentPosition();
						if( (tempPosition[0][0] != 0 )  && (Game.piece[x].canMove()) ) //if piece is still alive and it has one valid move at least
						{
							return false;
						}
					}
					break;
				
		}
		return true;
		
	}
	//The check for checkmate will hapen at the end of each move, if the last move was played by white, the checks will apply to black
	public boolean Checkmate()
	{
		String Color = Game.getTurn();
		
		
		int tempPosition[][] = new int[1][2];
		
		switch(Color)
		{
		case "white":
					tempPosition = Game.piece[13].getCurrentPosition();  //Get white king position
					if(Game.isCurrentlySafe(Color,tempPosition)) //If there is no check to start with, return false, no checkmate
					{
						return false;
					}
					
					break;
		case "black":
					tempPosition = Game.piece[29].getCurrentPosition();  //Get black king position
					if(Game.isCurrentlySafe(Color,tempPosition)) //If there is no check to start with, return false, no checkmate
					{
						return false;
					}
					
					break;
		}

		//If we got to this stage, this means The king is currently under attack; if there is no move that we can play at this stage
		//this will mean that we could not find any moves that puts king to safety which will lead to immediate checkmate
		//This can be achieved by checking for a stalemate at this stage because (Stalemate + Check is a checkmate)
		if(!Stalemate())
		{
			return false;  //No checkmate, we still have valid moves to defend the king
		}
		else
		{
			return true;  //No move will be able to defend the king, this is a checkmate
		}
		
	}
	/*
	 * This method will be called only when a pawn has just been moved for the first time; it will
	 * determine whether it can be taken in the very next move be an opposing pawn through enPassant
	 */
	public void checkPotentialEnPassant()  
	{
		int tempNewPosition[][] = new int[1][2];
		int tempOldPosition[][] = new int[1][2];
		int tempAttackingPawnPos[][] = new int[1][2];
		Piece tempAttackingPawn;
		String color;
		
		tempNewPosition = Game.selectedPiece.getCurrentPosition();
		tempOldPosition = Game.selectedPiece.getPrevPosition();
		color = Game.selectedPiece.getColour();
		
		switch (color)
		{
		case "white":
					if( (tempNewPosition[0][0]==5) && (tempOldPosition[0][0]==7))
					{
						if( (Game.checkSpotValue(tempNewPosition[0][0],tempNewPosition[0][1]+1)== RealBoard.black_pawn) )
						{ //check if there is an opposing pawn that can be taken by enPassant
							tempAttackingPawnPos[0][0] = tempNewPosition[0][0];
							tempAttackingPawnPos[0][1] = tempNewPosition[0][1]+1;
							tempAttackingPawn = Game.determineSelectedPiece(tempAttackingPawnPos);
							((Pawn) tempAttackingPawn).setEnPassant(true,-1);		
						}
						if( (Game.checkSpotValue(tempNewPosition[0][0],tempNewPosition[0][1]-1)== RealBoard.black_pawn) )
						{ 
							tempAttackingPawnPos[0][0] = tempNewPosition[0][0];
							tempAttackingPawnPos[0][1] = tempNewPosition[0][1]-1;
							tempAttackingPawn = Game.determineSelectedPiece(tempAttackingPawnPos);
							((Pawn) tempAttackingPawn).setEnPassant(true,1);	
						}
					
					 }
					break;
		case "black":
					if( (tempNewPosition[0][0]==4) && (tempOldPosition[0][0]==2))
					{
						if( (Game.checkSpotValue(tempNewPosition[0][0],tempNewPosition[0][1]+1)== RealBoard.white_pawn) )
						{ //check if there is an opposing pawn that can be taken by enPassant
							tempAttackingPawnPos[0][0] = tempNewPosition[0][0];
							tempAttackingPawnPos[0][1] = tempNewPosition[0][1]+1;
							tempAttackingPawn = Game.determineSelectedPiece(tempAttackingPawnPos);
							((Pawn) tempAttackingPawn).setEnPassant(true,-1);		
						}
						if( (Game.checkSpotValue(tempNewPosition[0][0],tempNewPosition[0][1]-1)== RealBoard.white_pawn) )
						{ 
							tempAttackingPawnPos[0][0] = tempNewPosition[0][0];
							tempAttackingPawnPos[0][1] = tempNewPosition[0][1]-1;
							tempAttackingPawn = Game.determineSelectedPiece(tempAttackingPawnPos);
							((Pawn) tempAttackingPawn).setEnPassant(true,1);			
						}
					 }
					break;	
		}
	
	}
	public void removePotentialEnPassant()  //One move after we flag potential en Passants, the flagged opportunities will be no longer available 	
	{
		String color = Game.getTurn();
		
		switch(color)
		{
			case "white":
					for(int x = 1; x<9; x++)
					{
						if(Game.piece[x].getValue()==RealBoard.white_pawn)
						{
							((Pawn) Game.piece[x]).setEnPassant(false,0); //set en passant flags for all white pawns to false
						}
					}
					break;
			case "black":
					for(int x = 1; x<9; x++)
					{
						if(Game.piece[x+16].getValue()==RealBoard.black_pawn)
						{
							((Pawn) Game.piece[x+16]).setEnPassant(false,0); //set en passant flags for all black pawns to false
						}
						
					}
					break;
		}
		
	}
	public boolean enPassant(int initial[][], int dest[][]) //can assume the piece is a pawn, detects if the move is enPassant
	{
		if ( (Game.selectedPiece.getValue()!= RealBoard.white_pawn) &&  (Game.selectedPiece.getValue()!=RealBoard.black_pawn) )
		{ //The move was not done by a pawn, no En passants available
			return false;
		}
		int colDsiplacement = Math.abs(dest[0][1] - initial[0][1]);
		if (  (colDsiplacement==1) && (Game.checkSpotValue(dest[0][0],dest[0][1])==0 ) )
		{
			return true;
		}
		return false;
	}
	public void processEnPassant(int destR[][])
	{
		
		int dest[][] = new int [1][2];
		for(int h =0; h<2;h++)
		{
			dest[0][h] = destR[0][h];
		}
		String color = Game.getTurn();
		Piece capturedPawn;
		switch(color)
		{
			case "white":
					dest[0][0] += 1;  //The column value stays the same, the row number changes by 1 to select the captured pawn
					break;
			case "black":
					dest[0][0] -= 1;
					break;
		}		
		
		capturedPawn = Game.determineSelectedPiece(dest);
		
		capturedPawn.setCurrentPosition(RealBoard.takenPiece);
		Game.updateSpotState(dest,RealBoard.empty_spot);
		RealBoard.updateView(dest, RealBoard.empty_spot);
	}
	
	public void updateCastleOpt()
	{
		int kingPosition[][] = new int[1][2];
		int kingPositionOffset[][] = new int[1][2];
		String color = Game.getTurn();
		Piece currentKing = null;
		Piece currentRook = null;
		int rookValue = 0;
		switch(color)
		{
			case "white":
					currentKing = Game.piece[13];
					rookValue = RealBoard.white_rook;
					((Rook)Game.piece[9]).moved();     //update white rooks to determine whether they ever moved from their initial position
					((Rook)Game.piece[16]).moved();
					break;
			case "black":
					currentKing = Game.piece[29];
					rookValue = RealBoard.black_rook;
					((Rook)Game.piece[25]).moved();     //update black rooks to determine whether they ever moved from their initial position
					((Rook)Game.piece[32]).moved();
					break;
		}
		
		kingPosition = currentKing.getCurrentPosition();
		for(int h = 0; h<2; h++)
		{
			kingPositionOffset[0][h] = kingPosition[0][h];
		}
		if( ((King) currentKing).moved() || !(Game.isCurrentlySafe(color, kingPosition) ) )  
		{  //if  king has moved, no castling will be valid
			((King) currentKing).setLongCastle(false);
			((King) currentKing).setShortCastle(false);
		}
		else
		{  //At This stage we knowKing is not under check and has not moved yet in the game
			//check short castling conditions for white king
			kingPositionOffset[0][1] += 1;   //check the very first square to the right of  king
			if( (Game.isCurrentlySafe(color, kingPositionOffset)) && (Game.checkSpotValue(kingPositionOffset[0][0], kingPositionOffset[0][1])==0 ) )
			{  //This means first square is safe
				kingPositionOffset[0][1] += 1;   //check the second square to the right of  king
				if( (Game.isCurrentlySafe(color, kingPositionOffset)) && (Game.checkSpotValue(kingPositionOffset[0][0], kingPositionOffset[0][1])==0 ) )
				{ 
					kingPositionOffset[0][1] += 1;
					if( (Game.checkSpotValue(kingPositionOffset[0][0], kingPositionOffset[0][1])==rookValue))
					{
						currentRook = Game.determineSelectedPiece(kingPositionOffset);
						if( !((Rook) currentRook).moved())
						{
							//if we got to this point then a short castle is permitted
							((King) currentKing).setShortCastle(true);
						}
						else
						{
							((King) currentKing).setShortCastle(false);
						}
					}
					else
					{
						((King) currentKing).setShortCastle(false);
					}
				
				}
				else
				{
					((King) currentKing).setShortCastle(false);
				}
			}
			else 
			{
				((King) currentKing).setShortCastle(false);
			}
			//Check for long white king castle
			for(int h = 0; h<2; h++)
			{
				kingPositionOffset[0][h] = kingPosition[0][h];  //set the Offset to start at king position again
				
			}
			
			kingPositionOffset[0][1] -= 1;   //check the very first square to the right of  king
			
			if( (Game.isCurrentlySafe(color, kingPositionOffset)) && (Game.checkSpotValue(kingPositionOffset[0][0], kingPositionOffset[0][1])==0 ) )
			{  //This means first square is safe
				
				kingPositionOffset[0][1] -= 1;   //check the second square to the right of  king
				
				if( (Game.isCurrentlySafe(color, kingPositionOffset)) && (Game.checkSpotValue(kingPositionOffset[0][0], kingPositionOffset[0][1])==0 ) )
				{  
					kingPositionOffset[0][1] -= 1;
					
					if(Game.checkSpotValue(kingPositionOffset[0][0], kingPositionOffset[0][1])==0)
					{
						kingPositionOffset[0][1] -= 1;
						if((Game.checkSpotValue(kingPositionOffset[0][0], kingPositionOffset[0][1])==rookValue))
						{
							currentRook = Game.determineSelectedPiece(kingPositionOffset);
							if( !((Rook) currentRook).moved())
							{
								//if we got to this point then a  long castle is permitted
								((King) currentKing).setLongCastle(true);
							}
							else
							{
								((King) currentKing).setLongCastle(false);
							}

						}
						else
						{
							((King) currentKing).setLongCastle(false);
						}

					}
					else
					{
						((King) currentKing).setLongCastle(false);
					}
				}
				else
				{
					((King) currentKing).setLongCastle(false);
				}
			}
			else 
			{
				((King) currentKing).setLongCastle(false);
			}
		}
	}
	public boolean isCastle(int kingDest[][])
	{
		if ( (Game.selectedPiece.getValue()!= RealBoard.white_king)  &&  (Game.selectedPiece.getValue()!= RealBoard.black_king)  )
		{
			return false;
		}
		int kingInitPos[][] = new int [1][2];
		kingInitPos = Game.selectedPiece.getCurrentPosition();
		int kingDisplacement = Math.abs(kingInitPos[0][1]-kingDest[0][1]);
		if(kingDisplacement==2)
		{
			return true;
		}
		return false;
	}
	public void processCastle(int destR[][])  //does the extra part of moving the rook in a castle
	{  //based on the king's final position we can decide which rook is to be involved in the castling
		int rookFinalPos[][] = new int[1][2];
		String color;
		color = Game.getTurn();
		switch(color)
		{
		case "white":  //decide which white rook to be moved
				if(destR[0][1]== 7)    //white shortCastle
				{
					rookFinalPos[0][0] = 8;
					rookFinalPos[0][1] = 6;
					Game.updateSpotState(Game.piece[16].getCurrentPosition(), RealBoard.empty_spot);
					RealBoard.updateView(Game.piece[16].getCurrentPosition(), RealBoard.empty_spot);
					Game.piece[16].setCurrentPosition(rookFinalPos);
					RealBoard.updateView(rookFinalPos, RealBoard.white_rook);
					Game.updateSpotState(rookFinalPos,RealBoard.white_rook);
					MaintainMovesHistory.setShortCastle(true);
				}
				else //destR[0][1] == 3  white longCastle
				{
					rookFinalPos[0][0] = 8;
					rookFinalPos[0][1] = 4;
					Game.updateSpotState(Game.piece[9].getCurrentPosition(), RealBoard.empty_spot);
					RealBoard.updateView(Game.piece[9].getCurrentPosition(), RealBoard.empty_spot);
					Game.piece[9].setCurrentPosition(rookFinalPos);
					RealBoard.updateView(rookFinalPos, RealBoard.white_rook);
					Game.updateSpotState(rookFinalPos,RealBoard.white_rook);
					MaintainMovesHistory.setLongCastle(true);
				}
				break;
		case "black":
				if(destR[0][1]== 7)   //black shortCastle
				{
					rookFinalPos[0][0] = 1;
					rookFinalPos[0][1] = 6;
					Game.updateSpotState(Game.piece[32].getCurrentPosition(), RealBoard.empty_spot);
					RealBoard.updateView(Game.piece[32].getCurrentPosition(), RealBoard.empty_spot);
					Game.piece[32].setCurrentPosition(rookFinalPos);
					RealBoard.updateView(rookFinalPos, RealBoard.black_rook);
					Game.updateSpotState(rookFinalPos,RealBoard.black_rook);
					MaintainMovesHistory.setShortCastle(true);
				}
				else //destR[0][1] == 3  black longCastle
				{
					rookFinalPos[0][0] = 1;
					rookFinalPos[0][1] = 4;
					Game.updateSpotState(Game.piece[25].getCurrentPosition(), RealBoard.empty_spot);
					RealBoard.updateView(Game.piece[25].getCurrentPosition(), RealBoard.empty_spot);
					Game.piece[25].setCurrentPosition(rookFinalPos);
					RealBoard.updateView(rookFinalPos, RealBoard.black_rook);
					Game.updateSpotState(rookFinalPos,RealBoard.black_rook);
					MaintainMovesHistory.setLongCastle(true);
				}
				break;
			
		}
	}
	public boolean isPromotion(int destR[][])
	{
		if ( (Game.selectedPiece.getValue()!= RealBoard.white_pawn) &&  (Game.selectedPiece.getValue()!=RealBoard.black_pawn) )
		{
			return false;
		}
		if( (destR[0][0]==1) || (destR[0][0]==8) )  //A pawn on the first or the last rank = promotion
		{
			return true;
		}

		return false;
		
	}
	public void processPromotion(int destR[][])
	{
		int tempPos[][] = new int [1][2];
		String tmepColor;
		Object[] options = { "Queen", "Rook", "Bishop", "Knight"};
		int input = JOptionPane.showOptionDialog(null, "Promote To?", "Pawn Promotion", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);		
		for(int x=1; x<33;x++)
		{
			tempPos = Game.piece[x].getCurrentPosition();
			tmepColor = Game.piece[x].getColour();
			if ( (tempPos[0][0]==destR[0][0]) &&  (tempPos[0][1]==destR[0][1]) )
			{	
				Game.piece[x] = null;
				switch(input)
				{
				case 0:
						Game.piece[x] = new Queen(Game, tmepColor, tempPos[0][0], tempPos[0][1]);
						RealBoard.updateView(tempPos,( tmepColor=="white") ? RealBoard.white_queen : RealBoard.black_queen );
						Game.updateSpotState(tempPos, ( tmepColor=="white") ? RealBoard.white_queen : RealBoard.black_queen);
						break;
				case 1:
						Game.piece[x] = new Rook(Game, tmepColor, tempPos[0][0], tempPos[0][1]);
						RealBoard.updateView(tempPos,( tmepColor=="white") ? RealBoard.white_rook : RealBoard.black_rook );
						Game.updateSpotState(tempPos, ( tmepColor=="white") ? RealBoard.white_rook : RealBoard.black_rook);
						break;
				case 2:
						Game.piece[x] = new Bishop(Game, tmepColor, tempPos[0][0], tempPos[0][1]);
						RealBoard.updateView(tempPos,( tmepColor=="white") ? RealBoard.white_bishop : RealBoard.black_bishop );
						Game.updateSpotState(tempPos, ( tmepColor=="white") ? RealBoard.white_bishop : RealBoard.black_bishop);						
						break;
				case 3:
						Game.piece[x] = new Knight(Game, tmepColor, tempPos[0][0], tempPos[0][1]);
						RealBoard.updateView(tempPos,( tmepColor=="white") ? RealBoard.white_knight : RealBoard.black_knight );
						Game.updateSpotState(tempPos, ( tmepColor=="white") ? RealBoard.white_knight : RealBoard.black_knight);	
						break;
				default:   //default promotion is queen
						Game.piece[x] = new Queen(Game, tmepColor, tempPos[0][0], tempPos[0][1]);
						RealBoard.updateView(tempPos,( tmepColor=="white") ? RealBoard.white_queen : RealBoard.black_queen );
						Game.updateSpotState(tempPos, ( tmepColor=="white") ? RealBoard.white_queen : RealBoard.black_queen);
						break;
				
				}

				break;
			}
		}
	}

}
