/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author Tun
 */
public class SANPHAM {
    private int ID_SP;
    private String TENSP;
    private int MALOAIPK;
    private int MAHANGSX; 
    private int SOLUONG; 
    private float GIABAN;
    private String HINH; 
    private String MOTA;

    public SANPHAM(int ID_SP, String TENSP, int MALOAIPK, int MAHANGSX, int SOLUONG, float GIABAN, String HINH, String MOTA) {
        this.ID_SP = ID_SP;
        this.TENSP = TENSP;
        this.MALOAIPK = MALOAIPK;
        this.MAHANGSX = MAHANGSX;
        this.SOLUONG = SOLUONG;
        this.GIABAN = GIABAN;
        this.HINH = HINH;
        this.MOTA = MOTA;
    }

    public SANPHAM() {
    }

    public int getID_SP() {
        return ID_SP;
    }

    public void setID_SP(int ID_SP) {
        this.ID_SP = ID_SP;
    }

    public String getTENSP() {
        return TENSP;
    }

    public void setTENSP(String TENSP) {
        this.TENSP = TENSP;
    }

    public int getMALOAIPK() {
        return MALOAIPK;
    }

    public void setMALOAIPK(int MALOAIPK) {
        this.MALOAIPK = MALOAIPK;
    }

    public int getMAHANGSX() {
        return MAHANGSX;
    }

    public void setMAHANGSX(int MAHANGSX) {
        this.MAHANGSX = MAHANGSX;
    }

    public int getSOLUONG() {
        return SOLUONG;
    }

    public void setSOLUONG(int SOLUONG) {
        this.SOLUONG = SOLUONG;
    }

    public float getGIABAN() {
        return GIABAN;
    }

    public void setGIABAN(float GIABAN) {
        this.GIABAN = GIABAN;
    }

    public String getHINH() {
        return HINH;
    }

    public void setHINH(String HINH) {
        this.HINH = HINH;
    }

    public String getMOTA() {
        return MOTA;
    }

    public void setMOTA(String MOTA) {
        this.MOTA = MOTA;
    }
    
}
