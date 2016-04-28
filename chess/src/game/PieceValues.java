package game;

public interface PieceValues
{
	//value of an empty spot
		public static final int empty_spot = 0;
		
	//Values of white pieces
		public static final int white_king = 1;
		public static final int white_pawn = 2;
		public static final int white_knight = 3;
		public static final int white_bishop = 4;
		public static final int white_rook = 5;
		public static final int white_queen = 6;
	//values of black pieces
		public static final int black_king = 7;
		public static final int black_pawn = 8;
		public static final int black_knight = 9;
		public static final int black_bishop = 10;
		public static final int black_rook = 11;
		public static final int black_queen = 12;
		
		public static final int takenPiece[][] = new int [][]{new int[]{0,0}}; //to hold the x and y position of a taken piece 
	    //(This will be zero and zero  which means not on board)
}
