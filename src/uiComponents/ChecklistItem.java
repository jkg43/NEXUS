package uiComponents;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

import ui.UI;

public class ChecklistItem extends UIComponent
{


	public String text;
	public boolean checked;

	Checklist checklist;

	public ChecklistItem(UI u, Checklist cl, String text,boolean c)
	{
		super(u);
		this.text = text;
		checked = c;
		checklist = cl;
		width = 0;
	}

	@Override
	public void update()
	{

	}

	@Override
	public void draw(Graphics2D g2d)
	{
		width = g2d.getFontMetrics().stringWidth(text);

		g2d.setColor(Color.black);
		g2d.drawString(text,x,y);
		g2d.drawRect(x-25, y-20, 20, 20);

		if(checked)
		{
			g2d.setColor(new Color(30,120,50));
			drawCheckPolygon(g2d, x, y);
		}

	}

	@Override
	public void checkMouseAndActivate(int mx, int my,int button)
	{
		if(mx >= x-25 && mx <= x-5 && my>=y-20 && my<=y)
		{
			checked = !checked;
		}
		if(checklist.removing && mx >= x-25 && mx <= x+width && my >= y-30 && my <= y)
		{
			checklist.itemsToRemove.add(this);
		}

	}


	public static void drawCheckPolygon(Graphics2D g2d, int x,int y)
	{
		g2d.setColor(new Color(30,120,50));
		g2d.fillPolygon(new Polygon(new int[] {x-15,x+3,x-17,x-33}, new int[] {y,y-27,y-10,y-12}, 4));
	}


}
