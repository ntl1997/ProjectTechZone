/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.util.Date;

/**
 *
 * @author Tun
 */
public class CHITIETHOADON {
    private int ID_HDCT;
    private int MAHD, MASP;
    private Date NGAYLAP;
    private float GIABAN; 
    private int SOLUONG;

    public CHITIETHOADON(int ID_HDCT, int MAHD, int MASP, Date NGAYLAP, float GIABAN, int SOLUONG) {
        this.ID_HDCT = ID_HDCT;
        this.MAHD = MAHD;
        this.MASP = MASP;
        this.NGAYLAP = NGAYLAP;
        this.GIABAN = GIABAN;
        this.SOLUONG = SOLUONG;
    }

    public CHITIETHOADON() {
    }

    public int getID_HDCT() {
        return ID_HDCT;
    }

    public void setID_HDCT(int ID_HDCT) {
        this.ID_HDCT = ID_HDCT;
    }

    public int getMAHD() {
        return MAHD;
    }

    public void setMAHD(int MAHD) {
        this.MAHD = MAHD;
    }

    public int getMASP() {
        return MASP;
    }

    public void setMASP(int MASP) {
        this.MASP = MASP;
    }

    public Date getNGAYLAP() {
        return NGAYLAP;
    }

    public void setNGAYLAP(Date NGAYLAP) {
        this.NGAYLAP = NGAYLAP;
    }

    public float getGIABAN() {
        return GIABAN;
    }

    public void setGIABAN(float GIABAN) {
        this.GIABAN = GIABAN;
    }

    public int getSOLUONG() {
        return SOLUONG;
    }

    public void setSOLUONG(int SOLUONG) {
        this.SOLUONG = SOLUONG;
    }
    
    
    
}
