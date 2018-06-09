package com.distributed.system.project;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

/*
*AUTHOR: HUSSEIN AWALA
*this is the main of the data server
* it create a manager and run it
* then wait connections from the sensors
* after each connection it create a sensor thread for serve a real sensor
 */

public class Main {
    static private final int portWithSensors=1234;

    public static void main(String[] args) throws IOException {
        Manager manager=new Manager();
        manager.start();
        ServerSocket socket=new ServerSocket(portWithSensors);
        while (true){
            Socket connection=socket.accept();
            new Sensor(connection).start();
        }
    }
}
