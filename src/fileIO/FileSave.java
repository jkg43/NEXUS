package fileIO;

import java.io.FileWriter;
import java.io.IOException;

import dataTypes.GoalsList;
import dataTypes.GoalsList.Goal;

public class FileSave
{







	public static void saveFile(GoalsList list, String filename)
	{
		FileWriter out;
		try
		{
			out = new FileWriter(filename);

			for(Goal g : list.goals)
			{
				out.write((g.complete ? '1' : '0')+g.name+"\n");
			}


			out.close();




		}
		catch (IOException e)
		{
			e.printStackTrace();
		}



	}







}
