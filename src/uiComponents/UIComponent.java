package uiComponents;

import java.awt.Graphics2D;

import ui.UI;

public abstract class UIComponent
{


	public int x,y,width,height;

	public boolean hidden = false;

	protected UI ui;

	public abstract void update();

	public abstract void draw(Graphics2D g2d);

	public abstract void checkMouseAndActivate(int mx,int my,int button);

	public UIComponent(UI u)
	{
		this.ui = u;
	}


}
