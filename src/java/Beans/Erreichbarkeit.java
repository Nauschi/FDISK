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
    private String strErreichbarkeitsTyp;
    private String strSichtbarkeit;

    public Erreichbarkeit(int intId_Erreichbarkeiten, String strErreichbarkeitsArt, String strErreichbarkeitsTyp, String strSichtbarkeit) {
        this.intId_Erreichbarkeiten = intId_Erreichbarkeiten;
        this.strErreichbarkeitsArt = strErreichbarkeitsArt;
        this.strErreichbarkeitsTyp = strErreichbarkeitsTyp;
        this.strSichtbarkeit = strSichtbarkeit;
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

    public String getStrErreichbarkeitsTyp() {
        return strErreichbarkeitsTyp;
    }

    public void setStrErreichbarkeitsTyp(String strErreichbarkeitsTyp) {
        this.strErreichbarkeitsTyp = strErreichbarkeitsTyp;
    }

    public String getStrSichtbarkeit() {
        return strSichtbarkeit;
    }

    public void setStrSichtbarkeit(String strSichtbarkeit) {
        this.strSichtbarkeit = strSichtbarkeit;
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
        if (!Objects.equals(this.strErreichbarkeitsTyp, other.strErreichbarkeitsTyp)) {
            return false;
        }
        if (!Objects.equals(this.strSichtbarkeit, other.strSichtbarkeit)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Erreichbarkeit{" + "intId_Erreichbarkeiten=" + intId_Erreichbarkeiten + ", strErreichbarkeitsArt=" + strErreichbarkeitsArt + ", strErreichbarkeitsTyp=" + strErreichbarkeitsTyp + ", strSichtbarkeit=" + strSichtbarkeit + '}';
    }
    
    
}
