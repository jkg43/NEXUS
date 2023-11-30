package fileIO;

import java.io.FileReader;
import java.io.IOException;

import dataTypes.GoalsList;

public class FileRead
{



	public static GoalsList readGoalsListFile(String filename)
	{
		GoalsList list = new GoalsList();

		StringBuilder b = new StringBuilder();

		FileReader in;
		try
		{
			in = new FileReader(filename);

			int c;

			while((c=in.read()) != -1)
			{
				b.append((char)c);
			}

			in.close();


			String fileData = b.toString();

			String[] lines = fileData.split("\n");

			for(String l : lines)
			{
				list.goals.add(list.new Goal(l.substring(1),(l.charAt(0)=='1')));
			}


		}
		catch (IOException e)
		{
			System.out.println("Error: file not found");
		}





		return list;
	}






}
