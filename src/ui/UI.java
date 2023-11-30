package ui;

import manager.Manager;
import modules.ChecklistModule;
import uiComponents.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class UI extends JPanel implements ActionListener
{
	public final int HEIGHT=600,WIDTH=800;
	public boolean drawDebug = true;

	public final Manager m;



	public ArrayList<UIComponent> globalComponents;

	public UIComponent selectedComponent = null;

	public ModuleSelector selector;


	private void init()
	{
		globalComponents = new ArrayList<>();


		ChecklistModule checklistMod = new ChecklistModule(this, "Checklist 1");
		ChecklistModule checklistMod2 = new ChecklistModule(this, "Checklist 2");


		m.modules.add(checklistMod);
		m.modules.add(checklistMod2);
		m.currentModule = checklistMod;

		selector = new ModuleSelector(this);
		globalComponents.add(selector);
	}




	private void update()
	{
		for(UIComponent c : globalComponents)
		{
			c.update();
		}

		m.currentModule.update();

	}




	private void draw(Graphics2D g2d)
	{
		m.currentModule.draw(g2d);

		for(UIComponent c : globalComponents)
		{
			if(!c.hidden)
			{
				c.draw(g2d);
			}
		}
	}





	private void drawDebug(Graphics2D g2d)
	{
		g2d.setColor(Color.BLACK);

		String[] debugInfo = {
			"M: "+in.mouseX+", "+in.mouseY,
			"T: "+in.typing,
			"SN: "+(selectedComponent==null)
		};

		int yPos = HEIGHT-10;
		for(String s : debugInfo)
		{
			g2d.drawString(s, 10, yPos);
			yPos -= 30;
		}
	}


































	Font defaultFont = new Font("arial", Font.PLAIN, 30);
	BasicStroke defaultStroke = new BasicStroke(1);
	Timer t = new Timer(50, this);
	public Input in;

	@Override
	public void actionPerformed(ActionEvent e)
	{
		update();
		repaint();
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;

		resetGraphics(g2d);
		draw(g2d);
		if(drawDebug)
		{
			drawDebug(g2d);
		}
	}

	public void resetGraphics(Graphics2D g2d)
	{
		g2d.setFont(defaultFont);
		g2d.setStroke(defaultStroke);
	}

	public UI(Manager m)
	{
		this.m = m;
		in = new Input(this);
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setBackground(Color.LIGHT_GRAY);
		setFocusable(true);
		addMouseMotionListener(in);
		addKeyListener(in);
		addMouseListener(in);
		t.start();
		init();
	}



	public static class UIWindow extends JFrame
	{
		public UIWindow(Manager m)
		{
			UI u = new UI(m);

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setTitle("UI");

			add(u);
			pack();

			addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					m.closeFunction();
				}
			});

			setVisible(true);
		}
	}






}
