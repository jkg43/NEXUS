package uiComponents;

import java.awt.*;

public class CycleGraph extends Graph {


    double[] values;

    int currentValue;
    double maxValue, minValue;

    private static final int LABEL_X_OFFSET = 5,LABEL_Y_OFFSET = 10;

    public CycleGraph(int x, int y, int width, int height, int n, double min, double max) {
        super(x,y,width,height,n,min,max);
        values = new double[N];
        this.currentValue = 0;
    }

    public void addValue(double val) {
        values[currentValue] = val;
        currentValue++;
        if(currentValue >= N) {
            currentValue = 0;
        }
    }

    @Override
    public double[] getValues() {
        double[] shifted = new double[N];
        for (int i = 0; i < N; i++) {
            shifted[i] = values[(i+currentValue)%N];
        }
        return shifted;
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        g2d.setColor(Color.BLACK);
        g2d.drawString(""+maxValue,x+width+LABEL_X_OFFSET,y+LABEL_Y_OFFSET);
        g2d.drawString(""+minValue,x+width+LABEL_X_OFFSET,y+height+LABEL_Y_OFFSET);
    }


}
