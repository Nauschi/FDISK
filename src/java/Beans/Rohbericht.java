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
    //Was wird im Vordefiniert bei dem User Interface benötigt
    //0 kein Datum, 1 nur Jahr, 2 von-bis, 3 von-bis-kennzeichen
    private int intTypeOfDateUI;

    public Rohbericht(String strBerichtname, LinkedList<String> liBerichtSpalten, int intTypeOfDateUI)
    {
        this.strBerichtname = strBerichtname;
        this.liBerichtSpalten = liBerichtSpalten;
        this.intTypeOfDateUI = intTypeOfDateUI;
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

    public int getIntTypeOfDateUI()
    {
        return intTypeOfDateUI;
    }

    public void setIntTypeOfDateUI(int intTypeOfDateUI)
    {
        this.intTypeOfDateUI = intTypeOfDateUI;
    }
    
    

    public String toHTMLString()
    {
        String strHTML = "<a class='item' onclick='onListItemClicked(this)' id='"+strBerichtname+"'>"
           
         + "<span>" + strBerichtname + "</span>"
                + "<div style='display:none'>"
               // + "<table id='table' class='ui sortable celled table'>"
                + "<table id='table' class='tablesorter ui celled striped table'>"
                + " <thead>"
                + "     <tr>";
        for (int i = 0; i < liBerichtSpalten.size(); i++)
        {
            String strSpalte = liBerichtSpalten.get(i);
            if(strSpalte.equals("Bemerkung")||strSpalte.equals("-"))
            {
                strHTML += "<th>" + strSpalte + "</th>";
            }
            else
            {
                strHTML += "<th data-content='nach "+strSpalte+" sortieren' class='sort' >" + strSpalte + "</th>";
            }
            
        }
        strHTML += "</tr></thead><tbody>";

        strHTML += "</tbody></table></div><div style='display:none'>"+intTypeOfDateUI+"</div></a>";
        return strHTML;
    }

}
