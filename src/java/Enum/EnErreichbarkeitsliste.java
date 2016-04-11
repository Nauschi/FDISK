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
public enum EnErreichbarkeitsliste
{

    getErreichbarkeitslisteBereich("SELECT DISTINCT sm.id_personen 'PersID', standesbuchnummer 'STB', dienstgrad 'DGR', titel 'Titel', vorname 'Vorname', zuname 'Zuname', se.erreichbarkeitsart 'Erreichbarkeitsart', se.code 'Code'"
            + " FROM FDISK.dbo.stmkmitglieder sm INNER JOIN FDISK.dbo.stmkerreichbarkeiten se ON(sm.id_personen = se.id_personen)"
            + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(sm.instanznummer = f.instanznummer)"
            + " WHERE (sm.abgemeldet = 0) AND (NOT (LEFT(sm.instanznummer, 2) = 'GA')) AND (NOT (LEFT(sm.instanzname, 7) = 'FW GAST'))"
            + " AND f.Bereich_Nr = ? "
            + " ORDER BY sm.id_personen"),
    getErreichbarkeitslisteAbschnitt("SELECT DISTINCT sm.id_personen 'PersID', standesbuchnummer 'STB', dienstgrad 'DGR', titel 'Titel', vorname 'Vorname', zuname 'Zuname', se.erreichbarkeitsart 'Erreichbarkeitsart', se.code 'Code'"
            + " FROM FDISK.dbo.stmkmitglieder sm INNER JOIN FDISK.dbo.stmkerreichbarkeiten se ON(sm.id_personen = se.id_personen)"
            + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(sm.instanznummer = f.instanznummer)"
            + " WHERE (sm.abgemeldet = 0) AND (NOT (LEFT(sm.instanznummer, 2) = 'GA')) AND (NOT (LEFT(sm.instanzname, 7) = 'FW GAST'))"
            + " AND f.abschnitt_instanznummer = ?"
            + " ORDER BY sm.id_personen"),
    getErreichbarkeitslisteFubwehr("SELECT DISTINCT sm.id_personen 'PersID', standesbuchnummer 'STB', dienstgrad 'DGR', titel 'Titel', vorname 'Vorname', zuname 'Zuname', se.erreichbarkeitsart 'Erreichbarkeitsart', se.code 'Code'"
            + " FROM FDISK.dbo.stmkmitglieder sm INNER JOIN FDISK.dbo.stmkerreichbarkeiten se ON(sm.id_personen = se.id_personen)"
            + " WHERE (sm.abgemeldet = 0) AND (NOT (LEFT(sm.instanznummer, 2) = 'GA')) AND (NOT (LEFT(sm.instanzname, 7) = 'FW GAST'))"
            + " AND sm.instanznummer = ?"
            + " ORDER BY sm.id_personen");

    private final String strStatement;

    private EnErreichbarkeitsliste(String strStatement)
    {
        this.strStatement = strStatement;
    }

    @Override
    public String toString()
    {
        return strStatement;
    }
}
