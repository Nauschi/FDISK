/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author kinco_000
 */
public class MitgliedsGeburtstag extends Mitglied
{
    private Date dateGeburtsdatum;
    private int intZielalter;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public MitgliedsGeburtstag(int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname, boolean boCheckbox, Date dateGeburtsdatum, int intAlter) {
        super(intId_Personen, strStammblattnummer, strDienstgrad, strTitel, strVorname, strZuname, boCheckbox);
        this.dateGeburtsdatum = dateGeburtsdatum;
        this.intZielalter = intAlter;
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
        return "MitgliedsGeburtstag{" + "dateGeburtsdatum=" + dateGeburtsdatum + ", intAlter=" + intZielalter + '}';
    }
    
       /**
     * HTML String für vereinfachtes einfügen in die Tabellen
     * @return 
     */
    public String toHTMLString()
    {
        String strHtml = "<tr><td>" 
                + strStammblattnummer + "</td><td>"
                + strDienstgrad + "</td><td>"
                + strTitel + "</td><td>"
                + strVorname + "</td><td>"
                + strZuname + "</td><td>"
                + sdf.format(dateGeburtsdatum)+"</td><td>"
                + intZielalter+"</td></tr>";

        return strHtml;
    }
    
    
}
