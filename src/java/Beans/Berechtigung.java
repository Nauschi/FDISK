/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.io.Serializable;

/**
 *
 * @author philipp
 */
public class Berechtigung implements Serializable{
    private String strBerechtigung;
    private int intIDGruppe;
    private String strFubwehr;
    private int intAbschnitt;
    private int intBereich;
    private int intUserID;

    public Berechtigung(String strBerechtigung, int intIDGruppe, String fubwerh, int abschnitt, int bereich, int userID) {
        this.strBerechtigung = strBerechtigung;
        this.intIDGruppe = intIDGruppe;
        this.strFubwehr = fubwerh;
        this.intAbschnitt = abschnitt;
        this.intBereich = bereich;
        this.intUserID = userID;
    }

    public String getStrBerechtigung() {
        return strBerechtigung;
    }

    public void setStrBerechtigung(String strBerechtigung) {
        this.strBerechtigung = strBerechtigung;
    }

    public int getIntIDGruppe() {
        return intIDGruppe;
    }

    public void setIntIDGruppe(int intIDGruppe) {
        this.intIDGruppe = intIDGruppe;
    }

    public String getStrFubwehr() {
        return strFubwehr;
    }

    public void setStrFubwehr(String strFubwehr) {
        this.strFubwehr = strFubwehr;
    }

    public int getIntAbschnitt() {
        return intAbschnitt;
    }

    public void setIntAbschnitt(int intAbschnitt) {
        this.intAbschnitt = intAbschnitt;
    }

    public int getIntBereich() {
        return intBereich;
    }

    public void setIntBereich(int intBereich) {
        this.intBereich = intBereich;
    }

    public int getIntUserID() {
        return intUserID;
    }

    public void setIntUserID(int intUserID) {
        this.intUserID = intUserID;
    } 
}
