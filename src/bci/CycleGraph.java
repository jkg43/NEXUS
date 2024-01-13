package bci;

import uiComponents.UIComponent;

import java.awt.*;

public class CycleGraph extends UIComponent {

    final int N;

    int[] values;

    int currentValue, maxValue, minValue;

    private static final int LABEL_X_OFFSET = 5,LABEL_Y_OFFSET = 10;

    public CycleGraph(int x, int y, int width, int height, int n, int max, int min) {
        super();
        this.N = n;
        values = new int[N];
        this.maxValue = max;
        this.minValue = min;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.currentValue = 0;
    }

    public void addValue(int val) {
        values[currentValue] = val;
        currentValue++;
        if(currentValue >= N) {
            currentValue = 0;
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g2d) {

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));

        g2d.drawRect(x,y,width,height);

        g2d.setColor(new Color(208, 137, 12));


        float dx = (float) width / N;

        float prevX = x;
        int prevY = getY(values[currentValue]);

        for (int i = 0; i < N; i++) {
            float newX = prevX + dx;
            int newY = getY(values[(i+currentValue)%N]);

            g2d.drawLine((int) prevX,prevY, (int) newX,newY);

            prevX = newX;
            prevY = newY;
        }


        g2d.setColor(Color.BLACK);
        g2d.drawString(""+maxValue,x+width+LABEL_X_OFFSET,y+LABEL_Y_OFFSET);
        g2d.drawString(""+minValue,x+width+LABEL_X_OFFSET,y+height+LABEL_Y_OFFSET);
    }

    private int getY(int val) {
        return y + height - height * (val-minValue) / (maxValue - minValue);
    }

    @Override
    public void checkMouseAndActivate(int mx, int my, int button) {

    }
}
