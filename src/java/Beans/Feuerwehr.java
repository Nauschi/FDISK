/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.io.Serializable;

/**
 *
 * @author Corinna
 */
public class Feuerwehr implements Serializable
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

    public String getSortedFubwehr(){
        return strFeuerwehrNummer.substring(2);
    }
    
    public String getStrNameOhneArt(){
        String strName2;
        strName2 = strName.replace("FF", "");
        strName2 = strName2.replace("BTF", "");
        return strName2;
    }
    
    @Override
    public String toString()
    {
        String strHTML = "<option value='" + strFeuerwehrNummer + "'>" + strName+"</option>";
        return strHTML;
    }
    
    
}
