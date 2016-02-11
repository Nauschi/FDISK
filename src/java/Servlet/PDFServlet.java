/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import PDF.PDF_KopfFußzeile;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.exceptions.CssResolverException;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
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

/**
 *
 * @author user
 */
@WebServlet(name = "PDFServlet", urlPatterns =
{
    "/PDFServlet"
})
public class PDFServlet extends HttpServlet
{

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String strUebungsbericht = "<h1>Übungsbericht</h1>"
            + "<b><p><u>Allgemein</u></p></b>"
            + "<p>Verfasser:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _</p>"
            + "<p>Übungs-Art:&nbsp;_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _</p>"
            + "<p>Unterart:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _</p>"
            + "<p>Eigener Einsatzbereich: &nbsp;&Omicron;&nbsp;JA&nbsp;&nbsp;&nbsp;&Omicron;&nbsp;NEIN</p>"
            + "<p>Datum / Uhrzeit Beginn: __ __ . __ __ . __ __ __ __ - __ __ : __ __ Uhr</p>"
            + "<p>Datum / Uhrzeit Ende: __ __ . __ __ . __ __ __ __ - __ __ : __ __ Uhr</p>"
            + "<p>Übungsziel: _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ "
            + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ "
            + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ "
            + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ </p>"
            + "<p>Bemerkungen: _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ "
            + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ "
            + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ "
            + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ </p>"
            + "<b><p><u>Eingesetzte Fahrzeuge</u></p></b>"
            + "<table border='0'><tbody>##FahrzeugData##</tbody></table>"
            + "<b><p><u>Eingesetzte Mitglieder</u></p></b>"
            + "<table border='0'><tbody>##MitgliedData##</tbody></table>";

    private String strTaetigkeitsbericht = "<h1>Tätigkeitsbericht</h1>"
            + "<b><p><u>Allgemein</u></p></b>"
            + "<p>Verfasser:&nbsp;&nbsp;&nbsp;_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _</p>"
            + "<p>Tätigkeits-Art:&nbsp;&nbsp;_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _</p>"
            + "<p>Unterart:&nbsp;&nbsp;&nbsp;&nbsp;_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _</p>"
            + "<p>Eigener Einsatzbereich: &nbsp;&Omicron;&nbsp;JA&nbsp;&nbsp;&nbsp;&Omicron;&nbsp;NEIN</p>"
            + "<p>Datum / Uhrzeit Beginn: __ __ . __ __ . __ __ __ __ - __ __ : __ __ Uhr</p>"
            + "<p>Datum / Uhrzeit Ende: __ __ . __ __ . __ __ __ __ - __ __ : __ __ Uhr</p>"
            + "<p>Bemerkungen: _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ "
            + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ "
            + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ "
            + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ </p>"
            + "<b><p><u>Eingesetzte Fahrzeuge</u></p></b>"
            + "<table border='0'><tbody>##FahrzeugData##</tbody></table>"
            + "<b><p><u>Eingesetzte Mitglieder</u></p></b>"
            + "<table border='0'><tbody>##MitgliedData##</tbody></table>";

    private String strEinsatzbericht = "";
    
