/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.ssdfsdfds
 */
package Servlet;

import Beans.Rohbericht;
import Database.DB_Access;
import PDF.PDFCreator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
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
@WebServlet(name = "MainServlet", urlPatterns =
{
    "/MainServlet"
})
public class MainServlet extends HttpServlet
{

    private DB_Access access;
    private SimpleDateFormat sdf;
    private PDFCreator pdf;

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
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("lastPage") == null)
        {
            System.out.println("MainServlet.doPost: session = null");
            request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
            return;
        }
        String strLastPage = (String) session.getAttribute("lastPage");
        request.getRequestDispatcher("jsp/" + strLastPage + ".jsp").forward(request, response);
        processRequest(request, response);
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
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedIn") == null)
        {
            System.out.println("MainServlet.doPost: session = null");
            request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
        } else
        {
            if (request.getParameter("select_berechtigung") != null)
            {
                int intIDGruppe =  Integer.parseInt(request.getParameter("select_berechtigung"));
                System.out.println("MainServlet.doPost: bestaetigen");
                request.getRequestDispatcher("jsp/vordefiniert.jsp").forward(request, response);
            } else if (request.getParameter("dynamisch") != null)
            {
                System.out.println("MainServlet.doPost: dynamisch");
                request.getRequestDispatcher("jsp/dynamisch_mitglieder.jsp").forward(request, response);
            } else if (request.getParameter("vordefiniert") != null)
            {
                System.out.println("MainServlet.doPost: vordefiniert");
                request.getRequestDispatcher("jsp/vordefiniert.jsp").forward(request, response);
            } else if (request.getParameter("button_vorschau") != null)
            {
                generiereVorschau(request, response);
            } else if (request.getParameter("hidden_zaehler") != null)
            {
                System.out.println("MainServlet.doPost: hidden_zaeler");
                request.getRequestDispatcher("jsp/dynamisch_mitglieder.jsp").forward(request, response);
            } else if (request.getParameter("logout") != null)
            {
                System.out.println("MainServlet.doPost: logout");
                request.getSession(false).invalidate();
                if (request.getSession(false) == null)
                {
                    System.out.println("session deleted");
                }
                request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
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
        }
        if (intIDUser != -1)
        //if (true)
        {
            HttpSession session = request.getSession(true);
            try
            {
                session.setAttribute("loggedIn", true);
                request.setAttribute("berechtigungen", access.getBerechtigungen(intIDUser));
                System.out.println("Berechtigungen gesetzt");
            } catch (Exception ex)
            {
                Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
        } else
        {
            request.setAttribute("login_error", true);
            request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
        }
    }

    private void generiereVorschau(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        System.out.println("MainServlet.generiereVorschau: button_vorschau");
        try
        {

            LinkedList<Rohbericht> liRohberichte = (LinkedList<Rohbericht>) this.getServletContext().getAttribute("rohberichte");
            String strBericht = request.getParameter("input_aktbericht");
//            HttpSession session = request.getSession(false);

            //int intIDGruppe = Integer.parseInt(request.getParameter("select_berechtigung"));
//            try
//            {
//                Date dateVon = sdf.parse(request.getParameter("input_von_datum"));
//                Date dateBis = sdf.parse(request.getParameter("input_bis_datum"));
//
//                //System.out.println("GruppeID: " + intIDGruppe);
//                System.out.println("Date von: " + sdf.format(dateVon));
//                System.out.println("Date bis: " + sdf.format(dateBis));
//            } catch (Exception ex)
//            {
//                System.out.println(ex.toString());
//            }
            if (strBericht.equals(liRohberichte.get(0).getStrBerichtname()))
            {
//                request.setAttribute("liste", access.getEinfacheMitgliederliste());
            } else if (strBericht.equals(liRohberichte.get(1).getStrBerichtname()))
            {
                request.setAttribute("liste", access.getErreichbarkeitsliste());
            } else if (strBericht.equals(liRohberichte.get(2).getStrBerichtname()))
            {
                request.setAttribute("liste", access.getAdressListe());
            } else if (strBericht.equals(liRohberichte.get(3).getStrBerichtname()))
            {
               //NEUE ÜBERGABEPARAMETER request.setAttribute("liste", access.getGeburtstagsliste(2014)); //welche Zahl??
            } else if (strBericht.equals(liRohberichte.get(4).getStrBerichtname()))
            {
                System.out.println("MainServlet.generiereVorschau: In Tätigkeitsbericht");
                request.setAttribute("liste", access.getDienstzeitListe());
            } else if (strBericht.equals(liRohberichte.get(6).getStrBerichtname()))
            {
                request.setAttribute("liste", access.getLeerberichtMitglied());
            } else if (strBericht.equals(liRohberichte.get(8).getStrBerichtname()))
            {
                request.setAttribute("liste", access.getLeerberichtMitglied());
            }else if (strBericht.equals(liRohberichte.get(9).getStrBerichtname()))
            {
                request.setAttribute("liste", access.getEinsatzbericht());
            }else if (strBericht.equals(liRohberichte.get(10).getStrBerichtname()))
            {
                request.setAttribute("liste", access.getTaetigkeitsbericht());
            }else if (strBericht.equals(liRohberichte.get(11).getStrBerichtname()))
            {
                request.setAttribute("liste", access.getUebungsbericht());
            } 
            else if (strBericht.equals(liRohberichte.get(13).getStrBerichtname()))
            {
                request.setAttribute("liste", access.getKursstatistik());
            } else if (strBericht.equals(liRohberichte.get(14).getStrBerichtname()))
            {
                request.setAttribute("liste", access.getFahrtenbuch());
            }
        } catch (Exception ex)
        {
            Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.getRequestDispatcher("jsp/vordefiniert.jsp").forward(request, response);
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
        sdf = new SimpleDateFormat("dd.MM.yyyy");
        pdf = new PDFCreator();
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
            intTypeOfDateUI = Integer.parseInt(strElemente[strElemente.length-1]);
            for (int i = 1; i < strElemente.length-1; i++)
            {
                liBerichtSpalten.add(strElemente[i]);
            }
            
            liRohberichte.add(new Rohbericht(strBerichtname, liBerichtSpalten,intTypeOfDateUI));
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
