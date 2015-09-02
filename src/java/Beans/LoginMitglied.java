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
    String strVorname;
    String strNachname;
    String strTitel; 

    public LoginMitglied(int intId_User, String strVorname, String strNachname, String strTitel)
    {
        this.intId_User = intId_User;
        this.strVorname = strVorname;
        this.strNachname = strNachname;
        this.strTitel = strTitel;
    }

    public int getIntId_User()
    {
        return intId_User;
    }

    public void setIntId_User(int intId_User)
    {
        this.intId_User = intId_User;
    }

    public String getStrVorname()
    {
        return strVorname;
    }

    public void setStrVorname(String strVorname)
    {
        this.strVorname = strVorname;
    }

    public String getStrNachname()
    {
        return strNachname;
    }

    public void setStrNachname(String strNachname)
    {
        this.strNachname = strNachname;
    }

    public String getStrTitel()
    {
        return strTitel;
    }

    public void setStrTitel(String strTitel)
    {
        this.strTitel = strTitel;
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
        if (!Objects.equals(this.strVorname, other.strVorname))
        {
            return false;
        }
        if (!Objects.equals(this.strNachname, other.strNachname))
        {
            return false;
        }
        if (!Objects.equals(this.strTitel, other.strTitel))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "LoginMitglied{" + "intId_User=" + intId_User + ", strVorname=" + strVorname + ", strNachname=" + strNachname + ", strTitel=" + strTitel + '}';
    }
    
    
}
