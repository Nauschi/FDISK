/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import Beans.MitgliedsStundenPDF;
import Database.DB_Access;
import Enum.EnLeerberichte;
import PDF.PDF_KopfFußzeile;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.exceptions.CssResolverException;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.parser.XMLParserListener;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Marcel Schmidt
 */
@WebServlet(name = "PDFServlet", urlPatterns
        = {
            "/PDFServlet"
        })
public class PDFServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String strUebungsbericht = EnLeerberichte.getUebungsberichtLeer.toString();
    private String strTaetigkeitsbericht = EnLeerberichte.getTaetigkeitsbericht.toString();
    private String strEinsatzbericht = EnLeerberichte.getEinsatzbericht.toString();

    private LinkedList<String> liBerHochformat;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet PDFServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PDFServlet at " + request.getContextPath() + "</h1>");
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
            throws ServletException, IOException {
        request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
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
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String strData = request.getParameter("hidden_pdfData");
        //System.out.println("strData_new --> " + strData);
        String[] strSplitData = strData.split("###");
        String strBerichtname;
        String strTable;

        strBerichtname = strSplitData[0];
        strTable = strSplitData[1];

        String strAusgabe = "Es ist ein unerwartetes Problem aufgetreten";
        boolean boolLeerbericht = true;
        HttpSession userSession = request.getSession();
        switch (strBerichtname) {
            case "Einsatzbericht leer":
                strAusgabe = generiereAusgabeEinsatzberichtLeer(strTable, strSplitData[2]);
                break;
            case "Übungsbericht leer":
                strAusgabe = generiereAusgabeUebungsberichtLeer(strTable, strSplitData[2]);
                break;
            case "Tätigkeitsbericht leer":
                strAusgabe = generiereAusgabeTaetigkeitsberichtLeer(strTable, strSplitData[2]);
                break;
            case "Dynamisch":
                strAusgabe = "<h1>" + strBerichtname + "</h1>" + strTable;
                boolLeerbericht = false;
                break;
            case "Kursstatistik":
                if (strTable.split("<td>").length > 1 && strSplitData[2].split("<td>").length > 1) {
                    strAusgabe = "<h1>" + strBerichtname + "</h1>" + strSplitData[2].replace("</br>", "") + "<p>&nbsp;</p>" + strTable;
                } else if (strTable.split("<td>").length < 2) {
                    strAusgabe = "<h1>" + strBerichtname + "</h1>" + strSplitData[2].replace("</br>", "");
                } else if (strSplitData[2].split("<td>").length < 2) {
                    strAusgabe = "<h1>" + strBerichtname + "</h1>" + strTable;
                }
                boolLeerbericht = false;
                break;
            case "Digitales Fahrtenbuch":
                if (strSplitData.length > 2) {
                    strAusgabe = "<h1>" + strBerichtname + "</h1>" + strSplitData[2] + "<p>&nbsp;</p>" + strTable;
                } else {
                    strAusgabe = "<h1>" + strBerichtname + "</h1>" + strTable;
                }
                boolLeerbericht = false;
                break;
            case "Stundenauswertung je Mitglied je Instanz":
                strAusgabe = "<h1>" + strBerichtname + "</h1>" + generiereStundenauswertungJeMitgliedJeInstanz(strTable);
                boolLeerbericht = false;
                break;
            case "Geburtstagsliste":
                strAusgabe = "<h1>" + strBerichtname + " " + userSession.getAttribute("attJahr") + "</h1>" + strTable;
                boolLeerbericht = false;
                break;
            case "Dienstzeitliste":
                strAusgabe = "<h1>" + strBerichtname + " " + userSession.getAttribute("attJahrDienst") + "</h1>" + strTable;
                boolLeerbericht = false;
                break;
            default:
                strAusgabe = "<h1>" + strBerichtname + "</h1>" + strTable;
                boolLeerbericht = false;
        }
        String strContextPath = this.getServletContext().getRealPath("/");
        String strCSSPath1 = strContextPath + File.separator + "css" + File.separator + "pdfSimpel.css";
        String strFontPath = strContextPath + File.separator + "res" + File.separator + "Cambria.ttf";

        //FAIL - just ignore
        //strAusgabe = strAusgabe.substring(0, pos+5) + ("<thead>" + strAusgabe.split("<thead>")[1].split("</thead>")[0] + "</thead>") + strAusgabe.substring(pos+5);
        String strHead = "<thead>" + strAusgabe.split("<thead>")[1].split("</thead>")[0] + "</thead>";
        String strUeberschrift = "<h1>" + strAusgabe.split("<h1>")[1].split("</h1")[0] + "</h1>";
        strAusgabe = strAusgabe.replace("<table", "<table style='repeat-header: yes;' ");
        
        Rectangle rect;
        try {
            Document document;
            if (liBerHochformat.contains(strBerichtname)) {
                document = new Document(PageSize.A4, 36, 36, 100, 54);
                rect = new Rectangle(36, 54, 559, 788);
            } else {
                document = new Document(PageSize.A4.rotate(), 36, 36, 70, 54);
                rect = new Rectangle(36, 54, 805, 559);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            PDF_KopfFußzeile event = new PDF_KopfFußzeile(strFontPath, writer);
            writer.setBoxSize("pageRect", rect);
            writer.setPageEvent(event);

            document.open();
            HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
            CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(false);
            //hier werden die CSS files eingebunden
            cssResolver.addCssFile(strCSSPath1, true);

            if (liBerHochformat.contains(strBerichtname)) {
                cssResolver.addCssFile(strCSSPath1.replace("Simpel", "Hoch"), true);
            } else {
                cssResolver.addCssFile(strCSSPath1.replace("Simpel", "Quer"), true);
            }
            if (!boolLeerbericht) {
                cssResolver.addCssFile(strCSSPath1.replace("Simpel", "StandartBericht"), true);
            } else {
                cssResolver.addCssFile(strCSSPath1.replace("Simpel", "Leerbericht"), true);
            }
            Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(htmlContext, new PdfWriterPipeline(document, writer)));

            XMLWorker worker = new XMLWorker(pipeline, true);
            XMLParser p = new XMLParser(worker);
            XMLParser p2 = new XMLParser(worker);
            p.toString();
            //ByteArrayInputStream bis = new ByteArrayInputStream(strAusgabe.toString().getBytes());
            //ByteArrayInputStream cis = new ByteArrayInputStream(strCSSPath1.replace("Simple", "Hoch").toString().getBytes());
            //XMLWorkerHelper.getInstance().parseXHtml(writer, document, bis, cis);
            //p.selectState().selfClosing();
            
           
            p.parse(new StringReader(strAusgabe));
            
            
            document.close();
            writer.close();
            strBerichtname = strBerichtname.replaceAll(" ", "_");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "filename=" + strBerichtname + ".pdf");
            response.setContentLength(baos.size());

            ServletOutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();
        } catch (DocumentException de) {
            throw new IOException(de.getMessage());
        } catch (CssResolverException ex) {
            Logger.getLogger(PDFServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Wird nur bei dem Bericht "Stundenauswertung Je Mitglied Je Instanz"
     * verwendet und wandelt den übegebenen String in das gewünschte Format um
     *
     * @param strTemp
     * @return
     */
    public String generiereStundenauswertungJeMitgliedJeInstanz(String strTemp) {
        strTemp = strTemp.replace("<table class=\"tablesorter ui celled table\">", "");
        strTemp = strTemp.replaceAll("style=\"display:none\"", "");
        strTemp = strTemp.replaceAll("style=\"display: none;\"", "");
        int intIndex1 = strTemp.indexOf("<thead>");
        int intIndex2 = strTemp.indexOf("</thead>") + 8;
        String strThead = strTemp.substring(intIndex1, intIndex2);
        intIndex1 = strTemp.indexOf("<tbody>") + 7;
        intIndex2 = strTemp.lastIndexOf("</tbody>");
        String strTRs = strTemp.substring(intIndex1, intIndex2);
        //System.out.println("strTRs --> " + strTemp);
        int intIndex = -1;
        //Struktur der TableRow
        //<tr>
        //<td></td>
        //<td></td>
        //<td></td>
        //<td></td>
        //<td></td>
        //<td></td>
        //<td><div></div></td>
        //</tr>
        LinkedList<MitgliedsStundenPDF> liPDFMit = new LinkedList<>();
        try {
            while (true) {
                intIndex = strTRs.indexOf("</tr>") + 5;
                String strOriginalAktRow = strTRs.substring(0, intIndex);
                //System.out.println("strOriginalAktRow --> " + strOriginalAktRow);
                MitgliedsStundenPDF msp = getMitgliedVonString(strOriginalAktRow);
                msp.setStrHead(strThead);
                boolean boolNeu = true;
                for (int i = 0; i < liPDFMit.size(); i++) {
                    //System.out.println(liPDFMit.get(i).toString());
                    if (liPDFMit.get(i).toString().equals(msp.toString())) {
                        boolNeu = false;
                        MitgliedsStundenPDF mspGleich = liPDFMit.get(i);
                        mspGleich.addInstanzname(msp.getLiInstanznamen().get(0));
                        mspGleich.addStundenSumme(msp.getLiStundenSumme().get(0));
                        mspGleich.setIntMinutenEb(mspGleich.getIntMinutenEb() + msp.getIntMinutenEb());
                        mspGleich.setIntMinutenTb(mspGleich.getIntMinutenTb() + msp.getIntMinutenTb());
                        mspGleich.setIntMinutenUb(mspGleich.getIntMinutenUb() + msp.getIntMinutenUb());
                        mspGleich.setIntAnzahlEb(mspGleich.getIntAnzahlEb() + msp.getIntAnzahlEb());
                        mspGleich.setIntAnzahlTb(mspGleich.getIntAnzahlTb() + msp.getIntAnzahlTb());
                        mspGleich.setIntAnzahlUb(mspGleich.getIntAnzahlUb() + msp.getIntAnzahlUb());
                        break;
                    }
                }

                if (boolNeu) {
                    liPDFMit.add(msp);
                    //System.out.println(liPDFMit.get(0).toString());
                }
                strTRs = strTRs.replace(strOriginalAktRow, "");
            }
        } catch (Exception ex) {
            //Muss leer abgefangen werden
        }

        String ausgabe = "";
        for (int i = 0; i < liPDFMit.size(); i++) {
            ausgabe += liPDFMit.get(i).toString();
        }
        return ausgabe;
    }

    /**
     * Zerlegt den übergebenen String und filtert alle wichtigen Daten für ein
     * neues MitgliederStundenPDF Objekt herraus. Das neue erstellt Objekt wird
     * zurückgegeben
     *
     * @param strOriginalAktRow
     * @return
     */
    public MitgliedsStundenPDF getMitgliedVonString(String strOriginalAktRow) {
        String strData = strOriginalAktRow.substring(strOriginalAktRow.indexOf("<div >"), strOriginalAktRow.indexOf("</div>") + 6);
        String strAktRow = strOriginalAktRow.replace(strData, "");
        strData = strData.replace("<div >", "");
        strData = strData.replace("</div>", "");
        strAktRow = strAktRow.replace("<tr>", "");
        strAktRow = strAktRow.replace("</tr>", "");
        strAktRow = strAktRow.replaceAll("\\<td[^>]*>", "");
        String[] splitAktRow = strAktRow.split("</td>");
        String[] splitData = strData.split(";");
        MitgliedsStundenPDF msp = new MitgliedsStundenPDF(splitAktRow[0], splitAktRow[1], splitAktRow[2], splitAktRow[3], splitAktRow[4],
                Integer.parseInt(splitData[0]), Integer.parseInt(splitData[1]), Integer.parseInt(splitData[2]), Integer.parseInt(splitData[3]), Integer.parseInt(splitData[4]), Integer.parseInt(splitData[5]));
        msp.addInstanzname(splitAktRow[5]);
        msp.addStundenSumme(splitAktRow[6]);
        //System.out.println("strAktRow --> " + strAktRow);
        //System.out.println("strData --> " + strData);
        return msp;
    }

    /**
     * Generiert einen Tätigkeitsbericht mit Hilfe des übergebenen Strings.
     * strTable ist der HTML String der die Zeilen des Tables beinhaltet
     * "<tr>...</tr><tr>...</tr><tr>...</tr>...."
     *
     * @param strTableMitglieder
     * @param strTableFahrzeuge
     * @return
     */
    public String generiereAusgabeTaetigkeitsberichtLeer(String strTableMitglieder, String strTableFahrzeuge) {
        String strHTMLOutput = strTaetigkeitsbericht;
        strHTMLOutput = strHTMLOutput.replace("##FahrzeugData##", strTableFahrzeuge);
        strHTMLOutput = strHTMLOutput.replace("##MitgliedData##", strTableMitglieder);
        return strHTMLOutput;
    }

    /**
     * Generiert einen Uebungsbericht mit Hilfe des übergebenen Strings.
     * strTable ist der HTML String der die Zeilen des Tables beinhaltet
     * "<tr>...</tr><tr>...</tr><tr>...</tr>...."
     *
     * @param strTableMitglieder
     * @param strTableFahrzeuge
     * @return
     */
    public String generiereAusgabeUebungsberichtLeer(String strTableMitglieder, String strTableFahrzeuge) {
        String strHTMLOutput = strUebungsbericht;
        strHTMLOutput = strHTMLOutput.replace("##FahrzeugData##", strTableFahrzeuge);
        strHTMLOutput = strHTMLOutput.replace("##MitgliedData##", strTableMitglieder);

        return strHTMLOutput;
    }

    /**
     * Generiert einen Einsatzbericht mit Hilfe des übergebenen Strings.
     * strTable ist der HTML String der die Zeilen des Tables beinhaltet
     * "<tr>...</tr><tr>...</tr><tr>...</tr>...."
     *
     * @param strTableMitglieder
     * @param strTableFahrzeuge
     * @return
     */
    public String generiereAusgabeEinsatzberichtLeer(String strTableMitglieder, String strTableFahrzeuge) {
        String strHTMLOutput = strEinsatzbericht;

        String[] fahrzeuge = strTableFahrzeuge.replaceAll("<tr>", "").replaceAll("</tr>", "").split("</td>");

        String fahrzeugeFuerBericht = "";

        for (String string : fahrzeuge) {
            fahrzeugeFuerBericht += "<tr>" + string + "</td><td>Von:</td><td>Bis:</td><td>&Omicron;&nbsp;Verr.</td><td>km:</td><td>Pump.:</td></tr>";
        }

        strHTMLOutput = strHTMLOutput.replace("##FahrzeugeFuerBericht##", fahrzeugeFuerBericht);
        strHTMLOutput = strHTMLOutput.replace("##FahrzeugData##", strTableFahrzeuge);
        strHTMLOutput = strHTMLOutput.replace("##MitgliedData##", strTableMitglieder);

        return strHTMLOutput;
    }

    /**
     * Wird beim Starten des Servlets aufgerufen und initialisiert eine Liste
     * mit den Namen der Berichte die im Hochformat sind
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config); //To change body of generated methods, choose Tools | Templates.
        liBerHochformat = new LinkedList<>();
        liBerHochformat.add("Tätigkeitsbericht leer");
        liBerHochformat.add("Übungsbericht leer");
        liBerHochformat.add("Einsatzbericht leer");
        liBerHochformat.add("Einfache Mitgliederliste");
        //liBerHochformat.add("Dienstzeitliste");
        liBerHochformat.add("Geburtstagsliste");
        liBerHochformat.add("Kursstatistik");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
