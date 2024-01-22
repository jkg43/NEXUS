package fourier;

import manager.Module;
import uiComponents.CycleGraph;

public class FourierTestModule extends Module {

    CycleGraph dataGraph;
    FourierGraph fourierGraph;

    //l - number of samples in time domain
    //n - number of samples in freq domain
    int L = 200, N = 200;


    public FourierTestModule(String name) {
        super(name, "/fourier/");

        dataGraph = new CycleGraph(100,50,500,200,L,-3,3);
        components.add(dataGraph);

        fourierGraph = new FourierGraph(100,300,500,200,N,1.0/ui.FPS);
        components.add(fourierGraph);

    }


    @Override
    public void update() {

        double t = (double) System.currentTimeMillis() / 500;

        dataGraph.addValue(Math.sin(2*Math.PI*t)+Math.sin(5*t)+Math.cos(2*Math.PI*t/3));

        fourierGraph.fourierUpdate(dataGraph.getValues());

    }






}
