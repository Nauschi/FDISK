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

    double doStunden;
    String strInstanznummer;
    private DB_Access theInstance;

    public MitgliedsStunden(int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname, double doStunden, String strInstanznummer) throws ClassNotFoundException {
        super(intId_Personen, strStammblattnummer, strDienstgrad, strTitel, strVorname, strZuname);
        this.doStunden = doStunden;
        this.strInstanznummer = strInstanznummer;
        
        theInstance = DB_Access.getInstance(); 
    }

    public double getDoStunden() {
        return doStunden;
    }

    public void setDoStunden(double doStunden) {
        this.doStunden = doStunden;
    }

    public String getStrInstanznummer() {
        return strInstanznummer;
    }

    public void setStrInstanznummer(String strInstanznummer) {
        this.strInstanznummer = strInstanznummer;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.doStunden) ^ (Double.doubleToLongBits(this.doStunden) >>> 32));
        hash = 59 * hash + Objects.hashCode(this.strInstanznummer);
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
        if (Double.doubleToLongBits(this.doStunden) != Double.doubleToLongBits(other.doStunden)) {
            return false;
        }
        if (!Objects.equals(this.strInstanznummer, other.strInstanznummer)) {
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
                + (doStunden*60) / 60 + ":" + doStunden%60 + "</td>"
                + "<td></td></tr>";

        return strHtml;
    }

}
