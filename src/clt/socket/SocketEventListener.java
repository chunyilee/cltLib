/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clt.socket;
import java.util.EventListener;

/**
 *
 * @author 7000005
 */
public  interface SocketEventListener extends EventListener {
    
    public void SocketEventReceived(SocketMessageEvent event);      
    public void SocketMessage(Object o,Object Msg);
}

