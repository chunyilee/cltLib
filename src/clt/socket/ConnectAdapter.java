/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clt.socket;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author 7000006
 */
public abstract class ConnectAdapter extends SocketEventSource {

    public Queue<byte[]> receiverQe = new LinkedList<>();

    public abstract boolean sendData(String queryData);
    
    public abstract boolean sendData(byte [] queryData);
    
    public abstract void stop();

    public abstract void run();
      
    public  abstract  boolean isThreadRunning();
    
    public abstract boolean isConnected();
}