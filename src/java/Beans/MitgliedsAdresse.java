/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import BL.BL;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author kinco_000
 */
public class MitgliedsAdresse extends Mitglied implements Serializable {

    private int intId_Adressen;
    private String strStrasse;
    private String strNummer;
    private String strStiege;
    private int intPLZ;
    private String strOrt;
    private boolean boBemerkung;

    private BL bl = new BL();

    public MitgliedsAdresse(int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname, boolean boCheckbox, int intId_Adressen, String strStrasse, String intNummer, String strStiege, int intPLZ, String strOrt, String strFubwehr, boolean boBemerkung) throws ClassNotFoundException {
        super(intId_Personen, strStammblattnummer, strDienstgrad, strTitel, strVorname, strZuname, strFubwehr);
        this.intId_Adressen = intId_Adressen;
        this.strStrasse = strStrasse;
        this.strNummer = intNummer;
        this.strStiege = strStiege;
        this.intPLZ = intPLZ;
        this.strOrt = strOrt;
        this.boBemerkung = boBemerkung;

    }

    public int getIntId_Adressen() {
        return intId_Adressen;
    }

    public void setIntId_Adressen(int intId_Adressen) {
        this.intId_Adressen = intId_Adressen;
    }

    public String getStrStrasse() {
        return strStrasse;
    }

    public void setStrStrasse(String strStrasse) {
        this.strStrasse = strStrasse;
    }

    public String getStrNummer() {
        return strNummer;
    }

    public void setStrNummer(String strNummer) {
        this.strNummer = strNummer;
    }

    public String getStrStiege() {
        return strStiege;
    }

    public void setStrStiege(String strStiege) {
        this.strStiege = strStiege;
    }

    public int getIntPLZ() {
        return intPLZ;
    }

    public void setIntPLZ(int intPLZ) {
        this.intPLZ = intPLZ;
    }

    public String getStrOrt() {
        return strOrt;
    }

    public void setStrOrt(String strOrt) {
        this.strOrt = strOrt;
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
        final MitgliedsAdresse other = (MitgliedsAdresse) obj;
        if (this.intId_Adressen != other.intId_Adressen) {
            return false;
        }
        if (!Objects.equals(this.strStrasse, other.strStrasse)) {
            return false;
        }
        if (!Objects.equals(this.strNummer, other.strNummer)) {
            return false;
        }
        if (!Objects.equals(this.strStiege, other.strStiege)) {
            return false;
        }
        if (this.intPLZ != other.intPLZ) {
            return false;
        }
        if (!Objects.equals(this.strOrt, other.strOrt)) {
            return false;
        }
        if (this.boBemerkung != other.boBemerkung) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
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
        if (strStrasse == null) {
            strStrasse = "";
        }
        if (strNummer == null) {
            strNummer = "";
        }
        if (strOrt == null) {
            strOrt = "";
        }
        if (strFubwehr == null) {
            strFubwehr = "";
        }

        strZuname = bl.formatiereAusgabe(strZuname);
        strVorname = bl.formatiereAusgabe(strVorname);
        strStrasse = bl.formatiereAusgabe(strStrasse);
        strOrt = bl.formatiereAusgabe(strOrt);

//        String strHtml = "<tr><td>"
//                + strFubwehr + "</td><td>"
//                + strStammblattnummer + "</td><td>"
//                + strDienstgrad + "</td><td>"
//                + strTitel + "</td><td>"
//                + strVorname + "</td><td>"
//                + strZuname + "</td><td>"
//                + strStrasse + " " + strNummer + ", " + intPLZ + " " + strOrt + "</td>"
//                + "<td class='bemerkung'></td></tr>";

//        String strHtml = "<tr><td>"
//                + strFubwehr + "</td><td>"
//                + strStammblattnummer + "</td><td>"
//                + strDienstgrad + "</td><td>"
//                + strTitel + "</td><td>"
//                + strVorname + "</td><td>"
//                + strZuname + "</td><td style='display:none;'>"
//                + strStrasse + " " + strNummer + ", " + intPLZ + " " + strOrt + "</td><td style='display:none;'>"
//                + strStrasse + "</td><td style='display:none;'>"
//                + strNummer + "</td><td style='display:none;'>"
//                + intPLZ + "</td><td style='display:none;'>"
//                + strOrt + "</td>"
//                + "<td class='bemerkung'></td></tr>";

        String strHtml = "<tr><td id='multipleFb'>"
                + strFubwehr + "</td><td>"
                + strStammblattnummer + "</td><td>"
                + strDienstgrad + "</td><td>"
                + strTitel + "</td><td>"
                + strVorname + "</td><td>"
                + strZuname + "</td><td>"
                + strStrasse + "</td><td id='remove'>"
                + strNummer + "</td><td id='remove'>"
                + intPLZ + "</td><td id='remove'>"
                + strOrt + "</td>"
                + "<td class='bemerkung'></td></tr>";

        return strHtml;
    }

    public String toCsvString() {
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
        if (strStrasse == null) {
            strStrasse = "";
        }
        if (strNummer == null) {
            strNummer = "";
        }
        if (strOrt == null) {
            strOrt = "";
        }
        if (strFubwehr == null) {
            strFubwehr = "";
        }

        strZuname = bl.formatiereAusgabe(strZuname);
        strVorname = bl.formatiereAusgabe(strVorname);
        strStrasse = bl.formatiereAusgabe(strStrasse);
        strOrt = bl.formatiereAusgabe(strOrt);

        String strHtml = "<tr><td>"
                + strFubwehr + "</td><td>"
                + strStammblattnummer + "</td><td>"
                + strDienstgrad + "</td><td>"
                + strTitel + "</td><td>"
                + strVorname + "</td><td>"
                + strZuname + "</td><td>"
                + strStrasse + "</td><td>"
                + strNummer + "</td><td>"
                + intPLZ + "</td><td>"
                + strOrt + "</td>"
                + "<td class='bemerkung'></td></tr>";

        return strHtml;
    }

}
