/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author kinco_000
 */
public class MitgliedsDienstzeit extends Mitglied
{
    private Date dateGeburtsdatum;
    private int intDienstalter;

    public MitgliedsDienstzeit(int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname, boolean boCheckbox, Date dateGeburtsdatum, int intDienstalter) {
        super(intId_Personen, strStammblattnummer, strDienstgrad, strTitel, strVorname, strZuname, boCheckbox);
        this.dateGeburtsdatum = dateGeburtsdatum;
        this.intDienstalter = intDienstalter;
    }

    public Date getDateGeburtsdatum() {
        return dateGeburtsdatum;
    }

    public void setDateGeburtsdatum(Date dateGeburtsdatum) {
        this.dateGeburtsdatum = dateGeburtsdatum;
    }

    public int getIntDienstalter() {
        return intDienstalter;
    }

    public void setIntDienstalter(int intDienstalter) {
        this.intDienstalter = intDienstalter;
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
        if (this.intDienstalter != other.intDienstalter) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MitgliedsDienstzeit{" + "dateGeburtsdatum=" + dateGeburtsdatum + ", intDienstalter=" + intDienstalter + '}';
    }
    
    
}
