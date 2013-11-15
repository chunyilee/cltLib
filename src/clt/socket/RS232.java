/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clt.socket;

// <editor-fold defaultstate="collapsed" desc="Import">
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.math.BigInteger;
import java.util.*;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Thread;
import java.text.SimpleDateFormat;
import javax.swing.event.EventListenerList;
// </editor-fold>

/**
 *
 * @author 7000006
 */
public class RS232 extends ConnectAdapter implements Runnable {

    // <editor-fold defaultstate="collapsed" desc="Variables">
    /**
     * @param args the command line arguments
     */
    private final int BufferSize = 1024;
    private final int NoDataDelay = 100;
    private final int DataDelay = 10;
    boolean isThreadRun = true;
    private boolean isSocketOpen = false;
    private byte[] buffer = new byte[BufferSize];
    private int BaudRate = 115200;
    private int DataBit = 8;
    private int StopBits = 1;
    private int Parity = 0;
    private int tail = 0;
    private int numBytes;
    private InputStream in;
    private SerialPort serialPort;
    private String COMName = "COM1";
    // </editor-fold>
    private int avaiableBytes;

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

    public RS232(String portName) throws IOException {
        RS232(portName);
    }

    public RS232(String portName, int baudRate, int dataBit, int stopBits, int parity) throws IOException {

        COMName = portName;
        BaudRate = baudRate;
        DataBit = dataBit;
        StopBits = stopBits;
        Parity = parity;

        RS232(portName);
    }

    private void RS232(String portName) throws IOException {

        try {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

            if (portIdentifier.isCurrentlyOwned()) {
                //   SetOnMessage("Port in use!");
            } else {
                // points who owns the port and connection timeout    
                serialPort = (SerialPort) portIdentifier.open(this.getClass().getName(), 2000);

                // setup connection parameters    
                serialPort.setSerialPortParams(BaudRate, DataBit, StopBits, Parity);
                //115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                // setup serial port writer    
                CommPortSender.setWriterStream(serialPort.getOutputStream());

                // setup serial port reader    
                //comPortReceiver = new CommPortReceiver(serialPort.getInputStream());
                //comPortReceiver.start();
                in = serialPort.getInputStream();
                //run();
                //IsOpen = true;
                this.isSocketOpen = true;

                //new CommPortReceiver(serialPort.getInputStream()).start();

            }
        } catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException | IOException e) {
            socketMessage(this, "Connect RS232 error: " + e.getMessage());
        }

    }

    @Override
    public void run() {
        boolean hr = false;
        int size = 0;
        int rceBytes = 0;
        int delayMs = 10;


        while (isThreadRunning()) {
            try {
                // if stream is not bound in.read() method returns -1 
                rceBytes = in.available();
                if (rceBytes > 0) {
                    if (rceBytes > BufferSize) {
                        buffer = new byte[BufferSize];
                        delayMs = DataDelay;
                    } else {
                        buffer = new byte[rceBytes];
                        delayMs = NoDataDelay;
                    }
                    size = in.read(buffer, 0, buffer.length);
                    printDebug("Receive", buffer);
                    if (size == -1) {   //連線中斷
                        this.isSocketOpen = false;
                    }
                    receiverQe.offer(buffer);
                    SetfireNetwork_messageEvent(this, 0);
                }
                // wait 10ms when stream is broken and check again    
                Thread.sleep(delayMs);
            } catch (IOException | InterruptedException e) {
                socketMessage(this, e.getMessage());
            }
        }
    }

    @Override
    public void stop() {
        try {
            Thread.sleep(1000);   //need to wait sleep 1000
            if (serialPort == null) {
                return;
            }
            this.isThreadRun = false;
            CommPortSender.close();
            this.in.close();
            serialPort.close();
            this.isSocketOpen = false;
            // socketMessage(this, "Connected succuessfully");
        } catch (InterruptedException | IOException e) {
            socketMessage(this, "Stop RS232 error: " + e.getMessage());
            //e.printStackTrace();

        }
    }

    @Override
    public boolean sendData(String sendBytes) {
        return sendData(sendBytes.getBytes());
    }

    @Override
    public boolean sendData(byte[] sendBytes) {
        if (sendBytes.length <= 0) {
            return false;
        }
        try {
            CommPortSender.send(sendBytes);
        } catch (Exception e) {
            socketMessage(this, "Send RS232 data error: " + e.getMessage());
            return false;
        }
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

    public static String[] ListCommPorts() {
        String[] aryCom = null;
        List<String> arr = new ArrayList<String>();
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            //PORT_Serial=1, PORT_PARALLEL=2, PORT_I2C=3, PORT+RS485=4, PORT_RAW=5
            if (portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                arr.add((portIdentifier.getName()));
            }
        }

        aryCom = new String[arr.size()];
        aryCom = arr.toArray(aryCom);

        return aryCom;
    }

    static void printDebug(String title, byte[] bs) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String s = sdf.format(date) + " " + title + ":: ";
        for (int i = 0; i < bs.length; i++) {
            s += "0x" + Integer.toHexString((int) bs[i] & 0xff) + ",";
        }
        s += "\r\n";
        System.out.print(s);
    }

    public static class CommPortSender {

        static OutputStream out;

        public static void setWriterStream(OutputStream out) {
            CommPortSender.out = out;
        }

        public static void close() throws IOException {
            out.close();
        }

        public static void send(byte[] bytes) throws Exception {
//                String s = "";
//                for (byte element : bytes) {
//                    s += Integer.toString((element & 0xff) + 0x100, 16).toUpperCase().substring(1) + " ";
//                }
//                System.out.println("SENDING:  " + s);
            //System.out.println("SENDING: " + new String(bytes, 0, bytes.length));
            // sending through serial port is simply writing into OutputStream    
            printDebug("Send   ", bytes);
            out.write(bytes);
            out.flush();
        }
    }
}