package ui;

import manager.CameraModule2D;
import uiComponents.TextInput;
import uiComponents.UIComponent;

import java.awt.event.*;

public class Input implements KeyListener,MouseListener,MouseMotionListener, MouseWheelListener
{

	public int mouseX=-10000,mouseY=-10000;

	private final UI ui;

	public boolean typing = false;


	public Input(UI u)
	{
		this.ui = u;
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		int dx = e.getX() - mouseX;
		int dy = e.getY() - mouseY;

		mouseX = e.getX();
		mouseY = e.getY();
		if(ui.m.currentModule instanceof CameraModule2D cam) {
			cam.processMouseDrag(dx,dy,e);
		}
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

		typing = false;
		ui.selectedComponent=null;

		for(UIComponent c : ui.globalComponents)
		{
			c.checkMouseAndActivate(mouseX, mouseY,e.getButton());
		}



		if(ui.contextMenuHovered()) {
			ui.currentContextMenu.checkMouseAndActivate(mouseX,mouseY,e.getButton());
		} else {
			ui.currentContextMenu = null;
			if(!ui.selector.menuHovered())
			{
				for(UIComponent c : ui.m.currentModule.components)
				{
					c.checkMouseAndActivate(mouseX,mouseY,e.getButton());
				}
				if(ui.m.currentModule instanceof CameraModule2D cam) {
					for(UIComponent c : cam.staticComponents) {
						c.checkMouseAndActivate(mouseX,mouseY,e.getButton());
					}
				}
			}
		}



	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();

		if(ui.m.currentModule instanceof CameraModule2D cam) {
			cam.draggedComponent = null;
		}
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
			if(ui.selectedComponent instanceof TextInput t && e.getKeyChar()!='\n')
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
				case KeyEvent.VK_G:
					ui.drawDebug = !ui.drawDebug;
					break;
			}
			if(ui.m.currentModule instanceof CameraModule2D cam) {
				cam.processKeyCode(e.getKeyCode());
			}
		}
		else
		{
			if(ui.selectedComponent instanceof TextInput t)
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

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int rotation = e.getWheelRotation();
		if(ui.m.currentModule instanceof CameraModule2D cam) {
			double scaleFactor;
			if(rotation < 0) {
				scaleFactor = 1.1;
				rotation = -rotation;
			} else {
				scaleFactor = 1.0/1.1;
			}
			for (int i = 0; i < rotation; i++) {
				cam.scaleAroundPoint(scaleFactor, mouseX, mouseY);
			}
		}

	}


}