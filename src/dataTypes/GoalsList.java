package dataTypes;

import java.util.ArrayList;

import uiComponents.Checklist;
import uiComponents.ChecklistItem;

public class GoalsList
{



	public ArrayList<Goal> goals;



	public GoalsList()
	{
		goals = new ArrayList<Goal>();
	}

	public void add(String n, boolean c)
	{
		goals.add(new Goal(n,c));
	}

	public void loadIntoChecklist(Checklist c)
	{
		c.clear();
		for(Goal g : goals)
		{
			c.add(g.name,g.complete);
		}
	}

	public void loadFromChecklist(Checklist c)
	{
		goals.clear();
		for(ChecklistItem i : c.items)
		{
			goals.add(new Goal(i.text,i.checked));
		}
	}

	public class Goal
	{
		public String name;
		public boolean complete;

		public Goal(String n, boolean c)
		{
			name=n;
			complete = c;
		}
	}

}
