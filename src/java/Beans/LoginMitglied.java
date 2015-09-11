/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.Objects;

/**
 *
 * @author Yvonne
 */
public class LoginMitglied
{
    int intId_User; 
    int intFubwehr;
    int intIDGruppe;
    String strGruppe;

    public LoginMitglied(int intId_User, int intFubwehr, int intIDGruppe, String strGruppe)
    {
        this.intId_User = intId_User;
        this.intFubwehr = intFubwehr;
        this.intIDGruppe = intIDGruppe;
        this.strGruppe = strGruppe;
    }

    public int getIntId_User()
    {
        return intId_User;
    }

    public void setIntId_User(int intId_User)
    {
        this.intId_User = intId_User;
    }

    public int getIntFubwehr()
    {
        return intFubwehr;
    }

    public void setIntFubwehr(int intFubwehr)
    {
        this.intFubwehr = intFubwehr;
    }

    public int getIntIDGruppe()
    {
        return intIDGruppe;
    }

    public void setIntIDGruppe(int intIDGruppe)
    {
        this.intIDGruppe = intIDGruppe;
    }

    public String getStrGruppe()
    {
        return strGruppe;
    }

    public void setStrGruppe(String strGruppe)
    {
        this.strGruppe = strGruppe;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final LoginMitglied other = (LoginMitglied) obj;
        if (this.intId_User != other.intId_User)
        {
            return false;
        }
        if (this.intFubwehr != other.intFubwehr)
        {
            return false;
        }
        if (this.intIDGruppe != other.intIDGruppe)
        {
            return false;
        }
        if (!Objects.equals(this.strGruppe, other.strGruppe))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "LoginMitglied{" + "intId_User=" + intId_User + ", intFubwehr=" + intFubwehr + ", intIDGruppe=" + intIDGruppe + ", strGruppe=" + strGruppe + '}';
    }
    
    
    
    
}
