/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clt.api;

import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 7000006
 */
@XmlRootElement(name = "displayItem")
public class DisplayItem {

    public DisplayItem() {
    }
    // <editor-fold defaultstate="collapsed" desc="Define">
    @XmlElement
    public boolean isVideoWall = false;
    @XmlElement
    public boolean isConnected = false;
    private byte InputSource = 0;

    @XmlElement
    public void setInputSource(byte inputSource) {
        InputSource = inputSource;
    }

    public byte getInputSource() {
        return InputSource;
    }
    private byte BL_Brightness = 0;

    @XmlElement
    public void setBL_Brightness(byte blBrightness) {
        BL_Brightness = blBrightness;
    }

    public byte getBL_Brightness() {
        return BL_Brightness;
    }
    private byte DigitalBrightnessLevel = 0;

    @XmlElement
    public void setDigitalBrightnessLevel(byte digiBrightnessLevel) {
        DigitalBrightnessLevel = digiBrightnessLevel;
    }

    public byte getDigitalBrightnessLevel() {
        return DigitalBrightnessLevel;
    }
    private boolean BacklightOn = false;

    @XmlElement
    public void setBacklightOn(boolean backlightOn) {
        BacklightOn = backlightOn;
    }

    public boolean getBacklightOn() {
        return BacklightOn;
    }
    private byte Contrast = 0;

    @XmlElement
    public void setContrast(byte contrast) {
        Contrast = contrast;
    }

    public byte getContrast() {
        return Contrast;
    }
    private byte Hue = 0;

    @XmlElement
    public void setHue(byte hue) {
        Hue = hue;
    }

    public byte getHue() {
        return Hue;
    }
    private byte Staturation = 0;

    @XmlElement
    public void setStaturation(byte staturation) {
        Staturation = staturation;
    }

    public byte getStaturation() {
        return Staturation;
    }
    private byte ColorTemperature = 0;

    @XmlElement
    public void setColorTemperature(byte colorTemperature) {
        ColorTemperature = colorTemperature;
    }

    public byte getColorTemperature() {
        return ColorTemperature;
    }
    private byte Gamma = 0;

    @XmlElement
    public void setGamma(byte gamma) {
        Gamma = gamma;
    }

    public byte getGamma() {
        return Gamma;
    }
    private byte Phase = 0;

    @XmlElement
    public void setPhase(byte phase) {
        Phase = phase;
    }

    public byte getPhase() {
        return Phase;
    }
    private byte Clock = 0;

    @XmlElement
    public void setClock(byte clock) {
        Clock = clock;
    }

    public byte getClock() {
        return Clock;
    }
    private int R_Gain = 0;

    @XmlElement
    public void setR_Gain(int rgain) {
        R_Gain = rgain;
    }

    public int getR_Gain() {
        return R_Gain;
    }
    private int G_Gain = 0;

    @XmlElement
    public void setG_Gain(int ggain) {
        G_Gain = ggain;
    }

    public int getG_Gain() {
        return G_Gain;
    }
    private int B_Gain = 0;

    @XmlElement
    public void setB_Gain(int bgain) {
        B_Gain = bgain;
    }

    public int getB_Gain() {
        return B_Gain;
    }
    private int R_Offset = 0;

    @XmlElement
    public void setR_Offset(int roffset) {
        R_Offset = roffset;
    }

    public int getR_Offset() {
        return R_Offset;
    }
    private int G_Offset = 0;

    @XmlElement
    public void setG_Offset(int goffset) {
        G_Offset = goffset;
    }

    public int getG_Offset() {
        return G_Offset;
    }
    private int B_Offset = 0;

    @XmlElement
    public void setB_Offset(int boffset) {
        B_Offset = boffset;
    }

    public int getB_Offset() {
        return B_Offset;
    }
    private byte PowerOnDelay = 0;

    @XmlElement
    public void setPowerOnDelay(byte poe) {
        PowerOnDelay = poe;
    }

