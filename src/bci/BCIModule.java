package bci;

import fourier.FourierGraph;
import manager.Module;
import uiComponents.CycleGraph;
import uiComponents.DataBar;
import uiComponents.TextButton;

import javax.bluetooth.BluetoothConnectionException;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;


public class BCIModule extends Module {

    BluetoothStreamDataThread btData;

    Integer rawWaveValue = null, attention = null, meditation = null, signalStrength = null;

    Integer[] asicEEGData = new Integer[8];

    boolean running = false;
    final static int TIMEOUT_MS = 5000;
    long connectionStartTime;

    DataBar signalBar, attentionBar, meditationBar;

    DataBar[] asicDataBars = new DataBar[8];

    String[] asicNames = {
        "Delta", "Theta", "Low Alpha", "High Alpha",
        "Low Beta", "High Beta", "Low Gamma", "Mid Gamma"
    };

    Color[] asicColors = {
        new Color(218, 121, 121),
        new Color(229, 171, 97),
        new Color(234, 226, 116),
        new Color(142, 241, 111),
        new Color(102, 245, 187),
        new Color(128, 185, 255),
        new Color(180, 111, 236),
        new Color(236, 93, 228)
    };

    CycleGraph rawWaveGraph;

    FourierGraph fourierGraph;


    public BCIModule(String name) {
        super(name, "/bci/");

        btData = new BluetoothStreamDataThread();

        components.add(new TextButton(100,100,290,40,"Connect Device",Color.BLUE,Color.BLUE.darker(),
            Color.BLACK,(x)->{
                if(!btData.isAlive()) {
                    System.out.println("Attempting to connect to device");
                    btData = new BluetoothStreamDataThread();
                    running = true;
                    btData.start();
                }
        }));

        components.add(new TextButton(500,100,200,40,"Disconnect", Color.RED,Color.RED.darker(),Color.BLACK,
            (x)-> disconnect()));




        signalBar = new DataBar(50,200,50,200,"Signal",
            new Color(232, 221, 17),0,200);

        attentionBar = new DataBar(200,200,50,200,"Attention",
            new Color(229, 114, 20),0,100);

        meditationBar = new DataBar(350,200,50,200,"Meditation",
            new Color(3, 163, 204),0,100);

        components.add(signalBar);
        components.add(attentionBar);
        components.add(meditationBar);

        for (int i = 0; i < 8; i++) {
            asicDataBars[i] = new DataBar(50+170*i,500,40,100,asicNames[i],
                asicColors[i],0,0xFFFFFF,false);
            components.add(asicDataBars[i]);
            asicDataBars[i].isHidden = true;
        }

        int N = 512;

        rawWaveGraph = new CycleGraph(500,150,800,250,N,-2048,2048);

        components.add(rawWaveGraph);

        fourierGraph = new FourierGraph(500,500,800,250,N,1.0/512);

        components.add(fourierGraph);


    }



    @Override
    public void update() {
        super.update();
        if(signalStrength!=null) {
            signalBar.updateValue(200-signalStrength);
        }
        if(meditation!=null) {
            meditationBar.updateValue(meditation);
        }
        if(attention!=null) {
            attentionBar.updateValue(attention);
        }
        for (int i = 0; i < 8; i++) {
            if(asicEEGData[i]!=null) {
                asicDataBars[i].updateValue(asicEEGData[i]);
            }
        }

        fourierGraph.fourierUpdate(rawWaveGraph.getValues());

        if(btData.connection != null && System.currentTimeMillis() - connectionStartTime > TIMEOUT_MS) {
            System.out.println("No data received before timeout");
            disconnect();
        }
    }



    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);

