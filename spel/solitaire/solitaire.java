//  A simple solitaire game
//
//  Karl Hornell, February 28, 1996

import java.awt.*;

public class solitaire extends java.applet.Applet
{
	int i,j,k,deadBalls;
	int pointX,pointY,pickX=-1,pickY=-1,dragX=-1,dragY=-1;
	int map[];
	boolean drawBoard;

	public void init()
	{
		map = new int[81];
		fillMap();
		resize(280,190);
		repaint();
	}

	public void fillMap()
	{
		for (i=0;i<81;i++)
			map[i]=1;
		for (i=0;i<3;i++)
			for (j=0;j<3;j++)
			{
				map[i*9+j]=-1;
				map[i*9+j+6]=-1;
				map[i*9+j+54]=-1;
				map[i*9+j+60]=-1;
			}
		map[40]=0;
		drawBoard=true;
		deadBalls=0;
	}

	public void fixBox(Graphics g,int x,int y,int w,int h,Color c)
	{
		g.setColor(c);
		g.fillRect(x,y,w,h);
	}

	public void fixDisc(Graphics g,int x,int y,int r,Color c)
	{
		g.setColor(c);
		g.fillOval(x,y,r,r);
	}

	public void fixCircle(Graphics g,int x,int y,int r,Color c)
	{
		g.setColor(c);
		g.drawOval(x,y,r,r);
	}

	public void boardBall(Graphics g,int x,int y)
	{
		g.setColor(Color.blue);
		g.fillRect(x,y,16,16);
		fixDisc(g,x+1,y+1,15,Color.black);
		fixDisc(g,x,y,14,Color.cyan);
		fixDisc(g,x+6,y+6,7,Color.blue);
		fixDisc(g,x+1,y+1,8,Color.white);
		fixDisc(g,x+9,y+9,3,Color.white);
	}

	public void deadBall(Graphics g,int x,int y)
	{
		fixDisc(g,x+3,y+3,14,Color.gray);
		fixDisc(g,x,y,14,Color.cyan);
		fixDisc(g,x+6,y+6,7,Color.gray);
		fixDisc(g,x+1,y+1,8,Color.white);
		fixDisc(g,x+9,y+9,3,Color.white);
	}

	public void boardHole(Graphics g,int x,int y)
	{
		g.setColor(Color.blue);
		g.fillRect(x,y,16,16);
		fixDisc(g,x+1,y+1,12,Color.black);
		fixDisc(g,x+4,y+4,10,Color.blue);
		g.setColor(Color.cyan);
		g.drawArc(x,y,14,14,-135,180);
	}

	public boolean mouseDown(java.awt.Event evt, int x, int y)
        {
                dragX=-1;
		pointX=-100;
		pointY=-100;
		pickX=-1;
		pickY=-1;
		i=(y-18)/17;
		j=(x-18)/17;
		if ((i>-1)&&(i<9)&&(j>-1)&&(j<9))
		{
			if (map[i*9+j]==1)
			{
				pickX=j;
				pickY=i;
				pointY=y;
				pointX=x;
			}
		}
		else if ((x>206)&&(x<266)&&(y>166)&&(y<190))
		{
			fillMap();
			repaint();
		}
		return false;
	}

	public boolean mouseDrag(java.awt.Event evt, int x, int y)
	{
		dragX=-1;
		if ((x>(pointX-8))&&(x<pointX+8))
		{
			if ((y<pointY-14)&&(pickY>1))
				if ((map[pickX+9*pickY-18]==0)&&(map[pickX+9*pickY-9]==1))
				{
					dragX=pickX;
					dragY=pickY-2;
				}
			if ((y>pointY+14)&&(pickY<7))
				if ((map[pickX+9*pickY+18]==0)&&(map[pickX+9*pickY+9]==1))
				{
					dragX=pickX;
					dragY=pickY+2;
				}
		}
		else if ((y>(pointY-8))&&(y<pointY+8))
		{
			if ((x<pointX-14)&&(pickX>1))
				if ((map[pickX+9*pickY-2]==0)&&(map[pickX+9*pickY-1]==1))
				{
					dragX=pickX-2;
					dragY=pickY;
				}
			if ((x>pointX+14)&&(pickX<7))
				if ((map[pickX+9*pickY+2]==0)&&(map[pickX+9*pickY+1]==1))
				{
					dragX=pickX+2;
					dragY=pickY;
				}
		}
		return false;
	}

	public boolean mouseUp(java.awt.Event evt, int x, int y)
	{
		if (dragX>-1)
		{
			drawBoard=false;
			repaint();
		}
		return false;
	}

	public void update(Graphics g)
	{
		paint(g);
	}

	public void paint(Graphics g)
	{
		if (drawBoard)
		{
			g.setColor(Color.lightGray);
			g.fillRect(0,0,size().width,size().height);
			fixBox(g,207,166,60,24,Color.black);
			fixBox(g,208,167,58,22,Color.blue);
			g.setColor(Color.cyan);
			g.drawString("Restart",215,183);
			fixDisc(g,2,2,187,Color.gray);
			fixDisc(g,0,0,185,Color.blue);
			fixDisc(g,0,0,183,Color.cyan);
			fixDisc(g,2,2,183,Color.black);
			fixDisc(g,2,2,181,Color.blue);
			fixCircle(g,8,8,171,Color.cyan);
			fixCircle(g,7,7,171,Color.black);

			for (i=0;i<9;i++)
				for (j=0;j<9;j++)
					if (map[i*9+j]>-1)
						if (map[i*9+j]==0)
							boardHole(g,18+j*17,18+i*17);
						else
							boardBall(g,18+j*17,18+i*17);
			for (i=0;i<deadBalls;i++)
			{
				k=i/5;
				j=i-k*5;
				deadBall(g,193+j*17,5+k*17);
			}
		}
		else
		{
			map[pickX+9*pickY]=0;
			map[(pickX+dragX)/2+9*(pickY+dragY)/2]=0;
			map[dragX+9*dragY]=1;
			boardHole(g,18+pickX*17,18+pickY*17);
			boardHole(g,18+((pickX+dragX)/2)*17,18+((pickY+dragY)/2)*17);
			boardBall(g,18+dragX*17,18+dragY*17);
			i=deadBalls/5;
			j=deadBalls-i*5;
			deadBall(g,193+j*17,5+i*17);
			deadBalls++;
			drawBoard=true;
		}
	}	
}
