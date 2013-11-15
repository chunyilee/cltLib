/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clt.socket;
import java.net.*;
import java.nio.channels.*;
import java.nio.*;
import java.io.*;
import java.nio.charset.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClientNIO extends ConnectAdapter implements Runnable{
    
    private String address = "127.0.0.1";// 連線的ip
    private int port = 8899;// 連線的port
    Queue<byte[]> requestQe = new LinkedList<>();
        
    private SocketChannel socketChannel = null;
    private ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);
    private Charset charset = Charset.forName("SJIS");
    private Selector selector;

    public TCPClientNIO() throws IOException {
    socketChannel = SocketChannel.open();
     InetAddress ia = InetAddress.getByName(address);
    InetSocketAddress isa = new InetSocketAddress(ia, port);
    
    socketChannel.connect(isa);
    socketChannel.configureBlocking(false);
    System.out.println("Sucessfully connected with Server");
    selector = Selector.open();
    
    }

    public void keepConnection() {
    String msg = "C:HB";

    while (true) {
        try {
        Thread.sleep(1000);
        } catch (InterruptedException e) {
        e.printStackTrace();
        }
        synchronized (sendBuffer) {
        sendBuffer.put(encode(msg + " "));
        }
        System.out.println("time: "+System.currentTimeMillis() +" <== C:HB");
        if (msg.equals("bye"))
        break;
    }
    }

    public void talk() throws IOException {
    socketChannel.register(selector, SelectionKey.OP_READ
        | SelectionKey.OP_WRITE);
    while (selector.select() > 0) {
        Set readyKeys = selector.selectedKeys();
        Iterator it = readyKeys.iterator();
        while (it.hasNext()) {
        SelectionKey key = null;
        try {
            key = (SelectionKey) it.next();
            it.remove();

            if (key.isReadable()) {
            receive(key);
            }
            if (key.isWritable()) {
            send(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
            if (key != null) {
                key.cancel();
                key.channel().close();
            }
            } catch (Exception ex) {
            e.printStackTrace();
            }
        }
        }//#while
        try {
        Thread.sleep(1);
        } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        }
    }//#while
    }

    public void send(SelectionKey key) throws IOException {
    SocketChannel socketChannel = (SocketChannel) key.channel();
    synchronized (sendBuffer) {
        sendBuffer.flip(); //把极限设为位置
        socketChannel.write(sendBuffer);
        sendBuffer.compact();
    }
    }

    public void receive(SelectionKey key) throws IOException {
    SocketChannel socketChannel = (SocketChannel) key.channel();
    socketChannel.read(receiveBuffer);
    receiveBuffer.flip();
    String receiveData = decode(receiveBuffer);

    if (receiveData.indexOf(" ") == -1)
        return;

    String outputData = receiveData.substring(0,
        receiveData.indexOf(" ") + 1);
    System.out.println("time:"+System.currentTimeMillis() + " ==> " + outputData);
    if (outputData.equals("C:End ")) {
        key.cancel();
        socketChannel.close();
        System.out.println("关闭与服务器的连接");
        selector.close();
        System.exit(0);
    }

    ByteBuffer temp = encode(outputData);
    receiveBuffer.position(temp.limit());
    receiveBuffer.compact();
    }

    public String decode(ByteBuffer buffer) { //解码
    CharBuffer charBuffer = charset.decode(buffer);
    return charBuffer.toString();
    }

    public ByteBuffer encode(String str) { //编码
    return charset.encode(str);
    }
 public  void ScanLan() {
        try {
            SearchNet sn = new SearchNet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void printDebug(byte[] bs, int len) {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String s = sdf.format(date) + " :: ";
        for (int i = 0; i < len; i++) {
            s += "0x" + Integer.toHexString((int) bs[i] & 0xff) + ",";
        }
        s += "\r\n";
        System.out.print(s);
    }
    
    @Override
    public boolean sendData(String queryData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean sendData(byte[] queryData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void run() {
       keepConnection();
        try {
            talk();
        } catch (IOException ex) {
            Logger.getLogger(TCPClientNIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean isThreadRunning() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isConnected() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addListener(SocketEventListener l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeListener(SocketEventListener l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fireNetwork_messageEvent(SocketMessageEvent evt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void socketMessage(Object o, Object msg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}