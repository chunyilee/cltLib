/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clt.api;

import clt.api.DisplayItem;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

@XmlRootElement(name = "displays")
public class Displays {

    public String name = "";
    public boolean unSaved = false;
    public Date LastLoggingTime = new Date();

    String xml = "Displays.xml";
    @XmlElement(required = true)
    public List<DisplayItem> displayItem;

    public void addDisplayItem(byte id,String name, String ip, String mac) {
        DisplayItem di = new DisplayItem();
        di.setName(name);
        di.setIP(ip);
        di.setMac(mac);
        di.setID(id);
        displayItem.add(di);
        sort();
    }

    public void addDisplayItem(DisplayItem di) {
        displayItem.add(di);
        sort();
    }

    public boolean removeDisplayItem(byte id, String ip, String mac) {
        boolean isFind = false;
        for (int i = 0; i < displayItem.size(); i++) {
            DisplayItem di = displayItem.get(i);

            //check three fields: id , ip and mac to remove
            if (di.getMac().equals(mac) && di.getIP().equals(ip) && di.getID() == id) {
                displayItem.remove(i);
                isFind = true;
            }
        }
        return isFind;
    }

    public boolean updateDisplayItemByID(byte id, String ip, String mac) {
        boolean isFind = false;
        for (int i = 0; i < displayItem.size(); i++) {
            DisplayItem di = displayItem.get(i);
            if (di.getID() == id) {
                displayItem.get(i).setIP(ip);
                displayItem.get(i).setMac(mac);
            }
        }
        if (isFind) {
            sort();
        }
        return isFind;
    }

    public boolean updateDisplayItemByMAC(byte id, String ip, String mac) {
        boolean isFind = false;
        for (int i = 0; i < displayItem.size(); i++) {
            DisplayItem di = displayItem.get(i);
            if (di.getMac().equals(mac)) {
                displayItem.get(i).setIP(ip);
                displayItem.get(i).setID(id);

            }
        }
        if (isFind) {
            sort();
        }
        return isFind;
    }

    public List<DisplayItem> sort() {

        Collections.sort(displayItem,
                new Comparator<DisplayItem>() {
                    public int compare(DisplayItem o1, DisplayItem o2) {
                        // return o2.getID() - o1.getID();
                        return o1.getID() - o2.getID();
                    }
                });
        return displayItem;

    }

    public List<DisplayItem> getDisplayItems() {
        return displayItem;
    }

    public DisplayItem getDisplayItemByID(byte id) {
        DisplayItem di = null;
        for (int i = 0; i < displayItem.size(); i++) {
            if (displayItem.get(i).getID() == id) {
                di = displayItem.get(i);
                break;
            }
        }
        return di;
    }
    
        public DisplayItem getDisplayItemByIP(String ip) {
        DisplayItem di = null;
        for (int i = 0; i < displayItem.size(); i++) {
            if (displayItem.get(i).getIP().equals(ip)) {
                di = displayItem.get(i);
                break;
            }
        }
        return di;
    }

    public int getDisplayItemIndexByID(byte id) {
        int index = -1;
        for (int i = 0; i < displayItem.size(); i++) {
            if (displayItem.get(i).getID() == id) {
                index = i;
                break;
            }
        }
        return index;
    }
 public int getDisplayItemIndexByIP(String ip) {
        int index = -1;
        for (int i = 0; i < displayItem.size(); i++) {
            if (displayItem.get(i).getIP().equals(ip)) {
                index = i;
                break;
            }
        }
        return index;
    }
  public int getDisplayItemIndexByMAC(String mac) {
        int index = -1;
        for (int i = 0; i < displayItem.size(); i++) {
            if (displayItem.get(i).getMac().equals(mac)) {
                index = i;
                break;
            }
        }
        return index;
    }
  public Date getLastLoggingTime() {
            return this.LastLoggingTime;
     }
    public void saveXml() {
        saveXML(xml);
    }

    public void saveXML(String xmlPath) {
        try {
            xml = xmlPath;
            File file = new File(xmlPath);//"C:\\file.xml"
            JAXBContext jaxbContext = JAXBContext.newInstance(Displays.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed  
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(this, file);
            jaxbMarshaller.marshal(this, System.out);

            //cancel unsaved state
            unSaved = false;

        } catch (JAXBException e) {
            e.printStackTrace();

        }
    }

    @SuppressWarnings( "unchecked")
    public static Displays loadXML(String xmlPath) {
        Displays xmlDisplays = new Displays();
        try {
            JAXBContext context;
            Source source = new StreamSource(new FileInputStream(xmlPath));
            context = JAXBContext.newInstance(Displays.class);
            Unmarshaller marshaller = context.createUnmarshaller();
            JAXBElement element = (JAXBElement) marshaller.unmarshal(source, Displays.class);
            // 讀取出根結點  
            xmlDisplays = (Displays) element.getValue();
            xmlDisplays.xml = xmlPath;
            xmlDisplays.unSaved = false;
            // displayItem=xmlDisplays.displayItem;
            //           System.out.println(name);  

            // 獲取另一子結點  
//            Rank rank = person.getRank();  
//              
//            String accountId = rank.getAccountId();  
//            double  money = rank.getMoney();  
//              
//            System.out.println(accountId);  
//            System.out.println(money);  
        } catch (FileNotFoundException | JAXBException e) {
            e.printStackTrace();
        }
        return xmlDisplays;
    }

    public Displays() {
        displayItem = new ArrayList<>();

    }
}
