/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clt.api;

import clt.socket.*;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.swing.event.EventListenerList;

/**
 *
 * @author 7000006 7/10 進行至將兩個介面結合在一起 , 尚未完成的部分有 1. 建立共同的傳送packet command , ex:
 * send data 2.建立Command List 的的命令格式 ,方便傳送命令至device 3.建立CHILIN Remote 的介面,
 * 開始實作那個power on , video wall 的功能
 */
public final class Display extends DisplayEventSource {

    ConnectAdapter adapter;

    public enum DisplayType {

        RS232, Ethernet
    };
    public DisplayType displayType = DisplayType.RS232;
    //  public Queue<PacketFrame> NetworkreceiverQe = null;
    public boolean IsConnected = false;
    public String ip = "";
    public int port = 0;
    public String comPort = "COM1";
    //public DisplayItem settings = new DisplayItem();

    public Display() {
    }

//    public Display(DisplayItem displaySetting, DisplayType dType) {
//        settings = displaySetting;
//        displayType = dType;
//        Connect();
//    }
    public Display(String IP, int Port) {

        ip = IP;
        port = Port;
        displayType = DisplayType.Ethernet;
        Connect();
    }

    public Display(String COM_Port) {
        comPort = COM_Port;
        displayType = DisplayType.RS232;
        Connect();
    }

    public void Connect() {
        try {
            if (displayType == DisplayType.RS232) {
                adapter = new clt.socket.RS232(comPort);
            } else {
                adapter = new TCPClientSocket(ip, port);
            }
            addEvent(adapter);
        } catch (IOException ex) {
            System.out.printf(ex.getMessage());
        }

    }

    public String showSource() {
        String res = "";
        if (IsConnected) {
            if (displayType == DisplayType.RS232) {
                res = this.comPort;
            } else if (displayType == DisplayType.Ethernet) {
                res = this.ip;
            }
        }
        return res;
    }

    private void addEvent(ConnectAdapter adapter) {
        //    NetworkreceiverQe = new LinkedList();

        adapter.addListener(new SocketEventListener() {
            Queue<Byte> workQe = new LinkedList<>();
            ArrayList<Byte> values = new ArrayList<>();

            @Override
            public synchronized void SocketEventReceived(SocketMessageEvent event) {
                //先將所有的byte array 轉成一個個的byte
                Queue<byte[]> TcprequestQe = ((ConnectAdapter) event.getSource()).receiverQe;
                while (!TcprequestQe.isEmpty()) {
                    //加入佇列中
                    byte[] add = (byte[]) TcprequestQe.poll();
                    for (byte b : add) {
                        workQe.offer(b);
                    }
                }
                //佇列大於8才進行處理
                while (workQe.size() >= 8) {
                    //啟頭不是 0x07 就放棄
                    if (workQe.poll() != Unit.CMD_STX) {
                        continue;
                    }

                    //Apply packet structure
                    PacketFrame EPacket = new PacketFrame();
                    EPacket.STX = Unit.CMD_STX;
                    EPacket.IDT = workQe.poll();
                    EPacket.Type = workQe.poll();

                    EPacket.Command[0] = workQe.poll();
                    EPacket.Command[1] = workQe.poll();
                    EPacket.Command[2] = workQe.poll();

                    //Value equals data - ETX
                    values.clear();
                    while (!workQe.isEmpty()) {
                        values.add(workQe.poll());    //retrun must have at least  one byte
                        byte data = workQe.poll();
                        if (data == Unit.CMD_ETX) {
                            EPacket.ETX = Unit.CMD_ETX;
                            break;
                        } else {
                            values.add(data);
                        }
                    }
                    //Send PacketFrame after found EXT byte
                    if (EPacket.ETX == Unit.CMD_ETX) {
                        if (values.size() > 0) {
                            EPacket.Value = new byte[values.size()];
                            for (int i = 0; i < values.size(); i++) {
                                EPacket.Value[i] = values.get(i);
                            }
                        }
                        EPacket.IP = ip;
                        setOnResonse(EPacket);
                    }

                }
            }
            //10/24 backup(funcion 是以區傳送,因此有時會有斷)
  /* public void SocketEventReceived(SocketMessageEvent event) {
             byte[] bs;
             Queue<byte[]> TcprequestQe = ((ConnectAdapter) event.getSource()).receiverQe;
             while (!TcprequestQe.isEmpty()) {
             bs = (byte[]) TcprequestQe.poll();
             if (bs[0] == Unit.CMD_STX && bs.length >= 8) {    ////STX+IDT+TYPE+Value(at least >=1)+EXT=8
             PacketFrame EPacket = new PacketFrame();
             EPacket.STX = bs[0];
             EPacket.IDT = bs[1];
             EPacket.Type = bs[2];
             System.arraycopy(bs, 3, EPacket.Command, 0, 3);

             if (bs.length > 7) {
             EPacket.Value = new byte[bs.length - 7];
             System.arraycopy(bs, 6, EPacket.Value, 0, bs.length - 7); //STX+IDT+TYPE+EXT=7
             }
             EPacket.IP = ip;
             setOnResonse(EPacket);
             }
             }
             }*/

            @Override
            public void SocketMessage(Object o, Object Msg) {
                SetOnMessage(o, (String) Msg);

            }
        });
        IsConnected = adapter.isConnected();
    }

