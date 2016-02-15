/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.ssdfsdfds
 */
package Servlet;

import Beans.Berechtigung;
import Beans.Fahrzeug;
import Beans.Kurs;
import Beans.Rohbericht;
import Database.DB_Access;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author user
 */
@WebServlet(name = "MainServlet", urlPatterns
        =
        {
            "/MainServlet"
        })
public class MainServlet extends HttpServlet
{

    private DB_Access access;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet MainServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MainServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
//        HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("lastPage") == null)
//        {
//            System.out.println("MainServlet.doPost: session = null");
        request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
        return;
//        }
//        String strLastPage = (String) session.getAttribute("lastPage");
//        request.getRequestDispatcher("jsp/" + strLastPage + ".jsp").forward(request, response);
//        return;
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        if (request.getParameter("button_login") != null)
        {
            loginUser(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedIn") == null)
        {
            System.out.println("MainServlet.doPost: session = null");
            request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
            return;
        } else
        {
            if (request.getParameter("select_berechtigung") != null)
            {
                try
                {
                    getBerechtigungsinformationen(request, response, session);
                    return;
                } catch (Exception ex)
                {
                    System.out.println(ex.toString());
                }
            } else if (request.getParameter("dynamisch") != null)
            {
                System.out.println("MainServlet.doPost: dynamisch");
                try
                {
                    session.setAttribute("hashMap_typ", access.getMethodeFuerTyp());
                    HashMap hm = access.getMethodeFuerTyp();
                    Iterator it = hm.entrySet().iterator();
                    while (it.hasNext())
                    {
                        Map.Entry pair = (Map.Entry) it.next();
                        System.out.println(pair.getKey() + " = " + pair.getValue());
                        it.remove(); // avoids a ConcurrentModificationException
                    }
                    request.getRequestDispatcher("jsp/dynamisch_mitglieder.jsp").forward(request, response);
                    return;
                } catch (Exception ex)
                {
                    Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if (request.getParameter("vordefiniert") != null)
            {
                System.out.println("MainServlet.doPost: vordefiniert");
                request.getRequestDispatcher("jsp/vordefiniert.jsp").forward(request, response);
                return;
            } else if (request.getParameter("button_vorschau") != null)
            {
                generiereVorschau(request, response);
                return;
            } else if (request.getParameter("hidden_action") != null) //Dynamisch Vorschau oder plus minus zeile
            {

                System.out.println("MainServlet.doPost: hidden_action: " + request.getParameter("hidden_action"));
                if (request.getParameter("hidden_action").equals("vorschau"))
                {
                    System.out.println("MainServlet.doPost: hidden_action: in vorschau");
                    generiereDynamischeVorschau(request, response, session);
                } else
                {
                    System.out.println("MainServlet.doPost: hidden_action: in plus-minus");

                }
                request.getRequestDispatcher("jsp/dynamisch_mitglieder.jsp").forward(request, response);
                return;
            } else if (request.getParameter("logout") != null)
            {
                System.out.println("MainServlet.doPost: logout");

                if (request.getSession(false) == null)
                {
                    System.out.println("session deleted");
                }
                request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
                return;
            }
        }

        processRequest(request, response);
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        System.out.println("MainServlet.loginUser: button_Login");
        String strBenutzername = request.getParameter("input_benutzername");
        String strKennwort = request.getParameter("input_kennwort");
        System.out.println("Benutzername: " + strBenutzername);
        System.out.println("Kennwort: " + strKennwort);

        //UserID zu login Daten bekommen
        int intIDUser = -1;
        try
        {
            intIDUser = access.getUserID(strBenutzername, strKennwort);
        } catch (Exception ex)
        {
            System.out.println("MainServlet.doPost: login:" + ex.toString());
            request.setAttribute("db_error", ex.toString());
        }
        if (intIDUser != -1) //if (true)
        {
            HttpSession session = request.getSession(true);
            try
            {

                session.setAttribute("loggedIn", true);
                session.setAttribute("intUserID", intIDUser);
                LinkedList<Berechtigung> liBerechtigung = access.getBerechtigungen(intIDUser);
                request.setAttribute("berechtigungen", liBerechtigung);
                if (liBerechtigung.size() == 1)
                {
                    generiereBerechtigungVorschau(request, response, session, liBerechtigung.get(0));
                    return;
                }
                System.out.println("Berechtigungen gesetzt");
            } catch (Exception ex)
            {
                Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
            return;
        } else
        {
            request.setAttribute("login_error", true);
            request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
            return;
        }
    }

    private void getBerechtigungsinformationen(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
    {
        int intIDUser = (int) session.getAttribute("intUserID");
        LinkedList<Berechtigung> liBerechtigungen = access.getBerechtigungen(intIDUser);
        String strBerechtigung = request.getParameter("select_berechtigung");
        Berechtigung aktBerechtigung = null;
        for (Berechtigung berechtigung : liBerechtigungen)
        {
            System.out.println(berechtigung.getStrBerechtigung() + " equals " + strBerechtigung);
            if (strBerechtigung.equals(berechtigung.getStrBerechtigung()))
            {
                aktBerechtigung = berechtigung;
            }
        }
        generiereBerechtigungVorschau(request, response, session, aktBerechtigung);
    }

    private void generiereBerechtigungVorschau(HttpServletRequest request, HttpServletResponse response, HttpSession session, Berechtigung aktBerechtigung) throws Exception
    {
        if (aktBerechtigung.getIntIDGruppe() == 5)
        {
            System.out.println("MainServlet.generiereBerechtigungVorschau: id=5");
            session.setAttribute("bezirk", access.getBezrik(aktBerechtigung.getIntBereich()));
            session.setAttribute("bezirkName", null);
            session.setAttribute("abschnitt", null);
            session.setAttribute("abschnittName", null);
            session.setAttribute("feuerwehr", null);
        } else if (aktBerechtigung.getIntIDGruppe() == 15)
        {
            System.out.println("MainServlet.generiereBerechtigungVorschau: id=15");
            session.setAttribute("bezirk", null);
            session.setAttribute("bezirkName", access.getBereichsnameFuerBereichnnummer(aktBerechtigung.getIntBereich()));
            session.setAttribute("abschnitt", access.getAbschnitt(aktBerechtigung.getIntAbschnitt()));
            session.setAttribute("abschnittName", null);
            session.setAttribute("feuerwehr", null);
        } else if (aktBerechtigung.getIntIDGruppe() == 9 || aktBerechtigung.getIntIDGruppe() == 0)
        {
            System.out.println("MainServlet.generiereBerechtigungVorschau: id=9/0");
            session.setAttribute("bezirk", null);
            session.setAttribute("bezirkName", access.getBereichsnameFuerBereichnnummer(aktBerechtigung.getIntBereich()));
            session.setAttribute("abschnitt", null);
            session.setAttribute("abschnittName", access.getAbschnittsnameFuerAbschnittsnummer(aktBerechtigung.getIntAbschnitt()));
            session.setAttribute("feuerwehr", access.getFeuerwehr(aktBerechtigung.getStrFubwehr()));
        } else
        {
            session.setAttribute("bezirk", null);
            session.setAttribute("bezirkName", null);
            session.setAttribute("abschnitt", null);
            session.setAttribute("abschnittName", null);
            session.setAttribute("feuerwehr", null);
        }
        request.getRequestDispatcher("jsp/vordefiniert.jsp").forward(request, response);
    }

    private void generiereVorschau(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        System.out.println("MainServlet.generiereVorschau: button_vorschau");
        try
        {

            LinkedList<Rohbericht> liRohberichte = (LinkedList<Rohbericht>) this.getServletContext().getAttribute("rohberichte");
            String strBericht = request.getParameter("input_aktbericht");

            int intBereichNr = Integer.parseInt(request.getParameter("select_bezirk"));
            int intAbschnittNr = Integer.parseInt(request.getParameter("select_abschnitt"));
            String strFeuerwehr = request.getParameter("select_feuerwehr");
            if (strBericht.equals(liRohberichte.get(0).getStrBerichtname()))//Einfache Mitgliederliste
            {
                request.setAttribute("liste", access.getEinfacheMitgliederliste(intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals(liRohberichte.get(1).getStrBerichtname()))//Erreichbarkeitsliste
            {
                request.setAttribute("liste", access.getErreichbarkeitsliste(intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals(liRohberichte.get(2).getStrBerichtname()))//Adressliste
            {

                request.setAttribute("liste", access.getAdressListe(intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals(liRohberichte.get(3).getStrBerichtname()))//Geburtstagsliste
            {
                int intJahr = Integer.parseInt(request.getParameter("select_jahr"));
                request.setAttribute("liste", access.getGeburtstagsliste(intJahr, intBereichNr, intAbschnittNr, strFeuerwehr)); //welche Zahl??
            } else if (strBericht.equals(liRohberichte.get(4).getStrBerichtname()))//Dienstzeitliste
            {
                request.setAttribute("liste", access.getDienstzeitListe(intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals(liRohberichte.get(5).getStrBerichtname()))//Stundenauswertung je Mitglied je Instanz
            {
                //request.setAttribute("liste", );
            } else if (strBericht.equals(liRohberichte.get(6).getStrBerichtname()))//Tätigkeitsbericht leer
            {
                request.setAttribute("liste", access.getLeerberichtMitglied(intBereichNr, intAbschnittNr, strFeuerwehr));
                request.setAttribute("zusatz_liste", access.getLeerberichtFahrzeug(intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals(liRohberichte.get(7).getStrBerichtname()))//Einsatzbericht leer
            {
                //request.setAttribute("liste", );
            } else if (strBericht.equals(liRohberichte.get(8).getStrBerichtname()))//Übungsbericht leer
            {
                request.setAttribute("liste", access.getLeerberichtMitglied(intBereichNr, intAbschnittNr, strFeuerwehr));
                request.setAttribute("zusatz_liste", access.getLeerberichtFahrzeug(intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals(liRohberichte.get(9).getStrBerichtname()))//Liste aller Einsatzberichte
            {
                String strVonDatum = request.getParameter("input_von_datum");
                String strBisDatum = request.getParameter("input_bis_datum");
                System.out.println("MainServler.generiereVorschau: " + strVonDatum + " bis " + strBisDatum);
                //!!Bitte das gewählte Datum als String übergeben (strVon, strBis)
                request.setAttribute("liste", access.getEinsatzbericht(strVonDatum, strBisDatum, intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals(liRohberichte.get(10).getStrBerichtname()))//Liste aller Tätigkeitsberichte
            {
                String strVonDatum = request.getParameter("input_von_datum");
                String strBisDatum = request.getParameter("input_bis_datum");
                //!!Bitte das gewählte Datum als String übergeben (strVon, strBis)
                request.setAttribute("liste", access.getTaetigkeitsbericht(strVonDatum, strBisDatum, intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals(liRohberichte.get(11).getStrBerichtname()))//Liste aller Übungsberichte
            {
                String strVonDatum = request.getParameter("input_von_datum");
                String strBisDatum = request.getParameter("input_bis_datum");
                //!!Bitte das gewählte Datum als String übergeben (strVon, strBis)
                request.setAttribute("liste", access.getUebungsbericht(strVonDatum, strBisDatum, intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals(liRohberichte.get(12).getStrBerichtname()))//Liste aller Berichte
            {
                String strVonDatum = request.getParameter("input_von_datum");
                String strBisDatum = request.getParameter("input_bis_datum");
                //!!Bitte das gewählte Datum als String übergeben (strVon, strBis)
                request.setAttribute("liste", access.getAlleBerichte(strVonDatum, strBisDatum, intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals(liRohberichte.get(13).getStrBerichtname()))//Kursstatistik
            {
                String strVonDatum = request.getParameter("input_von_datum");
                String strBisDatum = request.getParameter("input_bis_datum");
                request.setAttribute("liste", access.getKursstatistiktaetigkeit(intBereichNr, intAbschnittNr, strFeuerwehr, strVonDatum, strBisDatum));
                //Zweite methode noch aufrufen..
                request.setAttribute("zusatz_informationen", generiereKurstatistikZusatzTable(strVonDatum, strBisDatum));
            } else if (strBericht.equals(liRohberichte.get(14).getStrBerichtname()))//Digitales Fahrtenbuch
            {
                String strVonDatum = request.getParameter("input_von_datum");
                String strBisDatum = request.getParameter("input_bis_datum");
//                String strKennzeichen = request.getParameter("input_kennzeichen");
                String strKennzeichen = "GU331FF";

                //Login für Farhetnbuch implementiert, also können die Übergabeparameter da dazu gemacht werden lg nauschi
                //System.out.println(access.getFahrtenbuch("", "", strKennzeichen).toString());
                LinkedList<Fahrzeug> liFahrzeuge = access.getFahrtenbuch(intBereichNr, intAbschnittNr, strFeuerwehr, strVonDatum, strBisDatum, strKennzeichen);
                String strDetails = access.getDetailsFuerFahrtenbuchFahrzeug(liFahrzeuge);
                request.setAttribute("zusatz_informationen", strDetails);
                request.setAttribute("liste", liFahrzeuge);
            } else
            {
                System.out.println("MainServlet.generiereVorschau: last else");
            }
        } catch (Exception ex)
        {
            Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.getRequestDispatcher("jsp/vordefiniert.jsp").forward(request, response);
        return;
    }

    private String generiereKurstatistikZusatzTable(String strVonDatum, String strBisDatum) throws Exception
    {
        LinkedList<Kurs> liKurse = access.getKursstatistikkurse(strVonDatum, strBisDatum);
        String strZusatzInfo = "<table class='tablesorter2 ui celled table'><thead>";
        strZusatzInfo+="<tr>"
                + "<th data-content='nach Kursbezeichnung sortieren' class='sort'>Kursbezeichnung</th>"
                + "<th data-content='nach Kursstatus sortieren' class='sort'>Kursstatus</th>"
                + "<th data-content='nach Kursdatum sortieren' class='sort'>Kursdatum</th>"
                + "<th>-</th>"
                + "</tr>";
        strZusatzInfo+="</thead><tbody>";
        for (Kurs kurs : liKurse)
        {
            strZusatzInfo+=kurs.toString();
        }
        strZusatzInfo+="</tbody></table>";
        return strZusatzInfo;
    }

    private void generiereDynamischeVorschau(HttpServletRequest request, HttpServletResponse response, HttpSession session)
    {

        int intZaehler = Integer.parseInt(request.getParameter("hidden_zaehler"));
        System.out.println("MainServlet.doPost: hidden_action: zaehler: " + intZaehler);
        String[][] strDaten = new String[intZaehler][6];
        for (int i = 0; i < intZaehler; i++)
        {
            String strZeile = request.getParameter("hidden_element_data_" + (i + 1));
            System.out.println("MainServlet.generiereDynamischeVorschau: zeile: " + strZeile);
            String[] splitString = strZeile.split(";");
            System.out.println("MainServlet.generiereDynamischeVorschau: splitString size: " + splitString.length);
            for (int j = 0; j < 7; j++)
            {
                if (splitString[j].isEmpty())
                {
                    splitString[j] = "N/A";
                }
            }
            strDaten[i][0] = splitString[0];
            strDaten[i][1] = splitString[1];
            strDaten[i][2] = splitString[3];
            strDaten[i][3] = splitString[4];
            strDaten[i][4] = splitString[5];
            strDaten[i][5] = splitString[6];
        }

        try
        {
            StringBuilder sbDynHTML = access.getDynamischerBericht(strDaten);
            System.out.println("MainServlet.erstelleDynamischenBericht: sbDynHTML: " + sbDynHTML);
            request.setAttribute("dyn_table", sbDynHTML);
        } catch (Exception ex)
        {
            Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config); //To change body of generated methods, choose Tools | Templates.
        try
        {
            access = DB_Access.getInstance();

        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        String strPath = "";
        try
        {
            String strContextPath = this.getServletContext().getRealPath("/");
            strPath = strContextPath
                    + File.separator + "res";

            leseRohberichte(strPath + File.separator + "Rohberichte.csv");
            leseTypenDynamisch(strPath + File.separator + "TypenDynamisch.csv");
        } catch (IOException ex)
        {
//            try
//            {
//                strPath = System.getProperty("user.dir")
//                        + File.separator + "web"
//                        + File.separator + "res"
//                        + File.separator + "Rohberichte.csv";
//
//                leseRohberichte(strPath);
//            } catch (IOException ex1)
//            {
//                Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex1);
//            }
        }

    }

    /**
     * Liest Informationen über Berichte von einem .csv File
     *
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public void leseRohberichte(String strPfad) throws UnsupportedEncodingException, IOException
    {
        ServletContext servletContext = this.getServletContext();

        File file = new File(strPfad);
        LinkedList<Rohbericht> liRohberichte = new LinkedList<>();

        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, "ISO-8859-1");
        BufferedReader br = new BufferedReader(isr);
        String strReihe;

        while ((strReihe = br.readLine()) != null)
        {
            String strBerichtname;
            LinkedList<String> liBerichtSpalten = new LinkedList<>();
            int intTypeOfDateUI;
            String[] strElemente = strReihe.split(";");
            strBerichtname = strElemente[0];
            intTypeOfDateUI = Integer.parseInt(strElemente[strElemente.length - 1]);
            for (int i = 1; i < strElemente.length - 1; i++)
            {
                liBerichtSpalten.add(strElemente[i]);
            }

            liRohberichte.add(new Rohbericht(strBerichtname, liBerichtSpalten, intTypeOfDateUI));
        }
        br.close();

        servletContext.setAttribute("rohberichte", liRohberichte);
    }

    public void leseTypenDynamisch(String strPfad) throws FileNotFoundException, UnsupportedEncodingException, IOException
    {
        File file = new File(strPfad);
        LinkedList<String> liTypen = new LinkedList<>();

        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, "ISO-8859-1");
        BufferedReader br = new BufferedReader(isr);
        String strReihe;

        while ((strReihe = br.readLine()) != null)
        {
            liTypen.add(strReihe);
        }
        br.close();

        this.getServletContext().setAttribute("Typen", liTypen);
    }

}
