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
    private int intId_Erreichbarkeiten;
    private String strErreichbarkeitsArt;
    private String strSichtbarkeit;
    private String strCode;
    private boolean boBemerkung;

    public MitgliedsErreichbarkeit(int intId_Erreichbarkeiten, String strErreichbarkeitsArt, String strSichtbarkeit, String strCode, boolean boBemerkung, int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname) {
        super(intId_Personen, strStammblattnummer, strDienstgrad, strTitel, strVorname, strZuname);
        this.intId_Erreichbarkeiten = intId_Erreichbarkeiten;
        this.strErreichbarkeitsArt = strErreichbarkeitsArt;
        this.strSichtbarkeit = strSichtbarkeit;
        this.strCode = strCode;
        this.boBemerkung = boBemerkung;
    }

   


    public boolean getBoBemerkung() {
        return boBemerkung;
    }

    public void setBoBemerkung(boolean boBemerkung) {
        this.boBemerkung = boBemerkung;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MitgliedsErreichbarkeit other = (MitgliedsErreichbarkeit) obj;
        if (this.intId_Erreichbarkeiten != other.intId_Erreichbarkeiten) {
            return false;
        }
        if (!Objects.equals(this.strErreichbarkeitsArt, other.strErreichbarkeitsArt)) {
            return false;
        }
        if (!Objects.equals(this.strSichtbarkeit, other.strSichtbarkeit)) {
            return false;
        }
        if (!Objects.equals(this.strCode, other.strCode)) {
            return false;
        }
        if (this.boBemerkung != other.boBemerkung) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        // return String.format("%s %s %s %s", strVorname, strZuname, strStammblattnummer, strTitel);
        String strHtml = "<tr><td>"
                + strStammblattnummer + "</td><td>"
                + strDienstgrad + "</td><td>"
                + strTitel + "</td><td>"
                + strVorname + "</td><td>"
                + strZuname + "</td><td>"
                + strErreichbarkeitsArt + ": " + strCode
                + "</td><td></td></tr>";

        return strHtml;
    }
    

}
