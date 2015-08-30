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

    private String berichtname;
    private LinkedList<String> berichtSpalten;

    public Rohbericht(String berichtname, LinkedList<String> berichtSpalten)
    {
        this.berichtname = berichtname;
        this.berichtSpalten = berichtSpalten;
    }

    public String getBerichtname()
    {
        return berichtname;
    }

    public void setBerichtname(String berichtname)
    {
        this.berichtname = berichtname;
    }

    public LinkedList<String> getBerichtSpalten()
    {
        return berichtSpalten;
    }

    public void setBerichtSpalten(LinkedList<String> berichtSpalten)
    {
        this.berichtSpalten = berichtSpalten;
    }

    public String zuHTMLString()
    {
        String htmlString = "<a class='item' onclick='onListItemClicked(this)'>"
                + "<span>" + berichtname + "</span>"
                + "<div style='display:none'>"
                + "<table id='table' class='ui sortable celled table'>"
                + " <thead>"
                + "     <tr>";
        for (int i = 0; i < berichtSpalten.size(); i++)
        {
            String spalte = berichtSpalten.get(i);
            htmlString += "<th>" + spalte + "</th>";
        }
        htmlString += "</tr></thead><tbody>";

        for (int i = 0; i < 3; i++)
        {
            htmlString += "<tr>";
            for (int t = 0; t < berichtSpalten.size(); t++)
            {
                htmlString += "<td>...</td>";
            }
            htmlString += "</tr>";
        }

        htmlString += "</tbody></table></div></a>";
        return htmlString;
    }

}
