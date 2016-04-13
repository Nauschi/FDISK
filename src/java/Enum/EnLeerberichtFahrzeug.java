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
public enum EnLeerberichtFahrzeug {

    getLeerberichtFahrzeugAlle("SELECT "
            + "kennzeichen 'Kennzeichen' "
            + ",id_fahrzeuge 'Id_Fahrzeuge' "
            + ",fahrzeugtyp 'Fahrzeugtyp' "
            + ",taktischebezeichnung 'Taktische Bezeichnung' "
            + ",bezeichnung 'Bezeichnung' "
            + ",status 'Status' "
            + ",baujahr 'Baujahr' "
            + ",fahrzeugmarke 'Fahrzeugmarke' "
            + ",aufbaufirma 'Aufbaufirma'"
            + ",instanznummer 'Instanzummer' "
            + "FROM FDISK.dbo.stmkfahrzeuge "
            + "WHERE status = 'aktiv' "),
    getLeerberichtFahrzeugBereich("SELECT "
            + "kennzeichen 'Kennzeichen' "
            + ",id_fahrzeuge 'Id_Fahrzeuge' "
            + ",fahrzeugtyp 'Fahrzeugtyp' "
            + ",taktischebezeichnung 'Taktische Bezeichnung' "
            + ",bezeichnung 'Bezeichnung' "
            + ",status 'Status' "
            + ",baujahr 'Baujahr' "
            + ",fahrzeugmarke 'Fahrzeugmarke' "
            + ",aufbaufirma 'Aufbaufirma'"
            + ",fzg.instanznummer 'Instanzummer' "
            + "FROM FDISK.dbo.stmkfahrzeuge fzg INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(fzg.instanznummer = f.instanznummer) "
            + "WHERE status = 'aktiv' "
            + "AND f.Bereich_Nr = ?"),
    getLeerberichtFahrzeugAbschnitt("SELECT "
            + "kennzeichen 'Kennzeichen' "
            + ",id_fahrzeuge 'Id_Fahrzeuge' "
            + ",fahrzeugtyp 'Fahrzeugtyp' "
            + ",taktischebezeichnung 'Taktische Bezeichnung' "
            + ",bezeichnung 'Bezeichnung' "
            + ",status 'Status' "
            + ",baujahr 'Baujahr' "
            + ",fahrzeugmarke 'Fahrzeugmarke' "
            + ",aufbaufirma 'Aufbaufirma'"
            + ",fzg.instanznummer 'Instanzummer' "
            + "FROM FDISK.dbo.stmkfahrzeuge fzg INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(fzg.instanznummer = f.instanznummer) "
            + "WHERE status = 'aktiv' "
            + "AND f.abschnitt_instanznummer = ?"),
    getLeerberichtFahrzeugFubwehr("SELECT "
            + "kennzeichen 'Kennzeichen' "
            + ",id_fahrzeuge 'Id_Fahrzeuge' "
            + ",fahrzeugtyp 'Fahrzeugtyp' "
            + ",taktischebezeichnung 'Taktische Bezeichnung' "
            + ",bezeichnung 'Bezeichnung' "
            + ",status 'Status' "
            + ",baujahr 'Baujahr' "
            + ",fahrzeugmarke 'Fahrzeugmarke' "
            + ",aufbaufirma 'Aufbaufirma'"
            + ",instanznummer 'Instanzummer' "
            + "FROM FDISK.dbo.stmkfahrzeuge "
            + "WHERE status = 'aktiv' "
            + "AND instanznummer = ?");

    private final String strStatement;

    private EnLeerberichtFahrzeug(String strStatement) {
        this.strStatement = strStatement;
    }

    @Override
    public String toString() {
        return strStatement;
    }
}
