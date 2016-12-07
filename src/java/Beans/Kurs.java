package Beans;

import BL.BL;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author Yvonne
 */
public class Kurs implements Serializable {

    private int intIdKursart;
    private int intLehrgangsnummer;
    private String strKursbezeichnung;
    private String strKurskurzbezeichnung;
    private int intIdInstanzenVeranstalter;
    private int intIdInstanzenDurchfuehrend;
    private String strKursstatus;
    private int intAnzahlTeilnehmer;
    private int intAnzahlTaetigkeiten;
    private LinkedList<Mitglied> liTeilnehmer;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    private BL bl = new BL();

    public Kurs(int intIdKursart, int intLehrgangsnummer, String strKursbezeichnung, String strKurskurzbezeichnung, int intIdInstanzenVeranstalter, int intIdInstanzenDurchfuehrend, String strKursstatus, int intAnzhahlTeilnehmer) throws ClassNotFoundException {
        this.intIdKursart = intIdKursart;
        this.intLehrgangsnummer = intLehrgangsnummer;
        this.strKursbezeichnung = strKursbezeichnung;
        this.strKurskurzbezeichnung = strKurskurzbezeichnung;
        this.intIdInstanzenVeranstalter = intIdInstanzenVeranstalter;
        this.intIdInstanzenDurchfuehrend = intIdInstanzenDurchfuehrend;
        this.strKursstatus = strKursstatus;
        this.intAnzahlTeilnehmer = intAnzhahlTeilnehmer;
    }

    public Kurs(int intIdKursart, int intLehrgangsnummer, String strKursbezeichnung, String strKurskurzbezeichnung, int intIdInstanzenVeranstalter, int intIdInstanzenDurchfuehrend, String strKursstatus, LinkedList<Mitglied> liTeilnehmer) throws ClassNotFoundException {
        this.intIdKursart = intIdKursart;
        this.intLehrgangsnummer = intLehrgangsnummer;
        this.strKursbezeichnung = strKursbezeichnung;
        this.strKurskurzbezeichnung = strKurskurzbezeichnung;
        this.intIdInstanzenVeranstalter = intIdInstanzenVeranstalter;
        this.intIdInstanzenDurchfuehrend = intIdInstanzenDurchfuehrend;
        this.strKursstatus = strKursstatus;
        this.liTeilnehmer = liTeilnehmer;
    }

    public LinkedList<Mitglied> getLiTeilnehmer() {
        return liTeilnehmer;
    }

    public void setLiTeilnehmer(LinkedList<Mitglied> liTeilnehmer) {
        this.liTeilnehmer = liTeilnehmer;
    }

    public int getIntAnzahlTaetigkeiten() {
        return intAnzahlTaetigkeiten;
    }

    public void setIntAnzahlTaetigkeiten(int intAnzahlTaetigkeiten) {
        this.intAnzahlTaetigkeiten = intAnzahlTaetigkeiten;
    }

    public int getIntAnzahlTeilnehmer() {
        return intAnzahlTeilnehmer;
    }

    public void setIntAnzahlTeilnehmer(int intAnzahlTeilnehmer) {
        this.intAnzahlTeilnehmer = intAnzahlTeilnehmer;
    }

    public int getIntIdKursart() {
        return intIdKursart;
    }