    public byte getPowerOnDelay() {
        return PowerOnDelay;
    }
    private byte HorizontalPosition = 0;

    @XmlElement
    public void setHorizontalPosition(byte hPosition) {
        HorizontalPosition = hPosition;
    }

    public byte getHorizontalPosition() {
        return HorizontalPosition;
    }
    private byte VerticalPosition = 0;

    @XmlElement
    public void setVerticalPosition(byte vPosition) {
        VerticalPosition = vPosition;
    }

    public byte getVerticalPosition() {
        return VerticalPosition;
    }
    private byte Sharpness = 0;

    @XmlElement
    public void setSharpness(byte sharpness) {
        Sharpness = sharpness;
    }

    public byte getSharpness() {
        return Sharpness;
    }
    private byte PIP_Adjust = 0;

    @XmlElement
    public void setPIP_Adjust(byte adjust) {
        PIP_Adjust = adjust;
    }

    public byte getPIP_Adjust() {
        return PIP_Adjust;
    }
    private byte PIP_SourceSelection = 0;

    @XmlElement
    public void setPIP_SourceSelection(byte sourceSelection) {
        PIP_SourceSelection = sourceSelection;
    }

    public byte getPIP_SourceSelection() {
        return PIP_SourceSelection;
    }
    private byte PIP_Position = 0;

    @XmlElement
    public void setPIP_Position(byte position) {
        PIP_Position = position;
    }

    public byte getPIP_Position() {
        return PIP_Position;
    }
    private byte Scaling = 0;

    @XmlElement
    public void setScaling(byte scaling) {
        Scaling = scaling;
    }

    public byte getScaling() {
        return Scaling;
    }
    private byte BaudByte = 0;

    @XmlElement
    public void setBaudByte(byte baudByte) {
        BaudByte = baudByte;
    }

    public byte getBaudByte() {
        return BaudByte;
    }
    private boolean LockKey = false;

    @XmlElement
    public void setLockKey(boolean lockKey) {
        LockKey = lockKey;
    }

    public boolean getLockKey() {
        return LockKey;
    }
    private String SerialNumber;

    @XmlElement
    public void setSerialNumber(String sn) {
        SerialNumber = sn;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }
    private String ModelName = "ModelName";

    @XmlElement
    public void setModelName(String modeName) {
        ModelName = modeName;
    }

    public String getModelName() {
        return ModelName;
    }
    private String FW_Version = "0.00";

    @XmlElement
    public void setFW_Version(String fwVer) {
        FW_Version = fwVer;
    }

    public String getFW_Version() {
        return FW_Version;
    }
    private String RS232_Version = "0.00";

    @XmlElement
    public void setRS232_Version(String rs232Ver) {
        RS232_Version = rs232Ver;
    }

    public String getRS232_Version() {
        return RS232_Version;
    }
    private String ThermalVersion = "0.00";

    @XmlElement
    public void setThermalVersion(String thermalVer) {
        ThermalVersion = thermalVer;
    }

    public String getThermalVersion() {
        return ThermalVersion;
    }
    private byte Fan0Speed = 0;

    @XmlElement
    public void setFan0Speed(byte speed) {
        Fan0Speed = speed;
    }

    public byte getFan0Speed() {
        return Fan0Speed;
    }
    private byte Fan1Speed = 0;

    @XmlElement
    public void setFan1Speed(byte speed) {
        Fan1Speed = speed;
    }

    public byte getFan1Speed() {
        return Fan1Speed;
    }
    private byte Volume = 50;

    @XmlElement
    public void setVolume(byte vol) {
        Volume = vol;
    }

    public byte getVolume() {
        return Volume;
    }
    private boolean MuteOn = false;

    @XmlElement
    public void setMuteOn(boolean muteOn) {
        MuteOn = muteOn;
    }

    public boolean getMuteOn() {
        return MuteOn;
    }
    private byte Scheme = 0;

    @XmlElement
    public void setScheme(byte scheme) {
        Scheme = scheme;
    }

