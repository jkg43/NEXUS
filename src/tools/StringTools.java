package tools;

import java.awt.FontMetrics;

public class StringTools
{






	public static int getCharIndexFromPos(FontMetrics fm, String s, int pos)
	{

		int currentPos = 0,currentChar,nextPos;

		for(int i=0;i<s.length();i++)
		{
			currentChar = s.charAt(i);
			nextPos = currentPos + fm.charWidth(currentChar);
			if(pos < nextPos)
			{
				return i;
			}
			currentPos = nextPos;
		}




		return s.length();
	}






}
