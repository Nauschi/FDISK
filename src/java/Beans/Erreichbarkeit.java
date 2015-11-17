/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.Objects;

/**
 *
 * @author kinco_000
 */
public class Erreichbarkeit {

    private String strErreichbarkeitsArt;

    private String strCode;
    private int intPersID;

    public Erreichbarkeit(String strErreichbarkeitsArt, String strCode, int intPersID) {
        
        this.strErreichbarkeitsArt = strErreichbarkeitsArt;
        this.strCode = strCode;
        this.intPersID = intPersID;
    }

    public String getStrErreichbarkeitsArt() {
        return strErreichbarkeitsArt;
    }

    public void setStrErreichbarkeitsArt(String strErreichbarkeitsArt) {
        this.strErreichbarkeitsArt = strErreichbarkeitsArt;
    }

    public String getStrCode() {
        return strCode;
    }

    public void setStrCode(String strCode) {
        this.strCode = strCode;
    }

    public int getIntPersID() {
        return intPersID;
    }

    public void setIntPersID(int intPersID) {
        this.intPersID = intPersID;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Erreichbarkeit other = (Erreichbarkeit) obj;
        if (!Objects.equals(this.strErreichbarkeitsArt, other.strErreichbarkeitsArt)) {
            return false;
        }
        if (!Objects.equals(this.strCode, other.strCode)) {
            return false;
        }
        if (this.intPersID != other.intPersID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Erreichbarkeit{" + "strErreichbarkeitsArt=" + strErreichbarkeitsArt + ", strCode=" + strCode + ", intPersID=" + intPersID + '}';
    }

   
    
    

}
