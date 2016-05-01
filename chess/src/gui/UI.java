package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;

import game.FlipView;
import game.MaintainMovesHistory;
import game.RealBoard;

public final class UI extends JFrame implements ActionListener, UITools
{
	private static final long serialVersionUID = 1L;
	
	RealBoard GameInstance;
	
	int clickedX, clickedY;  //To hold the values of x and y coordinates of the last clicked values
	boolean GameOn = false;
	ListSelectionModel tableSelectionModel = movesTable.getSelectionModel();

	
	public static void main(String args[])
	 {		 
		 new UI(); 
	 }
	private UI() // constructor
	{
		super("Chess Master");
		setSize(1200,900);
		setResizable(false);

		for(int i=1; i<9; i++)
		{
			for(int j=1; j<9; j++)
			{
				spots[i][j] = new JButton();
				spots[i][j].addActionListener(this);
				spots[i][j].setFocusPainted(false);	
				centerPanel.add(spots[i][j]);
				
			}
		}
		drawBoard();
		colors[0] = new JLabel("WHITE");
		colors[1] = new JLabel("BLACK");
		for (int i=0; i<2; i++)
		{
			
			clocks[i] = new JLabel();
			rightTopPanel.add(colors[i]);
			rightTopPanel.add(clocks[i]);	
			clocks[i].setFont(new Font("SansSerif",Font.BOLD,24));
			colors[i].setFont(new Font("SansSerif",Font.BOLD,24));
			
			clocks[i].setPreferredSize(new Dimension(150,70));
     	}
		
		NewGame.addActionListener(this);
		NewGame.setPreferredSize(new Dimension(300,70));
		NewGame.setFont(new Font(null,Font.ITALIC+Font.BOLD,22));
		NewGame.setFocusable(false);
		NewGame.setBackground(Color.PINK);
		TimeControl.setFont (new Font(null,Font.BOLD,18));
		TimeControl.setPreferredSize(new Dimension(150,70));
		TimeOpt.setPreferredSize(new Dimension(100,30));
		flipBoard.addActionListener(this);
		flipBoard.setPreferredSize(new Dimension(300,70));
		flipBoard.setFocusable(false);
		flipBoard.setBackground(Color.PINK);
		flipBoard.setFont(new Font(null,Font.ITALIC+Font.BOLD,22));
		for(int x=0; x<TimeControlOptions.length; x++)
		{
			TimeOpt.addItem(TimeControlOptions[x]);
		}
		
		rightBottomPanel.add(NewGame);
		rightBottomPanel.add(TimeControl);
		rightBottomPanel.add(TimeOpt);
		rightBottomPanel.add(scrollPane);
		rightBottomPanel.add(flipBoard);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		rightPanel.add(rightTopPanel);
		rightPanel.add(rightBottomPanel);
		mainPanel.add(centerPanel,BorderLayout.CENTER);
		mainPanel.add(rightPanel, BorderLayout.AFTER_LINE_ENDS);
		add(mainPanel);
		rightPanel.setPreferredSize(new Dimension(300,300));
		rightTopPanel.setBackground(new Color(213,196,161));
		rightBottomPanel.setPreferredSize(new Dimension(300,900));
		scrollPane.setPreferredSize(new Dimension(200,200));
		
		movesTable.setCellSelectionEnabled(true);
		tableSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		movesTable.addMouseListener(new MouseListener () 
		{

			@Override
			public void mouseClicked(MouseEvent arg0) 
			{	
				int row = movesTable.getSelectedRow();
				int col = movesTable.getSelectedColumn();
				RealBoard.createMovesHistoryThread(row, col);
				System.out.println(" MOUSE CLICKED ON TABLE ON "+ row +" and "+ col);
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) { }
			
			@Override
			public void mouseExited(MouseEvent arg0) { }

			@Override
			public void mousePressed(MouseEvent arg0) { }

			@Override
			public void mouseReleased(MouseEvent arg0) { }
		} );
		
		

		setLocationRelativeTo(null);
		setVisible(true);
		
		
	}
	public final void drawBoard()
	{
		boolean lightSquareFlag=true; //starting with white square
		
		for(int i=1; i<9; i++)
		{
			for(int j=1; j<9; j++)
			{
				if(lightSquareFlag==true)
				{
					spots[i][j].setBackground(new Color(222,184,135));					
					lightSquareFlag=false;
				}
				else
				{
					spots[i][j].setBackground(new Color(139,69,19)); 
					lightSquareFlag=true;
				}
				
				
			}
			lightSquareFlag = !lightSquareFlag; //reverse starting colour for the next row
		}
	}
	
