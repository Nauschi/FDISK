package Beans;

import BL.BL;
import java.io.Serializable;

/**
 *
 * @author Yvonne
 */
public class LeerberichtFahrzeug implements Serializable
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
    private BL bl = new BL(); 

    public LeerberichtFahrzeug(String strFahrzeugTyp, String strKennzeichen, int intBaujahr, String strAufbaufirma, String strTaktischeBezeichnung, int intId_fahrzeuge, String strBezeichnung, String strFahrzeugmarke, int intInstanznummer) throws ClassNotFoundException
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

    @Override
    public String toString()
    {
        if(strBezeichnung == null)
        {
            return ""; 
        }
        
        strBezeichnung = bl.formatiereAusgabe(strBezeichnung); 

        String strHtml = "<td>&Omicron;&nbsp;" + strBezeichnung + "</td>";
        return strHtml;
    }
}
