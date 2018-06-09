package com.distributed.system.project;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/*
*AUTHOR: HUSSEIN AWALA
*the sensor class is a thread that interact with the real sensor
* this class is run with 2 method
* the 1st is to send data surely delivered for the 1st time
* the 2nd is to send data every T of time with UPD packet
* this class take a tcp socket as argument
* this class read the Request object from the socket
* then create a Compiler to generate the variables for serve the real sensor
 */

public class Sensor extends Thread{
    private String ID;
    private Socket tcpSocket;
    private InetAddress adr;
    private DatagramPacket packet;
    private DatagramSocket socket;
    private Request request;
    private boolean alive;
    Compiler compiler;

    public Sensor(Socket tcpSocket) {
        this.tcpSocket = tcpSocket;
        this.alive=true;
    }

    //this function is a template describe the 2 basics steps to serve a sensor
    private final void serve() throws IOException, ClassNotFoundException, InterruptedException {
        initialize();
        simulate();
    }

    //this is the 1st step, the connection is TCP, and the data is delivered surely
    public void initialize() throws IOException, ClassNotFoundException {
        adr=tcpSocket.getInetAddress();
        ObjectInputStream in=new ObjectInputStream(tcpSocket.getInputStream());
        request=(Request) in.readObject();
        ID=request.getID();
        compiler=new Compiler(request.getVariales());
        OutputStreamWriter out=new OutputStreamWriter(tcpSocket.getOutputStream());
        out.write(compiler.compile());
        out.flush();
        out.close();
        if (in.readBoolean())
            tcpSocket.close();
    }

    //this is the 2nd step, this thred send the data with UDP connection
    public void simulate() throws InterruptedException, IOException {
        socket=new DatagramSocket();
        while (kill(0))
        {
            DatagramPacket packet;
            Thread.sleep(request.getTau()*1000);
            byte[] data= compiler.compile().getBytes();
            packet=new DatagramPacket(data,data.length,adr,request.getPort());
            socket.send(packet);
        }
    }

    //this function is used to check the state of the sensor and to change this state
    public synchronized boolean kill(int mode){
        if (mode==-9)
            alive=false;
        return alive;
    }

    //getter for ID
    public String getID(){
        return ID;
    }

    //this method is the call of serve method
    @Override
    public void run(){
        try {
            serve();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
