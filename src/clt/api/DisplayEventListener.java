/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clt.api;

import java.util.EventListener;


/**
 *
 * @author 7000006
 */
public interface DisplayEventListener extends EventListener {

    public void OnConnected(Object evt);

    public void OnDisconnected(Object evt);

    public void OnResponse(Object evt, PacketFrame packetl);

    public void OnMessage(Object evt, String Msg);
}
abstract class DisplayEventSource {

    public abstract void addListener(DisplayEventListener l);

    public abstract void removeListener(DisplayEventListener l);

    public abstract void SetOnConnected(Object evt);

    public abstract void SetOnDisconnected(Object evt);
    
    public abstract void SetOnResponse(Object evt,PacketFrame packetl);

    public abstract void SetOnMessage(Object evt, String msg);
}
