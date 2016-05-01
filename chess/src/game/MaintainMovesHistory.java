package game;

import javax.swing.JScrollBar;
import javax.swing.table.DefaultTableModel;

import gui.UI;

public class MaintainMovesHistory
{
	RealBoard GameInstance;
	static String whiteNotation="";
	static String blackNotation="";
	static int rowsCounter=0;
	static int movesCounter=0;
	//special cases flags
	static boolean enPassant = false;
	static boolean shortCastle = false;
	static boolean longCastle = false;
	
	static DefaultTableModel model;
	
	private static boolean displayUpToDate = true;
	
	static JScrollBar bar = UI.scrollPane.getVerticalScrollBar();
	
	public enum Operation
	{
		add,
		edit
	}
	public static void addRow(int attackigPieceValue, boolean capture, int[][] initialPosition,int[][] finalPosition)
	{
		setNotation(attackigPieceValue, capture, initialPosition, finalPosition, Operation.add);
	
		model = (DefaultTableModel)UI.movesTable.getModel();				
		model.addRow(new Object[] { whiteNotation, blackNotation});
		rowsCounter++;
		movesCounter++;
		bar.setValue(bar.getMaximum());
	}

	public static void editRow(int attackigPieceValue, boolean capture, int[][] initialPosition, int[][] finalPosition)
	{
		setNotation(attackigPieceValue, capture, initialPosition, finalPosition, Operation.edit);
		
		DefaultTableModel model;
		model = (DefaultTableModel)UI.movesTable.getModel();		
		model.removeRow(rowsCounter-1);
		model.addRow(new Object[] { whiteNotation, blackNotation});	
		resetNotations();
		movesCounter++;
		bar.setValue(bar.getMaximum());
	}
	public static void resetNotations()
	{
		whiteNotation=" ";
		blackNotation=" ";
	}
	public static void setNotation(int attackigPieceValue, boolean capture,int[][] initialPosition, int[][] finalPosition,Operation opr)
	{
		String notation;
		
		String subNot1="";  //divide notation into three parts attacking piece, whether it captured a piece or not and destination
		String subNot2="";
		String subNot3="";
		String subNot4="";
		
		if (shortCastle)
		{
			subNot1 = "O-O";
		}
		else if(longCastle)
		{
			subNot1 = "O-O-O";
		}
		else
		{
			switch (attackigPieceValue)
			{
				case RealBoard.white_king: case RealBoard.black_king:
					subNot1 = "K";
					break;
				case RealBoard.white_pawn: case RealBoard.black_pawn:
					if(capture || enPassant)
					{
						subNot1 = Character.toString((char)(96+ initialPosition[0][1]) );
					}
					else
					{
						subNot1 = ""; 
					}		
					break;
				case RealBoard.white_bishop: case RealBoard.black_bishop:
					subNot1 = "B";
					break;
				case RealBoard.white_knight: case RealBoard.black_knight:
					subNot1 = "N";
					break;
				case RealBoard.white_rook: case RealBoard.black_rook:
					subNot1 = "R";
					break;
				case RealBoard.white_queen: case RealBoard.black_queen:
					subNot1 = "Q";
					break;
				default:
					subNot1 = "";
					break;
			}
			subNot2 = (capture|| enPassant)? "x":""; //if it is a capture put x, if not then donot put anything
			subNot3 = Character.toString((char)(96+ finalPosition[0][1]) );  ///set the coloumn of destination in notation [a,h]
			subNot4 = Integer.toString(9-finalPosition[0][0]);  //set row in destination
			
		}
		notation = subNot1 + subNot2 + subNot3 + subNot4;
		switch (opr)
		{
		
		case add:
				whiteNotation = notation;
				break;
		case edit:
				blackNotation = notation;
				break;
					
		}
		resetFlags();
	}
	public static void resetFlags()
	{
		enPassant = false;
		shortCastle = false;
		longCastle = false;
	}
	public static void setEnpassant(boolean enPassant)
	{
		MaintainMovesHistory.enPassant = enPassant; 
	}
	public static void setShortCastle(boolean shortCastle)
	{
		MaintainMovesHistory.shortCastle = shortCastle; 
	}
	public static void setLongCastle(boolean longCastle)
	{
		MaintainMovesHistory.longCastle = longCastle; 
	}
	public static void resetMovesHistory()
	{
		model = (DefaultTableModel)UI.movesTable.getModel();
		model.setRowCount(0);
		resetFlags();
		resetNotations();
		rowsCounter=0;
		movesCounter=0;
		RealBoard.allSpotStates.clear();
		setDisplayUptoDate(true);
	}
	public static void setDisplayUptoDate(boolean val)
	{
		displayUpToDate = val;
	}
	public static boolean getDisplayUptoDate()
	{
		return displayUpToDate;
	}
}
