/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import BL.BL;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author kinco_000
 */
public class MitgliedsGeburtstag extends Mitglied implements Serializable {

    private Date dateGeburtsdatum;
    private int intZielalter;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private BL bl = new BL();

    public MitgliedsGeburtstag(int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname, boolean boCheckbox, Date dateGeburtsdatum, int intAlter, String strFubwehr) throws ClassNotFoundException {
        super(intId_Personen, strStammblattnummer, strDienstgrad, strTitel, strVorname, strZuname, strFubwehr);
        this.dateGeburtsdatum = dateGeburtsdatum;
        this.intZielalter = intAlter;
    }
    
    public String getSortedDate(){
        String strDate = sdf.format(dateGeburtsdatum);
        String month = strDate.split("\\.")[1];
        String day = strDate.split("\\.")[0];
        String year = strDate.split("\\.")[2];
        return month + day + year;
    }

    public Date getDateGeburtsdatum() {
        return dateGeburtsdatum;
    }

    public void setDateGeburtsdatum(Date dateGeburtsdatum) {
        this.dateGeburtsdatum = dateGeburtsdatum;
    }

    public int getIntZielalter() {
        return intZielalter;
    }

    public void setIntZielalter(int intZielalter) {
        this.intZielalter = intZielalter;
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
        final MitgliedsGeburtstag other = (MitgliedsGeburtstag) obj;
        if (!Objects.equals(this.dateGeburtsdatum, other.dateGeburtsdatum)) {
            return false;
        }
        if (this.intZielalter != other.intZielalter) {
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
        if (intZielalter % 10 == 0) {
            strHtml = "<tr><td id='multipleFb'><b>" + strFubwehr + "</b></td><td><b>"
                    + strStammblattnummer + "</b></td><td><b>"
                    + strDienstgrad + "</b></td><td style='width:40px;'><b>"
                    + strTitel + "</b></td><td><b>"
                    + strVorname + "</b></td><td><b>"
                    + strZuname + "</b></td><td><b>"
                    + sdf.format(dateGeburtsdatum) + "</b></td><td><b>"
                    + intZielalter + "</b></td></tr>";
        } else {
            strHtml = "<tr><td id='multipleFb'>" + strFubwehr + "</td><td>"
                    + strStammblattnummer + "</td><td>"
                    + strDienstgrad + "</td><td style='width:40px;'>"
                    + strTitel + "</td><td>"
                    + strVorname + "</td><td>"
                    + strZuname + "</td><td>"
                    + sdf.format(dateGeburtsdatum) + "</td><td>"
                    + intZielalter + "</td></tr>";
        }

        return strHtml;
    }

}
