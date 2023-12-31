package uiComponents;

import java.util.function.Consumer;

public abstract class ActivatableUIComponent extends UIComponent
{


	Consumer<ActivatableUIComponent> func;

	public UIComponent[] targets;


	public ActivatableUIComponent(Consumer<ActivatableUIComponent> f,UIComponent... t)
	{
		super();
		func = f;
		targets = t;
	}


	public void activate()
	{
		func.accept(this);
	}

}
