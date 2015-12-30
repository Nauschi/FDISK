/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.Objects;

/**
 *
 * @author Corinna
 */
public class Feuerwehr
{
    private String strName;
    private String strFeuerwehrNummer;

    public Feuerwehr(String strName, String strFeuerwehrNummer)
    {
        this.strName = strName;
        this.strFeuerwehrNummer = strFeuerwehrNummer;
    }

    public String getStrName()
    {
        return strName;
    }

    public void setStrName(String strName)
    {
        this.strName = strName;
    }

    public String getStrFeuerwehrNummer() {
        return strFeuerwehrNummer;
    }

    public void setStrFeuerwehrNummer(String strFeuerwehrNummer) {
        this.strFeuerwehrNummer = strFeuerwehrNummer;
    }

    @Override
    public String toString()
    {
//        return "Feuerwehr{" + "strName=" + strName + ", strFeuerwehrNummer=" + strFeuerwehrNummer + '}';
        String strHTML = "<option value='" + strFeuerwehrNummer + "'>" + strName+"</option>";
        return strHTML;
    }
    
    
}
