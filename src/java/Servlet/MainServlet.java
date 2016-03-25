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
 * @author Marcel Schmidt
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
     * Wird nur zu Beginn verwendet und leitet zum Login.jsp weiter
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
     * Je nach auf dem request vorhandene Parameter werden
     * Methoden aufgerufen
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
                } catch (Exception ex)
                {
                    Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                request.getRequestDispatcher("jsp/dynamisch_mitglieder.jsp").forward(request, response);
                return;

            } else if (request.getParameter("vordefiniert") != null)
            {
                request.getRequestDispatcher("jsp/vordefiniert.jsp").forward(request, response);
                return;
            } else if (request.getParameter("button_ladeMitglieder") != null)
            {
                try
                {
                    ladeMitgliederFuerStundenauswertung(request);
                } catch (Exception ex)
                {
                    System.out.println("ladeMitglieder.exception: " + ex.toString());
                }
                request.getRequestDispatcher("jsp/vordefiniert.jsp").forward(request, response);
                return;
            } else if (request.getParameter("button_ladeKennzeichen") != null)
            {
                try
                {
                    ladeKennzeichen(request);
                } catch (Exception ex)
                {
                    Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                request.getRequestDispatcher("jsp/vordefiniert.jsp").forward(request, response);
                return;
            } else if (request.getParameter("button_vorschau") != null) //Dynamisch Button Vorschau erstellen
            {
                generiereVorschau(request, response);
                return;
            } else if (request.getParameter("hidden_action") != null) //Dynamisch bei fast allen aktionen
            {

                System.out.println("//////////MainServlet.doPost.hidden_action");
                System.out.println("" + request.getParameter("hidden_berechtigungs_info"));
                if (request.getParameter("hidden_action").equals("vorschau"))
                {
                    System.out.println("MainServlet.doPost: hidden_action: in vorschau");
                    generiereDynamischeVorschau(request, response, session);
                } else if (request.getParameter("hidden_action").equals("erstelle_vorlage") || request.getParameter("hidden_action").equals("erstelle_vorlage_2"))
                {
                    erstelleDynamischeVorlage(request, response, session);
                } else
                {
                    System.out.println("MainServlet.doPost: hidden_action: in plus-minus");

                }
                request.getRequestDispatcher("jsp/dynamisch_mitglieder.jsp").forward(request, response);
                return;
            } else if (request.getParameter("select_lade_vorlage") != null)
            {
                ladeDynamischeVorlage(request, response, session);
                request.getRequestDispatcher("jsp/dynamisch_mitglieder.jsp").forward(request, response);
                return;
            } else if (request.getParameter("select_loesche_vorlage") != null)
            {
                loescheDynamischeVorlage(request, response, session);
                request.getRequestDispatcher("jsp/dynamisch_mitglieder.jsp").forward(request, response);
                return;
            } else if (request.getParameter("logout") != null)
            {
                System.out.println("MainServlet.doPost: logout");

                session.invalidate();
                if (request.getSession(false) == null)
                {
                    System.out.println("session deleted");
                }
                
                request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
                return;
            }
        }

        request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
    }

    /**
     * Überprüft die eingegebenen Login Daten:
     * Falls die Daten korrekt sind werden die Berechtigungen 
     * geladen und zum Login.jsp weiter geletet bzw. falls der User nur
     * eine Berechtigung hat gleich zum vordfiniert.jsp
     * Falls die Daten inkorrekt sind wird eine Error Nachricht auf dem request
     * gespeichert und zurück zum Login.jsp geleitet
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        System.out.println("MainServlet.loginUser: button_Login");
        String strBenutzername = request.getParameter("input_benutzername");
        String strKennwort = request.getParameter("input_kennwort");
//        System.out.println("Benutzername: " + strBenutzername);
//        System.out.println("Kennwort: " + strKennwort);

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
                System.out.println("Bla1");
                LinkedList<Berechtigung> liBerechtigung = access.getBerechtigungen(intIDUser);
                System.out.println("Bla2");
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

    /**
     * Holt sich die benötigten Informationen zur ausgewählten Berechtigung
     * @param request
     * @param response
     * @param session
     * @throws Exception 
     */
    private void getBerechtigungsinformationen(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
    {
        int intIDUser = (int) session.getAttribute("intUserID");
        LinkedList<Berechtigung> liBerechtigungen = access.getBerechtigungen(intIDUser);
        String strBerechtigung = request.getParameter("select_berechtigung");
        Berechtigung aktBerechtigung = null;
        for (Berechtigung berechtigung : liBerechtigungen)
        {
            if (strBerechtigung.equals(berechtigung.getStrBerechtigung()))
            {
                aktBerechtigung = berechtigung;
            }
        }
        generiereBerechtigungVorschau(request, response, session, aktBerechtigung);
    }

    /**
     * Setz je nach ausgewählter Sicht die Berechtigungen
     * @param request
     * @param response
     * @param session
     * @param aktBerechtigung
     * @throws Exception 
     */
    private void generiereBerechtigungVorschau(HttpServletRequest request, HttpServletResponse response, HttpSession session, Berechtigung aktBerechtigung) throws Exception
    {
        System.out.println("//////////////////generiereBerechtigungVorschau");
        if (aktBerechtigung.getIntIDGruppe() == 1)
        {
            System.out.println("//////////////////generiereBerechtigungVorschau in 1");
            System.out.println("MainServlet.generiereBerechtigungVorschau: id=1");
            session.setAttribute("alleBezirke", access.getAllBezirke());
            session.setAttribute("bezirk", null);
            session.setAttribute("bezirkName", null);
            session.setAttribute("abschnitt", null);
            session.setAttribute("abschnittName", null);
            session.setAttribute("feuerwehr", null);
        } else if (aktBerechtigung.getIntIDGruppe() == 5)
        {
            System.out.println("MainServlet.generiereBerechtigungVorschau: id=5");
            session.setAttribute("bezirk", access.getBezirk(aktBerechtigung.getIntBereich()));
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
        System.out.println("//////////////////generiereBerechtigungVorschau forward");
        request.getRequestDispatcher("jsp/vordefiniert.jsp").forward(request, response);
    }

    /**
     * Lädt je nach im request vorhandenen Bericht die benötigten
     * Daten und speichert sie auf dem request
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
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
            if (strBericht.equals("Einfache Mitgliederliste"))//Einfache Mitgliederliste
            {
                request.setAttribute("liste", access.getEinfacheMitgliederliste(intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals("Erreichbarkeitsliste"))//Erreichbarkeitsliste
            {
                request.setAttribute("liste", access.getErreichbarkeitsliste(intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals("Adressliste"))//Adressliste
            {

                request.setAttribute("liste", access.getAdressliste(intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals("Geburtstagsliste"))//Geburtstagsliste
            {
                int intJahr = Integer.parseInt(request.getParameter("select_jahr"));
                request.setAttribute("liste", access.getGeburtstagsliste(intJahr, intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals("Dienstzeitliste"))//Dienstzeitliste
            {
                int intJahr = Integer.parseInt(request.getParameter("select_jahr"));
                request.setAttribute("liste", access.getDienstzeitListe(intJahr, intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals("Stundenauswertung je Mitglied je Instanz"))//Stundenauswertung je Mitglied je Instanz
            {
                //request.setAttribute("liste", );
                String strVonDatum = request.getParameter("input_von_datum");
                String strBisDatum = request.getParameter("input_bis_datum");
                int intPersID = Integer.parseInt(request.getParameter("select_mitglied").split("###")[0]);
                request.setAttribute("liste", access.getStundenauswertungProMitgliedProInstanz(strVonDatum, strBisDatum, intBereichNr, intAbschnittNr, strFeuerwehr, intPersID));

            } else if (strBericht.equals("Tätigkeitsbericht leer"))//Tätigkeitsbericht leer
            {
                request.setAttribute("liste", access.getLeerberichtMitglied(intBereichNr, intAbschnittNr, strFeuerwehr));
                request.setAttribute("zusatz_liste", access.getLeerberichtFahrzeug(intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals("Einsatzbericht leer"))//Einsatzbericht leer
            {
                request.setAttribute("liste", access.getLeerberichtMitglied(intBereichNr, intAbschnittNr, strFeuerwehr));
                request.setAttribute("zusatz_liste", access.getLeerberichtFahrzeug(intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals("Übungsbericht leer"))//Übungsbericht leer
            {
                request.setAttribute("liste", access.getLeerberichtMitglied(intBereichNr, intAbschnittNr, strFeuerwehr));
                request.setAttribute("zusatz_liste", access.getLeerberichtFahrzeug(intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals("Liste aller Einsatzberichte"))//Liste aller Einsatzberichte
            {
                String strVonDatum = request.getParameter("input_von_datum");
                String strBisDatum = request.getParameter("input_bis_datum");
                System.out.println("MainServler.generiereVorschau: " + strVonDatum + " bis " + strBisDatum);
                //!!Bitte das gewählte Datum als String übergeben (strVon, strBis)
                request.setAttribute("liste", access.getEinsatzbericht(strVonDatum, strBisDatum, intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals("Liste aller Tätigkeitsberichte"))//Liste aller Tätigkeitsberichte
            {
                String strVonDatum = request.getParameter("input_von_datum");
                String strBisDatum = request.getParameter("input_bis_datum");
                //!!Bitte das gewählte Datum als String übergeben (strVon, strBis)
                request.setAttribute("liste", access.getTaetigkeitsbericht(strVonDatum, strBisDatum, intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals("Liste aller Übungsberichte"))//Liste aller Übungsberichte
            {
                String strVonDatum = request.getParameter("input_von_datum");
                String strBisDatum = request.getParameter("input_bis_datum");
                //!!Bitte das gewählte Datum als String übergeben (strVon, strBis)
                request.setAttribute("liste", access.getUebungsbericht(strVonDatum, strBisDatum, intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals("Liste aller Berichte"))//Liste aller Berichte
            {
                String strVonDatum = request.getParameter("input_von_datum");
                String strBisDatum = request.getParameter("input_bis_datum");
                //!!Bitte das gewählte Datum als String übergeben (strVon, strBis)
                request.setAttribute("liste", access.getAlleBerichte(strVonDatum, strBisDatum, intBereichNr, intAbschnittNr, strFeuerwehr));
            } else if (strBericht.equals("Kursstatistik"))//Kursstatistik
            {
                String strVonDatum = request.getParameter("input_von_datum");
                String strBisDatum = request.getParameter("input_bis_datum");
                request.setAttribute("liste", access.getKursstatistiktaetigkeit(intBereichNr, intAbschnittNr, strFeuerwehr, strVonDatum, strBisDatum));
                request.setAttribute("zusatz_informationen", generiereKurstatistikZusatzTable(intBereichNr, intAbschnittNr, strFeuerwehr, strVonDatum, strBisDatum));
            } else if (strBericht.equals("Digitales Fahrtenbuch"))//Digitales Fahrtenbuch
            {
                String strVonDatum = request.getParameter("input_von_datum");
                String strBisDatum = request.getParameter("input_bis_datum");

                String strKennzeichen1 = request.getParameter("input_kennzeichen");
//                System.out.println("Kennzeichen: " + strKennzeichen1);
//                String strKennzeichen = "HB-2-FXE";

                //Login für Farhetnbuch implementiert, also können die Übergabeparameter da dazu gemacht werden lg nauschi
                //System.out.println(access.getFahrtenbuch("", "", strKennzeichen).toString());
                LinkedList<Fahrzeug> liFahrzeuge = access.getFahrtenbuch(intBereichNr, intAbschnittNr, strFeuerwehr, strVonDatum, strBisDatum, strKennzeichen1);
                String strDetails = access.getDetailsFuerFahrtenbuchFahrzeug(liFahrzeuge);
                request.setAttribute("zusatz_informationen", strDetails.isEmpty() ? null : strDetails);
                request.setAttribute("liste", liFahrzeuge);
            } else if (strBericht.equals("Einsatztaugliche Atemschutzgeräteträger"))
            {

                request.setAttribute("liste", access.getGereatetraegerMitglied(intBereichNr, intAbschnittNr, strFeuerwehr));

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

    /**
     * Erstellt den HTML String für die zweite Tabelle die bei der Kurstatistik
     * benötigt wird
     * @param intBereichnr
     * @param intAbschnittnr
     * @param strFubwehr
     * @param strVonDatum
     * @param strBisDatum
     * @return
     * @throws Exception 
     */
    private String generiereKurstatistikZusatzTable(int intBereichnr, int intAbschnittnr, String strFubwehr, String strVonDatum, String strBisDatum) throws Exception
    {
        System.out.println("/////////////generiereKurstatistikZusatzTable//////////////");
        LinkedList<Kurs> liKurse = access.getKursstatistikkurse(intBereichnr, intAbschnittnr, strFubwehr, strVonDatum, strBisDatum);
        System.out.println(liKurse.size());
        String strZusatzInfo = "<table class='tablesorter ui celled table'><thead>";
        strZusatzInfo += "<tr>"
                + "<th data-content='nach Kursbezeichnung sortieren' class='sort'>Kursbezeichnung</th>"
                + "<th data-content='nach Anzahl Teilnehmer sortieren' class='sort'>Anz Teiln</th>"
                + "</tr>";
        strZusatzInfo += "</thead><tbody>";
        for (Kurs kurs : liKurse)
        {
            strZusatzInfo += kurs.toString();
        }
        strZusatzInfo += "</tbody></table>";
        return strZusatzInfo;
    }

    /**
     * Erstellt ein Objekt, dass den HTML String für den dynamischen Bericht
     * enthält
     * @param request
     * @param response
     * @param session 
     */
    private void generiereDynamischeVorschau(HttpServletRequest request, HttpServletResponse response, HttpSession session)
    {
        System.out.println("//////////////generiereDynamischeVorschau////////////");
        String strTypen = request.getParameter("hidden_typen_daten");
        System.out.println("Typen: " + strTypen);
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
            String[] strBerechtigung = request.getParameter("hidden_berechtigungs_info").split(";");
            int intBezirk = Integer.parseInt(strBerechtigung[0]);
            int intAbschnitt = Integer.parseInt(strBerechtigung[1]);
            String strFeuerwehr = strBerechtigung[2];
            //Login für dynamischen Bericht implementiert. Bereich, Abnschnitt, & Feuerwehr sind neue Übergabeparameter.
            //Müss ma aber no genau bereden wie ma des machen. Mit Dropdowns oder ohne Dropdowns? Karli hat nix erwähnt
            //47030 --> Sinnersdorf
            //4704 --> 4. Abschnitt im Bereich 47
            //47 --> Bereich 47
            //Test
            //-2 --> alles
            StringBuilder sbDynHTML = access.getDynamischerBericht(strDaten, strTypen, intBezirk, intAbschnitt, strFeuerwehr);
            System.out.println("MainServlet.erstelleDynamischenBericht: sbDynHTML: " + sbDynHTML);
            request.setAttribute("dyn_table", sbDynHTML);
        } catch (Exception ex)
        {
            Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Falls der Name der Vorlage noch nicht vorhanden ist
     * erstellt diese Mehtode eine neue Vorlage
     * Falls der Name schon vorhanden ist sendet er diese Information
     * an das dynamisch.jsp in dem die Informationen verwertet werden
     * @param request
     * @param response
     * @param session 
     */
    private void erstelleDynamischeVorlage(HttpServletRequest request, HttpServletResponse response, HttpSession session)
    {
        System.out.println("/////////////MainServlet.erstelleDynamischeVorlage///////////////");
        String strVorlageName = request.getParameter("hidden_vorlage_name");
        String strTypen = request.getParameter("hidden_typen_daten");
        int intUserID = (int) session.getAttribute("intUserID");
        ServletContext sc = this.getServletContext();
        HashMap<String, LinkedList<String>> hsVorlagen = new HashMap<>();
        if (request.getParameter("hidden_action").equals("erstelle_vorlage"))
        {
            if (sc.getAttribute("userid_" + intUserID + "_vorlagen") != null)
            {
                hsVorlagen = (HashMap<String, LinkedList<String>>) sc.getAttribute("userid_" + intUserID + "_vorlagen");
                if (hsVorlagen.get(strVorlageName) != null && request.getParameter("hidden_action").equals("erstelle_vorlage"))
                {
                    request.setAttribute("dynamisch_vorlage_vorhanden", true);
                    System.out.println("///Shit///");
                    return;
                } else if (hsVorlagen.get(strVorlageName) != null && request.getParameter("hidden_action").equals("erstelle_vorlage_2"))
                {
                    hsVorlagen.remove(strVorlageName);
                }
            }
        }
        LinkedList<String> liDaten = new LinkedList<>();
        liDaten.add(strTypen);
        int intZaehler = Integer.parseInt(request.getParameter("hidden_zaehler"));
        for (int i = 1; i <= intZaehler; i++)
        {
            String strZeile = request.getParameter("hidden_element_data_" + i);
            liDaten.add(strZeile);
        }
        hsVorlagen.put(strVorlageName, liDaten);
        sc.setAttribute("userid_" + intUserID + "_vorlagen", hsVorlagen);

        System.out.println("HS: " + hsVorlagen.size());
    }

    /**
     * Ladet eine dynamische Vorlage und speichert die notwendigen Daten auf dem request
     * @param request
     * @param response
     * @param session 
     */
    private void ladeDynamischeVorlage(HttpServletRequest request, HttpServletResponse response, HttpSession session)
    {
        System.out.println("/////////////MainServlet.ladeDynamischeVorlage///////////////");
        ServletContext sc = this.getServletContext();
        String strVorlageName = request.getParameter("select_lade_vorlage");
        int intUserID = (int) session.getAttribute("intUserID");
        HashMap<String, LinkedList<String>> hsVorlagen = (HashMap<String, LinkedList<String>>) sc.getAttribute("userid_" + intUserID + "_vorlagen");
        LinkedList<String> liDaten = hsVorlagen.get(strVorlageName);
        request.setAttribute("hidden_zaehler", liDaten.size() - 1);
        request.setAttribute("hidden_typen_daten", liDaten.get(0));
        for (int i = 1; i < liDaten.size(); i++)
        {
            String strZeile = liDaten.get(i);
            request.setAttribute("hidden_element_data_" + i, strZeile);
        }
        System.out.println("Li.size: " + liDaten.size());
    }

    /**
     * Löscht eine dynamische Vorlage von dem ServletContext
     * @param request
     * @param response
     * @param session 
     */
    private void loescheDynamischeVorlage(HttpServletRequest request, HttpServletResponse response, HttpSession session)
    {
        System.out.println("/////////////MainServlet.loescheDynamischeVorlage///////////////");
        ServletContext sc = this.getServletContext();
        String strVorlageName = request.getParameter("select_loesche_vorlage");
        int intUserID = (int) session.getAttribute("intUserID");
        HashMap<String, LinkedList<String>> hsVorlagen = (HashMap<String, LinkedList<String>>) sc.getAttribute("userid_" + intUserID + "_vorlagen");
        hsVorlagen.remove(strVorlageName);
        sc.setAttribute("userid_" + intUserID + "_vorlagen", hsVorlagen);
    }

    /**
     * Ladet alle Kenzeichen von der momentanten Sicht
     * @param request
     * @throws Exception 
     */
    private void ladeKennzeichen(HttpServletRequest request) throws Exception
    {
        System.out.println("////////////////Lade Kennzeichen/////////////");
        int intBereichNr = Integer.parseInt(request.getParameter("select_bezirk"));
        int intAbschnittNr = Integer.parseInt(request.getParameter("select_abschnitt"));
        String strFeuerwehr = request.getParameter("select_feuerwehr");
        LinkedList<String> liTest = access.getFahrtenbuchKennzeichen(intBereichNr, intAbschnittNr, strFeuerwehr);
        System.out.println(liTest.size() + "----------------------------");
        request.setAttribute("select_kennzeichen_liste", liTest);
    }

    /**
     * Ladet alle Mitglieder von der momentanten Sicht
     * @param request
     * @throws Exception 
     */
    private void ladeMitgliederFuerStundenauswertung(HttpServletRequest request) throws Exception
    {
        System.out.println("////////////////Lade Kennzeichen/////////////");
        int intBereichNr = Integer.parseInt(request.getParameter("select_bezirk"));
        int intAbschnittNr = Integer.parseInt(request.getParameter("select_abschnitt"));
        String strFeuerwehr = request.getParameter("select_feuerwehr");
        request.setAttribute("select_mitglieder_hs", access.getMitgliederFuerStundenauswertung(intBereichNr, intAbschnittNr, strFeuerwehr));
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

    /**
     * Wird beim erstmaligen Starten des Servlets aufgerufen
     * @param config
     * @throws ServletException 
     */
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
            leseTypenAusgabeDynamisch(strPath + File.separator + "TypenDynamischOutput.csv");
            leseOperatorenDynamisch(strPath + File.separator + "TypenDynamischOperatoren.csv");
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

    /**
     * Liest benötigte Daten für das dynamisch.jsp aus einem .csv File
     * @param strPfad
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
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
    
    /**
     * Liest benötigte Daten für das dynamisch.jsp aus einem .csv File
     * @param strPfad
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
    public void leseTypenAusgabeDynamisch(String strPfad) throws FileNotFoundException, UnsupportedEncodingException, IOException
    {
        File file = new File(strPfad);
        LinkedList<String> liTypenAusgabe = new LinkedList<>();

        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, "ISO-8859-1");
        BufferedReader br = new BufferedReader(isr);
        String strReihe;

        while ((strReihe = br.readLine()) != null)
        {
            liTypenAusgabe.add(strReihe);
        }
        br.close();

        this.getServletContext().setAttribute("TypenAusgabe", liTypenAusgabe);
    }
    
    /**
     * Liest benötigte Daten für das dynamisch.jsp aus einem .csv File
     * @param strPfad
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
    public void leseOperatorenDynamisch(String strPfad) throws FileNotFoundException, UnsupportedEncodingException, IOException
    {
        File file = new File(strPfad);
        LinkedList<String> liOperatoren = new LinkedList<>();

        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, "ISO-8859-1");
        BufferedReader br = new BufferedReader(isr);
        String strReihe;

        while ((strReihe = br.readLine()) != null)
        {
            liOperatoren.add(strReihe);
        }
        br.close();

        this.getServletContext().setAttribute("liOperatoren", liOperatoren);
    }

}
