package ui;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import uiComponents.TextInput;
import uiComponents.UIComponent;

public class Input implements KeyListener,MouseListener,MouseMotionListener
{

	public int mouseX=-10000,mouseY=-10000;

	private final UI u;

	public boolean typing = false;


	public Input(UI u)
	{
		this.u = u;
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();

		typing = false;
		u.selectedComponent=null;


		for(UIComponent c : u.globalComponents)
		{
			c.checkMouseAndActivate(mouseX, mouseY,e.getButton());
		}

		if(!u.selector.menuHovered())
		{
			for(UIComponent c : u.m.currentModule.components)
			{
				c.checkMouseAndActivate(mouseX,mouseY,e.getButton());
			}
		}


	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		if(typing)
		{
			if(u.selectedComponent instanceof TextInput t && e.getKeyChar()!='\n')
			{
				if(e.getKeyChar()==KeyEvent.VK_BACK_SPACE)
				{
					if(!t.builder.isEmpty() && t.cursorPos!=0)
					{
						t.builder.deleteCharAt(t.cursorPos-1);
						t.moveCursor(-1);
					}
				}
				else
				{
					t.builder.insert(t.cursorPos, e.getKeyChar());
					t.moveCursor(1);
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(!typing)
		{
			switch(e.getKeyCode())
			{
				case KeyEvent.VK_D:
					u.drawDebug = !u.drawDebug;
					break;
				case KeyEvent.VK_S:

					break;
			}
		}
		else
		{
			if(u.selectedComponent instanceof TextInput t)
			{
				switch(e.getKeyCode())
				{
					case KeyEvent.VK_RIGHT:
						t.moveCursor(1);
						break;
					case KeyEvent.VK_LEFT:
						t.moveCursor(-1);
						break;
					case KeyEvent.VK_ENTER:
						t.activate();
						break;
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{

	}





}