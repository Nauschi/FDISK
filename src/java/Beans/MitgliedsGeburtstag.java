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
public class MitgliedsGeburtstag extends Mitglied
{
    private Date dateGeburtsdatum;
    private int intAlter;

    public MitgliedsGeburtstag(int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname, boolean boCheckbox, Date dateGeburtsdatum, int intAlter) {
        super(intId_Personen, strStammblattnummer, strDienstgrad, strTitel, strVorname, strZuname, boCheckbox);
        this.dateGeburtsdatum = dateGeburtsdatum;
        this.intAlter = intAlter;
    }

    public Date getDateGeburtsdatum() {
        return dateGeburtsdatum;
    }

    public void setDateGeburtsdatum(Date dateGeburtsdatum) {
        this.dateGeburtsdatum = dateGeburtsdatum;
    }

    public int getIntAlter() {
        return intAlter;
    }

    public void setIntAlter(int intAlter) {
        this.intAlter = intAlter;
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
        if (this.intAlter != other.intAlter) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MitgliedsGeburtstag{" + "dateGeburtsdatum=" + dateGeburtsdatum + ", intAlter=" + intAlter + '}';
    }
    
    
    
    
}
