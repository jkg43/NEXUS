package manager;

import java.awt.Graphics2D;
import java.util.ArrayList;

import ui.UI;
import uiComponents.UIComponent;

public abstract class Module
{

	public ArrayList<UIComponent> components;

	public UI u;

	public String modulePath;

	public String name;

	public void update()
	{
		for(UIComponent c : components)
		{
			c.update();
		}
	}


	public void draw(Graphics2D g2d)
	{
		for(UIComponent c : components)
		{
			if(!c.hidden)
			{
				c.draw(g2d);
			}
		}
	}


	public Module(UI u, String name,String modulePath)
	{
		this.components = new ArrayList<>();
		this.u = u;
		this.name = name;
		this.modulePath = Manager.dataPath + modulePath;
	}



}
