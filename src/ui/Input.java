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

	private boolean ctrl = false, shift = false, alt = false;


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
				if(e.getKeyChar()=='\b')
				{
					if(!t.builder.isEmpty() && t.cursorPos!=0)
					{
						t.builder.deleteCharAt(t.cursorPos-1);
						t.moveCursor(-1);
					}
				} else if(!(ctrl || alt))
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
            switch (e.getKeyCode()) {
                case KeyEvent.VK_L, KeyEvent.VK_G -> ui.drawDebug = !ui.drawDebug;
            }
			if(ui.m.currentModule instanceof CameraModule2D cam) {
				cam.processKeyCode(e.getKeyCode());
			}
		}
		else
		{
			if(ui.selectedComponent instanceof TextInput t)
			{
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_RIGHT -> {
						t.moveCursor(1);
						if(ctrl) {
							while(t.cursorPos!=t.builder.length() &&
								!Character.isWhitespace(t.builder.charAt(t.cursorPos))) {
								t.moveCursor(1);
							}
							while(t.cursorPos!=t.builder.length() &&
								Character.isWhitespace(t.builder.charAt(t.cursorPos))) {
								t.moveCursor(1);
							}
						}
						if(shift) {
							System.out.println("Select rightward"); //TODO
						}
					}
                    case KeyEvent.VK_LEFT -> {
						t.moveCursor(-1);
						if(ctrl) {
							while(t.cursorPos!=0 && !Character.isWhitespace(t.builder.charAt(t.cursorPos))) {
								t.moveCursor(-1);
							}
							while(t.cursorPos!=0 && Character.isWhitespace(t.builder.charAt(t.cursorPos-1))) {
								t.moveCursor(-1);
							}
						}
						if(shift) {
							System.out.println("Select leftward");
						}
					}
                    case KeyEvent.VK_UP -> t.cursorPos = 0;
                    case KeyEvent.VK_DOWN -> t.cursorPos = t.builder.length();
                    case KeyEvent.VK_ENTER -> t.activate();
					case KeyEvent.VK_BACK_SPACE -> {
						if(e.isControlDown()) {
							while(!t.builder.isEmpty() && !Character.isWhitespace(t.builder.charAt(t.builder.length()-1))) {
								if(t.cursorPos!=0)
								{
									t.builder.deleteCharAt(t.cursorPos-1);
									t.moveCursor(-1);
								} else {
									break;
								}
							}
							while(!t.builder.isEmpty() && Character.isWhitespace(t.builder.charAt(t.builder.length()-1))) {
								if(t.cursorPos!=0)
								{
									t.builder.deleteCharAt(t.cursorPos-1);
									t.moveCursor(-1);
								} else {
									break;
								}
							}
						}
					}
                }
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_CONTROL) {
			ctrl = true;
		}
		if(e.getKeyCode()==KeyEvent.VK_SHIFT) {
			shift = true;
		}
		if(e.getKeyCode()==KeyEvent.VK_ALT) {
			alt = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyCode()==KeyEvent.VK_CONTROL) {
			ctrl = false;
		}
		if(e.getKeyCode()==KeyEvent.VK_SHIFT) {
			shift = false;
		}
		if(e.getKeyCode()==KeyEvent.VK_ALT) {
			alt = false;
		}
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