    public byte getScheme() {
        return Scheme;
    }
    private String IP = "169.8.8.8";

    @XmlElement
    public void setIP(String ip) {
        IP = ip;
    }

    public String getIP() {
        return IP;
    }
    private int Port = 23;

    @XmlElement
    public void setPort(int port) {
        Port = port;
    }

    public int getPort() {
        return Port;
    }
    private String COM = "COM1";

    @XmlElement
    public void setCOM(String com) {
        COM = com;
    }

    public String getCOM() {
        return COM;
    }
    private String MAC = "00-00-00-00-00-00";

    @XmlElement
    public void setMac(String mac) {
        MAC = mac;
    }

    public String getMac() {
        return MAC;
    }
    private String Name = "CLS Display";

    @XmlElement
    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }
    private byte ID = 0x0;

    @XmlElement
    public void setID(byte id) {
        ID = id;
    }

    public byte getID() {
        return ID;
    }
    private boolean PowerOn = false;

    @XmlElement
    public void setPowerOn(boolean power) {
        PowerOn = power;
    }

    public boolean getPowerOn() {
        return PowerOn;
    }
    private byte TimeMode = 0;

    @XmlElement
    public void setTimeMode(byte tMode) {
        TimeMode = tMode;
    }

    public byte getTimeMode() {
        return TimeMode;
    }
    private String MondayOn = "0:0";

    public void setMondayOn(String hour, String minute) {
        String[] t = MondayOn.split(":");
        String hr = t[0];
        String min = t[1];
        if (!"".equals(hour)) {
            hr = hour;
        }
        if (!"".equals(minute)) {
            min = minute;
        }
        MondayOn = hr + ":" + min;
    }

    @XmlElement
    public String getMondayOn() {
        return MondayOn;
    }
    private String MondayOff = "0:0";

    public void setMondayOff(String hour, String minute) {
        String[] t = MondayOff.split(":");
        String hr = t[0];
        String min = t[1];
        if (!"".equals(hour)) {
            hr = hour;
        }
        if (!"".equals(minute)) {
            min = minute;
        }
        MondayOff = hr + ":" + min;
    }

    @XmlElement
    public String getMondayOff() {
        return MondayOff;
    }
    private String TuesdayOn = "0:0";

    public void seTuesdayOn(String hour, String minute) {
        String[] t = TuesdayOn.split(":");
        String hr = t[0];
        String min = t[1];
        if (!"".equals(hour)) {
            hr = hour;
        }
        if (!"".equals(minute)) {
            min = minute;
        }
        TuesdayOn = hr + ":" + min;
    }

    @XmlElement
    public String getTuesdayOn() {
        return TuesdayOn;
    }
    private String TuesdayOff = "0:0";

    public void setTuesdayOff(String hour, String minute) {
        String[] t = TuesdayOff.split(":");
        String hr = t[0];
        String min = t[1];
        if (!"".equals(hour)) {
            hr = hour;
        }
        if (!"".equals(minute)) {
            min = minute;
        }
        TuesdayOff = hr + ":" + min;
    }

    @XmlElement
    public String getTuesdayOff() {
        return TuesdayOff;
    }
    private String WednesdayOn = "0:0";

    public void setWednesdayOn(String hour, String minute) {
        String[] t = WednesdayOn.split(":");
        String hr = t[0];
        String min = t[1];
        if (!"".equals(hour)) {
            hr = hour;
        }
        if (!"".equals(minute)) {
            min = minute;
        }
        WednesdayOn = hr + ":" + min;
    }

    @XmlElement
    public String getWednesdayOn() {
        return WednesdayOn;
    }
    private String WednesdayOff = "0:0";

    public void setWednesdayOff(String hour, String minute) {
        String[] t = WednesdayOff.split(":");
        String hr = t[0];
        String min = t[1];
        if (!"".equals(hour)) {
            hr = hour;
        }
        if (!"".equals(minute)) {
            min = minute;
        }
        WednesdayOff = hr + ":" + min;
    }

    @XmlElement
    public String getWednesdayOff() {
        return WednesdayOff;
    }
    private String ThursdayOn = "0:0";

    public void setThursdayOn(String hour, String minute) {
        String[] t = ThursdayOn.split(":");
        String hr = t[0];
        String min = t[1];
        if (!"".equals(hour)) {
            hr = hour;
        }
        if (!"".equals(minute)) {
            min = minute;
        }
        ThursdayOn = hr + ":" + min;
    }

    @XmlElement
    public String getThursdayOn() {
        return ThursdayOn;
    }
    private String ThursdayOff = "0:0";

    public void setThursdayOff(String hour, String minute) {
        String[] t = ThursdayOff.split(":");
        String hr = t[0];
        String min = t[1];
        if (!"".equals(hour)) {
            hr = hour;
        }
        if (!"".equals(minute)) {
            min = minute;
        }
        ThursdayOff = hr + ":" + min;
    }

    @XmlElement
    public String getThursdayOff() {
        return ThursdayOff;
    }
    private String FridayOn = "0:0";

    public void setFridayOn(String hour, String minute) {
        String[] t = FridayOn.split(":");
        String hr = t[0];
        String min = t[1];
        if (!"".equals(hour)) {
            hr = hour;
        }
        if (!"".equals(minute)) {
            min = minute;
        }
        FridayOn = hr + ":" + min;
    }

    @XmlElement
    public String getFridayOn() {
        return FridayOn;
    }
    private String FridayOff = "0:0";

    public void setFridayOff(String hour, String minute) {
        String[] t = FridayOff.split(":");
        String hr = t[0];
        String min = t[1];
        if (!"".equals(hour)) {
            hr = hour;
        }
        if (!"".equals(minute)) {
            min = minute;
        }
        FridayOff = hr + ":" + min;
    }

    @XmlElement
    public String getFridayOff() {
        return FridayOn;
    }
    private String SaturdayOn = "0:0";

    public void setSaturdayOn(String hour, String minute) {
        String[] t = SaturdayOn.split(":");
        String hr = t[0];
        String min = t[1];
        if (!"".equals(hour)) {
            hr = hour;
        }
        if (!"".equals(minute)) {
            min = minute;
        }
        SaturdayOn = hr + ":" + min;
    }

    @XmlElement
    public String getSaturdayOn() {
        return SaturdayOn;
    }
    private String SaturdayOff = "0:0";

    public void setSaturdayOff(String hour, String minute) {
        String[] t = SaturdayOff.split(":");
        String hr = t[0];
        String min = t[1];
        if (!"".equals(hour)) {
            hr = hour;
        }
        if (!"".equals(minute)) {
            min = minute;
        }
        SaturdayOff = hr + ":" + min;
    }

    @XmlElement
    public String getSaturdayOff() {
        return SaturdayOff;
    }
    private String SundayOn = "0:0";

    public void setSundayOn(String hour, String minute) {
        String[] t = SundayOn.split(":");
        String hr = t[0];
        String min = t[1];
        if (!"".equals(hour)) {
            hr = hour;
        }
        if (!"".equals(minute)) {
            min = minute;
        }
        SundayOn = hr + ":" + min;
    }

    @XmlElement
    public String getSundayOn() {
        return SundayOn;
    }
    private String SundayOff = "0:0";

    public void setSundayOff(String hour, String minute) {
        String[] t = SundayOff.split(":");
        String hr = t[0];
        String min = t[1];
        if (!"".equals(hour)) {
            hr = hour;
        }
        if (!"".equals(minute)) {
            min = minute;
        }
        SundayOff = hr + ":" + min;
    }

    @XmlElement
    public String getSundayOff() {
        return SundayOff;
    }
    private Date LastUpdateTime = new Date();

    @XmlElement
    public void setLastUpdateTime(Date date) {
        LastUpdateTime = date;
    }

    public Date getLastUpdateTime() {
        return LastUpdateTime;
    }
// </editor-fold>
}
