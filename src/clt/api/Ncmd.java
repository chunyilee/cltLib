/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clt.api;

import com.sun.jna.FunctionMapper;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author A000847
 */
public interface Ncmd extends Library {   //StdCallLibrary

        public static class pIP210Node extends Structure  implements Structure.ByReference {

        public int DeviceId;
        public int ConnectionId;
        public byte[] MACAddr = new byte[6];
        public byte[] IPAddr = new byte[4];
        public byte[] IPMask = new byte[4];
        public byte[] IPGateway = new byte[4];
        public byte[] Nickname = new byte[20];
        public byte[] ProjectName = new byte[50];
        public String pOption_Ptr;
        public pIP210Node next;
        
        public static class ByReference extends pIP210Node implements Structure.ByReference { } 
        public static class ByValue extends pIP210Node implements Structure.ByValue{ }
       

       public pIP210Node() {
        }

        public pIP210Node(Pointer p) {
            super(p);
            read();
        }

        @Override
        protected List getFieldOrder() {
            //throw new UnsupportedOperationException("Not supported yet.");
            List lstIP210 = new ArrayList();
            lstIP210.add("DeviceId");
            lstIP210.add("ConnectionId");
            lstIP210.add("MACAddr");
            lstIP210.add("IPAddr");
            lstIP210.add("IPMask");
            lstIP210.add("IPGateway");
            lstIP210.add("Nickname");
            lstIP210.add("ProjectName");
            lstIP210.add("pOption_Ptr");
            lstIP210.add("next");
            return lstIP210;
        }
    }
          //Custom structure for the completely information
    public static class pIP210Node2 {

        public int DeviceId;
        public int ConnectionId;
        public String MacAddr;
        public String IPAddr;
        public String IPMask;
        public String IPGateway;
        public String Nickname;
        public String ProjectName;
        public byte pOption_Ptr;
    }
    
     class Mapper implements FunctionMapper {

        @Override
        public String getFunctionName(NativeLibrary nl, Method method) {
            String res = "";
             switch (method.getName()) {
                 case "initNCMD":
                     res = GenieConnector.FUNCTION_PREFIX + method.getName() + GenieConnector.FUNCTION_INITNCMD_SUEFIX;
                     break;
                 case "NcmdSrhDevice":
                     res = GenieConnector.FUNCTION_PREFIX + method.getName() + GenieConnector.FUNCTION_NCMDSRHDEVICE_SUEFIX;
                     break;
             }
            return res;
        }
    }
   String path =System.getProperty("user.dir");
    Ncmd lib = (Ncmd) Native.loadLibrary(path+"\\ncmd", Ncmd.class, new HashMap() {
        {
            put(
                    Library.OPTION_FUNCTION_MAPPER,
                    new Mapper() {
                        @Override
                        public String getFunctionName(NativeLibrary library, Method method) {
                            return super.getFunctionName(library, method);
                        }
                    });
        }
    });

    public IntByReference initNCMD(int isStartup, short srhTime, short opTime);

    public pIP210Node NcmdSrhDevice(IntByReference pThis);
}
