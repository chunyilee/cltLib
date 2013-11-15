/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clt.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author 7000005
 */
public class TCPServerSocket implements Runnable {

    private int serverPort = 0;
    private ServerSocket serverSock = null;
    private Socket sock = null;
    private InputStream sockInput = null;
    private OutputStream sockOutput = null;
    private boolean isStopped = false;
    public Queue<byte[]> InputQe = new LinkedList<byte[]>();

    public TCPServerSocket(int Serverport) throws IOException {
        this.serverPort = Serverport;
        serverSock = new ServerSocket(serverPort);

        InputQe.clear();
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSock.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    @Override
    public void run() {
        byte[] buf = new byte[1024];
        int bytes_read = 0;
        try {
             //synchronized (serverSock) {
                sock = serverSock.accept();
            //}
            sockInput = sock.getInputStream();
            sockOutput = sock.getOutputStream();
            while (!isStopped()) {
                try {
                    if (sockInput.available() > 6) {
                        Thread.sleep(5);
                        bytes_read = sockInput.available();
                        if (bytes_read > 0) {
                            bytes_read = sockInput.read(buf, 0, bytes_read);
                        }
                        InputQe.offer(buf);//new String(buf, 0, bytes_read));
                        sockOutput.write(buf, 0, bytes_read);
                        sockOutput.flush();
                    }
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
            //break;
        }
        try {
            System.err.println("SimpleHandler:Closing socket.");
            sock.close();
        } catch (Exception e) {
            System.err.println("SimpleHandler: Exception while closing socket, e=" + e);
            e.printStackTrace(System.err);
        }

    }
}
