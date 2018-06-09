package com.distributed.system.project;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/*
*AUTHOR: HUSSEIN AWALA
*this thread has 1 function that kill a sensor thread
 */

public class Killer extends Thread{
    Manager manager;
    Socket socket;

    public Killer(Manager manager,Socket socket) {
        this.manager=manager;
        this.socket = socket;
    }

    //this method kill the sensor thread using the ID of this sensor
    @Override
    public void run() {
        try {
            Scanner scanner=new Scanner(socket.getInputStream());
            String id=scanner.nextLine();
            manager.kill(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
