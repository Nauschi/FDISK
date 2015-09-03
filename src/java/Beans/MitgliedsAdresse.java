/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.Objects;

/**
 *
 * @author kinco_000
 */
public class MitgliedsAdresse extends Mitglied
{

    private int intId_Adressen;
    private String strStrasse;
    private String intNummer;
    private String strStiege;
    private int intPLZ;
    private String strOrt;
    private boolean boBemerkung;

    public MitgliedsAdresse(int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname, boolean boCheckbox, int intId_Adressen, String strStrasse, String intNummer, String strStiege, int intPLZ, String strOrt, boolean boBemerkung)
    {
        super(intId_Personen, strStammblattnummer, strDienstgrad, strTitel, strVorname, strZuname, boCheckbox);
        this.intId_Adressen = intId_Adressen;
        this.strStrasse = strStrasse;
        this.intNummer = intNummer;
        this.strStiege = strStiege;
        this.intPLZ = intPLZ;
        this.strOrt = strOrt;
        this.boBemerkung = boBemerkung;
    }

    public int getIntId_Adressen()
    {
        return intId_Adressen;
    }

    public void setIntId_Adressen(int intId_Adressen)
    {
        this.intId_Adressen = intId_Adressen;
    }

    public String getStrStrasse()
    {
        return strStrasse;
    }

    public void setStrStrasse(String strStrasse)
    {
        this.strStrasse = strStrasse;
    }

    public String getIntNummer()
    {
        return intNummer;
    }

    public void setIntNummer(String intNummer)
    {
        this.intNummer = intNummer;
    }

    public String getStrStiege()
    {
        return strStiege;
    }

    public void setStrStiege(String strStiege)
    {
        this.strStiege = strStiege;
    }

    public int getIntPLZ()
    {
        return intPLZ;
    }

    public void setIntPLZ(int intPLZ)
    {
        this.intPLZ = intPLZ;
    }

    public String getStrOrt()
    {
        return strOrt;
    }

    public void setStrOrt(String strOrt)
    {
        this.strOrt = strOrt;
    }

    public boolean isBoBemerkung()
    {
        return boBemerkung;
    }

    public void setBoBemerkung(boolean boBemerkung)
    {
        this.boBemerkung = boBemerkung;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
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
        final MitgliedsAdresse other = (MitgliedsAdresse) obj;
        if (this.intId_Adressen != other.intId_Adressen)
        {
            return false;
        }
        if (!Objects.equals(this.strStrasse, other.strStrasse))
        {
            return false;
        }
        if (!Objects.equals(this.intNummer, other.intNummer))
        {
            return false;
        }
        if (!Objects.equals(this.strStiege, other.strStiege))
        {
            return false;
        }
        if (this.intPLZ != other.intPLZ)
        {
            return false;
        }
        if (!Objects.equals(this.strOrt, other.strOrt))
        {
            return false;
        }
        if (this.boBemerkung != other.boBemerkung)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        //return "MitgliedsAdresse{" + "intId_Adressen=" + intId_Adressen + ", strStrasse=" + strStrasse + ", intNummer=" + intNummer + ", strStiege=" + strStiege + ", intPLZ=" + intPLZ + ", strOrt=" + strOrt + ", boBemerkung=" + boBemerkung + '}';
        String strHtml = "<tr><td>"
                + strStammblattnummer + "</td><td>"
                + strDienstgrad + "</td><td>"
                + strTitel + "</td><td>"
                + strVorname + "</td><td>"
                + strZuname + "</td><td>"
                + strStrasse + " " + intNummer + ", " + intPLZ + " " + strOrt + "</td>"
                + "<td></td></tr>";

        return strHtml;
    }

}