//        g2d.setColor(Color.BLACK);
//        g2d.drawString("Signal   Quality: "+(signalStrength==null ? "ND" : signalStrength),50,30);
//        g2d.drawString("Raw Wave Value: "+(rawWaveValue==null ? "ND" : rawWaveValue),50,250);
//        g2d.drawString("Attention: "+(attention==null ? "ND" : attention),50,300);
//        g2d.drawString("Meditation: "+(meditation==null ? "ND" : meditation),50,350);
//        StringBuilder asicEEG = new StringBuilder("ASIC EEG Data: ");
//        for (int i = 0; i < 8; i++) {
//            asicEEG.append(asicEEGData[i] == null ? "ND" : asicEEGData[i]).append(", ");
//        }
//        asicEEG.delete(asicEEG.length()-2,asicEEG.length());
//        g2d.drawString(asicEEG.toString(),50,400);
    }

    public void disconnect() {
        btData.disconnect();
    }

    public class BluetoothStreamDataThread extends Thread {

        final int SYNC = 0xAA;
        final int EXCODE = 0x55;
        final int POOR_SIGNAL = 0x02;
        final int ATTENTION = 0x04;
        final int MEDITATION = 0x05;
        final int RAW_8BIT = 0x06;
        final int RAW_MARKER = 0x07;
        final int RAW_WAVE_VALUE = 0x80; //len 2
        final int EEG_POWER = 0x81; //len 32
        final int ASIC_EEG_POWER = 0x83; //len 24
        final int RR_INTERVAL = 0x86; //len 2

        StreamConnection connection = null;




        InputStream input = null;

        public void run() {
            String deviceAddress = "btspp://c464e3e6e013:1";

            try {

                connection = (StreamConnection) Connector.open(deviceAddress);

                if(connection==null) {
                    System.out.println("Could not connect to the device");
                    return;
                } else {
                    System.out.println("Connection successful");
                }

                connectionStartTime = System.currentTimeMillis();

                input = connection.openInputStream();


                int[] payload = new int[256];

                while(running) {
                    connectionStartTime = System.currentTimeMillis();
                    int data = read();

                    if(data == SYNC) {
                        int sync2 = read();
                        if(sync2 == SYNC) {
                            int pLength = getPLength();
                            if(pLength <= 170) {
                                int checksum = 0;
                                for (int i = 0; i < pLength; i++) {
                                    payload[i] = read();
                                    checksum += payload[i];
                                }
                                checksum &= 0xFF;
                                checksum = ~checksum & 0xFF;
                                int targetChecksum = read();
                                if(targetChecksum < 0) {
                                    targetChecksum += 256;
                                }
                                if(checksum == targetChecksum) {
//                                    System.out.println("Match");
                                    int i = 0;
                                    while(i<pLength) {
                                        int excodeCount = 0;
                                        int byteData = 0;
                                        int[] arrData = new int[256];
                                        while(payload[i]==EXCODE) {
                                            excodeCount++;
                                            i++;
                                        }
                                        int code = payload[i];
                                        i++;
                                        if(code >= 0x80) {
                                            int vLength = payload[i];
                                            i++;
                                            for (int j = 0; j < vLength; j++) {
                                                arrData[j] = payload[i];
                                                i++;
                                            }
                                        } else {
                                            byteData = payload[i];
                                            i++;
                                        }
                                        if(excodeCount==0) {

                                            switch (code) {
                                                case POOR_SIGNAL:
//                                                    System.out.println("Poor signal: "+byteData);
                                                    signalStrength = byteData;
                                                    break;
                                                case ATTENTION:
//                                                    System.out.println("Attention: "+byteData);
                                                    attention = byteData;
                                                    break;
                                                case MEDITATION:
//                                                    System.out.println("Meditation: "+byteData);
                                                    meditation = byteData;
                                                    break;
                                                case RAW_8BIT:
                                                    System.out.println("Raw 8 bit: "+byteData);
                                                    break;
                                                case RAW_MARKER:
                                                    System.out.println("Raw marker");
                                                    break;
                                                case RAW_WAVE_VALUE:
                                                    int[] highLow = new int[2];
                                                    highLow[0] = arrData[0] & 0xFF;
                                                    highLow[1] = arrData[1] & 0xFF;
                                                    int raw = (highLow[0] * 256) + highLow[1];
                                                    if(raw > 32768) {
                                                        raw -= 65536;
                                                    }
//                                                    System.out.println("Raw wave value: "+raw);
                                                    rawWaveValue = raw;
                                                    rawWaveGraph.addValue(raw);
                                                    break;
                                                case EEG_POWER:
                                                    float[] eegData = new float[8];
                                                    for (int j = 0; j < 8; j++) {
                                                        eegData[j] = Float.intBitsToFloat(
                                                            arrData[4*j] ^ arrData[4*j+1]<<8 ^ arrData[4*j+2]<<16
                                                                ^ arrData[4*j+3]<<24 );
                                                    }
                                                    System.out.print("EEG Power: ");
                                                    for (int j = 0; j < 8; j++) {
                                                        System.out.print(eegData[j]+" ");
                                                    }
                                                    System.out.println();
                                                    break;
                                                case ASIC_EEG_POWER:
//                                                    System.out.println("ASIC EEG Power");
                                                    for (int j = 0; j < 8; j++) {
                                                        int value = arrData[3*j+2] & 0xFF;
                                                        value |= (arrData[3*j+1] << 8) & 0xFFFF;
                                                        value |= (arrData[3*j] << 16) & 0xFFFFFF;
                                                        asicEEGData[j] = value;
                                                    }
                                                    break;
                                                case RR_INTERVAL:
                                                    System.out.println("RR Interval");
                                                    break;
                                                default:
                                                    System.out.println("Unexpected code "+code);
                                            }
                                        } else if(excodeCount>=1) {
                                            System.out.println("Excode " + excodeCount + " detected");
                                        }
                                    }
                                } else {
                                    System.out.println("Checksum "+checksum+ " doesn't match target checksum "+targetChecksum);
                                }
//                                System.out.print("Data Packet: ");
//                                for (int i = 0; i < pLength; i++) {
//                                    System.out.print(payload[i]+" ");
//                                }
//                                System.out.println();


                            }
                        }
                    }
                }


            }
            catch(ConnectionNotFoundException c) {
                System.out.println("Could not connect to the device");
            }
            catch(BluetoothConnectionException b) {
                System.out.println("Bluetooth error: "+b.getLocalizedMessage());
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            finally {
                disconnect();
            }

        }

        private int getPLength() throws IOException {
            int pLength = read();
            if(pLength == SYNC) {
                pLength = getPLength();
            }
            return pLength;
        }

        private int read() throws IOException {
            return input.read();
        }

        public void disconnect() {
            if(connection!=null) {
                try {
                    System.out.println("Disconnecting");
                    input.close();
                    connection.close();
                    input = null;
                    connection = null;
                    running = false;
                    Thread.currentThread().interrupt();
                } catch (IOException e) {
                    System.out.println("Could not close the connection");
                }
            }

        }


    }

}
