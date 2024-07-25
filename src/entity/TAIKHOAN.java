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
public class TAIKHOAN {
    private int ID_TK;
    private int MACV;
    private String TENDN,TENNV, EMAIL,MATKHAU,DIACHI,DIENTHOAI;
    private Date NGAYSINH; 
    private int GIOITINH;
    private int TRANGTHAI; 

    public TAIKHOAN(int ID_TK, int MACV, String TENDN, String TENNV, String EMAIL, String MATKHAU, String DIACHI, String DIENTHOAI, Date NGAYSINH, int GIOITINH, int TRANGTHAI) {
        this.ID_TK = ID_TK;
        this.MACV = MACV;
        this.TENDN = TENDN;
        this.TENNV = TENNV;
        this.EMAIL = EMAIL;
        this.MATKHAU = MATKHAU;
        this.DIACHI = DIACHI;
        this.DIENTHOAI = DIENTHOAI;
        this.NGAYSINH = NGAYSINH;
        this.GIOITINH = GIOITINH;
        this.TRANGTHAI = TRANGTHAI;
    }

    public TAIKHOAN() {
    }

    public int getID_TK() {
        return ID_TK;
    }

    public void setID_TK(int ID_TK) {
        this.ID_TK = ID_TK;
    }

    public int getMACV() {
        return MACV;
    }

    public void setMACV(int MACV) {
        this.MACV = MACV;
    }

    public String getTENDN() {
        return TENDN;
    }

    public void setTENDN(String TENDN) {
        this.TENDN = TENDN;
    }

    public String getTENNV() {
        return TENNV;
    }

    public void setTENNV(String TENNV) {
        this.TENNV = TENNV;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getMATKHAU() {
        return MATKHAU;
    }

    public void setMATKHAU(String MATKHAU) {
        this.MATKHAU = MATKHAU;
    }

    public String getDIACHI() {
        return DIACHI;
    }

    public void setDIACHI(String DIACHI) {
        this.DIACHI = DIACHI;
    }

    public String getDIENTHOAI() {
        return DIENTHOAI;
    }

    public void setDIENTHOAI(String DIENTHOAI) {
        this.DIENTHOAI = DIENTHOAI;
    }

    public Date getNGAYSINH() {
        return NGAYSINH;
    }

    public void setNGAYSINH(Date NGAYSINH) {
        this.NGAYSINH = NGAYSINH;
    }

    public int getGIOITINH() {
        return GIOITINH;
    }

    public void setGIOITINH(int GIOITINH) {
        this.GIOITINH = GIOITINH;
    }

    public int getTRANGTHAI() {
        return TRANGTHAI;
    }

    public void setTRANGTHAI(int TRANGTHAI) {
        this.TRANGTHAI = TRANGTHAI;
    }
    
    
            
}
