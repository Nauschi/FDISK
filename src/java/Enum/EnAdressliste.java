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
public enum EnAdressliste
{
    getAdresslisteBereich("SELECT adressen.id_adressen 'AdressID', adressen.strasse 'Strasse', adressen.nummer 'Nummer',"
                        + " adressen.stiege 'Stiege', adressen.plz 'PLZ', adressen.ort 'Ort', mitglied.id_personen 'PersID',"
                        + " mitglied.standesbuchnummer 'STB', mitglied.dienstgrad 'DGR',"
                        + " mitglied.titel 'Titel', mitglied.vorname 'Vorname', mitglied.zuname 'Zuname',"
                        + " mitglied.geburtsdatum 'Geburtsdatum'"
                        + " FROM FDISK.dbo.stmkadressen adressen INNER JOIN FDISK.dbo.stmkmitglieder mitglied"
                        + " ON (adressen.id_personen = mitglied.id_personen)"
                        + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(mitglied.instanznummer = f.instanznummer)"
                        + " WHERE f.Bereich_Nr = ?"),
    getAdresslisteAbschnitt("SELECT adressen.id_adressen 'AdressID', adressen.strasse 'Strasse', adressen.nummer 'Nummer',"
                        + " adressen.stiege 'Stiege', adressen.plz 'PLZ', adressen.ort 'Ort', mitglied.id_personen 'PersID',"
                        + " mitglied.standesbuchnummer 'STB', mitglied.dienstgrad 'DGR',"
                        + " mitglied.titel 'Titel', mitglied.vorname 'Vorname', mitglied.zuname 'Zuname',"
                        + " mitglied.geburtsdatum 'Geburtsdatum'"
                        + " FROM FDISK.dbo.stmkadressen adressen INNER JOIN FDISK.dbo.stmkmitglieder mitglied"
                        + " ON (adressen.id_personen = mitglied.id_personen)"
                        + " INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(mitglied.instanznummer = f.instanznummer)"
                        + " WHERE f.abschnitt_instanznummer = ?"),
    getAdresslisteFubwehr("SELECT adressen.id_adressen 'AdressID', adressen.strasse 'Strasse', adressen.nummer 'Nummer',"
                        + " adressen.stiege 'Stiege', adressen.plz 'PLZ', adressen.ort 'Ort', mitglied.id_personen 'PersID',"
                        + " mitglied.standesbuchnummer 'STB', mitglied.dienstgrad 'DGR',"
                        + " mitglied.titel 'Titel', mitglied.vorname 'Vorname', mitglied.zuname 'Zuname',"
                        + " mitglied.geburtsdatum 'Geburtsdatum'"
                        + " FROM FDISK.dbo.stmkadressen adressen INNER JOIN FDISK.dbo.stmkmitglieder mitglied"
                        + " ON (adressen.id_personen = mitglied.id_personen)"
                        + " WHERE mitglied.instanznummer = ?");
    
    private final String strStatement;

    private EnAdressliste(String strStatement)
    {
        this.strStatement = strStatement;
    }

    @Override
    public String toString()
    {
        return strStatement;
    }
}
