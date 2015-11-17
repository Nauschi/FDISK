/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.LinkedList;

/**
 *
 * @author user
 */
public class Rohbericht
{

    private String strBerichtname;
    private LinkedList<String> liBerichtSpalten;

    public Rohbericht(String berichtname, LinkedList<String> berichtSpalten)
    {
        this.strBerichtname = berichtname;
        this.liBerichtSpalten = berichtSpalten;
    }

    public String getStrBerichtname()
    {
        return strBerichtname;
    }

    public void setStrBerichtname(String strBerichtname)
    {
        this.strBerichtname = strBerichtname;
    }

    public LinkedList<String> getLiBerichtSpalten()
    {
        return liBerichtSpalten;
    }

    public void setLiBerichtSpalten(LinkedList<String> liBerichtSpalten)
    {
        this.liBerichtSpalten = liBerichtSpalten;
    }

    public String toHTMLString()
    {
        String strHTML = "<a class='item' onclick='onListItemClicked(this)' id='"+strBerichtname+"'>"
                + "<span>" + strBerichtname + "</span>"
                + "<div style='display:none'>"
                + "<table id='table' class='ui sortable celled table'>"
                + " <thead>"
                + "     <tr>";
        for (int i = 0; i < liBerichtSpalten.size(); i++)
        {
            String strSpalte = liBerichtSpalten.get(i);
            strHTML += "<th data-content='nach "+strSpalte+" sortieren'>" + strSpalte + "</th>";
        }
        strHTML += "</tr></thead><tbody>";

//        for (int i = 0; i < 3; i++)
//        {
//            strHTML += "<tr>";
//            for (int t = 0; t < liBerichtSpalten.size(); t++)
//            {
//                strHTML += "<td>...</td>";
//            }
//            strHTML += "</tr>";
//        }

        strHTML += "</tbody></table></div></a>";
        return strHTML;
    }

}
