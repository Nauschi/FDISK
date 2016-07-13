/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import BL.BL;
import Database.DB_Access;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author philipp
 */
public class MitgliedsStunden extends Mitglied implements Serializable {

    int intMinuten;
    String strInstanznummer;
    int intMinutenUb;
    int intMinutenEb;
    int intMinutenTb;
    String strInstanzname;
    int intAnzUb;
    int intAnzEb;
    int intAnzTb;
    private BL bl = new BL();

    public MitgliedsStunden(int intMinuten, String strInstanznummer, int intAnzUb, int intAnzEb, int intAnzTb, int intMinutenUb, int intMinutenEb, int intMinutenTb, int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname, String strInstanzname) throws ClassNotFoundException {
        super(intId_Personen, strStammblattnummer, strDienstgrad, strTitel, strVorname, strZuname);
        this.intMinuten = intMinuten;
        this.strInstanznummer = strInstanznummer;
        this.intMinutenUb = intMinutenUb;
        this.intMinutenEb = intMinutenEb;
        this.intMinutenTb = intMinutenTb;
        this.strInstanzname = strInstanzname;
        this.intAnzEb = intAnzEb;
        this.intAnzTb = intAnzTb;
        this.intAnzUb = intAnzUb;

    }

    public String getStrInstanzname() {
        return strInstanzname;
    }

    public void setStrInstanzname(String strInstanzname) {
        this.strInstanzname = strInstanzname;
    }

    public int getIntMinuten() {
        return intMinuten;
    }

    public void setIntMinuten(int intMinuten) {
        this.intMinuten = intMinuten;
    }

    public String getStrInstanznummer() {
        return strInstanznummer;
    }

    public void setStrInstanznummer(String strInstanznummer) {
        this.strInstanznummer = strInstanznummer;
    }

    public int getIntMinutenUb() {
        return intMinutenUb;
    }

    public void setIntMinutenUb(int intMinutenUb) {
        this.intMinutenUb = intMinutenUb;
    }

    public int getIntMinutenEb() {
        return intMinutenEb;
    }

    public void setIntMinutenEb(int intMinutenEb) {
        this.intMinutenEb = intMinutenEb;
    }

    public int getIntMinutenTb() {
        return intMinutenTb;
    }

    public void setIntMinutenTb(int intMinutenTb) {
        this.intMinutenTb = intMinutenTb;
    }

    public int getIntAnzUb() {
        return intAnzUb;
    }

    public void setIntAnzUb(int intAnzUb) {
        this.intAnzUb = intAnzUb;
    }

    public int getIntAnzEb() {
        return intAnzEb;
    }

    public void setIntAnzEb(int intAnzEb) {
        this.intAnzEb = intAnzEb;
    }

    public int getIntAnzTb() {
        return intAnzTb;
    }

    public void setIntAnzTb(int intAnzTb) {
        this.intAnzTb = intAnzTb;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.intMinuten;
        hash = 29 * hash + Objects.hashCode(this.strInstanznummer);
        hash = 29 * hash + this.intMinutenUb;
        hash = 29 * hash + this.intMinutenEb;
        hash = 29 * hash + this.intMinutenTb;
        hash = 29 * hash + Objects.hashCode(this.strInstanzname);
        hash = 29 * hash + this.intAnzUb;
        hash = 29 * hash + this.intAnzEb;
        hash = 29 * hash + this.intAnzTb;
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
        final MitgliedsStunden other = (MitgliedsStunden) obj;
        if (this.intMinuten != other.intMinuten) {
            return false;
        }
        if (this.intMinutenUb != other.intMinutenUb) {
            return false;
        }
        if (this.intMinutenEb != other.intMinutenEb) {
            return false;
        }
        if (this.intMinutenTb != other.intMinutenTb) {
            return false;
        }
        if (this.intAnzUb != other.intAnzUb) {
            return false;
        }
        if (this.intAnzEb != other.intAnzEb) {
            return false;
        }
        if (this.intAnzTb != other.intAnzTb) {
            return false;
        }
        if (!Objects.equals(this.strInstanznummer, other.strInstanznummer)) {
            return false;
        }
        if (!Objects.equals(this.strInstanzname, other.strInstanzname)) {
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

        strZuname = bl.formatiereAusgabe(strZuname);
        strVorname = bl.formatiereAusgabe(strVorname);

        String strHtml = "<tr><td class='STB'>"
                + strStammblattnummer + "</td><td class='DGR'>"
                + strDienstgrad + "</td><td>"
                + strTitel + "</td><td>"
                + strVorname + "</td><td>"
                + strZuname + "</td><td>"
                + strInstanzname + "</td><td>"
                + ((intMinuten / 60 != 0) ? intMinuten / 60 + "h " : "") + intMinuten % 60 + "min"
                + "<div style='display:none'>"
                + intMinutenEb + ";" + intMinutenTb + ";" + intMinutenUb + ";" + intAnzEb + ";" + intAnzTb + ";" + intAnzUb
                + "</div></td>"
                + "</tr>";

//        strHtml += "<tr style='display:none'>"
//                + "<td colspan= '7'>"
//                + "<table style='page-break-after: always;'>"
//                + "<thead>"
//                + "<tr>"
//                + "<th>Bericht</th>"
//                + "<th>Anz Stunden</th>"
//                + "</tr>"
//                + "</thead>"
//                + "<tbody>"
//                + "<tr>"
//                + "<td>Einsatzbericht</td>"
//                + "<td>" + ((intMinutenEb / 60 != 0) ? intMinutenEb / 60 + "h " : "") + intMinutenEb % 60 + "min</td>"
//                + "</tr>"
//                + "<tr>"
//                + "<td>Tätigkeitsbericht</td>"
//                + "<td>" + ((intMinutenTb / 60 != 0) ? intMinutenTb / 60 + "h " : "") + intMinutenTb % 60 + "min</td>"
//                + "</tr>"
//                + "<tr>"
//                + "<td>Übungsbericht</td>"
//                + "<td>" + ((intMinutenUb / 60 != 0) ? intMinutenUb / 60 + "h " : "") + intMinutenUb % 60 + "min</td>"
//                + "</tr>"
//                + "</tbody>"
//                + "</table>"
//                + "</tr>";
        return strHtml;
    }

}
