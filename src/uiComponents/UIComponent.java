package uiComponents;

import java.awt.Graphics2D;

import ui.UI;

public abstract class UIComponent
{


	public int x,y,width,height;

	public boolean isHidden = false;

	public boolean isMovable = false;

	protected UI ui;

	public abstract void update();

	public abstract void draw(Graphics2D g2d);

	public abstract void checkMouseAndActivate(int mx,int my,int button);

	public UIComponent(UI u)
	{
		this.ui = u;
	}

	public boolean isPointInside(int px, int py) {
		return !(px < x || py < y || px > x + width || py > y + height);
	}

	public void translate(int dx, int dy) {
		x+=dx;
		y+=dy;
	}

	public void destroy() {
		if(ui.selectedComponent==this) {
			ui.selectedComponent = null;
		}
	}


}
