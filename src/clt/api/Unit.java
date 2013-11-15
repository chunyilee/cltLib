/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clt.api;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.ArrayList;

//import java.util.Vector;
/**
 *
 * @author 7000006
 */
public class Unit {

    // <editor-fold defaultstate="collapsed" desc="PacketFormat">
    // Packet basic unit :  STX (1)+ IDT(1) + Type(1) + CMD(3) + [Send/Reply bytes] + ETX(1)
    // <editor-fold defaultstate="collapsed" desc="Common">
    static final byte CMD_STX = 0x07;                                                        //STX
    static final byte CMD_ETX = 0x08;                                                        //ETX
    public static final byte DEFAULT_ID = 0x01;                                         //Default  ID
    static final byte TYPE_WRITE = 0x02;                                                    //Send Command type > write
    static final byte TYPE_READ_ACTION = 0x01;                                      //Send Command type > read/action
    static final byte TYPE_HOST = 0x00;                                                     //Send Command type > return from host
    public static final String[] ON_OFF_TYPE = {
        "OFF",
        "ON",};
     public static final String[] ZOOM_TYPE = {
        "IN",
        "OUT",};
    //Command Packet( 3 bytes)
    public static final byte[] FW_VERSION = {0x47, 0x56, 0x45};               // GVE
    public static final byte[] RS232_VERSION = {0x52, 0x54, 0x56};          // RTV
    public static final byte[] SN_NUMBER = {0x53, 0x45, 0x52};                // SER
    public static final byte[] MODEL_NAME = {0x4D, 0x4E, 0x41};            // MNA
    //Power Control and Input Source
    public static final byte[] CMD_POWER_CONTROL = {0x50, 0x4F, 0x57};   // POW
    public static final byte PARM_POWER_OFF = 0X0;
    public static final byte PARM_POWER_ON = 0x01;
    public static final byte PARM_POWER_STANDBY = 0x02;
    public static final byte[] CMD_INPUT_SOURCE = {0x4D, 0x49, 0x4E};        // MIN   
    public static final byte PARM_VGA = 0X0;
    public static final byte PARM_DVI = 0x01;
    public static final byte PARM_SVIDEO = 0x02;
    public static final byte PARM_COMPOSITE1 = 0x03;
    public static final byte PARM_COMPONENT1 = 0x04;
    public static final byte PARM_HDSDI1 = 0x05;
    public static final byte PARM_HDSDI2 = 0x06;
    public static final byte PARM_COMPOSITE2 = 0x07;
    public static final byte PARM_COMPONENT2 = 0x08;
    public static final byte PARM_HDMI1 = 0x09;
    public static final byte PARM_HDMI2 = 0x10;
    public static final byte PARM_HDMI3 = 0x11;
    public static final byte PARM_HDMI4 = 0x12;
    public static final byte PARM_DISPLAYPORT = 0x13;
    public static final String[] SOURCE_TYPE = {
        "VGA",
        "DVI",
        "PARM_SVIDEO",
        "COMPOSITE1",
        "COMPONENT1",
        "HDSDI1",
        "HDSDI2",
        "COMPOSITE2",
        "COMPONENT2",
        "HDMI1",
        "HDMI2",
        "HDMI3",
        "HDMI4",
        "DISPLAYPORT"
    };
    //Display Adjustment
    public static final byte[] CMD_BACKLIGHT_BRIGHTNESS = {0x42, 0x52, 0x49};                              // BRI
    public static final byte[] CMD_DIGITAL_BRIGHTNESS_LEVEL = {0x42, 0x52, 0x4C};                        // BRL
    public static final byte[] CMD_BACKLIGHT = {0x42, 0x4C, 0x43};                                                      //BLC
    public static final byte[] CMD_CONTRAST = {0x43, 0x4F, 0x4E};                                                   // CON
    public static final byte[] CMD_HUE = {0x48, 0x55, 0x45};                                                              // HUE
    public static final byte[] CMD_SATURATION = {0x53, 0x41, 0x54};                                              // SAT
    public static final byte[] CMD_GAMMA = {0x47, 0x41, 0x43};                                           // GAC
    public static final byte PARM_GAMMA_OFF = 0;
    public static final byte PARM_GAMMA_22 = 1;                                                                 // 2.2
    public static final byte PARM_GAMMA_24 = 2;                                                                 // 2.4
    public static final byte PARM_GAMMA_25 = 3;                                                                 // 2.5
    public static final byte PARM_GAMMA_20 = 4;                                                                 // 2.0
    public static final String[] GAMMA_TYPE = {
        "OFF",
        "2.2",
        "2.4",
        "2.5",
        "2.0"
    };
    public static final byte[] CMD_R_GAIN = {0x55, 0x53, 0x52};                                           // USR
    public static final byte[] CMD_G_GAIN = {0x55, 0x53, 0x47};                                           // USG
    public static final byte[] CMD_B_GAIN = {0x55, 0x53, 0x42};                                           // USB
    public static final byte[] CMD_R_OFFSET = {0x55, 0x4F, 0x52};                                       // UOR
    public static final byte[] CMD_G_OFFSET = {0x55, 0x4F, 0x47};                                      // UOG
    public static final byte[] CMD_B_OFFSET = {0x55, 0x4F, 0x42};                                      // UOB
    public static final byte[] CMD_PHASE = {0x50, 0x48, 0x41};                                           // PHA
    public static final byte[] CMD_CLOCK = {0x43, 0x4C, 0x4F};                                            // CLO
    public static final byte[] CMD_HORIZONTAL_POSITION = {0x48, 0x4F, 0x52};              // HOR
    public static final byte[] CMD_VERTICAL_POSITION = {0x56, 0x45, 0x52};                     // VER
    public static final byte[] CMD_AUTO_ADJUST = {0x41, 0x44, 0x4A};                               // ADJ
    public static final byte[] CMD_SHARPNESS = {0x53, 0x48, 0x41};                                    // SHA
    //Other Control
    //PIP
    public static final byte[] CMD_PIP_Adjust = {0x50, 0x53, 0x43};                                       // PSC
    public static final byte PARM_PIP_OFF = 0x0;
    public static final byte PARM_PIP_SMALL = 0x01;
    public static final byte PARM_PIP_MEDIUM = 0x02;
    public static final byte PARM_PIP_LARGE = 0x03;
    public static final byte PARM_PIP_SIDE_BY_SIDE = 0x04;
    public static final String[] PIP_SIZE = {
        "OFF",
        "Small",
        "Medium",
        "Large",
        "Side by Side"
    };
    public static final byte[] CMD_PIP_SOURCE_SELECTION = {0x50, 0x49, 0x4E};              // PIN
    //public static final String[] PIP_SOURCE = SOURCE_TYPE;
    public static final byte[] CMD_PIP_POSITION = {0x50, 0x50, 0x4F};                                // PPO
    public static final byte PARM_PIP_BOTTOM_LEFT = 0x0;
    public static final byte PARM_PIP_BOTTOM_RIGHT = 0x1;
    public static final byte PARM_PIP_TOP_LEFT = 0x2;
    public static final byte PARM_PIP_TOP_RIGHT = 0x3;
    public static final String[] PIP_Postion = {
        "Botton Left",
        "Botton Right",
        "Top Left",
        "Top Right"
    };
    public static final byte[] CMD_PIP_MAIN_SWAP = {0x53, 0x57, 0x41};                            // SWA
    public static final byte[] CMD_SCALING = {0x41, 0x53, 0x50};                                         // ASP
    public static final byte PARM_NATIVE = 0x0;
    public static final byte PARM_FILL = 0x1;
    public static final byte PARM_PILLAR_BOX = 0x2;
    public static final byte PARM_LETTER_BOX = 0x3;
    public static final String[] SCALING_TYPE = {
        "Native",
        "Fill",
        "Pillar Box",
        "Letter Box"
    };
    public static final byte[] CMD_SCALING_ZOOM = {0x5A, 0x4F, 0x4D};                           // SWA
    public static final byte[] CMD_BAUD_RATE = {0x42, 0x52, 0x41};                                    // SWA
    public static final byte PARM_115200 = 0x0;
    public static final byte PARM_38400 = 0x1;
    public static final byte PARM_19200 = 0x2;
    public static final byte PARM_9600 = 0x3;
    // "KEYS" 特別是給 MENU / UP / DOWN / LEFT / RIGHT / ENTER / EXIT Keys 用
    public static final byte[] CMD_MENU_KEY = {0x52, 0x43, 0x55};                               // RCU
    public static final byte PARM_KEY_MENU = 0x00;
    public static final byte PARM_KEY_INFO = 0x01;
    public static final byte PARM_KEY_UP = 0x02;
    public static final byte PARM_KEY_DOWN = 0x03;
    public static final byte PARM_KEY_LEFT = 0x04;
    public static final byte PARM_KEY_RIGHT = 0x05;
    public static final byte PARM_KEY_ENTER = 0x06;
    public static final byte PARM_KEY_EXIT = 0x07;
    public static final byte PARM_KEY_VGA = 0x08;
    public static final byte PARM_KEY_DVI = 0x09;
    public static final byte PARM_KEY_HDMI1 = 0x0A;
    public static final byte PARM_KEY_HDMI2 = 0x0B;
    public static final byte PARM_KEY_DISPLAYPORT = 0x0C;
    public static final byte PARM_KEY_COMP = 0x0D;
    public static final byte PARM_KEY_SVIDEO = 0x0E;
    public static final byte PARM_KEY_AV = 0x0F;
    public static final byte PARM_KEY_SOURCE = 0x12;
    public static final byte PARM_KEY_PSOURCE = 0x13;
    public static final byte PARM_KEY_PIP = 0x14;
    public static final byte PARM_KEY_PPOSITION = 0x15;
    public static final byte PARM_KEY_SWAP = 0x16;
    public static final byte PARM_KEY_SCALING = 0x17;
    public static final byte PARM_KEY_FREEZE = 0x18;
    public static final byte PARM_KEY_MUTE = 0x19;
    public static final byte PARM_KEY_BRIGHT = 0x1A;
    public static final byte PARM_KEY_CONTRAST = 0x1B;
    public static final byte PARM_KEY_AUTO = 0x1C;
    public static final byte PARM_KEY_VOLUME_ADD = 0x1D;
    public static final byte PARM_KEY_VOLUME_REDUCE = 0x1E;
    public static final byte[] CMD_RESET_ALL = {0x41, 0x4C, 0x4C};                                                            // ALL
    public static final byte[] CMD_LOCK_KEYS = {0x4B, 0x4C, 0x43};                                                          // KLC
    public static final byte[] CMD_READ_SERIAL_NUMBER = {0x53, 0x45, 0x52};                                     // SER
    public static final byte[] CMD_READ_MODEL_NAME = {0x4D, 0x4E, 0x41};                                       // MNA
    public static final byte[] CMD_READ_FIRMWARE_VERSION = {0x47, 0x56, 0x45};                            // GVE
    public static final byte[] CMD_READ_RS232_VERSION = {0x52, 0x54, 0x56};                                    // RTV
    public static final byte[] CMD_READ_THERMAL_TEMP = {0x52, 0x54, 0x54};                                   // RTT
    public static final byte[] CMD_READ_FAN_SPEED = {0x52, 0x53, 0x46};                                          // RSF
    public static final byte[] CMD_VOLUME = {0x56, 0x4F, 0x4C};                                                                 // VOL
    public static final byte[] CMD_MUTE = {0x4D, 0x55, 0x54};                                                                      //MUT
    public static final byte[] CMD_SCHEME_SELECTION = {0x53, 0x43, 0x4D};                    // SCM
    public static final byte PARM_SPORT = 0x01;
    public static final byte PARM_GAME = 0x02;
    public static final byte PARM_CINEMA = 0x03;
    public static final byte PARM_VIVID = 0x04;
    public static final String[] SCHEME_TYPE = {
        "User",
        "Sport",
        "Game",
        "Cinema",
        "Vivid"
    };
    // Project ID (chilin 內部使用, 沒有放在 engineer spec 中)
    public static final byte[] CMD_PROJECT_ID = {0x50, 0x4A, 0x44};	// PJD
    //Polymedia specified 
    public static final byte[] CMD_POWER_UP = {0x50, 0x50, 0x55};	// PPU
    public static final byte[] CMD_SHOW_LOGO_OLD = {0x50, 0x4C, 0x47};                   // PLG (expired)
    public static final byte[] CMD_SHOW_LOGO = {0x50, 0x53, 0x4C};                         // PSL
    public static final byte[] CMD_SET_LOGO = {0x50, 0x4C, 0x54};                             // PLT
    public static final byte PARM_FLIPBOX = 0x00;
    public static final byte PARM_FLAME = 0x01;
//    static final byte[][] UI_GROUP = {CMD_INPUT_SOURCE, MUTE, VOLUME, CMD_BRIGHTNESS, CMD_CONTRAST, CMD_BACKLIGHT,
//        CMD_GAMMA, COLOR_TEMP};
    //static final byte[][] INFO_GROUP = {FW_VERSION, RS232_VERSION, SN_NUMBER, MODEL_NAME};
    // 只有 video wall monitor 才能讀 RGB gain 的值, 所以要特別分開
//    static final byte[][] UI_GROUP_1 = {CMD_R_GAIN, CMD_G_GAIN, CMD_B_GAIN};
    //Packet Sequence
    static final int FRAME_HEADER = 0;
    static final int FRAME_ID = 1;                //00(Hex Num) for broadcast mode, 01~19(Hex Num) for single control mode.
    static final int FRAME_TYPE = 2;
    static final int FRAME_CMD_INDEX = 3;
    static final int FRAME_PAYLOAD = 6;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="VideoWall">
    public static final byte[] CMD_VIDEOWALL_COLOR_TEMP = {0x43, 0x43, 0x54};                         // CCT, color temperature for Video Wall monitors
    public static final byte[] CMD_LUMINANCE_COLOR_CHROMATICITY = {0x52, 0x58, 0x59};      // RXY
    public static final byte[] CMD_WOD_MODE = {0x57, 0x4F, 0x44};                                                 //WOD
    //Multi-Display
    public static final byte[] CMD_SHOW_MONITOR_ID = {0x53, 0x49, 0x44};                                        //SID
    public static final byte[] CMD_CHANGE_MONITOR_ID = {0x43, 0x49, 0x44};                                    //CID
    public static final byte[] CMD_VIDEOWALL_SWITCH = {0x56, 0x57, 0x53};                                       //VWS
    public static final byte[] CMD_VIDEOWALL_FRAMELESS = {0x56, 0x57, 0x46};                                 //VWF
    public static final byte[] CMD_MATRIX_XY = {0x4D, 0x41, 0x54};                                                      //MAT
    public static final byte[] CMD_DISION_XY = {0x07, 0x00, 0x02, 0x56, 0x41, 0x55};                          //DIV
    public static final byte[] CMD_DVI_INDEMNITY = {0x44, 0x49, 0x44};                                             //DID
    public static final byte[] CMD_POWERONDELAY_INTEGRAL = {0x50, 0x4F, 0x44};                        //POD
    public static final byte[] CMD_POWERONDELAY_FRACTIONAL = {0x50, 0x4F, 0x45};                   //POE
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="RTC">
    public static final byte[] CMD_REALTIME_YEAR = {0x52, 0x54, 0x59};                                              //RTY
    public static final byte[] CMD_REALTIME_MONTH = {0x52, 0x54, 0x4D};                                        //RTM
    public static final byte[] CMD_REALTIME_DAY = {0x52, 0x54, 0x44};                                               //RTD
    public static final byte[] CMD_REALTIME_HOUR = {0x52, 0x54, 0x48};                                            //RTH
    public static final byte[] CMD_REALTIME_MINUTE = {0x52, 0x54, 0x4E};                                        //RTN
    public static final byte[] CMD_TIME_MODE = {0x54, 0x4D, 0x53};                                                //TMS
    public static final byte PARM_TIME_ALL_DAY = 0X0;
    public static final byte PARM_TIME_WORK_DAY = 0x01;
    public static final byte PARM_TIME_USER = 0x02;
    public static final String[] TIME_MODE = {
        "All Day",
        "Working Days",
        "User"
    };
    public static final byte[] CMD_ALARM_ENABLE = {0x41, 0x45, 0x4E};                                           //AEN
    public static final byte[] CMD_ALARM_DISABLE = {0x41, 0x45, 0x46};                                    //AEF
    public static final byte PARM_ALARM_SUNDAY = 0X01;
    public static final byte PARM_ALARM_MONDAY = 0x02;
    public static final byte PARM_ALARM_TUESDAY = 0x04;
    public static final byte PARM_ALARM_WEDNESDAY = 0X08;
    public static final byte PARM_ALARM_THURSDAY = 0x16;
    public static final byte PARM_ALARM_FRIDAY = 0x32;
    public static final byte PARM_ALARM_SATURDAY = 0X64;
    public static final byte[] CMD_MONDAY_ON_HOUR = {0x4E, 0x4E, 0x48};               //NNH 
    public static final byte[] CMD_MONDAY_ON_MINUTE = {0x4E, 0x4E, 0x4D};          //NNM
    public static final byte[] CMD_MONDAY_OFF_HOUR = {0x4E, 0x46, 0x48};              //NFH
    public static final byte[] CMD_MONDAY_OFF_MINUTE = {0x4E, 0x46, 0x4D};         //NFM
    public static final byte[] CMD_TUESDAY_ON_HOUR = {0x45, 0x4E, 0x48};               //ENH 
    public static final byte[] CMD_TUESDAY_ON_MINUTE = {0x45, 0x4E, 0x4D};          //ENM
    public static final byte[] CMD_TUESDAY_OFF_HOUR = {0x45, 0x46, 0x48};              //EFH
    public static final byte[] CMD_TUESDAY_OFF_MINUTE = {0x45, 0x46, 0x4D};         //EFM
    public static final byte[] CMD_WEDNESDAY_ON_HOUR = {0x44, 0x4E, 0x48};       //DNH 
    public static final byte[] CMD_WEDNESDAY_ON_MINUTE = {0x44, 0x4E, 0x4D};   //DNM
    public static final byte[] CMD_WEDNESDAY_OFF_HOUR = {0x44, 0x46, 0x48};       //DFH
    public static final byte[] CMD_WEDNESDAY_OFF_MINUTE = {0x44, 0x46, 0x4D};   //DFM
    public static final byte[] CMD_THURSDAY_ON_HOUR = {0x55, 0x4E, 0x48};           //UNH 
    public static final byte[] CMD_THURSDAY_ON_MINUTE = {0x55, 0x4E, 0x4D};      //UNM
    public static final byte[] CMD_THURSDAY_OFF_HOUR = {0x55, 0x46, 0x48};          //UFH
    public static final byte[] CMD_THURSDAY_OFF_MINUTE = {0x55, 0x46, 0x4D};      //UFM
    public static final byte[] CMD_FRIDAY_ON_HOUR = {0x49, 0x4E, 0x48};                 //INH 
    public static final byte[] CMD_FRIDAY_ON_MINUTE = {0x49, 0x4E, 0x4D};             //INM
    public static final byte[] CMD_FRIDAY_OFF_HOUR = {0x49, 0x46, 0x48};                 //IFH
    public static final byte[] CMD_FRIDAY_OFF_MINUTE = {0x49, 0x46, 0x4D};              //IFM
    public static final byte[] CMD_SATURDAY_ON_HOUR = {0x54, 0x4E, 0x48};            //TNH 
    public static final byte[] CMD_SATURDAY_ON_MINUTE = {0x54, 0x4E, 0x4D};       //TNM
    public static final byte[] CMD_SATURDAY_OFF_HOUR = {0x54, 0x46, 0x48};           //TFH
    public static final byte[] CMD_SATURDAY_OFF_MINUTE = {0x54, 0x46, 0x4D};       //TFM
    public static final byte[] CMD_SUNDAY_ON_HOUR = {0x53, 0x4E, 0x48};                //SNH 
    public static final byte[] CMD_SUNDAY_ON_MINUTE = {0x53, 0x4E, 0x4D};           //SNM
    public static final byte[] CMD_SUNDAY_OFF_HOUR = {0x53, 0x46, 0x48};               //SFH
    public static final byte[] CMD_SUNDAY_OFF_MINUTE = {0x53, 0x46, 0x4D};          //SFM
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ODX">
    public static final byte[] CMD_COLOR_TEMP = {0x43, 0x4F, 0x54};                                             // COT, color temperature for others
    public static final byte PARM_USER = 0X0;                                                                                   //for Display Adjustment and Scense selection
    public static final byte PARM_6500K = 0x01;
    public static final byte PARM_9300K = 0x02;
    public static final byte PARM_3200K = 0x03;
    public static final byte PARM_5400K = 0x04;
    public static final byte PARM_SMPTEC = 0x05;
    public static final byte PARM_5000K = 0x06;
    public static final byte PARM_7500K = 0x07;
    public static final byte PARM_8500K = 0x08;
    public static final byte PARM_9600K = 0x09;
    public static final String[] COLOR_TEMP = {
        "User",
        "6500K",
        "9300K",
        "3200K",
        "5400K",
        "SMPTE-C",
        "5000K",
        "7500K",
        "8500K",
        "9600K"
    };
    public static final byte[] CMD_WAKEUP_FROM_SLEEP = {0x57, 0x46, 0x53};	// WFS
    public static final byte PARM_VGA_ONLY = 0x00;
    public static final byte PARM_VGA_DIGITAL_RS232 = 0x01;
    public static final byte PARM_NERVER_SLEEP = 0x02;
    // </editor-fold >

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Convert Functions">
    public static byte High_UInt16(short Dt) {
        return (byte) ((Dt >> 8) & 0x0ff);
    }

