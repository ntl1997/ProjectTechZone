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
public class HANGSX {
    private int ID_HANGSX;
    private String TENHANGSX, MOTA;

    public HANGSX(int ID_HANGSX, String TENHANGSX, String MOTA) {
        this.ID_HANGSX = ID_HANGSX;
        this.TENHANGSX = TENHANGSX;
        this.MOTA = MOTA;
    }

    public HANGSX() {
    }

    public int getID_HANGSX() {
        return ID_HANGSX;
    }

    public void setID_HANGSX(int ID_HANGSX) {
        this.ID_HANGSX = ID_HANGSX;
    }

    public String getTENHANGSX() {
        return TENHANGSX;
    }

    public void setTENHANGSX(String TENHANGSX) {
        this.TENHANGSX = TENHANGSX;
    }

    public String getMOTA() {
        return MOTA;
    }

    public void setMOTA(String MOTA) {
        this.MOTA = MOTA;
    }

    @Override
    public String toString() {
        return TENHANGSX;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.TENHANGSX);
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
        final HANGSX other = (HANGSX) obj;
        return Objects.equals(this.TENHANGSX, other.TENHANGSX);
    }
    
}
