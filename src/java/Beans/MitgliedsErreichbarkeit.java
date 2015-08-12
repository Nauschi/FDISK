/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author kinco_000
 */
public class MitgliedsErreichbarkeit extends Mitglied
{
    private LinkedList<Erreichbarkeit> liErreichbarkeiten;
    private String strBemerkung;

    public MitgliedsErreichbarkeit(int intStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname, boolean boCheckbox, LinkedList<Erreichbarkeit> liErreichbarkeiten, String strBemerkung) {
        super(intStammblattnummer, strDienstgrad, strTitel, strVorname, strZuname, boCheckbox);
        this.liErreichbarkeiten = liErreichbarkeiten;
        this.strBemerkung = strBemerkung;
    }

    public LinkedList<Erreichbarkeit> getLiErreichbarkeiten() {
        return liErreichbarkeiten;
    }

    public void setLiErreichbarkeiten(LinkedList<Erreichbarkeit> liErreichbarkeiten) {
        this.liErreichbarkeiten = liErreichbarkeiten;
    }

    public String getStrBemerkung() {
        return strBemerkung;
    }

    public void setStrBemerkung(String strBemerkung) {
        this.strBemerkung = strBemerkung;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final MitgliedsErreichbarkeit other = (MitgliedsErreichbarkeit) obj;
        if (!Objects.equals(this.liErreichbarkeiten, other.liErreichbarkeiten)) {
            return false;
        }
        if (!Objects.equals(this.strBemerkung, other.strBemerkung)) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public String toString() {
        return "MitgliedsErreichbarkeit{" + "liErreichbarkeiten=" + liErreichbarkeiten + ", strBemerkung=" + strBemerkung + '}';
    }
    
    
    
    
}