    private void setOnResonse(PacketFrame pf) {
        SetOnResponse(this, pf);
    }

    private void sendCommand(byte id, byte type, byte[] cmd, byte[] payload) throws IOException {
        byte[] outBytes;
        if (!adapter.isConnected()) {
            SetOnDisconnected(this);
            return;
        }
        outBytes = Unit.Bytes2CLT_Packet(id, type, cmd, payload);
        adapter.sendData(outBytes);
        Unit.Delay(1500);
    }

    public void stop() {
        adapter.stop();
        SetOnDisconnected(this);
    }

    public void run() {
        if (!IsConnected) {
            return;
        }
        SetOnConnected(this);
        if (displayType == DisplayType.Ethernet) {
            new Thread((TCPClientSocket) adapter).start();

        } else if (displayType == DisplayType.RS232) {
            new Thread((RS232) adapter).start();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Command List">
    // <editor-fold defaultstate="collapsed" desc="Read Information">
    public void GetFan0Speed(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_READ_FAN_SPEED, new byte[]{0x0});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetFan1Speed(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_READ_FAN_SPEED, new byte[]{0x01});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetThermalTemperature(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_READ_THERMAL_TEMP, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetRS232_Version(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_READ_RS232_VERSION, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetFirmwareVersion(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_READ_FIRMWARE_VERSION, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetModelName(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_READ_MODEL_NAME, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetSerialNumber(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_READ_SERIAL_NUMBER, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetLockKeyStatus(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_BAUD_RATE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Audio">
    public void GetMuteStatus(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_MUTE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetMuteOn(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_MUTE, new byte[]{0x01});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetMuteOff(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_MUTE, new byte[]{0x0});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetVolume(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_VOLUME, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetVolume(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_VOLUME, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="PIP">
    public void SetPIP_Swap(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_PIP_MAIN_SWAP, new byte[]{0x0});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetPIP_Position(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_PIP_POSITION, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetPIP_Position(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_PIP_POSITION, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetPIP_SourceSelect(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_PIP_SOURCE_SELECTION, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetPIP_SourceSelect(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_PIP_SOURCE_SELECTION, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetPIP_Adjust(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_PIP_Adjust, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetPIP_Adjust(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_PIP_Adjust, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="RTC">
    public void SetRealTime(byte id, Calendar now) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_REALTIME_YEAR, new byte[]{(byte) now.YEAR});
            Thread.sleep(100);
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_REALTIME_MONTH, new byte[]{(byte) now.MONTH});
            Thread.sleep(100);
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_REALTIME_DAY, new byte[]{(byte) now.DATE});
            Thread.sleep(100);
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_REALTIME_HOUR, new byte[]{(byte) now.HOUR_OF_DAY});
            Thread.sleep(100);
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_REALTIME_MINUTE, new byte[]{(byte) now.MINUTE});

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void GetRealTime(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_REALTIME_YEAR, null);
            Thread.sleep(100);
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_REALTIME_MONTH, null);
            Thread.sleep(100);
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_REALTIME_DAY, null);
            Thread.sleep(100);
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_REALTIME_HOUR, null);
            Thread.sleep(100);
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_REALTIME_MINUTE, null);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetTimeMdoe(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_TIME_MODE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetTimeMode(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_TIME_MODE, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetAlarmEnable(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_ALARM_ENABLE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetAlarmEnable(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_ALARM_ENABLE, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetAlarmDisable(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_ALARM_DISABLE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetAlarmDisable(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_ALARM_DISABLE, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetMondayTime(byte id, byte OnHour, byte OnMin, byte OffHour, byte OffMin) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_MONDAY_ON_HOUR, new byte[]{OnHour});
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_MONDAY_ON_MINUTE, new byte[]{OnMin});
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_MONDAY_OFF_HOUR, new byte[]{OffHour});
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_MONDAY_OFF_MINUTE, new byte[]{OffMin});

        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    public void GetMondayTime(byte id) {
        try {

            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_MONDAY_ON_HOUR, null);
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_MONDAY_ON_MINUTE, null);
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_MONDAY_OFF_HOUR, null);
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_MONDAY_OFF_MINUTE, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetTuesdayTime(byte id, byte OnHour, byte OnMin, byte OffHour, byte OffMin) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_TUESDAY_ON_HOUR, new byte[]{OnHour});
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_TUESDAY_ON_MINUTE, new byte[]{OnMin});
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_TUESDAY_OFF_HOUR, new byte[]{OffHour});
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_TUESDAY_OFF_MINUTE, new byte[]{OffMin});

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetTuesdayTime(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_TUESDAY_ON_HOUR, null);
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_TUESDAY_ON_MINUTE, null);
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_TUESDAY_OFF_HOUR, null);
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_TUESDAY_OFF_MINUTE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetWednesdayTime(byte id, byte OnHour, byte OnMin, byte OffHour, byte OffMin) {
        try {

            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_WEDNESDAY_ON_HOUR, new byte[]{OnHour});
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_WEDNESDAY_ON_MINUTE, new byte[]{OnMin});
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_WEDNESDAY_OFF_HOUR, new byte[]{OffHour});
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_WEDNESDAY_OFF_MINUTE, new byte[]{OffMin});

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetWednesdayTime(byte id) {
        try {

            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_WEDNESDAY_ON_HOUR, null);
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_WEDNESDAY_ON_MINUTE, null);
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_WEDNESDAY_OFF_HOUR, null);
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_WEDNESDAY_OFF_MINUTE, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetThursdayTime(byte id, byte OnHour, byte OnMin, byte OffHour, byte OffMin) {
        try {

            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_THURSDAY_ON_HOUR, new byte[]{OnHour});
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_THURSDAY_ON_MINUTE, new byte[]{OnMin});
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_THURSDAY_OFF_HOUR, new byte[]{OffHour});
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_THURSDAY_OFF_MINUTE, new byte[]{OffMin});

        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    public void GetThursdayTime(byte id) {
        try {

            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_THURSDAY_ON_HOUR, null);
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_THURSDAY_ON_MINUTE, null);
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_THURSDAY_OFF_HOUR, null);
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_THURSDAY_OFF_MINUTE, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetFridayTime(byte id, byte OnHour, byte OnMin, byte OffHour, byte OffMin) {
        try {

            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_FRIDAY_ON_HOUR, new byte[]{OnHour});

            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_FRIDAY_ON_MINUTE, new byte[]{OnMin});
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_FRIDAY_OFF_HOUR, new byte[]{OffHour});
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_FRIDAY_OFF_MINUTE, new byte[]{OffMin});

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetFridayTime(byte id) {
        try {

            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_FRIDAY_ON_HOUR, null);

            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_FRIDAY_ON_MINUTE, null);

            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_FRIDAY_OFF_HOUR, null);

            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_FRIDAY_OFF_MINUTE, null);
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    public void SetSaturdayTime(byte id, byte OnHour, byte OnMin, byte OffHour, byte OffMin) {
        try {

            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_SATURDAY_ON_HOUR, new byte[]{OnHour});
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_SATURDAY_ON_MINUTE, new byte[]{OnMin});
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_SATURDAY_OFF_HOUR, new byte[]{OffHour});
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_SATURDAY_OFF_MINUTE, new byte[]{OffMin});

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetSaturdayTime(byte id) {
        try {

            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_SATURDAY_ON_HOUR, null);

            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_SATURDAY_ON_MINUTE, null);

            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_SATURDAY_OFF_HOUR, null);
  
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_SATURDAY_OFF_MINUTE, null);

        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    public void SetSundayTime(byte id, byte OnHour, byte OnMin, byte OffHour, byte OffMin) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_SUNDAY_ON_HOUR, new byte[]{OnHour});

            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_SUNDAY_ON_MINUTE, new byte[]{OnMin});

            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_SUNDAY_OFF_HOUR, new byte[]{OffHour});

            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_SUNDAY_OFF_MINUTE, new byte[]{OffMin});

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetSundayTime(byte id) {
        try {

            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_SUNDAY_ON_HOUR, null);
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_SUNDAY_ON_MINUTE, null);
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_SUNDAY_OFF_HOUR, null);
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_SUNDAY_OFF_MINUTE, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Adjustment ">

    public void GetSharpness(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_SHARPNESS, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetSharpness(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_SHARPNESS, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AutoAdjust(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_AUTO_ADJUST, new byte[]{0x0});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetVerticalPosition(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_VERTICAL_POSITION, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetHorizontalPosition(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_HORIZONTAL_POSITION, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetClock(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_CLOCK, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetClock(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_CLOCK, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetPhase(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_PHASE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetPhase(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_PHASE, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
// </editor-fold >

    // <editor-fold defaultstate="collapsed" desc="Display Adjustment">
    public void GetBlueOffset(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_B_OFFSET, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetBlueOffset(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_B_OFFSET, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetGreenOffset(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_G_OFFSET, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetGreenOffset(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_G_OFFSET, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetRedOffset(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_R_OFFSET, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetRedOffset(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_R_OFFSET, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetBlueGain(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_B_GAIN, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetBlueGain(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_B_GAIN, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetGreenGain(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_G_GAIN, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetGreenGain(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_G_GAIN, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetRedGain(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_R_GAIN, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetRedGain(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_R_GAIN, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetGamma(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_GAMMA, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetGamma(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_GAMMA, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetColorTemperature(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_COLOR_TEMP, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetColorTemperature(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_COLOR_TEMP, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetSaturation(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_SATURATION, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetSaturation(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_SATURATION, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetHue(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_HUE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetHue(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_HUE, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetContrast(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_CONTRAST, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetContrast(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_CONTRAST, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetBackLightStatus(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_BACKLIGHT, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetBackLightOn(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_BACKLIGHT, new byte[]{0x01});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetBackLightOff(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_BACKLIGHT, new byte[]{0x00});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetDigitalBrightnessLevel(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_DIGITAL_BRIGHTNESS_LEVEL, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetDigitalBrightnessLevel(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_DIGITAL_BRIGHTNESS_LEVEL, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetBL_Brightness(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_BACKLIGHT_BRIGHTNESS, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetBL_Brightness(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_BACKLIGHT_BRIGHTNESS, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Polymedia">  
    public void SetPowerUpOn(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_POWER_UP, new byte[]{0x1});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetPowerUpOff(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_POWER_UP, new byte[]{0x0});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetPowerUpStatus(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_POWER_UP, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ShowLogoOn(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_SHOW_LOGO_OLD, new byte[]{0x1});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ShowLogoOff(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_SHOW_LOGO_OLD, new byte[]{0x0});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetShowLogoStatus(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_SHOW_LOGO_OLD, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetLogoMode(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_SET_LOGO, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetLogoMode(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_SET_LOGO, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Power Control and Input Source">    
    public void GetInputSource(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_INPUT_SOURCE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetInputSource(byte id, byte source) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_INPUT_SOURCE, new byte[]{source});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetPower(byte id, byte source) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_POWER_CONTROL, new byte[]{source});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetPowerStatus(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_POWER_CONTROL, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Other Control">
    public void GetProjectID(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_PROJECT_ID, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetSchemeSelection(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_SCHEME_SELECTION, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetSchemeSelection(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_SCHEME_SELECTION, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetWakeupFromSleep(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_WAKEUP_FROM_SLEEP, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetWakeupFromSleep(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_WAKEUP_FROM_SLEEP, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetLockKey(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_LOCK_KEYS, new byte[]{0x01});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetUnlockKey(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_LOCK_KEYS, new byte[]{0x00});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ResetAll(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_RESET_ALL, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void MenuKeyControl(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_MENU_KEY, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetBaudRate(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_BAUD_RATE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetBaudRate(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_BAUD_RATE, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetZoomIn(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_SCALING_ZOOM, new byte[]{0x0});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetZoomOut(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_SCALING_ZOOM, new byte[]{0x01});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetScaling(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_SCALING, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetScaling(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_SCALING, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="VideoWall">
    public void GetVideoWallColorTemp(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_VIDEOWALL_COLOR_TEMP, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetGetVideoWallColorTemp(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_VIDEOWALL_COLOR_TEMP, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetLuminanceAndColorChromaticity(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_LUMINANCE_COLOR_CHROMATICITY, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void SetWOD_On(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_WOD_MODE, new byte[]{0x1});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetWOD_Off(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_WOD_MODE, new byte[]{0x0});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetWOD_Status(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_WOD_MODE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ShowMonitorID(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_SHOW_MONITOR_ID, new byte[]{0x0});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ChangeMonitorID(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_SHOW_MONITOR_ID, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetVideoWallSwitchOn(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_VIDEOWALL_SWITCH, new byte[]{0x1});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetVideoWallSwitchOff(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_VIDEOWALL_SWITCH, new byte[]{0x0});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetVideoWallSwitchStatus(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_VIDEOWALL_SWITCH, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetVideoWallFramelessOn(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_VIDEOWALL_FRAMELESS, new byte[]{0x1});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetVideoWallFramelessOff(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_VIDEOWALL_FRAMELESS, new byte[]{0x0});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetVideoWallFramelessStatus(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_VIDEOWALL_FRAMELESS, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetMatrixXY(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_MATRIX_XY, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetMatrixXY(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_MATRIX_XY, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetDivisionXY(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_DISION_XY, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetDivisionXY(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_DISION_XY, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetDVI_IndemnityOn(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_DVI_INDEMNITY, new byte[]{0x1});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetDVI_IndemnityOff(byte id) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_DVI_INDEMNITY, new byte[]{0x0});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetDVI_IndemnityStatus(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_DVI_INDEMNITY, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetPowerOnDelay(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_POWERONDELAY_INTEGRAL, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetPowerOnDelay(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_POWERONDELAY_INTEGRAL, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetFractionalPartPOE(byte id) {
        try {
            sendCommand(id, Unit.TYPE_READ_ACTION, Unit.CMD_POWERONDELAY_FRACTIONAL, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetFractionalPartPOE(byte id, byte value) {
        try {
            sendCommand(id, Unit.TYPE_WRITE, Unit.CMD_POWERONDELAY_FRACTIONAL, new byte[]{value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // </editor-fold >
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Event">
    protected EventListenerList listenerList = new EventListenerList();

    @Override
    public void addListener(DisplayEventListener l) {
        listenerList.add(DisplayEventListener.class, l);
    }

    @Override
    public void removeListener(DisplayEventListener l) {
        listenerList.remove(DisplayEventListener.class, l);
    }

    @Override
    public void SetOnConnected(Object evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == DisplayEventListener.class) {
                ((DisplayEventListener) listeners[i + 1]).OnConnected(evt);
            }
        }
    }

    @Override
    public void SetOnDisconnected(Object evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == DisplayEventListener.class) {
                ((DisplayEventListener) listeners[i + 1]).OnDisconnected(evt);
            }
        }
    }

    @Override
    public void SetOnResponse(Object source, PacketFrame Packetframe) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == DisplayEventListener.class) {
                ((DisplayEventListener) listeners[i + 1]).OnResponse(source, Packetframe);
            }
        }
    }

    @Override
    public void SetOnMessage(Object source, String message) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == DisplayEventListener.class) {
                ((DisplayEventListener) listeners[i + 1]).OnMessage(source, message);
                IsConnected = false;
            }
        }
    }
    // </editor-fold >
}