	public final void redrawBoard()
	{
		boolean lightSquareFlag=true; //starting with white square
		
		for(int i=1; i<9; i++)
		{
			for(int j=1; j<9; j++)
			{
				if(lightSquareFlag==true)
				{
					if(spots[i][j].getBackground()!=new Color(222,184,135))
					{
						spots[i][j].setBackground(new Color(222,184,135));
					}
										
					lightSquareFlag=false;
				}
				else
				{
					if(spots[i][j].getBackground()!=new Color(139,69,19))
					{
						spots[i][j].setBackground(new Color(139,69,19));
					}				 
					lightSquareFlag=true;
				}
				
				
			}
			lightSquareFlag = !lightSquareFlag; //reverse starting colour for the next row
		}
	}
	public final void setInitialBoard() //function to place pieces on board at start
	{
		
		//place white icons on board
		for (int i=1; i<9; i++)  //8 white pawns
		{
			spots[7][i].setIcon(whitepawn);
		}
		spots[8][5].setIcon(whiteking); //  white king
		spots[8][4].setIcon(whitequeen); // white queen
		spots[8][1].setIcon(whiterook); // 2 white rooks
		spots[8][8].setIcon(whiterook);
		spots[8][2].setIcon(whiteknight); // 2 white knights
		spots[8][7].setIcon(whiteknight);
		spots[8][3].setIcon(whitebishop); // 2 white bishops
		spots[8][6].setIcon(whitebishop);

		
		//place white icons on board
		for (int i=1; i<9; i++)  //8 white pawns
		{
			spots[2][i].setIcon(blackpawn);
		}
		spots[1][5].setIcon(blackking); //  white king
		spots[1][4].setIcon(blackqueen); // white queen
		spots[1][1].setIcon(blackrook); // 2 white rooks
		spots[1][8].setIcon(blackrook);
		spots[1][2].setIcon(blackknight); // 2 white knights
		spots[1][7].setIcon(blackknight);
		spots[1][3].setIcon(blackbishop); // 2 white bishops
		spots[1][6].setIcon(blackbishop);
		
		for(int x=3; x<7; x++)
		{
			for(int y=1; y<9; y++)
			{
				spots[x][y].setIcon(empty);  //set the rest of the board to be empty
			}
		}
		
	}
	public static void updateButton(int positionR[][], int spotValueR)
	{
		int[][] position = new int[1][2];
		
		position[0][0] = positionR[0][0];    //To avoid changing the value that was originally passed to this function
		position[0][1] = positionR[0][1];
		
		position[0][0] = (FlipView.isBoardFlipped()) ? (9 - positionR[0][0]) : positionR[0][0];
		position[0][1] = (FlipView.isBoardFlipped()) ? (9 - positionR[0][1]) : positionR[0][1];
		switch(spotValueR)	
		{
			case 0:
				spots[position[0][0]][position[0][1]].setIcon(empty);
				break;
			case 1:
				spots[position[0][0]][position[0][1]].setIcon(whiteking);
				break;
			case 2:
				spots[position[0][0]][position[0][1]].setIcon(whitepawn);
				break;
			case 3:
				spots[position[0][0]][position[0][1]].setIcon(whiteknight);
				break;
			case 4:
				spots[position[0][0]][position[0][1]].setIcon(whitebishop);
				break;
			case 5:
				spots[position[0][0]][position[0][1]].setIcon(whiterook);
				break;
			case 6:
				spots[position[0][0]][position[0][1]].setIcon(whitequeen);
				break;
			case 7:
				spots[position[0][0]][position[0][1]].setIcon(blackking);
				break;
			case 8:
				spots[position[0][0]][position[0][1]].setIcon(blackpawn);
				break;
			case 9:
				spots[position[0][0]][position[0][1]].setIcon(blackknight);
				break;
			case 10:
				spots[position[0][0]][position[0][1]].setIcon(blackbishop);
				break;
			case 11:
				spots[position[0][0]][position[0][1]].setIcon(blackrook);
				break;
			case 12:
				spots[position[0][0]][position[0][1]].setIcon(blackqueen);
				break;
			case 13:
				spots[position[0][0]][position[0][1]].setBackground(Color.LIGHT_GRAY);;
				break;
		}
			
	}
	public void setGameOn(boolean GameOn)
	{
		this.GameOn = GameOn;
	}
	public void updateLabel( String color, String time)
	{
		String formattedTime;
		int minutes;
		int seconds;
		minutes = Integer.parseInt(time)/ 60;
		seconds = Integer.parseInt(time) % 60;
		formattedTime = ""+Integer.toString(minutes)+" : " + Integer.toString(seconds)+"";

		switch (color)
		{
		case "white":
					clocks[0].setText(formattedTime);
					clocks[0].setForeground(Color.RED);
					clocks[1].setForeground(Color.black);
					break;
		case "black":
					clocks[1].setText(formattedTime);
					clocks[1].setForeground(Color.RED);
					clocks[0].setForeground(Color.black);
					break;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{	
		if(GameOn && (MaintainMovesHistory.getDisplayUptoDate()==true))  //only process a request to move if the view is uptodate!
		{
			//check if the event is a move (One of the board buttons were clicked)
			for (int i=1; i<9; i++ )
			{
				for(int j=1; j<9; j++)
				{
					if (e.getSource()==spots[i][j])
					{
						clickedX = (FlipView.isBoardFlipped()) ? 9-i : i;
						clickedY = (FlipView.isBoardFlipped()) ? 9-j : j;
						
						GameInstance.clickRecieved(clickedX, clickedY);
					
						return;
					}
				}
			}
		}
		
		if(e.getSource()==NewGame)
		{
			if(GameOn)
			{
				GameInstance.KillGameControlThread("user"); //user decided to stop the game				
				GameInstance = null;
				redrawBoard();
				NewGame.setText(" New Game");
				
			}
			else
			{
				GameInstance = new RealBoard(this);
				GameInstance.CreateGameControlThread(TimeOpt.getSelectedIndex());
				NewGame.setText(" End Game");
			}
			return;
		}
		if(e.getSource()==flipBoard)
		{
			if(FlipView.isBoardFlipped())
			{
				FlipView.unflipBoard();  //board is already flipped, unflip it
			}
			else
			{
				FlipView.flipBoard();  //flip the board
			}
			return;
		}

	}


}
