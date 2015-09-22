/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

/**
 *
 * @author philipp
 */
public class Berechtigung {
    private String strBerechtigung;
    private int intIDGruppe;

    public Berechtigung(String strBerechtigung, int intIDGruppe) {
        this.strBerechtigung = strBerechtigung;
        this.intIDGruppe = intIDGruppe;
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
    
    
}