    private LinkedList<String> liBerHochformat;
    
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
            throws ServletException, IOException
    {
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
        String strData = request.getParameter("hidden_pdfData");
        System.out.println("DataString: "+strData);
        String[] strSplitData = strData.split("###");
        System.out.println("size: "+strSplitData.length);
        String strBerichtname;
        String strTable;
        if(strSplitData.length<2)
        {
            strBerichtname = "Dynamisch";
            strTable = strData;
        }else
        {
            strBerichtname = strSplitData[0];
            strTable = strSplitData[1];
        }

        String strAusgabe = "Es ist ein unerwartetes Problem aufgetreten";
        boolean boolLeerbericht = true;
        
        switch (strBerichtname)
        {
            case "Einsatzbericht leer":
                
                break;
            case "Übungsbericht leer":
                strAusgabe = generiereAusgabeUebungsberichtLeer(strTable,strSplitData[2]);
                break;
            case "Tätigkeitsbericht leer":
                strAusgabe = generiereAusgabeTaetigkeitsberichtLeer(strTable,strSplitData[2]);
                break;
            case "Dynamisch":
                strAusgabe=strTable;
                boolLeerbericht=false;
                break;
            default:
                strAusgabe = "<h1>" + strBerichtname + "</h1>" + strTable;
                boolLeerbericht = false;
        }

        String strContextPath = this.getServletContext().getRealPath("/");
        String strCSSPath1 = strContextPath.replace("web", "web\\css\\pdfSimpel.css");
        String strFontPath = strContextPath.replace("web", "web\\res\\Cambria.ttf");
        
//        String strCSSPath1 = strContextPath.replace("build\\web", "web\\css\\pdfSimpel.css");
//        String strFontPath = strContextPath.replace("build\\web", "web\\res\\Cambria.ttf");
        
        System.out.println("PDFServlet.doPost: CSSPath:" + strCSSPath1);
        Rectangle rect;

        try
        {
            Document document;
//            document = new Document(PageSize.A4, 36, 36, 54, 54);
            if(liBerHochformat.contains(strBerichtname))
            {
                 document = new Document(PageSize.A4, 36, 36, 100, 54);
                 rect = new Rectangle(36, 54, 559, 788);
            }else
            {
                document = new Document(PageSize.A4.rotate(), 36, 36, 70, 54);
                rect = new Rectangle(36, 54, 805, 559);
            }
            
            

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            
            PDF_KopfFußzeile event = new PDF_KopfFußzeile(strFontPath,writer);
            writer.setBoxSize("pageRect", rect);
            writer.setPageEvent(event);

            document.open();
            HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
            CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(false);

            //hier das (falls benötigt) CSS File einbinden für die .pdf Datei
            cssResolver.addCssFile(strCSSPath1, true);
            if (!boolLeerbericht)
            {
                cssResolver.addCssFile(strCSSPath1.replace("Simpel", "StandartBericht"), true);
            }else
            {
                cssResolver.addCssFile(strCSSPath1.replace("Simpel", "Leerbericht"), true);
            }
            Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(htmlContext, new PdfWriterPipeline(document, writer)));

            XMLWorker worker = new XMLWorker(pipeline, true);
            XMLParser p = new XMLParser(worker);
            p.parse(new StringReader(strAusgabe));
            document.close();
            writer.close();
            strBerichtname=strBerichtname.replaceAll(" ", "_");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "filename=" + strBerichtname + ".pdf");
            response.setContentLength(baos.size());

            ServletOutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();
        } catch (DocumentException de)
        {
            throw new IOException(de.getMessage());
        } catch (CssResolverException ex)
        {
            Logger.getLogger(PDFServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Generiert einen Tätigkeitsbericht mit Hilfe des übergebenen Strings
     * strTable ist der HTML String der Zeilen des Tables (<tr>...</tr><tr>...</tr><tr>...</tr>....)
     * @param strTable
     * @return 
     */
    public String generiereAusgabeTaetigkeitsberichtLeer(String strTableMitglieder,String strTableFahrzeuge)
    {
        System.out.println("generiereAusgabeTaetigkeitsberichtLeer: "+strTableMitglieder);
        String strHTMLOutput = strTaetigkeitsbericht;
        strHTMLOutput = strHTMLOutput.replace("##FahrzeugData##", strTableFahrzeuge);
        strHTMLOutput = strHTMLOutput.replace("##MitgliedData##", strTableMitglieder);
        System.out.println("strHTMLOutput: "+strHTMLOutput);
        return strHTMLOutput;
    }
    /**
     * Generiert einen Uebungsbericht mit Hilfe des übergebenen Strings
     * strTable ist der HTML String der Zeilen des Tables (<tr>...</tr><tr>...</tr><tr>...</tr>....)
     * @param strTable
     * @return 
     */
    public String generiereAusgabeUebungsberichtLeer(String strTableMitglieder,String strTableFahrzeuge)
    {
        String strHTMLOutput = strUebungsbericht;
        strHTMLOutput = strHTMLOutput.replace("##FahrzeugData##", strTableFahrzeuge);
        strHTMLOutput = strHTMLOutput.replace("##MitgliedData##", strTableMitglieder);

        return strHTMLOutput;
    }
    

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config); //To change body of generated methods, choose Tools | Templates.
        liBerHochformat = new LinkedList<>();
        liBerHochformat.add("Tätigkeitsbericht leer");
        liBerHochformat.add("Übungsbericht leer");
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

}
