package manager;

import bci.BCIModule;
import ui.UI;
import ui.UI.UIWindow;

import java.util.ArrayList;

public class Manager
{

	public static final String filePath = "C:/Users/jkg43/Desktop/Files/NEXUS";

	public static final String dataPath = filePath + "/data";

	public ArrayList<Module> modules;

	public Module currentModule;

	public static UI ui;


	public void closeFunction()
	{
		System.out.println("Closing");
		if(currentModule instanceof BCIModule bci) {
			bci.disconnect();
		}
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
