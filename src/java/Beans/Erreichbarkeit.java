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
    private int intId_Erreichbarkeiten;
    private String strErreichbarkeitsArt;
    private String strSichtbarkeit;
    private String strCode;
    private int intPersID;

    public Erreichbarkeit(int intId_Erreichbarkeiten, String strErreichbarkeitsArt, String strSichtbarkeit, String strCode, int intPersID) {
        this.intId_Erreichbarkeiten = intId_Erreichbarkeiten;
        this.strErreichbarkeitsArt = strErreichbarkeitsArt;
        this.strSichtbarkeit = strSichtbarkeit;
        this.strCode = strCode;
        this.intPersID = intPersID;
    }

    public int getIntId_Erreichbarkeiten() {
        return intId_Erreichbarkeiten;
    }

    public void setIntId_Erreichbarkeiten(int intId_Erreichbarkeiten) {
        this.intId_Erreichbarkeiten = intId_Erreichbarkeiten;
    }

    public String getStrErreichbarkeitsArt() {
        return strErreichbarkeitsArt;
    }

    public void setStrErreichbarkeitsArt(String strErreichbarkeitsArt) {
        this.strErreichbarkeitsArt = strErreichbarkeitsArt;
    }

    public String getStrSichtbarkeit() {
        return strSichtbarkeit;
    }

    public void setStrSichtbarkeit(String strSichtbarkeit) {
        this.strSichtbarkeit = strSichtbarkeit;
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
        if (this.intId_Erreichbarkeiten != other.intId_Erreichbarkeiten) {
            return false;
        }
        if (!Objects.equals(this.strErreichbarkeitsArt, other.strErreichbarkeitsArt)) {
            return false;
        }
        if (!Objects.equals(this.strSichtbarkeit, other.strSichtbarkeit)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Erreichbarkeit{" + "intId_Erreichbarkeiten=" + intId_Erreichbarkeiten + ", strErreichbarkeitsArt=" + strErreichbarkeitsArt + ", strSichtbarkeit=" + strSichtbarkeit + ", strCode=" + strCode + '}';
    }

    
    
    
}
