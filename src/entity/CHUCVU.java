/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author Tun
 */
public class CHUCVU {
    private int ID_CV;
    private String TENCV;
    private String MOTA;

    public CHUCVU(int ID_CV, String TENCV, String MOTA) {
        this.ID_CV = ID_CV;
        this.TENCV = TENCV;
        this.MOTA = MOTA;
    }

    public CHUCVU() {
    }

    public int getID_CV() {
        return ID_CV;
    }

    public void setID_CV(int ID_CV) {
        this.ID_CV = ID_CV;
    }

    public String getTENCV() {
        return TENCV;
    }

    public void setTENCV(String TENCV) {
        this.TENCV = TENCV;
    }

    public String getMOTA() {
        return MOTA;
    }

    public void setMOTA(String MOTA) {
        this.MOTA = MOTA;
    }
    
    
}
