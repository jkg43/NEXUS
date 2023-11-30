package uiComponents;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import tools.GraphicsTools;
import ui.UI;

public class Checklist extends UIComponent
{




	public ArrayList<ChecklistItem> items;
	String title;

	int spacing = 30;

	TextInput textIn;

	boolean adding = false, removing = false;

	public ArrayList<ChecklistItem> itemsToRemove;


	public Checklist(UI u, int x,int y,String title)
	{
		super(u);
		items = new ArrayList<>();
		itemsToRemove = new ArrayList<>();
		this.x=x;
		this.y=y;
		this.title = title;

		textIn = new TextInput(u, x, y, 100, 40, "",(t)->{
			Checklist cl = (Checklist)t.targets[0];
			TextInput ti = (TextInput)t;
			cl.add(ti.builder.toString(),false);
			cl.adding = false;
			ti.hidden = true;
			ti.clear();
		},this);
		textIn.hidden = true;
		u.globalComponents.add(textIn);
	}


	public void add(String text,boolean checked)
	{
		items.add(new ChecklistItem(ui, this, text, checked));
	}

	public void clear()
	{
		items.clear();
	}


	@Override
	public void update()
	{

	}



	@Override
	public void draw(Graphics2D g2d)
	{

		g2d.setColor(Color.black);
		g2d.drawString(title, x, y);

		g2d.setStroke(new BasicStroke(3));

		g2d.drawLine(x, y+5, x+g2d.getFontMetrics().stringWidth(title), y+5);


		int yPos = y+5+spacing;

		for(ChecklistItem i : items)
		{
			i.x=x;
			i.y=yPos;
			i.draw(g2d);

			yPos += spacing;

		}

		textIn.y = yPos - spacing+5;

		GraphicsTools.setStrokeWidth(g2d,4);

		if(adding)
		{
			g2d.setColor(Color.RED);

			g2d.drawLine(x-30, y-20, x-5, y+5);
			g2d.drawLine(x-5, y-20, x-30, y+5);

			ChecklistItem.drawCheckPolygon(g2d, textIn.x, yPos+5);
		}
		else if(removing)
		{
			g2d.setColor(Color.RED);

			g2d.drawPolygon(new int[]{x-30,x-5,x-10,x-25}, new int[] {y-18,y-18,y+5,y+5}, 4);
			g2d.drawPolygon(new int[]{x-25,x-21,x-14,x-10}, new int[] {y-18,y-22,y-22,y-18}, 4);
		}
		else
		{
			g2d.setColor(Color.BLACK);

			g2d.drawLine(x-17, y-20, x-17, y+5);
			g2d.drawLine(x-30, y-7, x-5, y-7);
		}




	}


	@Override
	public void checkMouseAndActivate(int mx, int my, int button)
	{
		for(ChecklistItem i : items)
		{
			i.checkMouseAndActivate(mx, my, button);
		}

		for(ChecklistItem i : itemsToRemove)
		{
			items.remove(i);
		}
		itemsToRemove.clear();

		if(mx >= x-30 && mx <= x-5 && my >= y-20 && my <= y+5)
		{
			if(button == 1 && !removing)
			{
				textIn.hidden = adding;
				adding = !adding;
				if(adding)
				{
					ui.selectedComponent = textIn;
					ui.in.typing = true;
				}
				else
				{
					textIn.clear();
				}
			}
			else if(button == 3 && !adding)
			{
				removing = !removing;
            }



		}
		if(adding && button == 1 && mx >= textIn.x-35 && mx <= textIn.x && my >= textIn.y-30+spacing
			&& my <= textIn.y+spacing)
		{
			textIn.activate();
		}
	}

















}
