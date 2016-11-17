/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import BL.BL;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author kinco_000
 */
public class MitgliedsDienstzeit extends Mitglied implements Serializable {

    private Date dateGeburtsdatum;
    private double doubleDienstalter;
    private Date dateEintrittsdatum;
    private int intInstanznummer;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private BL bl = new BL();

    public MitgliedsDienstzeit(int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname, boolean boCheckbox, Date dateGeburtsdatum, double doubleDienstalter, int intInstanznummer, Date dateEintrittsdatum, String strFubwehr) throws ClassNotFoundException {
        super(intId_Personen, strStammblattnummer, strDienstgrad, strTitel, strVorname, strZuname, strFubwehr);
        this.dateGeburtsdatum = dateGeburtsdatum;
        this.doubleDienstalter = doubleDienstalter;
        this.dateEintrittsdatum = dateEintrittsdatum;
        this.intInstanznummer = intInstanznummer;
    }

    public Date getDateGeburtsdatum() {
        return dateGeburtsdatum;
    }

    public int getIntInstanznummer() {
        return intInstanznummer;
    }

    public void setIntInstanznummer(int intInstanznummer) {
        this.intInstanznummer = intInstanznummer;
    }

    public Date getDateEintrittsdatum() {
        return dateEintrittsdatum;
    }

    public void setDateEintrittsdatum(Date dateEintrittsdatum) {
        this.dateEintrittsdatum = dateEintrittsdatum;
    }

    public void setDateGeburtsdatum(Date dateGeburtsdatum) {
        this.dateGeburtsdatum = dateGeburtsdatum;
    }

    public double getDoubleDienstalter() {
        return doubleDienstalter;
    }

    public void setDoubleDienstalter(double doubleDienstalter) {
        this.doubleDienstalter = doubleDienstalter;
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
        final MitgliedsDienstzeit other = (MitgliedsDienstzeit) obj;
        if (!Objects.equals(this.dateGeburtsdatum, other.dateGeburtsdatum)) {
            return false;
        }
        if (Double.doubleToLongBits(this.doubleDienstalter) != Double.doubleToLongBits(other.doubleDienstalter)) {
            return false;
        }
        if (!Objects.equals(this.dateEintrittsdatum, other.dateEintrittsdatum)) {
            return false;
        }
        if (this.intInstanznummer != other.intInstanznummer) {
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
        if (dateGeburtsdatum == null) {

        }
        if (strFubwehr == null) {
            strFubwehr = "";
        }

        strZuname = bl.formatiereAusgabe(strZuname);
        strVorname = bl.formatiereAusgabe(strVorname);

        String strHtml = "";
        if (((int) doubleDienstalter) % 10 == 0) {
            strHtml = "<tr><td id='multipleFb'><b>" + strFubwehr
                    + "</b></td><td><b>"
                    + strStammblattnummer + "</b></td><td><b>"
                    + strDienstgrad + "</b></td><td><b>"
                    + strTitel + "</b></td><td><b>"
                    + strVorname + "</b></td><td><b>"
                    + strZuname + "</b></td><td><b>";

            if (dateGeburtsdatum == null) {
                strHtml += " ";
            } else {

                strHtml += sdf.format(dateGeburtsdatum);
            }
            strHtml += "</b></td><td><b>"
                    + (int) doubleDienstalter + "</b></td></tr>";
        } else {
            strHtml = "<tr><td id='multipleFb'>" + strFubwehr + "</td><td>"
                    + strStammblattnummer + "</td><td>"
                    + strDienstgrad + "</td><td>"
                    + strTitel + "</td><td>"
                    + strVorname + "</td><td>"
                    + strZuname + "</td><td>";
            if (dateGeburtsdatum == null) {
                strHtml += " ";

            } else {
                strHtml += sdf.format(dateGeburtsdatum);
            }
            strHtml += "</td><td>"
                    + (int) doubleDienstalter + "</td></tr>";
        }
        return strHtml;
    }

}
