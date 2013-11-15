/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clt.api;

/**
 *
 * @author Terence
 */
public class PacketFrame {
        public byte STX;
        public byte IDT;
        public byte Type;
        public byte [] Command=new byte[3];
        public byte[] Value;
        public byte ETX;
        public String IP;

        public PacketFrame(){}
        public PacketFrame(byte stx, byte idt ,byte type,byte [] cmd,byte [] value, byte etx,String ip)
        {
            this.STX=stx;
            this.IDT=idt;
            this.Type=type;
            this.Command=cmd;
            this.Value=value;
            this.ETX=etx;
            this.IP=ip;
        }
}
