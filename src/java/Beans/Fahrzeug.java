/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.Objects;

/**
 *
 * @author kinco_000
 */
public class Fahrzeug {
    private String strFahrzeugTyp;
    private String strKennzeichen;
    private int intBaujahr;
    private String strAufbaufirma;
    private String strTaktischeBezeichnung;
    private int intId_fahrzeuge;
    private String strBezeichnung;
    private String strFahrzeugmarke;

    public Fahrzeug(String strFahrzeugTyp, String strKennzeichen, int intBaujahr, String strAufbaufirma, String strTaktischeBezeichnung, int intId_fahrzeuge, String strBezeichnung, String strFahrzeugmarke) {
        this.strFahrzeugTyp = strFahrzeugTyp;
        this.strKennzeichen = strKennzeichen;
        this.intBaujahr = intBaujahr;
        this.strAufbaufirma = strAufbaufirma;
        this.strTaktischeBezeichnung = strTaktischeBezeichnung;
        this.intId_fahrzeuge = intId_fahrzeuge;
        this.strBezeichnung = strBezeichnung;
        this.strFahrzeugmarke = strFahrzeugmarke;
    }

    public String getStrFahrzeugTyp() {
        return strFahrzeugTyp;
    }

    public void setStrFahrzeugTyp(String strFahrzeugTyp) {
        this.strFahrzeugTyp = strFahrzeugTyp;
    }

    public String getStrKennzeichen() {
        return strKennzeichen;
    }

    public void setStrKennzeichen(String strKennzeichen) {
        this.strKennzeichen = strKennzeichen;
    }

    public int getIntBaujahr() {
        return intBaujahr;
    }

    public void setIntBaujahr(int intBaujahr) {
        this.intBaujahr = intBaujahr;
    }

    public String getStrAufbaufirma() {
        return strAufbaufirma;
    }

    public void setStrAufbaufirma(String strAufbaufirma) {
        this.strAufbaufirma = strAufbaufirma;
    }

    public String getStrTaktischeBezeichnung() {
        return strTaktischeBezeichnung;
    }

    public void setStrTaktischeBezeichnung(String strTaktischeBezeichnung) {
        this.strTaktischeBezeichnung = strTaktischeBezeichnung;
    }

    public int getIntId_fahrzeuge() {
        return intId_fahrzeuge;
    }

    public void setIntId_fahrzeuge(int intId_fahrzeuge) {
        this.intId_fahrzeuge = intId_fahrzeuge;
    }

    public String getStrBezeichnung() {
        return strBezeichnung;
    }

    public void setStrBezeichnung(String strBezeichnung) {
        this.strBezeichnung = strBezeichnung;
    }

    public String getStrFahrzeugmarke() {
        return strFahrzeugmarke;
    }

    public void setStrFahrzeugmarke(String strFahrzeugmarke) {
        this.strFahrzeugmarke = strFahrzeugmarke;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final Fahrzeug other = (Fahrzeug) obj;
        if (!Objects.equals(this.strFahrzeugTyp, other.strFahrzeugTyp)) {
            return false;
        }
        if (!Objects.equals(this.strKennzeichen, other.strKennzeichen)) {
            return false;
        }
        if (this.intBaujahr != other.intBaujahr) {
            return false;
        }
        if (!Objects.equals(this.strAufbaufirma, other.strAufbaufirma)) {
            return false;
        }
        if (!Objects.equals(this.strTaktischeBezeichnung, other.strTaktischeBezeichnung)) {
            return false;
        }
        if (this.intId_fahrzeuge != other.intId_fahrzeuge) {
            return false;
        }
        if (!Objects.equals(this.strBezeichnung, other.strBezeichnung)) {
            return false;
        }
        if (!Objects.equals(this.strFahrzeugmarke, other.strFahrzeugmarke)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Fahrzeug{" + "strFahrzeugTyp=" + strFahrzeugTyp + ", strKennzeichen=" + strKennzeichen + ", intBaujahr=" + intBaujahr + ", strAufbaufirma=" + strAufbaufirma + ", strTaktischeBezeichnung=" + strTaktischeBezeichnung + ", intId_fahrzeuge=" + intId_fahrzeuge + ", strBezeichnung=" + strBezeichnung + ", strFahrzeugmarke=" + strFahrzeugmarke + '}';
    }
}
