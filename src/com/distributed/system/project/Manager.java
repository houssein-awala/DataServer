package com.distributed.system.project;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/*
*AUTHOR: HUSSEIN AWALA
*this class is the manager of the sensors registered in the data server
* it exist a connection with the supervisor for knowing the dead sensor
* it has a killer thread for kill a sensor thread the serve a dead real sensor
 */

public class Manager extends Thread{
    private final int portWithSupervisor=9999;
    private HashMap<String,Sensor> sensors;
    private ServerSocket socket;

    public Manager() {
        sensors=new HashMap<>();
    }

    //this method is to add a new sensor and to kill a one
    public synchronized void registerOrKill(Sensor sensor,String id) {
        if (sensor==null){
            Sensor sensorToKill=sensors.get(id);
            if (sensorToKill!=null){
                sensorToKill.kill(-9);
                sensors.remove(id);
            }
        }
        else
            sensors.put(id,sensor);
    }

    //this method is to run the thread and create killer after each request from supervisor
    @Override
    public void run() {
        try {
            socket=new ServerSocket(portWithSupervisor);
            while (true){
                Socket connection=socket.accept();
                new Killer(this,connection).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //this method is to send the kill commend to the registerOrKill method
     public synchronized void kill(String id){
        registerOrKill(null,id);
     }
}
