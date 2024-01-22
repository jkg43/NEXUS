package uiComponents;

import uiComponents.UIComponent;

import java.awt.*;

public class DataBar extends UIComponent {

    int value, maxValue;
    Color color, highlightColor;

    String name;

    final int MARGIN = 4;

    boolean showAsPercent;

    public DataBar(int x, int y, int width, int height, String name, Color color, int initialValue,
                   int maxValue,boolean showAsPercent) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color.darker();
        this.maxValue = maxValue;
        this.value = initialValue;
        this.name = name;
        highlightColor = color;
        this.showAsPercent = showAsPercent;
    }

    public DataBar(int x, int y, int width, int height, String name, Color color, int initialValue,int maxValue) {
        this(x,y,width,height,name,color,initialValue,maxValue,true);
    }


        public void updateValue(int newValue) {
        value = newValue;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(highlightColor);
        g2d.fillRect(x,y,width,height);
        g2d.setColor(color);
        float percentFilled = (float)value / maxValue;
        int barHeight = (int)((height - MARGIN * 2)*percentFilled);

        g2d.fillRect(x+MARGIN,y+height-MARGIN-barHeight,width-MARGIN*2,barHeight);

        g2d.setColor(highlightColor);
        if(showAsPercent) {
            String percentString = String.format("%.1f",percentFilled*100.0f)+"%";
            int percentWidth = g2d.getFontMetrics().stringWidth(percentString);
            g2d.drawString(percentString,x+width/2-percentWidth/2,y+height+30);
        } else {
            int valWidth = g2d.getFontMetrics().stringWidth(""+value);
            g2d.drawString(""+value,x+width/2-valWidth/2,y+height+30);
        }


        int nameWidth = g2d.getFontMetrics().stringWidth(name);

        g2d.drawString(name,x+width/2-nameWidth/2,y-10);
    }

    @Override
    public void checkMouseAndActivate(int mx, int my, int button) {

    }
}
