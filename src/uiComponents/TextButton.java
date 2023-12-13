package uiComponents;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.function.Consumer;

import ui.UI;

public class TextButton extends ActivatableUIComponent
{

	public String text;

	public Color normalColor,hoverColor,currentColor,textColor;

	private final Font font;

	private boolean hovered = false;


	public TextButton(UI u, int x, int y, int w, int h, String t, Color normalColor, Color hoverColor, Color textColor,
					  Consumer<ActivatableUIComponent> func, UIComponent... target)
	{
		super(u,func,target);
		this.x=x;
		this.y=y;
		height=h;
		width=w;
		text=t;
		this.normalColor=normalColor;
		this.hoverColor=hoverColor;
		currentColor = normalColor;
		this.textColor = textColor;

		font = new Font("arial",Font.PLAIN,height);
	}

	public boolean isHovered()
	{
		return hovered;
	}

	@Override
	public void draw(Graphics2D g2d)
	{
		Font previousFont = g2d.getFont();
		g2d.setFont(font);
		g2d.setColor(currentColor);
		g2d.fillRect(x, y, width, height);
		g2d.setColor(textColor);
		g2d.drawString(text, x, y+(int)(height*0.8));
		g2d.setFont(previousFont);

	}

	@Override
	public void checkMouseAndActivate(int mx,int my,int button)
	{
		if(mx>=x && my>=y && mx<=x+width && my<=y+height)
		{
			activate();
		}
	}

	@Override
	public void update()
	{
		int mx = ui.in.mouseX,my = ui.in.mouseY;
		if(isPointInside(mx,my))
		{
			currentColor=hoverColor;
			hovered = true;
		}
		else
		{
			currentColor=normalColor;
			hovered = false;
		}
	}


}
