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
    private int MATAIKHOAN;
    private Date NGAYLAP; 
    private float TONGTIEN; 
    private int PHANTRAMGG; 
    private float THANHTIEN; 
    private int TRANGTHAI;
    private String TenKH;
    private String SDT;
    private String DiaChi;
    private boolean HinhThucTT;
    private String LyDo;

    public HOADON() {
    }

    public HOADON(int ID_HD, int MATAIKHOAN, Date NGAYLAP, float TONGTIEN, int PHANTRAMGG, float THANHTIEN, int TRANGTHAI, String TenKH, String SDT, String DiaChi, boolean HinhThucTT, String LyDo) {
        this.ID_HD = ID_HD;
        this.MATAIKHOAN = MATAIKHOAN;
        this.NGAYLAP = NGAYLAP;
        this.TONGTIEN = TONGTIEN;
        this.PHANTRAMGG = PHANTRAMGG;
        this.THANHTIEN = THANHTIEN;
        this.TRANGTHAI = TRANGTHAI;
        this.TenKH = TenKH;
        this.SDT = SDT;
        this.DiaChi = DiaChi;
        this.HinhThucTT = HinhThucTT;
        this.LyDo = LyDo;
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

    public String getTenKH() {
        return TenKH;
    }

    public void setTenKH(String TenKH) {
        this.TenKH = TenKH;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String DiaChi) {
        this.DiaChi = DiaChi;
    }

    public boolean isHinhThucTT() {
        return HinhThucTT;
    }

    public void setHinhThucTT(boolean HinhThucTT) {
        this.HinhThucTT = HinhThucTT;
    }

    public String getLyDo() {
        return LyDo;
    }

    public void setLyDo(String LyDo) {
        this.LyDo = LyDo;
    }
    
    
}
