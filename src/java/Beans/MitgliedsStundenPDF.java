/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author user
 */
public class MitgliedsStundenPDF implements Serializable {

    private String strStammblattnummer;
    private String strDienstgrad;
    private String strTitel;
    private String strVorname;
    private String strZuname;
    private LinkedList<String> liInstanznamen = new LinkedList();
    private LinkedList<String> liStundenSumme = new LinkedList();

    private int intMinutenEb;
    private int intMinutenTb;
    private int intMinutenUb;

    private int intAnzahlEb;
    private int intAnzahlTb;
    private int intAnzahlUb;

    private String strHead;

    public MitgliedsStundenPDF(String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname, int intMinutenEb, int intMinutenTb, int intMinutenUb, int intAnzEb, int intAnzTb, int intAnzUb) {
        this.strStammblattnummer = strStammblattnummer;
        this.strDienstgrad = strDienstgrad;
        this.strTitel = strTitel;
        this.strVorname = strVorname;
        this.strZuname = strZuname;
        this.intMinutenEb = intMinutenEb;
        this.intMinutenTb = intMinutenTb;
        this.intMinutenUb = intMinutenUb;
        this.intAnzahlEb = intAnzEb;
        this.intAnzahlTb = intAnzTb;
        this.intAnzahlUb = intAnzUb;
    }

    public void addInstanzname(String strInstanzname) {
        liInstanznamen.add(strInstanzname);
    }

    public void addStundenSumme(String strStundenSumme) {
        liStundenSumme.add(strStundenSumme);
    }

    public String getStrStammblattnummer() {
        return strStammblattnummer;
    }

    public void setStrStammblattnummer(String strStammblattnummer) {
        this.strStammblattnummer = strStammblattnummer;
    }

    public String getStrDienstgrad() {
        return strDienstgrad;
    }

    public void setStrDienstgrad(String strDienstgrad) {
        this.strDienstgrad = strDienstgrad;
    }

    public String getStrTitel() {
        return strTitel;
    }

    public void setStrTitel(String strTitel) {
        this.strTitel = strTitel;
    }

    public String getStrVorname() {
        return strVorname;
    }

    public void setStrVorname(String strVorname) {
        this.strVorname = strVorname;
    }

    public String getStrZuname() {
        return strZuname;
    }

    public void setStrZuname(String strZuname) {
        this.strZuname = strZuname;
    }

    public LinkedList<String> getLiInstanznamen() {
        return liInstanznamen;
    }

    public void setLiInstanznamen(LinkedList<String> liInstanznamen) {
        this.liInstanznamen = liInstanznamen;
    }

    public LinkedList<String> getLiStundenSumme() {
        return liStundenSumme;
    }

    public void setLiStundenSumme(LinkedList<String> liStundenSumme) {
        this.liStundenSumme = liStundenSumme;
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

    public String getStrHead() {
        return strHead;
    }

    public void setStrHead(String strHead) {
        this.strHead = strHead;
    }

    public int getIntAnzahlEb() {
        return intAnzahlEb;
    }

    public void setIntAnzahlEb(int intAnzahlEb) {
        this.intAnzahlEb = intAnzahlEb;
    }

    public int getIntAnzahlTb() {
        return intAnzahlTb;
    }

    public void setIntAnzahlTb(int intAnzahlTb) {
        this.intAnzahlTb = intAnzahlTb;
    }

    public int getIntAnzahlUb() {
        return intAnzahlUb;
    }

    public void setIntAnzahlUb(int intAnzahlUb) {
        this.intAnzahlUb = intAnzahlUb;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.strStammblattnummer);
        hash = 67 * hash + Objects.hashCode(this.strDienstgrad);
        hash = 67 * hash + Objects.hashCode(this.strTitel);
        hash = 67 * hash + Objects.hashCode(this.strVorname);
        hash = 67 * hash + Objects.hashCode(this.strZuname);
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
        final MitgliedsStundenPDF other = (MitgliedsStundenPDF) obj;
        if (!Objects.equals(this.strStammblattnummer, other.strStammblattnummer)) {
            return false;
        }
        if (!Objects.equals(this.strDienstgrad, other.strDienstgrad)) {
            return false;
        }
        if (!Objects.equals(this.strTitel, other.strTitel)) {
            return false;
        }
        if (!Objects.equals(this.strVorname, other.strVorname)) {
            return false;
        }
        if (!Objects.equals(this.strZuname, other.strZuname)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String strOutput = "<p>&nbsp;</p><p>&nbsp;</p>"
                + "<table>" + strHead + "<tbody>";
        for (int i = 0; i < liInstanznamen.size(); i++) {
            strOutput += "<tr><td class='STB'>"
                    + strStammblattnummer + "</td><td class='DGR'>"
                    + strDienstgrad + "</td><td>"
                    + strTitel + "</td><td>"
                    + strVorname + "</td><td>"
                    + strZuname + "</td><td>"
                    + liInstanznamen.get(i) + "</td><td>"
                    + liStundenSumme.get(i) + "</td>"
                    + "</tr>";
        }

        strOutput += "</tbody></table>";
        strOutput += "<p>&nbsp;</p><p>&nbsp;</p>"
                + "<div class='extraTable'>";
        strOutput += "<table style='page-break-after: always;'>"
                + "<thead>"
                + "<tr>"
                + "<th>Bericht</th>"
                + "<th>Anzahl</th>"
                + "<th>Stunden</th>"
                + "</tr>"
                + "</thead>"
                + "<tbody>"
                + "<tr>"
                + "<td>Einsatzbericht</td>"
                + "<td>" + intAnzahlEb + "</td>"
                + "<td>" + ((intMinutenEb / 60 != 0) ? intMinutenEb / 60 + "h " : "") + intMinutenEb % 60 + "min</td>"
                + "</tr>"
                + "<tr>"
                + "<td>Tätigkeitsbericht</td>"
                + "<td>" + intAnzahlTb + "</td>"
                + "<td>" + ((intMinutenTb / 60 != 0) ? intMinutenTb / 60 + "h " : "") + intMinutenTb % 60 + "min</td>"
                + "</tr>"
                + "<tr>"
                + "<td>Übungsbericht</td>"
                + "<td>" + intAnzahlUb + "</td>"
                + "<td>" + ((intMinutenUb / 60 != 0) ? intMinutenUb / 60 + "h " : "") + intMinutenUb % 60 + "min</td>"
                + "</tr>"
                + "</tbody>"
                + "</table>";
        strOutput += "</div>";
        return strOutput;
    }

}
