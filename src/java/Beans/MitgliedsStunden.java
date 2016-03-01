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
    String strInstanzname;
    private DB_Access theInstance;

    public MitgliedsStunden(int intMinuten, String strInstanznummer, int intMinutenUb, int intMinutenEb, int intMinutenTb, int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname, String strInstanzname) throws ClassNotFoundException {
        super(intId_Personen, strStammblattnummer, strDienstgrad, strTitel, strVorname, strZuname);
        this.intMinuten = intMinuten;
        this.strInstanznummer = strInstanznummer;
        this.intMinutenUb = intMinutenUb;
        this.intMinutenEb = intMinutenEb;
        this.intMinutenTb = intMinutenTb;
        this.strInstanzname = strInstanzname;
        
        theInstance = DB_Access.getInstance();
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + this.intMinuten;
        hash = 23 * hash + Objects.hashCode(this.strInstanznummer);
        hash = 23 * hash + this.intMinutenUb;
        hash = 23 * hash + this.intMinutenEb;
        hash = 23 * hash + this.intMinutenTb;
        hash = 23 * hash + Objects.hashCode(this.strInstanzname);
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

        strZuname = theInstance.formatiereAusgabe(strZuname);
        strVorname = theInstance.formatiereAusgabe(strVorname);

        String strHtml = "<tr><td>"
                + strStammblattnummer + "</td><td>"
                + strDienstgrad + "</td><td>"
                + strTitel + "</td><td>"
                + strVorname + "</td><td>"
                + strZuname + "</td><td>"
                + strInstanzname + "</td><td>"
                + (intMinuten) / 60 + ":" + intMinuten%60 + "</td>"
                + "<td></td></tr>";
        
//        strHtml += "<tr><td>"+intMinutenEb+"</td><td>"+intMinutenTb+"</td><td>"+intMinutenUb+"</td><td>"
//                + strVorname + "</td></tr>";

        return strHtml;
    }

}
