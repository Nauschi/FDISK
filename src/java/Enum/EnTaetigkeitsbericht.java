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
public enum EnTaetigkeitsbericht
{
    getTaetigkeitsberichtBereich("SELECT DISTINCT id_berichte 'ID'"
                    + " ,tb.instanznummer 'Instanznummer'"
                    + " ,tb.instanzname 'Instanzname'"
                    + " ,taetigkeitsart 'Taetigkeitsart'"
                    + " ,taetigkeitsunterart 'Taetigkeitsunterart'"
                    + " ,nummer 'Nummer'"
                    + " ,beginn 'Beginn'"
                    + " ,ende 'Ende'"
                    + " ,strasse 'Strasse'"
                    + " ,nummeradr 'NummerAdr'"
                    + " ,stiege 'Stiege'"
                    + " ,plz 'PLZ'"
                    + " ,ort 'Ort'"
                    + " ,meldung 'Meldung'"
                    + " ,Fehlalarm 'Fehlalarm'"
                    + " FROM FDISK.dbo.stmktaetigkeitsberichte tb INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(tb.instanznummer = f.instanznummer)"
                    + " WHERE f.Bereich_Nr = ?"),
    getTaetigkeitsberichtAbschnitt("SELECT DISTINCT id_berichte 'ID'"
                        + " ,tb.instanznummer 'Instanznummer'"
                        + " ,tb.instanzname 'Instanzname'"
                        + " ,taetigkeitsart 'Taetigkeitsart'"
                        + " ,taetigkeitsunterart 'Taetigkeitsunterart'"
                        + " ,nummer 'Nummer'"
                        + " ,beginn 'Beginn'"
                        + " ,ende 'Ende'"
                        + " ,strasse 'Strasse'"
                        + " ,nummeradr 'NummerAdr'"
                        + " ,stiege 'Stiege'"
                        + " ,plz 'PLZ'"
                        + " ,ort 'Ort'"
                        + " ,meldung 'Meldung'"
                        + " ,Fehlalarm 'Fehlalarm'"
                        + " FROM FDISK.dbo.stmktaetigkeitsberichte tb INNER JOIN FDISK.dbo.qry_alle_feuerwehren_mit_Abschnitt_und_Bereich f ON(tb.instanznummer = f.instanznummer)"
                        + " WHERE f.abschnitt_instanznummer = ?"),
    getTaetigkeitsberichtFubwehr("SELECT DISTINCT id_berichte 'ID'"
                        + " ,instanznummer 'Instanznummer'"
                        + " ,instanzname 'Instanzname'"
                        + " ,taetigkeitsart 'Taetigkeitsart'"
                        + " ,taetigkeitsunterart 'Taetigkeitsunterart'"
                        + " ,nummer 'Nummer'"
                        + " ,beginn 'Beginn'"
                        + " ,ende 'Ende'"
                        + " ,strasse 'Strasse'"
                        + " ,nummeradr 'NummerAdr'"
                        + " ,stiege 'Stiege'"
                        + " ,plz 'PLZ'"
                        + " ,ort 'Ort'"
                        + " ,meldung 'Meldung'"
                        + " ,Fehlalarm 'Fehlalarm'"
                        + " FROM FDISK.dbo.stmktaetigkeitsberichte"
                        + " WHERE instanznummer = ?");
    
    private final String strStatement;

    private EnTaetigkeitsbericht(String strStatement)
    {
        this.strStatement = strStatement;
    }

    @Override
    public String toString()
    {
        return strStatement;
    }
}
