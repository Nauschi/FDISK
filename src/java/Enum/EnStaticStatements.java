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
public enum EnStaticStatements {

    getUserID("SELECT IDUser, username, passwort FROM FDISK.dbo.tbl_login_benutzer WHERE username = ? AND passwort = ?"),
    getLoginBerechtigung("SELECT fubwehr 'Fubwehr', gruppe.IDGruppe 'IDGruppe', Bezeichnung 'Bezeichnung' FROM FDISK.dbo.tbl_login_benutzerdetail benutzerdetail "
                + "INNER JOIN FDISK.dbo.tbl_login_gruppenbenutzer gruppenbenutzer ON(benutzerdetail.IDUser = gruppenbenutzer.IDUser) "
                + "INNER JOIN FDISK.dbo.tbl_login_gruppe gruppe ON(gruppe.IDGruppe = gruppenbenutzer.IDGruppe) "
                + "WHERE benutzerdetail.IDUser = ?"),
    getFubwehrForUserID("SELECT fubwehr 'Fubwehr' FROM FDISK.dbo.tbl_login_benutzerdetail WHERE IDUser = ?"),
    getNameFuerFubwehr("SELECT CONCAT(instanzart, ' ' , instanzname) 'Name', instanznummer FROM FDISK.dbo.qry_alle_feuerwehren "
            + "WHERE instanznummer = ?"),
    getAbschnittsnameFuerFubwehr("SELECT Abschnitt_Instanznummer 'Instanznummer', Abschnittsname 'Name' "
            + "FROM FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich "
            + "WHERE instanznummer = ?"),
    getBereichsnameFuerFubwehr("SELECT uebergeordneteInstanz 'Name', Bereich_Nr 'Nr' "
            + "  FROM FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich "
            + "  WHERE instanznummer = ?"),
    getBereichsnummerFuerFubwehr("SELECT Bereich_Nr 'Nr'"
            + "  FROM FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich "
            + "  WHERE instanznummer = ?"),
    getBereichsnameFuerBereichnnummer("SELECT DISTINCT uebergeordneteInstanz 'Bereichname', Bereich_Kuerzel 'Kuerzel', Bereich_Nr 'Nummer'"
            + " FROM FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich"
            + " WHERE Bereich_Nr = ? "),
    getAbschnittsnameFuerAbschnittsnummer("SELECT DISTINCT Abschnitt_Instanznummer 'Abschnittnummer', Abschnittsname 'Abschnittname'"
            + " FROM FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich"
            + " WHERE Abschnitt_Instanznummer = ?"),
    getAbschnittNummernFuerBereich("SELECT DISTINCT f.Abschnitt_Instanznummer 'Abschnittnr'"
            + " FROM FDISK.dbo.qry_alle_feuerwehren af INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(af.instanznummer = f.instanznummer)"
            + " WHERE f.Bereich_Nr = ?"),
    getFubwehrNummernFuerAbschnitt("SELECT af.instanznummer 'Fubwehr', af.instanzname 'Instanzname'"
            + " FROM FDISK.dbo.qry_alle_feuerwehren af INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(af.instanznummer = f.instanznummer)"
            + " WHERE f.abschnitt_instanznummer = ?"),
    getAlleBereiche("SELECT DISTINCT Bereich_Nr Bezirksnummer, uebergeordneteInstanz Bezirksname, Bereich_Kuerzel"
            + " FROM FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich");

    private final String strStatement;

    private EnStaticStatements(String strStatement) {
        this.strStatement = strStatement;
    }

    @Override
    public String toString() {
        return strStatement;
    }

}
