package tools;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

public class GraphicsTools
{


	public static void setStrokeWidth(Graphics2D g2d, int width)
	{
		g2d.setStroke(new BasicStroke(width));
	}





}
