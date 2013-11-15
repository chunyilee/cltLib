/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clt.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 7000005
 */
public class UDPClientSocket implements Runnable {

    private boolean isstopped = false;
    static protected MulticastSocket socket = null;
    protected BufferedReader in = null;
    public InetAddress group;
    int port = 4567;
    String ServerAddress;
    //Thread currentThread;
    public Queue<String> UDPSreverQe = new LinkedList<String>();
    public Queue<byte[]> UDPSreverQe2 = new LinkedList<byte[]>();

    public UDPClientSocket(String ServerIP, int port) throws IOException {
        try {
            this.port = port;
            ServerAddress = ServerIP;
            UDPSreverQe2.clear();
            socket = new MulticastSocket(this.port);
            group = InetAddress.getByName(ServerAddress);//InetAddress.getByName ("192.168.1.255"); //
           // socket.joinGroup(group);
            socket.setLoopbackMode(true);
        } catch (IOException e) {
            System.out.print(e.getMessage()+"\n");
        }
    }
    final byte UDP_Header = (byte) 0xd0;
    final byte Header_UDP_ACK = (byte) 0xd1;
    final byte TAG_CMD = (byte) 0xff;
    final byte CMD_DISCOVER_TARGET1 = (byte) 0xaa;
    final byte CMD_DISCOVER_TARGET2 = (byte) 0x55;
    final int MOD_NAME_LEN = 0x20;

    public void FinderSend() {
        byte[] bs = new byte[]{UDP_Header, TAG_CMD, CMD_DISCOVER_TARGET1, CMD_DISCOVER_TARGET2, (byte) 0x01, (byte) 0xa0, (byte) 0x00};
        for (int i = 0; i < bs.length - 1; i++) {
            bs[bs.length - 1] ^= bs[i];
        }
        DatagramPacket dgp = new DatagramPacket(bs, bs.length, group, port);
        // dgp.setData(bs);
        // dgp.setLength(bs.length);
        try {
            socket.send(dgp);
        } catch (IOException e) {
            //System.exit(-1);
        }
    }

    boolean UDPSend(byte[] bs) {
        try {
            DatagramPacket pkt = new DatagramPacket(bs, bs.length);
            //pkt = new DatagramPacket(bs, bs.length(), group, this.port);
            socket.send(pkt);
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    public boolean UDPSend(String s) {
        return UDPSend(s.getBytes());
    }

    public void stop() {
         //  
            isstopped = true;
           // currentThread = null;
           if(socket!=null) 
           {
            //try {
                //socket.leaveGroup(group);
                socket.close();
                socket=null;
           // } catch (IOException ex) {
              //  Logger.getLogger(UDPSocket.class.getName()).log(Level.SEVERE, null, ex);
           // }
           }
       
    }

    @Override
    public void run() {
        //currentThread = this.currentThread;
        while (!isstopped) {
            try {
                byte[] buf = new byte[256];
                DatagramPacket pkt = new DatagramPacket(buf, buf.length);
                socket.receive(pkt);
                if (pkt.getLength() > 0) {
                    byte[] bs = pkt.getData();
                    if (bs[0] == Header_UDP_ACK) {
                        UDPSreverQe2.offer(bs);

                        String received = new String(bs, 0, bs.length);
                        UDPSreverQe.offer(received);
                    }
                }
                Thread.sleep(10);
            } catch (IOException e) {
                System.out.println("Error:" + e);
            } catch (InterruptedException e) {
                System.out.println("Error:" + e);
            }
        }
    }
}
