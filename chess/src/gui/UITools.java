package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public interface UITools
{
	JPanel mainPanel = new JPanel(new BorderLayout()); //create a panel which will hold everything
	JPanel centerPanel = new JPanel(new GridLayout(8,8)); //Holds the board and the pieces only 
	JPanel rightPanel = new JPanel(); //to hold everything on the right hand side
	JPanel rightTopPanel =  new JPanel(new GridLayout(2,2));
	JPanel rightBottomPanel =  new JPanel(new FlowLayout());
	
	JButton spots[][] = new JButton[9][9]; //create 8 x 8 buttons for 64 board squares (start from button 1 and thus 9 x 9)
	
	
	ImageIcon empty = new ImageIcon(); //To be used when a piece leaves a spot
	
	//extract all white pieces
	ImageIcon whitepawn = new ImageIcon("source/whitepawn.png");
	ImageIcon whiteknight = new ImageIcon("source/whiteknight.png");
	ImageIcon whitebishop = new ImageIcon("source/whitebishop.png");
	ImageIcon whiterook = new ImageIcon("source/whiterook.png");
	ImageIcon whitequeen= new ImageIcon("source/whitequeen.png");
	ImageIcon whiteking = new ImageIcon("source/whiteking.png");
	
	//extract all black pieces
	ImageIcon blackpawn = new ImageIcon("source/blackpawn.png");
	ImageIcon blackknight = new ImageIcon("source/blackknight.png");
	ImageIcon blackbishop = new ImageIcon("source/blackbishop.png");
	ImageIcon blackrook = new ImageIcon("source/blackrook.png");
	ImageIcon blackqueen= new ImageIcon("source/blackqueen.png");
	ImageIcon blackking = new ImageIcon("source/blackking.png");
	
	
	JLabel clocks[] = new JLabel[2]; 
	JLabel colors[] = new JLabel[2];
	JButton NewGame = new JButton("New Game");
	JButton flipBoard = new JButton("Flip");
	JLabel TimeControl = new JLabel("Time Control");
	JComboBox<String> TimeOpt = new JComboBox<String>();
	String TimeControlOptions[] = {"10 Mins","20 Mins","30 Mins","60 Mins" };
	
	Object[] column = { "WHITE", "BLACK"};
	static JTable movesTable = new JTable(new DefaultTableModel(null,column));
	static JScrollPane scrollPane = new JScrollPane(movesTable);
		
}
