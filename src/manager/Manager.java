package manager;

import ui.UI.UIWindow;

import java.util.ArrayList;

public class Manager
{

	public static final String filePath = "C:/Users/jkg43/Desktop/Files/NEXUS";

	public static final String dataPath = filePath + "/data";

	public ArrayList<Module> modules;

	public Module currentModule;


	public void closeFunction()
	{
		System.out.println("Closing");
	}


	private Manager()
	{
		modules = new ArrayList<>();
		new UIWindow(this);
	}


	public static void main(String[] args)
	{
		new Manager();
	}





}
