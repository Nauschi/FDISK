/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Database.DB_Access;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author kinco_000
 */
public class MitgliedsDienstzeit extends Mitglied
{

    private Date dateGeburtsdatum;
    private double doubleDienstalter;
    private Date dateEintrittsdatum; 
    private int intInstanznummer; 
    private double doVordienstzeit; 
    
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private DB_Access theInstance;

    public MitgliedsDienstzeit(int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname, boolean boCheckbox, Date dateGeburtsdatum, double doubleDienstalter, int intInstanznummer, Date dateEntrittsdatum, double doVordienstzeit) throws ClassNotFoundException
    {
        super(intId_Personen, strStammblattnummer, strDienstgrad, strTitel, strVorname, strZuname);
        this.dateGeburtsdatum = dateGeburtsdatum;
        this.doubleDienstalter = doubleDienstalter;
        this.dateEintrittsdatum = dateEintrittsdatum; 
        this.intInstanznummer = intInstanznummer; 
        this.doVordienstzeit = doVordienstzeit; 
        theInstance = DB_Access.getInstance();
    }

    public double getDoVordienstzeit()
    {
        return doVordienstzeit;
    }

    public void setDoVordienstzeit(double doVordienstzeit)
    {
        this.doVordienstzeit = doVordienstzeit;
    }

    
    public Date getDateGeburtsdatum()
    {
        return dateGeburtsdatum;
    }

    public int getIntInstanznummer()
    {
        return intInstanznummer;
    }

    public void setIntInstanznummer(int intInstanznummer)
    {
        this.intInstanznummer = intInstanznummer;
    }

    public Date getDateEintrittsdatum()
    {
        return dateEintrittsdatum;
    }

    public void setDateEintrittsdatum(Date dateEintrittsdatum)
    {
        this.dateEintrittsdatum = dateEintrittsdatum;
    }

    
    

    public void setDateGeburtsdatum(Date dateGeburtsdatum)
    {
        this.dateGeburtsdatum = dateGeburtsdatum;
    }

    public double getDoublleDienstalter()
    {
        return doubleDienstalter;
    }

    public void setDoubleDienstalter(double doubleDienstalter)
    {
        this.doubleDienstalter = doubleDienstalter;
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
        if (Double.doubleToLongBits(this.doubleDienstalter) != Double.doubleToLongBits(other.doubleDienstalter))
        {
            return false;
        }
        if (!Objects.equals(this.dateEintrittsdatum, other.dateEintrittsdatum))
        {
            return false;
        }
        if (this.intInstanznummer != other.intInstanznummer)
        {
            return false;
        }
        if (Double.doubleToLongBits(this.doVordienstzeit) != Double.doubleToLongBits(other.doVordienstzeit))
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


        strZuname = theInstance.formatiereAusgabe(strZuname);
        strVorname = theInstance.formatiereAusgabe(strVorname);
        

        String strHtml = ""; 
        if(((int)doubleDienstalter)%10==0)
        {
             strHtml = "<tr><td><b>"
                + strStammblattnummer + "</b></td><td><b>"
                + strDienstgrad + "</b></td><td><b>"
                + strTitel + "</b></td><td><b>"
                + strVorname + "</b></td><td><b>"
                + strZuname + "</b></td><td><b>"
                + sdf.format(dateGeburtsdatum) + "</b></td><td><b>"
                +(int)doubleDienstalter+"</b></td></tr>";
        }else
        {
             strHtml = "<tr><td>"
                + strStammblattnummer + "</td><td>"
                + strDienstgrad + "</td><td>"
                + strTitel + "</td><td>"
                + strVorname + "</td><td>"
                + strZuname + "</td><td>"
                + sdf.format(dateGeburtsdatum) + "</td><td>"
                +(int)doubleDienstalter +"</td></tr>";
        }
        return strHtml;
    }

}
