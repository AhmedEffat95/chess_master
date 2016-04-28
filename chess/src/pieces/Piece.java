package pieces;

public interface Piece 
{
	public int[][] getCurrentPosition(); //returns a 2D array of two elements corresponding to x and y position of the piece
	
	public void setCurrentPosition(int updatedPosition[][]);  //holds the position of a piece
	
	public int[][] getPrevPosition();
	
	public String getColour();
	
	public int getValue();
	
	public int[][] getAttackedSpots();  //returns a 2D array of spots that are currently being attacked by a piece
	
	public boolean canMove(); //returns a boolean to indicate whether a piece has at least one valid move at a certain time

	public int[][] getValidMoves();  //returns the valid moves that a user can play with a particular piece at a particular time
	 
	
}
