package fourier;

import dataTypes.ComplexCart;
import dataTypes.ComplexExp;
import uiComponents.Graph;

import java.awt.*;
import java.util.Arrays;

public class FourierGraph extends Graph {

    double[] freqValues;

    double dt,fs;

    int numLabels = 6;

    private static final int F_LABEL_X_OFFSET = -25, F_LABEL_Y_OFFSET = 30,
        T_LABEL_X_OFFSET = 5, T_LABEL_Y_OFFSET = 10;

    @Override
    public double[] getValues() {
        return freqValues;
    }

    public FourierGraph(int x, int y, int w, int h, int N, double dt) {
        super(x,y,w,h,N,0,50);
        freqValues = new double[N];
        this.dt = dt;
        fs = 1.0 / dt;
    }


    public void fourierUpdate(double[] timeDomain) {
        int L = timeDomain.length;

        ComplexCart[] frequencies = new ComplexCart[N];

        for (int k = 0; k < N; k++) {
            ComplexCart currentSum = new ComplexCart(0,0);
            for (int n = 0; n < L; n++) {
                currentSum.add(new ComplexExp(timeDomain[n],-1.0*Math.PI/L*k*n).toCartesian());
            }
            frequencies[k] = currentSum;
        }

        for (int i = 0; i < N; i++) {
            freqValues[i] = frequencies[i].modulus();
        }

    }

    @Override
    public void draw(Graphics2D g2d) {
        double currentMax = Arrays.stream(freqValues).max().orElse(0);

        if(currentMax > maxValue) {
            maxValue = currentMax;
        } else if(currentMax < maxValue / 2) {
            maxValue = currentMax * 2;
        }

        super.draw(g2d);
        g2d.setColor(Color.BLACK);

        for (int i = 0; i < numLabels; i++) {
            double freq = fs*i/(numLabels-1)/4;
            int labelX = x+width*i/(numLabels-1);
            g2d.drawString(String.format("%.1f",freq),labelX + F_LABEL_X_OFFSET,
                y+height+ F_LABEL_Y_OFFSET);
            g2d.drawLine(labelX,y+height,labelX,y+height+5);
        }

        g2d.drawString(String.format("%.0f",maxValue),x+width+ T_LABEL_X_OFFSET,y+ T_LABEL_Y_OFFSET);

    }


}
