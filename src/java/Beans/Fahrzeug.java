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
 * @author kinco_000
 */
public class Fahrzeug
{

    private String strFahrzeugTyp;
    private String strKennzeichen;
    private int intBaujahr;
    private String strAufbaufirma;
    private String strTaktischeBezeichnung;
    private int intId_fahrzeuge;
    private String strBezeichnung;
    private String strFahrzeugmarke;
    private int intInstanznummer;
    private String strFahrzeugart;
    private int intLeistung;
    private int intEigengewicht;
    private int intGesamtgewicht;
    private String strTreibstoff;

    private DB_Access theInstance; 
    
    public Fahrzeug(String strFahrzeugTyp, String strKennzeichen, int intBaujahr, String strAufbaufirma, String strTaktischeBezeichnung, int intId_fahrzeuge, String strBezeichnung, String strFahrzeugmarke, int intInstanznummer, String strFahrzeugart, int intLeistung, int intEigengewicht, int intGesamtgewicht, String strTreibstoff) throws ClassNotFoundException
    {
        this.strFahrzeugTyp = strFahrzeugTyp;
        this.strKennzeichen = strKennzeichen;
        this.intBaujahr = intBaujahr;
        this.strAufbaufirma = strAufbaufirma;
        this.strTaktischeBezeichnung = strTaktischeBezeichnung;
        this.intId_fahrzeuge = intId_fahrzeuge;
        this.strBezeichnung = strBezeichnung;
        this.strFahrzeugmarke = strFahrzeugmarke;
        this.intInstanznummer = intInstanznummer;
        this.strFahrzeugart = strFahrzeugart;
        this.intLeistung = intLeistung;
        this.intEigengewicht = intEigengewicht;
        this.intGesamtgewicht = intGesamtgewicht;
        this.strTreibstoff = strTreibstoff;
        
        theInstance = DB_Access.getInstance();
    }

    public String getStrFahrzeugTyp()
    {
        return strFahrzeugTyp;
    }

    public void setStrFahrzeugTyp(String strFahrzeugTyp)
    {
        this.strFahrzeugTyp = strFahrzeugTyp;
    }

    public String getStrKennzeichen()
    {
        return strKennzeichen;
    }

    public void setStrKennzeichen(String strKennzeichen)
    {
        this.strKennzeichen = strKennzeichen;
    }

    public int getIntBaujahr()
    {
        return intBaujahr;
    }

    public void setIntBaujahr(int intBaujahr)
    {
        this.intBaujahr = intBaujahr;
    }

    public String getStrAufbaufirma()
    {
        return strAufbaufirma;
    }

    public void setStrAufbaufirma(String strAufbaufirma)
    {
        this.strAufbaufirma = strAufbaufirma;
    }

    public String getStrTaktischeBezeichnung()
    {
        return strTaktischeBezeichnung;
    }

    public void setStrTaktischeBezeichnung(String strTaktischeBezeichnung)
    {
        this.strTaktischeBezeichnung = strTaktischeBezeichnung;
    }

    public int getIntId_fahrzeuge()
    {
        return intId_fahrzeuge;
    }

    public void setIntId_fahrzeuge(int intId_fahrzeuge)
    {
        this.intId_fahrzeuge = intId_fahrzeuge;
    }

    public String getStrBezeichnung()
    {
        return strBezeichnung;
    }

    public void setStrBezeichnung(String strBezeichnung)
    {
        this.strBezeichnung = strBezeichnung;
    }

    public String getStrFahrzeugmarke()
    {
        return strFahrzeugmarke;
    }

    public void setStrFahrzeugmarke(String strFahrzeugmarke)
    {
        this.strFahrzeugmarke = strFahrzeugmarke;
    }

    public int getIntInstanznummer()
    {
        return intInstanznummer;
    }

    public void setIntInstanznummer(int intInstanznummer)
    {
        this.intInstanznummer = intInstanznummer;
    }

    public String getStrFahrzeugart()
    {
        return strFahrzeugart;
    }

    public void setStrFahrzeugart(String strFahrzeugart)
    {
        this.strFahrzeugart = strFahrzeugart;
    }

    public int getIntLeistung()
    {
        return intLeistung;
    }

    public void setIntLeistung(int intLeistung)
    {
        this.intLeistung = intLeistung;
    }

    public int getIntEigengewicht()
    {
        return intEigengewicht;
    }

    public void setIntEigengewicht(int intEigengewicht)
    {
        this.intEigengewicht = intEigengewicht;
    }

    public int getIntGesamtgewicht()
    {
        return intGesamtgewicht;
    }

    public void setIntGesamtgewicht(int intGesamtgewicht)
    {
        this.intGesamtgewicht = intGesamtgewicht;
    }

    public String getStrTreibstoff()
    {
        return strTreibstoff;
    }

    public void setStrTreibstoff(String strTreibstoff)
    {
        this.strTreibstoff = strTreibstoff;
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
        final Fahrzeug other = (Fahrzeug) obj;
        if (!Objects.equals(this.strFahrzeugTyp, other.strFahrzeugTyp))
        {
            return false;
        }
        if (!Objects.equals(this.strKennzeichen, other.strKennzeichen))
        {
            return false;
        }
        if (this.intBaujahr != other.intBaujahr)
        {
            return false;
        }
        if (!Objects.equals(this.strAufbaufirma, other.strAufbaufirma))
        {
            return false;
        }
        if (!Objects.equals(this.strTaktischeBezeichnung, other.strTaktischeBezeichnung))
        {
            return false;
        }
        if (this.intId_fahrzeuge != other.intId_fahrzeuge)
        {
            return false;
        }
        if (!Objects.equals(this.strBezeichnung, other.strBezeichnung))
        {
            return false;
        }
        if (!Objects.equals(this.strFahrzeugmarke, other.strFahrzeugmarke))
        {
            return false;
        }
        if (this.intInstanznummer != other.intInstanznummer)
        {
            return false;
        }
        if (!Objects.equals(this.strFahrzeugart, other.strFahrzeugart))
        {
            return false;
        }
        if (this.intLeistung != other.intLeistung)
        {
            return false;
        }
        if (this.intEigengewicht != other.intEigengewicht)
        {
            return false;
        }
        if (this.intGesamtgewicht != other.intGesamtgewicht)
        {
            return false;
        }
        if (!Objects.equals(this.strTreibstoff, other.strTreibstoff))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        
        if (strFahrzeugart == null)
        {
            strFahrzeugart = "";
        }
        if (strKennzeichen == null)
        {
            strKennzeichen = "";
        }
        if (strAufbaufirma == null)
        {
            strAufbaufirma = "";
        }
        if (strTreibstoff == null)
        {
            strTreibstoff = "";
        }
        if (strFahrzeugmarke == null)
        {
            strFahrzeugmarke = "";
        }

//        strFahrzeugart = theInstance.capitalizeEachWord(strFahrzeugart); 
//        strKennzeichen = strKennzeichen.toUpperCase(); 
//        strAufbaufirma = theInstance.capitalizeEachWord(strAufbaufirma); 
//        strFahrzeugmarke = theInstance.capitalizeEachWord(strFahrzeugmarke); 
//        strTreibstoff = theInstance.capitalizeEachWord(strTreibstoff); 
        
              
        String strHtml = "<tr><td>"
                + strFahrzeugart + "</td><td>"
                + strKennzeichen + "</td><td>"
                + intBaujahr + "</td><td>"
                + strAufbaufirma + "</td><td>"
                + strFahrzeugmarke + "</td><td>"
                + strTreibstoff + "</td><td></td></tr>";

        return strHtml;
    }
}
