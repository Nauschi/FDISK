/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author kinco_000
 */
public class KursAlt {
    private int intId_Kurse;
    private int intId_Kursarten;
    private int intLehrgangsnummer;
    private String strKursbezeichnung;
    private Date dateDatum;
    private int intId_instanzen_veranstalter;
    private int intId_instanzen_durchfuehrend;
    private String strKursstatus;
    private int intAnzahlBesucher;

    public KursAlt(int intId_kurse, int intId_Kursarten, int intLehrgangsnummer, String strKursbezeichnung, Date dateDatum, int intId_instanzen_veranstalter, int intId_instanzen_durchfuehrend, String strKursstatus, int intAnzahlBesucher) {
        this.intId_Kurse = intId_kurse;
        this.intId_Kursarten = intId_Kursarten;
        this.intLehrgangsnummer = intLehrgangsnummer;
        this.strKursbezeichnung = strKursbezeichnung;
        this.dateDatum = dateDatum;
        this.intId_instanzen_veranstalter = intId_instanzen_veranstalter;
        this.intId_instanzen_durchfuehrend = intId_instanzen_durchfuehrend;
        this.strKursstatus = strKursstatus;
        this.intAnzahlBesucher = intAnzahlBesucher;
    }

    public int getIntId_Kurse() {
        return intId_Kurse;
    }

    public void setIntId_Kurse(int intId_Kurse) {
        this.intId_Kurse = intId_Kurse;
    }

    public int getIntId_Kursarten() {
        return intId_Kursarten;
    }

    public void setIntId_Kursarten(int intId_Kursarten) {
        this.intId_Kursarten = intId_Kursarten;
    }

    public int getIntLehrgangsnummer() {
        return intLehrgangsnummer;
    }

    public void setIntLehrgangsnummer(int intLehrgangsnummer) {
        this.intLehrgangsnummer = intLehrgangsnummer;
    }

    public String getStrKursbezeichnung() {
        return strKursbezeichnung;
    }

    public void setStrKursbezeichnung(String strKursbezeichnung) {
        this.strKursbezeichnung = strKursbezeichnung;
    }

    public Date getDateDatum() {
        return dateDatum;
    }

    public void setDateDatum(Date dateDatum) {
        this.dateDatum = dateDatum;
    }

    public int getIntId_instanzen_veranstalter() {
        return intId_instanzen_veranstalter;
    }

    public void setIntId_instanzen_veranstalter(int intId_instanzen_veranstalter) {
        this.intId_instanzen_veranstalter = intId_instanzen_veranstalter;
    }

    public int getIntId_instanzen_durchfuehrend() {
        return intId_instanzen_durchfuehrend;
    }

    public void setIntId_instanzen_durchfuehrend(int intId_instanzen_durchfuehrend) {
        this.intId_instanzen_durchfuehrend = intId_instanzen_durchfuehrend;
    }

    public String getStrKursstatus() {
        return strKursstatus;
    }

    public void setStrKursstatus(String strKursstatus) {
        this.strKursstatus = strKursstatus;
    }

    public int getIntAnzahlBesucher() {
        return intAnzahlBesucher;
    }

    public void setIntAnzahlBesucher(int intAnzahlBesucher) {
        this.intAnzahlBesucher = intAnzahlBesucher;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KursAlt other = (KursAlt) obj;
        if (this.intId_Kurse != other.intId_Kurse) {
            return false;
        }
        if (this.intId_Kursarten != other.intId_Kursarten) {
            return false;
        }
        if (this.intLehrgangsnummer != other.intLehrgangsnummer) {
            return false;
        }
        if (!Objects.equals(this.strKursbezeichnung, other.strKursbezeichnung)) {
            return false;
        }
        if (!Objects.equals(this.dateDatum, other.dateDatum)) {
            return false;
        }
        if (this.intId_instanzen_veranstalter != other.intId_instanzen_veranstalter) {
            return false;
        }
        if (this.intId_instanzen_durchfuehrend != other.intId_instanzen_durchfuehrend) {
            return false;
        }
        if (!Objects.equals(this.strKursstatus, other.strKursstatus)) {
            return false;
        }
        if (this.intAnzahlBesucher != other.intAnzahlBesucher) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Kurs{" + "intId_kurse=" + intId_Kurse + ", intId_Kursarten=" + intId_Kursarten + ", intLehrgangsnummer=" + intLehrgangsnummer + ", strKursbezeichnung=" + strKursbezeichnung + ", dateDatum=" + dateDatum + ", intId_instanzen_veranstalter=" + intId_instanzen_veranstalter + ", intId_instanzen_durchfuehrend=" + intId_instanzen_durchfuehrend + ", strKursstatus=" + strKursstatus + ", intAnzahlBesucher=" + intAnzahlBesucher + '}';
    }

}
