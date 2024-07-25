/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author Tun
 */
public class KHACHHANG {
    private int ID_KH;
    private String HOTEN;
    private String DIENTHOAI;
    private String DIACHI;
    private int TICHDIEM;

    public KHACHHANG(int ID_KH, String HOTEN, String DIENTHOAI, String DIACHI, int TICHDIEM) {
        this.ID_KH = ID_KH;
        this.HOTEN = HOTEN;
        this.DIENTHOAI = DIENTHOAI;
        this.DIACHI = DIACHI;
        this.TICHDIEM = TICHDIEM;
    }

    public KHACHHANG() {
    }

    public int getID_KH() {
        return ID_KH;
    }

    public void setID_KH(int ID_KH) {
        this.ID_KH = ID_KH;
    }

    public String getHOTEN() {
        return HOTEN;
    }

    public void setHOTEN(String HOTEN) {
        this.HOTEN = HOTEN;
    }

    public String getDIENTHOAI() {
        return DIENTHOAI;
    }

    public void setDIENTHOAI(String DIENTHOAI) {
        this.DIENTHOAI = DIENTHOAI;
    }

    public String getDIACHI() {
        return DIACHI;
    }

    public void setDIACHI(String DIACHI) {
        this.DIACHI = DIACHI;
    }

    public int getTICHDIEM() {
        return TICHDIEM;
    }

    public void setTICHDIEM(int TICHDIEM) {
        this.TICHDIEM = TICHDIEM;
    }
    
    
}
