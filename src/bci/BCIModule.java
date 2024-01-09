package bci;

import manager.Module;
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

    boolean running = false;


    public BCIModule(String name) {
        super(name, "/bci/");

        btData = new BluetoothStreamDataThread();

        components.add(new TextButton(100,100,290,40,"Connect Device",Color.BLUE,Color.BLUE.darker(),
            Color.BLACK,(x)->{
                if(!btData.isAlive()) {
                    btData = new BluetoothStreamDataThread();
                    running = true;
                    btData.start();
                }
        }));

        components.add(new TextButton(500,100,180,40,"Stop Data", Color.RED,Color.RED.darker(),Color.BLACK,
            (x)-> running = false));




    }



    @Override
    public void update() {
        super.update();
    }



    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
    }

    public class BluetoothStreamDataThread extends Thread {

        final byte SYNC = (byte) 0xAA;

        InputStream input = null;

        public void run() {

            try {

                String deviceAddress = "btspp://c464e3e6e013:1";


                StreamConnection connection = (StreamConnection) Connector.open(deviceAddress);

                if(connection==null) {
                    System.out.println("Could not connect to the device");
                    return;
                }

                input = connection.openInputStream();


                int[] payload = new int[256];

                while(running) {
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
                                    System.out.println("Match");
                                } else {
                                    System.out.println("Checksum "+checksum+ " doesn't match target checksum "+targetChecksum);
                                }
                                System.out.print("Data Packet: ");
                                for (int i = 0; i < pLength; i++) {
                                    System.out.print(payload[i]+" ");
                                }
                                System.out.println();

                                //TODO parse data
                            }
                        }
                    }
                }


            }
            catch(ConnectionNotFoundException c) {
                System.out.println("Could not connect to the device");
            }
            catch(BluetoothConnectionException b) {
                System.out.println("Connection request timed out");
            }
            catch(Exception e) {
                e.printStackTrace();
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
            return (byte) input.read();
        }


    }

}
