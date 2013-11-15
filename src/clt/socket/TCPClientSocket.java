/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clt.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InterfaceAddress;

import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;

/**
 *
 * @author 7000005
 */
public class TCPClientSocket extends ConnectAdapter implements Runnable {   //NetworkEventSource

    // <editor-fold defaultstate="collapsed" desc="Variable">
    private final int BufferSize = 1024;
    private final int NoDataDelay = 100;
    private final int DataDelay = 10;
    public byte[] buffer = new byte[BufferSize];
    //public Queue<byte[]> receiverQe = new LinkedList<>();
    Queue<byte[]> requestQe = new LinkedList<>();
    private InputStream sockInput = null;
    private boolean isThreadRun = false;
    private boolean isSocketOpen = false;
    private String address = "127.0.0.1";// 連線的ip
    private int port = 8899;// 連線的port
    Socket client;
    private PrintStream sockOutput = null;
    // Thread bkcurrentThread;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Event">
    protected void SetfireNetwork_messageEvent(Object source, int len) {
        fireNetwork_messageEvent(new SocketMessageEvent(source, len));
    }
    protected EventListenerList listenerList = new EventListenerList();

    @Override
    public synchronized void addListener(SocketEventListener l) {
        listenerList.add(SocketEventListener.class, l);
    }

    @Override
    public synchronized void removeListener(SocketEventListener l) {
        listenerList.remove(SocketEventListener.class, l);
    }

    @Override
    public synchronized void fireNetwork_messageEvent(SocketMessageEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == SocketEventListener.class) {
                ((SocketEventListener) listeners[i + 1]).SocketEventReceived(evt);
            }
        }
    }

    @Override
    public synchronized void socketMessage(Object o, Object msg) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == SocketEventListener.class) {
                ((SocketEventListener) listeners[i + 1]).SocketMessage(o, msg);
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Overide ConnectAdapter">
    @Override
    public synchronized boolean sendData(String queryData) {
        boolean res = false;
        try {
            res = sendData(queryData.getBytes(queryData));

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TCPClientSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    @Override
    public synchronized boolean sendData(byte[] queryData) {
        requestQe.add(queryData);
        return true;
    }

    @Override
    public synchronized boolean isThreadRunning() {
        return this.isThreadRun;
    }

    @Override
    public synchronized boolean isConnected() {
        return this.isSocketOpen;
    }

    @Override
    public void run() {
        byte[] header = new byte[3];
        int rceBytes = 0;
        int delayMs = NoDataDelay;
        int size = 0;
        // bkcurrentThread = this;
        while (!this.isThreadRunning() || !client.isClosed()) {
            try {
                Thread.sleep(1);
                rceBytes = sockInput.available();
                if (rceBytes > 0) {
                    
                    if (rceBytes > BufferSize) {
                        buffer = new byte[BufferSize];
                        delayMs = DataDelay;
                    } else {
                        buffer = new byte[rceBytes];
                        delayMs = NoDataDelay;
                    }

                    size = sockInput.read(buffer, 0, buffer.length);
                    printDebug("Receive",buffer, buffer.length);
                    if (size == -1) {   //連線中斷
                        this.isSocketOpen = false;
                    }
                    receiverQe.offer(buffer);//new String(buf, 0, bytes_read)); 
                    SetfireNetwork_messageEvent(this, 0);
                }
                SendDataAsync();
                Thread.sleep(delayMs);
            } catch (IOException | InterruptedException e) {
                socketMessage(this, "Send Ethernet data error: " + e.getMessage());
                //e.printStackTrace(System.err);
                break;
            }
        }

        try {
            // System.err.println("SimpleHandler:Closing socket.");
            client.close();
        } catch (Exception e) {
            // System.err.println("SimpleHandler: Exception while closing socket, e=" + e);
            e.printStackTrace(System.err);
        }

    }

    void SendDataAsync() {
        while (!requestQe.isEmpty()) {
            try {
                if (client == null || client.isClosed() == true) {
                    this.isSocketOpen = false;
                    break;
                }
                byte[] bs = requestQe.poll();
                sockOutput.write(bs);
                printDebug("Send   ",bs, bs.length);

            } catch (IOException e) {
                socketMessage(this, "Send Ethernet data error: " + e.getMessage());
            }
        }
    }
    /*backup ess received data 
     public void run() {
     byte[] buf;//= new byte[1024];
     byte[] header = new byte[3];
     int bytes_read = 0;
     // bkcurrentThread = this;
     while (!this.isThreadRunning()) {
     try {
     if (sockInput.available() >= 6) {
     Thread.sleep(1);
     bytes_read = sockInput.available();
     if (bytes_read > 3) {
     sockInput.read(header, 0, 3);
     if (header[0] == (byte) 0xff && header[1] == (byte) 0xeb) {
     bytes_read = header[2];
     buf = new byte[bytes_read + 3];
     bytes_read = sockInput.read(buf, 3, bytes_read);


     } else {
     buf = new byte[bytes_read];
     bytes_read = sockInput.read(buf, 3, bytes_read - 3);
     }
     buf[0] = header[0];
     buf[1] = header[1];
     buf[2] = header[2];
     receiverQe.offer(buf);//new String(buf, 0, bytes_read));
     printDebug(buf, bytes_read);
     // SetfireNetwork_messageEvent(this, 0);
     }
     }
     SendDataAsync();
     } catch (IOException | InterruptedException e) {
     e.printStackTrace(System.err);
     break;
     }
     }

     try {
     // System.err.println("SimpleHandler:Closing socket.");
     client.close();
     } catch (Exception e) {
     // System.err.println("SimpleHandler: Exception while closing socket, e=" + e);
     e.printStackTrace(System.err);
     }

     }*/

    @Override
    public synchronized void stop() {
        this.isThreadRun = true;
        // bkcurrentThread = null;
        try {
            if (client == null) {
                return;
            }
            client.shutdownInput();
            client.shutdownOutput();
            this.client.close();
            client = null;
            this.isSocketOpen = false;
        } catch (IOException e) {
            socketMessage(this, "Stop Ethernet error: " + e.getMessage());
            //e.printStackTrace();
            //  throw new RuntimeException("Error closing server", e);
        }
    }
    // </editor-fold>

    public TCPClientSocket(String ServerName, int Serverport) throws IOException {
        this.address = ServerName;
        this.port = Serverport;
        client = new Socket(this.address, this.port);
        client.setKeepAlive(true);    // Allow to keep connection does'net send any data max two hours.  
        SocketAddress socketAddress = client.getRemoteSocketAddress();

        sockInput = client.getInputStream();
        sockOutput = new PrintStream(client.getOutputStream());
        // client.connect(socketAddress, 2000);
        receiverQe.clear();
        requestQe.clear();
        this.isSocketOpen = true;
    }

    public void ScanLan() {
        try {
            SearchNet sn = new SearchNet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void printDebug(String Title,byte[] bs, int len) {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String s = sdf.format(date) +" " +address+" "+Title+": ";
        for (int i = 0; i < len; i++) {
            s += "0x" + Integer.toHexString((int) bs[i] & 0xff) + ",";
        }
        s += "\r\n";
        System.out.print(s);
    }
}
