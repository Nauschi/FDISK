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
public enum EnEinfacheMitgliederliste {

    getEinfacheMitgliederlisteBereich("SELECT id_personen 'PersID', standesbuchnummer 'STB', dienstgrad 'DGR', titel 'Titel', vorname 'Vorname', zuname 'Zuname' "
            + " FROM FDISK.dbo.stmkmitglieder s INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(s.instanznummer = f.instanznummer) "
            + " WHERE (FDISK.dbo.stmkmitglieder.abgemeldet = 0) AND (NOT (LEFT(FDISK.dbo.stmkmitglieder.instanznummer, 2) = 'GA')) AND (NOT (LEFT(FDISK.dbo.stmkmitglieder.instanzname, 7) = 'FW GAST'))"
            + " AND f.Bereich_Nr = ?"),
    getEinfacheMitgliederlisteAbschnitt("SELECT id_personen 'PersID', standesbuchnummer 'STB', dienstgrad 'DGR', titel 'Titel', vorname 'Vorname', zuname 'Zuname' "
            + " FROM FDISK.dbo.stmkmitglieder s INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(s.instanznummer = f.instanznummer) "
            + " WHERE (FDISK.dbo.stmkmitglieder.abgemeldet = 0) AND (NOT (LEFT(FDISK.dbo.stmkmitglieder.instanznummer, 2) = 'GA')) AND (NOT (LEFT(FDISK.dbo.stmkmitglieder.instanzname, 7) = 'FW GAST'))"
            + " AND f.abschnitt_instanznummer = ?"),
    getEinfacheMitgliederlisteFubwehr("SELECT id_personen 'PersID', standesbuchnummer 'STB', dienstgrad 'DGR', titel 'Titel', vorname 'Vorname', zuname 'Zuname' "
            + " FROM FDISK.dbo.stmkmitglieder "
            + " WHERE (FDISK.dbo.stmkmitglieder.abgemeldet = 0) AND (NOT (LEFT(FDISK.dbo.stmkmitglieder.instanznummer, 2) = 'GA')) AND (NOT (LEFT(FDISK.dbo.stmkmitglieder.instanzname, 7) = 'FW GAST'))"
            + " AND instanznummer = ?");

    private final String strStatement;

    private EnEinfacheMitgliederliste(String strStatement) {
        this.strStatement = strStatement;
    }

    @Override
    public String toString() {
        return strStatement;
    }

}
