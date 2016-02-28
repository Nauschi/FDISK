/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Database.DB_Access;
import java.util.Objects;

/**
 *
 * @author philipp
 */
public class MitgliedsStunden extends Mitglied {

    int intMinuten;
    String strInstanznummer;
    int intMinutenUb;
    int intMinutenEb;
    int intMinutenTb;
    private DB_Access theInstance;

    public MitgliedsStunden(int intMinuten, String strInstanznummer, int intMinutenUb, int intMinutenEb, int intMinutenTb, int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname) throws ClassNotFoundException {
        super(intId_Personen, strStammblattnummer, strDienstgrad, strTitel, strVorname, strZuname);
        this.intMinuten = intMinuten;
        this.strInstanznummer = strInstanznummer;
        this.intMinutenUb = intMinutenUb;
        this.intMinutenEb = intMinutenEb;
        this.intMinutenTb = intMinutenTb;
        
        theInstance = DB_Access.getInstance();
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.intMinuten;
        hash = 97 * hash + Objects.hashCode(this.strInstanznummer);
        hash = 97 * hash + this.intMinutenUb;
        hash = 97 * hash + this.intMinutenEb;
        hash = 97 * hash + this.intMinutenTb;
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
        final MitgliedsStunden other = (MitgliedsStunden) obj;
        if (this.intMinuten != other.intMinuten) {
            return false;
        }
        if (!Objects.equals(this.strInstanznummer, other.strInstanznummer)) {
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

        strZuname = theInstance.formatiereAusgabe(strZuname);
        strVorname = theInstance.formatiereAusgabe(strVorname);

        String strHtml = "<tr><td>"
                + strStammblattnummer + "</td><td>"
                + strDienstgrad + "</td><td>"
                + strTitel + "</td><td>"
                + strVorname + "</td><td>"
                + strZuname + "</td><td>"
                + strInstanznummer + "</td><td>"
                + (intMinuten) / 60 + ":" + intMinuten%60 + "</td>"
                + "<td></td></tr>";

        return strHtml;
    }

}