    public void setIntIdKursart(int intIdKursart) {
        this.intIdKursart = intIdKursart;
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

    public String getStrKurskurzbezeichnung() {
        return strKurskurzbezeichnung;
    }

    public void setStrKurskurzbezeichnung(String strKurskurzbezeichnung) {
        this.strKurskurzbezeichnung = strKurskurzbezeichnung;
    }

    public int getIntIdInstanzenVeranstalter() {
        return intIdInstanzenVeranstalter;
    }

    public void setIntIdInstanzenVeranstalter(int intIdInstanzenVeranstalter) {
        this.intIdInstanzenVeranstalter = intIdInstanzenVeranstalter;
    }

    public int getIntIdInstanzenDurchfuehrend() {
        return intIdInstanzenDurchfuehrend;
    }

    public void setIntIdInstanzenDurchfuehrend(int intIdInstanzenDurchfuehrend) {
        this.intIdInstanzenDurchfuehrend = intIdInstanzenDurchfuehrend;
    }

    public String getStrKursstatus() {
        return strKursstatus;
    }

    public void setStrKursstatus(String strKursstatus) {
        this.strKursstatus = strKursstatus;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.intIdKursart;
        hash = 83 * hash + this.intLehrgangsnummer;
        hash = 83 * hash + Objects.hashCode(this.strKursbezeichnung);
        hash = 83 * hash + Objects.hashCode(this.strKurskurzbezeichnung);
        hash = 83 * hash + this.intIdInstanzenVeranstalter;
        hash = 83 * hash + this.intIdInstanzenDurchfuehrend;
        hash = 83 * hash + Objects.hashCode(this.strKursstatus);
        hash = 83 * hash + this.intAnzahlTeilnehmer;
        hash = 83 * hash + this.intAnzahlTaetigkeiten;
        hash = 83 * hash + Objects.hashCode(this.liTeilnehmer);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Kurs other = (Kurs) obj;
        if (this.intIdKursart != other.intIdKursart) {
            return false;
        }
        if (this.intLehrgangsnummer != other.intLehrgangsnummer) {
            return false;
        }
        if (this.intIdInstanzenVeranstalter != other.intIdInstanzenVeranstalter) {
            return false;
        }
        if (this.intIdInstanzenDurchfuehrend != other.intIdInstanzenDurchfuehrend) {
            return false;
        }
        if (this.intAnzahlTeilnehmer != other.intAnzahlTeilnehmer) {
            return false;
        }
        if (this.intAnzahlTaetigkeiten != other.intAnzahlTaetigkeiten) {
            return false;
        }
        if (!Objects.equals(this.strKursbezeichnung, other.strKursbezeichnung)) {
            return false;
        }
        if (!Objects.equals(this.strKurskurzbezeichnung, other.strKurskurzbezeichnung)) {
            return false;
        }
        if (!Objects.equals(this.strKursstatus, other.strKursstatus)) {
            return false;
        }
        if (!Objects.equals(this.liTeilnehmer, other.liTeilnehmer)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

        if (strKursbezeichnung == null) {
            strKursbezeichnung = "";
        }
        if (strKursstatus == null) {
            strKursstatus = "";
        }

        strKursbezeichnung = bl.formatiereAusgabe(strKursbezeichnung);
        strKursstatus = bl.formatiereAusgabe(strKursstatus);

        String strHtml = "<tr><td style='width: 250px;'>"
                + strKursbezeichnung + "</td><td>";
        String fub = "";
        boolean first = true;
        for (Mitglied teilnehmer : liTeilnehmer) {
            if (teilnehmer.strStammblattnummer == null) {
                teilnehmer.strStammblattnummer = "";
            }
            if (teilnehmer.strDienstgrad == null) {
                teilnehmer.strDienstgrad = "";
            }
            if (teilnehmer.strTitel == null) {
                teilnehmer.strTitel = "";
            }
            if (teilnehmer.strVorname == null) {
                teilnehmer.strVorname = "";
            }
            if (teilnehmer.strZuname == null) {
                teilnehmer.strZuname = "";
            }
            if (teilnehmer.strFubwehr == null) {
                teilnehmer.strFubwehr = "";
            }
            teilnehmer.strZuname = bl.formatiereAusgabe(teilnehmer.strZuname);
            teilnehmer.strVorname = bl.formatiereAusgabe(teilnehmer.strVorname);
            
            if (first) {
                strHtml += "<div><b>" + teilnehmer.strInstanzname + ":</b></div>";
                strHtml += "<div>" + teilnehmer.strStammblattnummer + " " + teilnehmer.strDienstgrad + " " + teilnehmer.strTitel + " " + teilnehmer.strZuname + " " + teilnehmer.strVorname + "</div>";
                fub = teilnehmer.strFubwehr;
                first = false;
            } else if (!teilnehmer.strFubwehr.equals(fub)) {
                strHtml += "<div>&nbsp</div>";
                strHtml += "<div><b>" + teilnehmer.strInstanzname + ":</b></div>";
                strHtml += "<div>" + teilnehmer.strStammblattnummer + " " + teilnehmer.strDienstgrad + " " + teilnehmer.strTitel + " " + teilnehmer.strZuname + " " + teilnehmer.strVorname + "</div>";
                fub = teilnehmer.strFubwehr;
            } else {
                strHtml += "<div>" + teilnehmer.strStammblattnummer + " " + teilnehmer.strDienstgrad + " " + teilnehmer.strTitel + " " + teilnehmer.strZuname + " " + teilnehmer.strVorname + "</div>";
            }
            
        }
        strHtml += "</td><td style='width: 50;'>" + liTeilnehmer.size() + "</td></tr>";
        System.out.println("STRHTML Kursstatistik: " + strHtml);
                
        return strHtml;
    }

}
