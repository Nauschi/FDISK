/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import BL.BL;
import Database.DB_Access;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author kinco_000
 */
public class MitgliedsErreichbarkeit extends Mitglied implements Serializable {

    LinkedList<Erreichbarkeit> liErreichbarkeiten;
    private boolean boBemerkung;

    private BL bl = new BL();

    public MitgliedsErreichbarkeit(boolean boBemerkung, int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname, String strFubwehr) throws ClassNotFoundException {
        super(intId_Personen, strStammblattnummer, strDienstgrad, strTitel, strVorname, strZuname, strFubwehr);
        this.boBemerkung = boBemerkung;

    }

    public LinkedList<Erreichbarkeit> getLiErreichbarkeiten() {
        return liErreichbarkeiten;
    }

    public void setLiErreichbarkeiten(LinkedList<Erreichbarkeit> liErreichbarkeiten) {
        this.liErreichbarkeiten = liErreichbarkeiten;
    }

    public boolean isBoBemerkung() {
        return boBemerkung;
    }

    public void setBoBemerkung(boolean boBemerkung) {
        this.boBemerkung = boBemerkung;
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
        final MitgliedsErreichbarkeit other = (MitgliedsErreichbarkeit) obj;
        if (!Objects.equals(this.liErreichbarkeiten, other.liErreichbarkeiten)) {
            return false;
        }
        if (this.boBemerkung != other.boBemerkung) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (liErreichbarkeiten != null) {
            if (strStammblattnummer == null) {
                strStammblattnummer = "";
            }
            if (strDienstgrad == null) {
                strDienstgrad = "";
            }
            if (strTitel == null) {
                strTitel = "";
            }
            if (strVorname == null) {
                strVorname = "";
            }
            if (strZuname == null) {
                strZuname = "";
            }
            if (strFubwehr == null) {
                strFubwehr = "";
            }

            strVorname = bl.formatiereAusgabe(strVorname);
            strZuname = bl.formatiereAusgabe(strZuname);

            String strHtml = "<tr><td id='multipleFb'>"
                    + strFubwehr + "</td><td>"
                    + strStammblattnummer + "</td><td>"
                    + strDienstgrad + "</td><td>"
                    + strTitel + "</td><td>"
                    + strVorname + "</td><td>"
                    + strZuname + "</td><td class='erreichbarkeiten'>";

            for (Erreichbarkeit erreichbarkeit : liErreichbarkeiten) {
                //String str = bl.formatiereAusgabe(erreichbarkeit.getStrErreichbarkeitsArt());
                strHtml += "<div>" + erreichbarkeit.getStrErreichbarkeitsArt() + ": " + erreichbarkeit.getStrCode() + "</div>";
            }
            strHtml += "</td><td></td></tr>";

            return strHtml;
        }
        return null;
    }
}
