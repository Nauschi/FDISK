/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Database.DB_Access;
import java.text.SimpleDateFormat;
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
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private DB_Access theInstance;

    public MitgliedsDienstzeit(int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname, boolean boCheckbox, Date dateGeburtsdatum, int intDienstalter) throws ClassNotFoundException
    {
        super(intId_Personen, strStammblattnummer, strDienstgrad, strTitel, strVorname, strZuname);
        this.dateGeburtsdatum = dateGeburtsdatum;
        this.intDienstalter = intDienstalter;

        theInstance = DB_Access.getInstance();
    }

    public Date getDateGeburtsdatum()
    {
        return dateGeburtsdatum;
    }

    public void setDateGeburtsdatum(Date dateGeburtsdatum)
    {
        this.dateGeburtsdatum = dateGeburtsdatum;
    }

    public int getIntDienstalter()
    {
        return intDienstalter;
    }

    public void setIntDienstalter(int intDienstalter)
    {
        this.intDienstalter = intDienstalter;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final MitgliedsDienstzeit other = (MitgliedsDienstzeit) obj;
        if (!Objects.equals(this.dateGeburtsdatum, other.dateGeburtsdatum))
        {
            return false;
        }
        if (this.intDienstalter != other.intDienstalter)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {

        if (strStammblattnummer == null)
        {
            strStammblattnummer = "";
        }
        if (strDienstgrad == null)
        {
            strDienstgrad = "";
        }
        if (strTitel == null)
        {
            strTitel = "";
        }
        if (strVorname == null)
        {
            strVorname = "";
        }
        if (strZuname == null)
        {
            strZuname = "";
        }
        if (dateGeburtsdatum == null)
        {

        }

        strDienstgrad = strDienstgrad.toUpperCase();

        strZuname = theInstance.capitalizeEachWord(strZuname);
        strVorname = theInstance.capitalizeEachWord(strVorname);
        

        String strHtml = "<tr><td>"
                + strStammblattnummer + "</td><td>"
                + strDienstgrad + "</td><td>"
                + strTitel + "</td><td>"
                + strVorname + "</td><td>"
                + strZuname + "</td><td>"
                + sdf.format(dateGeburtsdatum) + "</td><td>"
                + intDienstalter + "</td></tr>";

        return strHtml;
    }

}
