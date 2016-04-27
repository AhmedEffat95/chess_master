package game;

import gui.UI;

public class TimeManagement implements Runnable
{
	
	private volatile Thread th;
	private  String threadName;
	private boolean GameOn;
	RealBoard GameObject;
	UI GUIt;
	GameControl GameControl;
	private int whiteSeconds;
	private int blackSeconds;
	
	private long initialTime;
	private long currentTime;
	
	public TimeManagement(String name, RealBoard GameR, UI GUIr, GameControl GameControl)
	{
		threadName = name;
		GameObject = GameR;
		GUIt = GUIr;
		this.GameControl = GameControl;
		whiteSeconds = this.GameControl.getTimeControl();
		blackSeconds = whiteSeconds;

	}
	@Override
	public void run() 
	{		
		GUIt.updateLabel("white", Integer.toString(whiteSeconds));
		GUIt.updateLabel("black", Integer.toString(blackSeconds));
		GameOn = true;
	
		while(GameOn)
		{
								
			decrementWhite();

			decrementBlack();
	
		}
	}
	
	public void start()
	{
		if (th==null)
		{
			th = new Thread(this,threadName);
			th.start();
		}
	}
	public void decrementWhite()
	{
		initialTime = System.currentTimeMillis();
		while( (GameObject.getTurn()=="white") && (GameOn) )
		{
			currentTime =  System.currentTimeMillis();
			if(currentTime >= (initialTime+1000))
			{			
				
				if (whiteSeconds==0)
				{
					GameObject.KillGameControlThread("time");
					
				}
				else if (GameObject.getTurn()=="white")
				{
					
						whiteSeconds--;
						GUIt.updateLabel("white", Integer.toString(whiteSeconds));
						initialTime = currentTime;
				}
				
			}
		}

	}
	public void decrementBlack()
	{
		initialTime = System.currentTimeMillis();
		while( (GameObject.getTurn()=="black") && (GameOn))
		{
			currentTime =  System.currentTimeMillis();
			if(currentTime >= (initialTime+1000))
			{			
				
				if (blackSeconds==0)
				{
					GameObject.KillGameControlThread("time");
					
				}
				else if (GameObject.getTurn()=="black")
				{
					
						blackSeconds--;
						GUIt.updateLabel("black", Integer.toString(blackSeconds));
						initialTime = currentTime;
				}
				
			}
		}
		
	}
	public void killThread()
	{
		GameOn = false;
		th = null;
	}

}

