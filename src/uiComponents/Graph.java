package uiComponents;

import java.awt.*;

public abstract class Graph extends UIComponent {



    public abstract double[] getValues();

    public final int N;
    protected double minValue, maxValue;

    public Graph(int x, int y, int w, int h,int N, double min, double max) {
        super();
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.N = N;
        this.minValue = min;
        this.maxValue = max;
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

        double[] values = getValues();


        float dx = (float) width / N;

        float prevX = x;
        double prevY = getY(values[0]);

        for (int i = 1; i < N; i++) {
            float newX = prevX + dx;
            double newY = getY(values[i]);

            g2d.drawLine((int) prevX, (int) prevY, (int) newX, (int) newY);

            prevX = newX;
            prevY = newY;
        }
    }

    private double getY(double val) {
        return y + height - height * (val-minValue) / (maxValue - minValue);
    }

    @Override
    public void checkMouseAndActivate(int mx, int my, int button) {

    }

}
