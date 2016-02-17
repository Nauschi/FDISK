/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enum;

/**
 *
 * @author Corinna
 */
public enum EnAlleBerichteStatements
{

    //DES GEH I SICHER NET UMSCHREIBEN DES KÖNNTS SELBER MACHEN xDD
    getBla("Select BLA");
    
    private final String strStatement;

    private EnAlleBerichteStatements(String strStatement)
    {
        this.strStatement = strStatement;
    }

    @Override
    public String toString()
    {
        return strStatement;
    }
    
    
}
