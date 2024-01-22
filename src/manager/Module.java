package manager;

import java.awt.Graphics2D;
import java.util.ArrayList;

import ui.UI;
import uiComponents.UIComponent;

public abstract class Module
{

	public ArrayList<UIComponent> components;

	//will be added to the component list after every update cycle
	public ArrayList<UIComponent> componentsToAdd;

	//will be removed from the component list after every update cycle
	public ArrayList<UIComponent> componentsToRemove;

	public UI ui;

	public String modulePath;

	public String name;

	public void update()
	{
		for(UIComponent c : components)
		{
			c.update();
		}

		components.addAll(componentsToAdd);

		for(UIComponent c : componentsToRemove) {
			c.destroy();
		}
		components.removeAll(componentsToRemove);

		componentsToAdd.clear();
		componentsToRemove.clear();
	}


	public void draw(Graphics2D g2d)
	{
		for(UIComponent c : components)
		{
			if(!c.isHidden)
			{
				c.draw(g2d);
			}
		}
	}


	/**
	 * @param name module name
	 * @param modulePath data directory path, with slashes on either side
	 */
	public Module(String name,String modulePath)
	{
		this.components = new ArrayList<>();
		this.componentsToAdd = new ArrayList<>();
		this.componentsToRemove = new ArrayList<>();
		this.ui = Manager.ui;
		this.name = name;
		this.modulePath = Manager.dataPath + modulePath;
	}




}