    public static byte Low_UInt16(short Dt) {
        return (byte) (Dt & 0x0ff);
    }

    public static int ByteToInt(byte Hbyte, byte Lbyte) {
        int us = (int) (Hbyte & 0x00ff);
        us <<= 8;

        us = (int) (us + ((int) Lbyte & 0xFF));
        return us;
    }

    public static String Byte2HexString(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return hex;
    }

    public static byte Break_UInt32(int var, int ByteNum) {
        return (byte) ((int) (((var) >> ((ByteNum) * 8)) & 0x00FF));
    }

    public static byte HexString2Byte(String s) {
        byte b = 0;
        try {

            int intValue = Integer.parseInt(s, 16);
            b = (byte) intValue;
            // b = (Byte)s;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return b;
    }

    public static String Bytes2String(byte[] bytes) {
        String text = "";
        try {
            text = new String(bytes, 0, bytes.length, "ASCII");

        } catch (UnsupportedEncodingException exp) {
            exp.printStackTrace();
        }
        return text;
    }

    public static String Bytes2ASCII(byte[] data) {
        String res = "";
        for (int i = 0; i < data.length; i++) {
            //res += new Character((char)data[i]).toString();
            res += (char) data[i];
        }
        return res;
    }

    public static byte[] Bytes2BytesRedue0(byte[] data) {
        ArrayList<Byte> ary = new ArrayList<>();
        for (byte b : data) {
            if (b > 0) {
                ary.add(b);
            }
        }
        byte[] res = new byte[ary.size()];
        for (int i = 0; i <ary.size(); i++) {
            res[i] = (byte) ary.get(i);
        }
        return res;
    }

    public static byte[] Bytes2CLT_Packet(byte id, byte type, byte[] cmd, byte[] payload) {

        int payloadLen = 0;
        if (payload != null) {
            payloadLen = payload.length;
        }

        int packetLen = 4 + cmd.length + payloadLen;  //STX+IDT+TYPE+EXT=4
        byte[] sendB = new byte[packetLen];
        sendB[FRAME_HEADER] = CMD_STX;
        sendB[FRAME_ID] = id;
        sendB[FRAME_TYPE] = type;

        //Copy CMD
        System.arraycopy(cmd, 0, sendB, FRAME_CMD_INDEX, cmd.length);

        //Copy Payload
        if (payload != null) {
            System.arraycopy(payload, 0, sendB, FRAME_PAYLOAD, payload.length);
        }

        sendB[packetLen - 1] = CMD_ETX;
        return sendB;

    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="NetworkFunctions">

    public static String listHostAddress() {
        try {
            String ip = "";
            String hostName = InetAddress.getLocalHost().getHostName();
            //System.out.println("HostName = " + hostName);
            ip = InetAddress.getLocalHost().getHostAddress();
            //  InetAddress[] inetAddresses = InetAddress.getAllByName(hostName);
            // for (InetAddress inetAddress : inetAddresses) {
            //jcobIP.addItem( inetAddress.getHostAddress());
            // }
            return ip;
        } catch (Exception e) {

            return "";
        }
    }
    // </editor-fold> 

    public static void Delay(int millSeconds) {
        try {
            Thread.sleep(millSeconds);
        } catch (InterruptedException exp) {
        }
    }
}
