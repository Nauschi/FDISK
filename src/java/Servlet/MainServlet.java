/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.ssdfsdfds
 */
package Servlet;

import Beans.Mitglied;
import Beans.MitgliedsAdresse;
import Beans.MitgliedsDienstzeit;
import Beans.MitgliedsErreichbarkeit;
import Beans.MitgliedsGeburtstag;
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
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

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

        request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
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

        if (request.getParameter("button_login") != null)
        {
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
                    session.setAttribute("berechtigungen", access.getBerechtigungen(intIDUser));
                } catch (Exception ex)
                {
                    Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
                }

                request.getRequestDispatcher("jsp/vordefiniert.jsp").forward(request, response);
            } else
            {
                request.setAttribute("login_error", true);
                request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
            }
        } else if (request.getParameter("dynamisch") != null)
        {
            request.getRequestDispatcher("jsp/dynamisch_mitglieder.jsp").forward(request, response);
        } else if (request.getParameter("vordefiniert") != null)
        {
            request.getRequestDispatcher("jsp/vordefiniert.jsp").forward(request, response);
        } else if (request.getParameter("button_vorschau") != null)
        {
            try
            {
                LinkedList<Rohbericht> liRohberichte = (LinkedList<Rohbericht>) this.getServletContext().getAttribute("rohberichte");
                String strBericht = request.getParameter("input_aktbericht");
                int intIDGruppe = Integer.parseInt(request.getParameter("select_berechtigung"));
                try
                {
                    Date dateVon = sdf.parse(request.getParameter("input_von_datum"));
                    Date dateBis = sdf.parse(request.getParameter("input_bis_datum"));

                    System.out.println("GruppeID: " + intIDGruppe);
                    System.out.println("Date von: " + sdf.format(dateVon));
                    System.out.println("Date bis: " + sdf.format(dateBis));
                } catch (Exception ex)
                {
                    System.out.println(ex.toString());
                }
                if (strBericht.equals(liRohberichte.get(0).getStrBerichtname()))
                {

                    //LinkedList<Mitglied> liMitglieder = access.getEinfacheMitgliederliste();
                    //request.setAttribute("liste", liMitglieder);
                } else if (strBericht.equals(liRohberichte.get(1).getStrBerichtname()))
                {
                    LinkedList<MitgliedsErreichbarkeit> liErreichtbarkeiten = access.getErreichbarkeitsliste();
                    request.setAttribute("liste", liErreichtbarkeiten);
                } else if (strBericht.equals(liRohberichte.get(2).getStrBerichtname()))
                {
                    LinkedList<MitgliedsAdresse> liAdressen = access.getAdressListe();
                    request.setAttribute("liste", liAdressen);
                } else if (strBericht.equals(liRohberichte.get(3).getStrBerichtname()))
                {
                    LinkedList<MitgliedsGeburtstag> liGeburtstage = access.getGeburtstagsliste(2014);//welche Zahl??
                    request.setAttribute("liste", liGeburtstage);
                } else if (strBericht.equals(liRohberichte.get(4).getStrBerichtname()))
                {
                    LinkedList<MitgliedsDienstzeit> liDienstzeiten = access.getDienstzeitListe();
                    request.setAttribute("liste", liDienstzeiten);
                }
            } catch (Exception ex)
            {
                Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            request.getRequestDispatcher("jsp/vordefiniert.jsp").forward(request, response);
        } else if (request.getParameter("button_bestaetigen") != null)
        {
            System.out.println("In bestätigen");
//            JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir")); ????????????????
//            fileChooser.showSaveDialog(); 
//            File file = fileChooser.getSelectedFile();
//            if(file!=null)
//            {
//                //save
//                System.out.println("Test");
//            }
        } else if (request.getParameter("hidden_zaehler") != null)
        {
            request.getRequestDispatcher("jsp/dynamisch_mitglieder.jsp").forward(request, response);
        }

        readXML(request);

        processRequest(request, response);
    }
    
    
    public void readXML(HttpServletRequest request)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiii");
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(request.getInputStream());
            System.out.println(((Element)doc.getDocumentElement()).getNodeName());

        } catch (Exception ex)
        {
            System.out.println("bestätigen: "+ex.toString());
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
        sdf = new SimpleDateFormat("dd.MM.yyyy");
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
            String[] strElemente = strReihe.split(";");
            strBerichtname = strElemente[0];
            for (int i = 1; i < strElemente.length; i++)
            {
                liBerichtSpalten.add(strElemente[i]);
            }
            liRohberichte.add(new Rohbericht(strBerichtname, liBerichtSpalten));
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
