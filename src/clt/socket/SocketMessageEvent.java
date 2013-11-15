/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clt.socket;

import java.util.EventObject;


/**
 *
 * @author 7000005
 */
public class SocketMessageEvent extends EventObject {

    private int eventType;

    public SocketMessageEvent(Object source, int eventType) {
        super(source);
        this.eventType = eventType;
    }

    public int getEventType() {
        return eventType;
    }
}
abstract class SocketEventSource {

    public abstract void addListener(SocketEventListener l);

    public abstract void removeListener(SocketEventListener l);

    public abstract void fireNetwork_messageEvent(SocketMessageEvent evt);
    
    public abstract void socketMessage(Object o,Object msg);
}
/*
 * ESSSocket.addListener(new EssEventListener() { @Override public void
 * EssEventReceived(EssEvent event) { System.out.println("fired"); } });
 */
/*  fireEvent(new EssEvent(this,1));
  protected EventListenerList listenerList = new EventListenerList();
    @Override
    public void addListener(EssEventListener l) {
        listenerList.add(EssEventListener.class, l);
    }

    @Override
    public void removeListener(EssEventListener l) {
        listenerList.remove(EssEventListener.class, l);
    }

    @Override
    public void fireEvent(EssEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == EssEventListener.class) {
                ((EssEventListener) listeners[i + 1]).EssEventReceived(evt);
            }
        }
    }
 */