/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author kinco_000
 */
public class MitgliedsErreichbarkeit extends Mitglied
{
//    private int intId_Erreichbarkeiten;
//    private String strErreichbarkeitsArt;
//    private String strSichtbarkeit;
//    private String strCode;

    LinkedList<Erreichbarkeit> liErreichbarkeiten;
    private boolean boBemerkung;

//    public MitgliedsErreichbarkeit(int intId_Erreichbarkeiten, String strErreichbarkeitsArt, String strSichtbarkeit, String strCode, boolean boBemerkung, int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname) {
//        super(intId_Personen, strStammblattnummer, strDienstgrad, strTitel, strVorname, strZuname);
//        this.intId_Erreichbarkeiten = intId_Erreichbarkeiten;
//        this.strErreichbarkeitsArt = strErreichbarkeitsArt;
//        this.strSichtbarkeit = strSichtbarkeit;
//        this.strCode = strCode;
//        this.boBemerkung = boBemerkung;
//    }
    public MitgliedsErreichbarkeit(boolean boBemerkung, int intId_Personen, String strStammblattnummer, String strDienstgrad, String strTitel, String strVorname, String strZuname)
    {
        super(intId_Personen, strStammblattnummer, strDienstgrad, strTitel, strVorname, strZuname);
        this.liErreichbarkeiten = liErreichbarkeiten;
        this.boBemerkung = boBemerkung;
    }

    public LinkedList<Erreichbarkeit> getLiErreichbarkeiten()
    {
        return liErreichbarkeiten;
    }

    public void setLiErreichbarkeiten(LinkedList<Erreichbarkeit> liErreichbarkeiten)
    {
        this.liErreichbarkeiten = liErreichbarkeiten;
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
        final MitgliedsErreichbarkeit other = (MitgliedsErreichbarkeit) obj;
        if (!Objects.equals(this.liErreichbarkeiten, other.liErreichbarkeiten))
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
        // return String.format("%s %s %s %s", strVorname, strZuname, strStammblattnummer, strTitel);
        String strHtml = "<tr><td>"
                + strStammblattnummer + "</td><td>"
                + strDienstgrad + "</td><td>"
                + strTitel + "</td><td>"
                + strVorname + "</td><td>"
                + strZuname + "</td><td>";

        for (Erreichbarkeit erreichbarkeit : liErreichbarkeiten)
        {
            strHtml+= "<p>"+erreichbarkeit.getStrErreichbarkeitsArt()+": "+erreichbarkeit.getStrCode()+"</p>";
        }
        strHtml += "</td><td></td></tr>";

        return strHtml;
    }

}
