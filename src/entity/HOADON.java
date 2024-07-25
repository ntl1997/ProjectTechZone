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
public class HOADON {
    private int ID_HD; 
    private int MATAIKHOAN, MAKH;
    private Date NGAYLAP; 
    private float TONGTIEN; 
    private int PHANTRAMGG; 
    private float TICHDIEM; 
    private float THANHTIEN; 
    private int TRANGTHAI;

    public HOADON(int ID_HD, int MATAIKHOAN, int MAKH, Date NGAYLAP, float TONGTIEN, int PHANTRAMGG, float TICHDIEM, float THANHTIEN, int TRANGTHAI) {
        this.ID_HD = ID_HD;
        this.MATAIKHOAN = MATAIKHOAN;
        this.MAKH = MAKH;
        this.NGAYLAP = NGAYLAP;
        this.TONGTIEN = TONGTIEN;
        this.PHANTRAMGG = PHANTRAMGG;
        this.TICHDIEM = TICHDIEM;
        this.THANHTIEN = THANHTIEN;
        this.TRANGTHAI = TRANGTHAI;
    }

    public HOADON() {
    }

    public int getID_HD() {
        return ID_HD;
    }

    public void setID_HD(int ID_HD) {
        this.ID_HD = ID_HD;
    }

    public int getMATAIKHOAN() {
        return MATAIKHOAN;
    }

    public void setMATAIKHOAN(int MATAIKHOAN) {
        this.MATAIKHOAN = MATAIKHOAN;
    }

    public int getMAKH() {
        return MAKH;
    }

    public void setMAKH(int MAKH) {
        this.MAKH = MAKH;
    }

    public Date getNGAYLAP() {
        return NGAYLAP;
    }

    public void setNGAYLAP(Date NGAYLAP) {
        this.NGAYLAP = NGAYLAP;
    }

    public float getTONGTIEN() {
        return TONGTIEN;
    }

    public void setTONGTIEN(float TONGTIEN) {
        this.TONGTIEN = TONGTIEN;
    }

    public int getPHANTRAMGG() {
        return PHANTRAMGG;
    }

    public void setPHANTRAMGG(int PHANTRAMGG) {
        this.PHANTRAMGG = PHANTRAMGG;
    }

    public float getTICHDIEM() {
        return TICHDIEM;
    }

    public void setTICHDIEM(float TICHDIEM) {
        this.TICHDIEM = TICHDIEM;
    }

    public float getTHANHTIEN() {
        return THANHTIEN;
    }

    public void setTHANHTIEN(float THANHTIEN) {
        this.THANHTIEN = THANHTIEN;
    }

    public int getTRANGTHAI() {
        return TRANGTHAI;
    }

    public void setTRANGTHAI(int TRANGTHAI) {
        this.TRANGTHAI = TRANGTHAI;
    }
    
    
}
