package ui;

import bci.BCIModule;
import fourier.FourierTestModule;
import manager.CameraModule2D;
import manager.Manager;
import modules.ChecklistModule;
import modules.TreeGraphModule;
import uiComponents.ContextMenu;
import uiComponents.ModuleSelector;
import uiComponents.UIComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class UI extends JPanel implements ActionListener
{
	public int HEIGHT=600,WIDTH=800;
	public boolean drawDebug = false;

	public final Manager m;



	public ArrayList<UIComponent> globalComponents;

	public UIComponent selectedComponent = null;

	public ModuleSelector selector;

	public ContextMenu currentContextMenu = null;


	private void init()
	{
		globalComponents = new ArrayList<>();

		ChecklistModule checklistMod = new ChecklistModule( "Checklist");
		TreeGraphModule treeMod = new TreeGraphModule( "Tree Graph");
		BCIModule bciMod = new BCIModule("BCI");
		FourierTestModule fourierMod = new FourierTestModule("Fourier Test");


		m.modules.add(checklistMod);
		m.modules.add(treeMod);
		m.modules.add(bciMod);
		m.modules.add(fourierMod);
		m.currentModule = fourierMod;

		selector = new ModuleSelector();
		globalComponents.add(selector);
	}




	private void update()
	{
		for(UIComponent c : globalComponents)
		{
			c.update();
		}

		m.currentModule.update();

		if(currentContextMenu!=null) {
			currentContextMenu.update();
		}

	}


	public boolean contextMenuHovered() {
		if(currentContextMenu==null) {
			return false;
		}
		return currentContextMenu.menuHovered();
	}


	private void draw(Graphics2D g2d)
	{
		m.currentModule.draw(g2d);

		if(currentContextMenu!=null) {
			currentContextMenu.draw(g2d);
		}

		for(UIComponent c : globalComponents)
		{
			if(!c.isHidden)
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
			"SC: "+(selectedComponent!=null),
			"HC: "+(m.currentModule instanceof CameraModule2D cam ? cam.hoveredComponent!=null: "N/A"),
			"CH: "+contextMenuHovered()
		};

		int yPos = HEIGHT-10;
		for(String s : debugInfo)
		{
			g2d.drawString(s, 10, yPos);
			yPos -= 30;
		}
	}
































	public final int FPS = 20;

	Font defaultFont = new Font("arial", Font.PLAIN, 30);
	BasicStroke defaultStroke = new BasicStroke(1);
	Timer t = new Timer(1000/FPS, this);
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
		Manager.ui = this;
		in = new Input(this);
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setBackground(Color.LIGHT_GRAY);
		setFocusable(true);
		addMouseMotionListener(in);
		addKeyListener(in);
		addMouseListener(in);
		addMouseWheelListener(in);
		t.start();
		init();
	}



	public static class UIWindow extends JFrame
	{
		public UIWindow(Manager m)
		{
			UI u = new UI(m);

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setTitle("NEXUS");

			add(u);
			pack();

			addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					m.closeFunction();
				}
			});

			addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent e) {
					super.componentResized(e);
					u.WIDTH = e.getComponent().getWidth();
					u.HEIGHT = e.getComponent().getHeight();
					System.out.println(u.WIDTH+", "+u.HEIGHT);
				}
			});



			setVisible(true);
		}
	}






}
