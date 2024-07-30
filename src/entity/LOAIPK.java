/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.util.Objects;

/**
 *
 * @author Tun
 */
public class LOAIPK {
    private int ID_LPK; 
    private String TENLPK, MOTA;

    public LOAIPK(int ID_LPK, String TENLPK, String MOTA) {
        this.ID_LPK = ID_LPK;
        this.TENLPK = TENLPK;
        this.MOTA = MOTA;
    }

    public LOAIPK() {
    }

    public int getID_LPK() {
        return ID_LPK;
    }

    public void setID_LPK(int ID_LPK) {
        this.ID_LPK = ID_LPK;
    }

    public String getTENLPK() {
        return TENLPK;
    }

    public void setTENLPK(String TENLPK) {
        this.TENLPK = TENLPK;
    }

    public String getMOTA() {
        return MOTA;
    }

    public void setMOTA(String MOTA) {
        this.MOTA = MOTA;
    }

    @Override
    public String toString() {
        return TENLPK;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.TENLPK);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LOAIPK other = (LOAIPK) obj;
        return Objects.equals(this.TENLPK, other.TENLPK);
    }

}
