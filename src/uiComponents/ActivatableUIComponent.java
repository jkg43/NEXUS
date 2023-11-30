package uiComponents;

import java.util.function.Consumer;

import ui.UI;

public abstract class ActivatableUIComponent extends UIComponent
{


	Consumer<ActivatableUIComponent> func;

	public UIComponent[] targets;


	public ActivatableUIComponent(UI u,Consumer<ActivatableUIComponent> f,UIComponent... t)
	{
		super(u);
		func = f;
		targets = t;
	}


	public void activate()
	{
		func.accept(this);
	}

}
