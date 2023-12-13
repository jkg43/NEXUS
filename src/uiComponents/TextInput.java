package uiComponents;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.function.Consumer;

import tools.StringTools;
import ui.UI;

public class TextInput extends ActivatableUIComponent
{

	public StringBuilder builder;

	public int cursorPos=0;

	Font inputFont;

	int mouseX=Integer.MIN_VALUE;

	int minWidth;

	private final Font font;

	public TextInput(UI u,int x, int y, int w, int h,String disp,Consumer<ActivatableUIComponent> f,UIComponent... t)
	{
		super(u,f,t);
		this.x=x;
		this.y=y;
		width = w;
		height = h;
		inputFont = new Font("arial", Font.PLAIN, height-10);
		builder = new StringBuilder();
		builder.append(disp);
		minWidth = width;

		font = new Font("arial",Font.PLAIN,height-10);
	}

	@Override
	public void update()
	{

	}

	@Override
	public void draw(Graphics2D g2d)
	{

		String disp = builder.toString();

		int strWidth = g2d.getFontMetrics().stringWidth(disp);

		if(strWidth > width - 15)
		{
			width = strWidth + 15;
			if(width < minWidth)
			{
				width = minWidth;
			}
		}

		g2d.setFont(font);

		g2d.setColor(Color.BLACK);
		g2d.fillRect(x, y, width, height);
		g2d.setColor(new Color(160,160,160));
		g2d.fillRect(x+5, y+5, width-10, height-10);

		g2d.setColor(Color.BLACK);
		g2d.drawString(disp, x+5, y+height-10);

		if(mouseX != Integer.MIN_VALUE)
		{
			cursorPos = StringTools.getCharIndexFromPos(g2d.getFontMetrics(), disp, mouseX);
			mouseX=Integer.MIN_VALUE;
		}

		if(ui.selectedComponent==this)
		{
			g2d.setColor(Color.DARK_GRAY);
			g2d.fillRect(x+4+g2d.getFontMetrics().stringWidth(disp.substring(0,cursorPos)), y+7, 3, height-16);
		}

		ui.resetGraphics(g2d);
	}

	public void moveCursor(int delta)
	{
		cursorPos+=delta;
		if(cursorPos<0)
		{
			cursorPos = 0;
		}
		if(cursorPos>builder.length()-1)
		{
			cursorPos = builder.length();
		}
	}

	public void clear()
	{
		builder.setLength(0);
		cursorPos = 0;
	}


	@Override
	public void checkMouseAndActivate(int mx, int my,int button)
	{
		if(isPointInside(mx,my))
		{
			ui.selectedComponent = this;
			ui.in.typing = true;

			mouseX=mx-x+5;
		}
	}

}
