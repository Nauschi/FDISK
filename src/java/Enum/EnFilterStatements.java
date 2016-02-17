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
public enum EnFilterStatements
{
    getFilterFuerGeschlecht("SELECT DISTINCT Geschlecht FROM FDISK.dbo.stmkmitglieder ORDER BY Geschlecht"),
    getFilterFuerFuehrerscheinklassen("SELECT DISTINCT Fahrgenehmigungsklasse FROM FDISK.dbo.stmkgesetzl_fahrgenehmigungen ORDER BY Fahrgenehmigungsklasse"),
    getFilterFuerDienstgrade("SELECT DISTINCT Dienstgrad FROM FDISK.dbo.stmkmitglieder ORDER BY Dienstgrad"),
    getFilterFuerFamilienstand("SELECT DISTINCT Familienstand FROM FDISK.dbo.stmkmitglieder ORDER BY Familienstand"),
    getFilterFuerBlutgruppe("SELECT DISTINCT Blutgruppe FROM FDISK.dbo.stmkmitglieder ORDER BY Blutgruppe"),
    getFilterFuerAmtstitel("SELECT DISTINCT Amtstitel FROM FDISK.dbo.stmkmitglieder ORDER BY Amtstitel"),
    getFilterFuerTitel("SELECT DISTINCT Titel FROM FDISK.dbo.stmkmitglieder ORDER BY Titel"),
    getFilterFuerBeruf("SELECT DISTINCT Beruf FROM FDISK.dbo.stmkmitglieder ORDER BY Beruf"),
    getFilterFuerUntersuchungsart("SELECT DISTINCT Expr1 FROM FDISK.dbo.stmkuntersuchungenmitglieder ORDER BY Expr1"),
    getFilterFuerLeistungsabzeichenstufe("SELECT DISTINCT Stufe FROM FDISK.dbo.stmkleistungsabzeichen ORDER BY Stufe"),
    getFilterFuerLeistungsabzeichenbezeichnung("SELECT DISTINCT Bezeichnung FROM FDISK.dbo.stmkleistungsabzeichen ORDER BY Bezeichnung"),
    getFilterFuerAuszeichnung("SELECT DISTINCT Auszeichnung FROM FDISK.dbo.stmkauszeichnungen ORDER BY Auszeichnung"),
    getFilterFuerAuszeichnungsstufe("SELECT DISTINCT Auszeichnungsstufe FROM FDISK.dbo.stmkauszeichnungen ORDER BY Auszeichnungsstufe"),
    getFilterFuerStaatsbuergerschaft("SELECT DISTINCT Staatsbuergerschaft FROM FDISK.dbo.stmkmitglieder ORDER BY Staatsbuergerschaft"),
    getFilterFuerKursbezeichnung("SELECT DISTINCT Kursbezeichnung FROM FDISK.dbo.stmkkurse ORDER BY Kursbezeichnung"),
    getFilterFuerFunktionsbezeichnung("SELECT DISTINCT Bezeichnung FROM FDISK.dbo.stmkfunktionen ORDER BY Bezeichnung"),
    getFilterFuerFunktionsinstanz("SELECT DISTINCT Id_Instanztypen FROM FDISK.dbo.stmkfunktionen ORDER BY Id_Instanztypen"),
    getFilterFuerErreichbarkeitsart("SELECT DISTINCT Erreichbarkeitsart FROM FDISK.dbo.stmkerreichbarkeiten ORDER BY Erreichbarkeitsart");

    
    private final String strStatement;

    private EnFilterStatements(String strStatement)
    {
        this.strStatement = strStatement;
    }

    @Override
    public String toString()
    {
        return strStatement;
    }
    
    
}
