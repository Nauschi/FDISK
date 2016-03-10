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
import java.io.File;
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

    private String strEinsatzbericht = "<h1>Einstatzbericht</h1>"
            + "<p>Datum: <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>  </p>"
            + "<p>Wehrtext: <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Instanznummer:&nbsp; <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </u></p>"
            + "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;BRAND&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;TECHNISCH&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "BSW&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Eigener EB</p>"
            + "<p>Einsatzart: &nbsp;&nbsp;&nbsp;&nbsp; <u>|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;|</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <u>|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;|</u> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<u>|&nbsp;&nbsp;&nbsp;|</u></p>"
            + "<p>Einsatzort: <font color='#D3D3D3'>Straße</font><u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "</u> / <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u> / <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u></p>"
            + "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='#D3D3D3'>PLZ</font><u> &nbsp;&nbsp; "
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u> <font color='#D3D3D3'>Ort</font> <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u></p>"
            + "<br/><p><b>Datum/Uhrzeit</b></p>"
            + "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Meldung:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Ausfahrt:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u></p>"
            + "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Alamierung:&nbsp; <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Rückkehr:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u> </p>"
            + "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Brand aus: &nbsp; &nbsp; <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Brandwache  "
            + "von: &nbsp; &nbsp; <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Brandwache bis: &nbsp; &nbsp; <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u> </p>"
            + "<br/><p><b>Lage: </b>&nbsp;</p><p>&nbsp;</p>"
            + "<table border='1px solid black'><tr><td rowspan='5'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "</td></tr></table>"
            + "<br/><p><b>T&auml;tigkeiten: </b>&nbsp;</p><p>&nbsp;</p>"
            + "<table border='1px solid black'><tr><td rowspan='5'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "</td></tr></table>"
            + "<br/><p><b>M&auml;ngel: </b>&nbsp;</p><p>&nbsp;</p>"
            + "<table border='1px solid black'><tr><td rowspan='5'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "</td></tr></table> <br/><br/>"
            + "<p>Einsatzleiter: <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u></p>"
            + "<p><b>Wetter:</b></p>"
            + "<table class='fuenf'>"
            + "<tr><td>&Omicron;&nbsp; Gl&auml;tte</td><td>&Omicron;&nbsp; Glatteis</td><td>&Omicron;&nbsp; Nebel</td><td>&Omicron;&nbsp; Regen</td><td>&Omicron;&nbsp; Schnee</td></tr>"
            + "<tr><td>&Omicron;&nbsp; Wind/Sturm</td></tr>"
            + "</table>"
            + "<p><b>Alamierung:</b></p>"
            + "<table class='fuenf'>"
            + "<tr><td>&Omicron;&nbsp; Funkempf. FW</td><td>&Omicron;&nbsp; Funkempf Florian</td><td>&Omicron;&nbsp; Pers&ouml;hnlich</td><td>&Omicron;&nbsp; Sirene Florian</td><td>&Omicron;&nbsp; Sirene Feuerw.</td></tr>"
            + "<tr><td>&Omicron;&nbsp; SMS</td><td>&Omicron;&nbsp; Telefon</td><td>&Omicron;&nbsp; Sonstiges</td></tr>"
            + "</table>"
            + "<p><b>Meldung:</b></p>"
            + "<table class='fuenf'>"
            + "<tr><td>&Omicron;&nbsp; Brandmelder</td><td>&Omicron;&nbsp; Florian/Feuerw.</td><td>&Omicron;&nbsp; Gemeinde</td><td>&Omicron;&nbsp; Polizei</td><td>&Omicron;&nbsp; Privatperson</td></tr>"
            + "<tr><td>&Omicron;&nbsp;1 Sonstige</td></tr>"
            + "</table>"
            + "<p><b>Anwesend:</b></p>"
            + "<table class='fuenf'>"
            + "<tr><td>&Omicron;&nbsp; BH</td><td>&Omicron;&nbsp; BFKDT/AFKDT</td><td>&Omicron;&nbsp; Bundesheer</td><td>&Omicron;&nbsp; EVU/WVU/GVU</td><td>&Omicron;&nbsp; Firmeninhaber</td></tr>"
            + "<tr><td>&Omicron;&nbsp; Gemeinde</td><td>&Omicron;&nbsp; Hubschrauber</td><td>&Omicron;&nbsp; &Ouml;lalarmdienst</td><td>&Omicron;&nbsp; Polizei</td><td>&Omicron;&nbsp; Rettung</td></tr>"
            + "<tr><td>&Omicron;&nbsp; Straßenverw.</td><td>&Omicron;&nbsp;Sonstige</td></tr>"
            + "</table>"
            + "<p><b>Gefahrenkl.:</b></p>"
            + "<table class='fuenf'>"
            + "<tr><td>&Omicron;&nbsp; 1 Sprengstoff</td><td>&Omicron;&nbsp; 2 Gase</td><td>&Omicron;&nbsp; 3 Brennb. Fl&uuml;ssig.</td><td>&Omicron;&nbsp; 4 Brennb. feste St.</td><td>&Omicron;&nbsp; 5 Brandf&ouml;. Stoffe</td></tr>"
            + "<tr><td>&Omicron;&nbsp; 6 Gifte</td><td>&Omicron;&nbsp; 7 Radioakt. Mat.</td><td>&Omicron;&nbsp; 8 &Auml;tzende Stoffe</td><td>&Omicron;&nbsp; 9 Sonstige</td></tr>"
            + "</table> <br /><br /><br />"
            + "<p><b>Technischer Einsatz:</b></p>"
            + "<table>"
            + "<tr><td>&Omicron;&nbsp; Ausl. v. ger. Mengen &Ouml;l/Treibst.</td><td>&Omicron;&nbsp; Freimachen von Verkehrswegen</td><td>&Omicron;&nbsp; Taucheinsatz</td></tr>"
            + "<tr><td>&Omicron;&nbsp; Auslaufen von &Ouml;l/Treibstoff</td><td>&Omicron;&nbsp; Hochwasser</td><td>&Omicron;&nbsp; T&uuml;r&ouml;ffnung oder Fenstereinstieg</td></tr>"
            + "<tr><td>&Omicron;&nbsp; Auspumparbeiten</td><td>&Omicron;&nbsp; Insektenbekämpfung</td><td>&Omicron;&nbsp; Unfall mit Schadstoffen</td></tr>"
            + "<tr><td>&Omicron;&nbsp; Beistellen von Ger&auml;ten</td><td>&Omicron;&nbsp; Lawinen- oder Murenabgang</td><td>&Omicron;&nbsp; Verkehrsregelung</td></tr>"
            + "<tr><td>&Omicron;&nbsp; Bergen von Tieren</td><td>&Omicron;&nbsp; Notstromversorgung</td><td>&Omicron;&nbsp; Verkehrsunfall Autobus</td></tr>"
            + "<tr><td>&Omicron;&nbsp; Bergen von G&uuml;tern</td><td>&Omicron;&nbsp; Retten/Befreien von Menschen</td><td>&Omicron;&nbsp; Verkehrsunfall einspur. Fahrzeug</td></tr>"
            + "<tr><td>&Omicron;&nbsp; Bergen von Toten</td><td>&Omicron;&nbsp; Retten/Befreien von Tieren</td><td>&Omicron;&nbsp; Verkehrsunfall LKW/Traktor</td></tr>"
            + "<tr><td>&Omicron;&nbsp; Dammbruch</td><td>&Omicron;&nbsp; Schneeeinsatz</td><td>&Omicron;&nbsp; Verkehrsunfall Luftfahrzeug</td></tr>"
            + "<tr><td>&Omicron;&nbsp; Einsturz von Bauwerken</td><td>&Omicron;&nbsp; Sicherungsdienst</td><td>&Omicron;&nbsp; Verkehrsunfall PKW</td></tr>"
            + "<tr><td>&Omicron;&nbsp; Elektrounfall</td><td>&Omicron;&nbsp; Sprengeinsatz</td><td>&Omicron;&nbsp; Verkehrsu. Schienen-/Wasserfzg.</td></tr>"
            + "<tr><td>&Omicron;&nbsp; Entfernung gef&auml;hrl. Baumteile</td><td>&Omicron;&nbsp; Strahlenschutzeinsatz</td><td>&Omicron;&nbsp; Wasserd. -Einsatz</td></tr>"
            + "<tr><td>&Omicron;&nbsp; Erd- oder Felsrutsch</td><td>&Omicron;&nbsp; Straßen- oder Kanalreinigung</td><td>&Omicron;&nbsp; Wasserschaden</td></tr>"
            + "<tr><td>&Omicron;&nbsp; Explosion ohne Brand</td><td>&Omicron;&nbsp; Sturmeinsatz</td><td>&Omicron;&nbsp; Wasserversorgung</td></tr>"
            + "<tr><td>&Omicron;&nbsp; Fahrzeugbereinigung</td><td>&Omicron;&nbsp; Suchaktion</td><td>&Omicron;&nbsp; Sonstiges:</td></tr>"
            + "</table>"
            + "<p><b>Brandeinsatz:</b></p>"
            + "<table>"
            + "<tr><td>&Omicron;&nbsp; Beherbergungsbetrieb</td><td>&Omicron;&nbsp; Kamin</td><td>&Omicron;&nbsp; Tankfahrzeug</td></tr>"
            + "<tr><td>&Omicron;&nbsp; B&uuml;rogeb&auml;ude</td><td>&Omicron;&nbsp; Landw. Betrieb, Heustock</td><td>&Omicron;&nbsp; Wald</td></tr>"
            + "<tr><td>&Omicron;&nbsp; Einsp. Fahrzeug oder PKW</td><td>&Omicron;&nbsp; LKW, Bus</td><td>&Omicron;&nbsp; Wohngeb&auml;ude</td></tr>"
            + "<tr><td>&Omicron;&nbsp; Feld, Wiese, Flur</td><td>&Omicron;&nbsp; &Ouml;ffentliches Geb&auml;ude</td><td>&Omicron;&nbsp; M&uuml;ll</td></tr>"
            + "<tr><td>&Omicron;&nbsp; Gewerbe-, Industriebetrieb</td><td>&Omicron;&nbsp; Schienen-, Luft-, Wasserfahrzeug</td><td>&Omicron;&nbsp; Sonstiges:</td></tr>"
            + "</table>"
            + "<p><b>T&auml;uschungsa.</b></p>"
            + "<table>"
            + "<tr><td>&Omicron;&nbsp; Autom. Brandanlage</td><td>&Omicron;&nbsp; Fehl- oder T&auml;uschungsalarm</td><td>&Omicron;&nbsp; B&ouml;swilliger Alarm</td></tr>"
            + "</table>"
            + "<p><b>Gesch&auml;digte</b></p>"
            + "<p>&nbsp;</p>"
            + "<table border='1px solid black'>"
            + "<tr><td colspan='8' align='center'><b>Gesch&auml;digter 1</b></td><td colspan='8' align='center'><b>Gesch&auml;digter 2</b></td></tr>"
            + "<tr><td colspan='4'><font color='#D3D3D3'>Vorname</font></td><td colspan='4'><font color='#D3D3D3'>Zuname</font></td><td colspan='4'><font color='#D3D3D3'>Vorname</font></td><td colspan='4'><font color='#D3D3D3'>Zuname</font></td></tr>"
            + "<tr><td colspan='8'><font color='#D3D3D3'>Straße und Hausnummer</font></td><td colspan='8'><font color='#D3D3D3'>Straße und Hausnummer</font></td></tr>"
            + "<tr><td colspan='2'><font color='#D3D3D3'>PLZ</font></td><td colspan='6'><font color='#D3D3D3'>Ort</font></td><td colspan='2'><font color='#D3D3D3'>PLZ</font></td><td colspan='6'><font color='#D3D3D3'>Ort</font></td></tr>"
            + "<tr><td colspan='3'><font color='#D3D3D3'>Kennzeichen</font></td><td colspan='5'><font color='#D3D3D3'>Fahrzeug</font></td><td colspan='3'><font color='#D3D3D3'>Kennzeichen</font></td><td colspan='5'><font color='#D3D3D3'>Fahrzeug</font></td></tr>"
            + "</table><p>&nbsp;</p>"
            + "<table border='1px solid black'>"
            + "<tr><td colspan='4'>Verl. Personen:</td><td colspan='4'>Get&ouml;t. Personen:</td><td colspan='4'>Gerett. Personen:</td><td colspan='4'>Gerettete Tiere:</td></tr>"
            + "</table>"
            + "<br /><p><b>Fahrzeuge:</b></p>"
            + "<table>"
            + "<tr><td>&Omicron;&nbsp;RLFA 1000</td><td>Von:</td><td>Bis:</td><td>&Omicron;&nbsp;Verr.</td><td>km:</td><td>Pump.:</td></tr>"
            + "<tr><td>&Omicron;&nbsp;MTF</td><td>Von:</td><td>Bis:</td><td>&Omicron;&nbsp;Verr.</td><td>km:</td><td>Pump.:</td></tr>"
            + "<tr><td>&Omicron;&nbsp;TSA 750</td><td>Von:</td><td>Bis:</td><td>&Omicron;&nbsp;Verr.</td><td>km:</td><td>Pump.:</td></tr>"
            + "</table>"
            + "<br /><br /><br /><br /><br /><p><b>Ger&auml;te:</b></p> <p>&nbsp;</p>"
            + "<table border='1px solid black'>"
            + "<tr><td align='center'><b>Az.</b></td><td colspan='8' align='center'><b>Bezeichnung</b></td><td colspan='2' align='center'><b>Az.</b></td><td colspan='8' align='center'><b>Bezeichnung</b></td></tr>"
            + "<tr><td align='center'>&nbsp;</td><td colspan='8' align='center'>&nbsp;</td><td colspan='2' align='center'>&nbsp;</td><td colspan='8' align='center'>&nbsp;</td></tr>"
            + "<tr><td align='center'>&nbsp;</td><td colspan='8' align='center'>&nbsp;</td><td colspan='2' align='center'>&nbsp;</td><td colspan='8' align='center'>&nbsp;</td></tr>"
            + "<tr><td align='center'>&nbsp;</td><td colspan='8' align='center'>&nbsp;</td><td colspan='2' align='center'>&nbsp;</td><td colspan='8' align='center'>&nbsp;</td></tr>"
            + "<tr><td align='center'>&nbsp;</td><td colspan='8' align='center'>&nbsp;</td><td colspan='2' align='center'>&nbsp;</td><td colspan='8' align='center'>&nbsp;</td></tr>"
            + "<tr><td align='center'>&nbsp;</td><td colspan='8' align='center'>&nbsp;</td><td colspan='2' align='center'>&nbsp;</td><td colspan='8' align='center'>&nbsp;</td></tr>"
            + "</table>"
            + "<p><b>Erstellt am: <table border='1px solid black'><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td></tr></table></b></p>"
            + "<p><b>Erstellt am: <table border='1px solid black'><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td></tr></table></b></p>"
            + "<b><p><u>Eingesetzte Fahrzeuge</u></p></b>"
            + "<table border='0'><tbody>##FahrzeugData##</tbody></table>"
            + "<b><p><u>Eingesetzte Mitglieder</u></p></b>"
            + "<table border='0'><tbody>##MitgliedData##</tbody></table>";;

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
        request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
    }

    //style='page-break-after: always'
    //style="display: none" colspan="?"
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
        System.out.println("DataString: " + strData);
        String[] strSplitData = strData.split("###");
        System.out.println("size: " + strSplitData.length);
        String strBerichtname;
        String strTable;

        strBerichtname = strSplitData[0];
        strTable = strSplitData[1];
        
        String strAusgabe = "Es ist ein unerwartetes Problem aufgetreten";
        boolean boolLeerbericht = true;

        switch (strBerichtname)
        {
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
                if (strTable.split("<td>").length > 1 && strSplitData[2].split("<td>").length > 1)
                {
                    strAusgabe = "<h1>" + strBerichtname + "</h1>" + strSplitData[2].replace("</br>", "") + "<p>&nbsp;</p>" + strTable;
                } else if (strTable.split("<td>").length < 2)
                {
                    strAusgabe = "<h1>" + strBerichtname + "</h1>" + strSplitData[2].replace("</br>", "");
                } else if (strSplitData[2].split("<td>").length < 2)
                {
                    strAusgabe = "<h1>" + strBerichtname + "</h1>" + strTable;
                }
                boolLeerbericht = false;
                break;
            case "Digitales Fahrtenbuch":
                if (strSplitData.length > 2)
                {
                    strAusgabe = "<h1>" + strBerichtname + "</h1>" + strSplitData[2] + "<p>&nbsp;</p>" + strTable;
                } else
                {
                    strAusgabe = "<h1>" + strBerichtname + "</h1>" + strTable;
                }
                boolLeerbericht = false;
                break;
            case "Stundenauswertung je Mitglied je Instanz":
                strAusgabe = "<h1>" + strBerichtname + "</h1>" + generiereStundenauswertungJeMitgliedJeInstanz(strTable);
                boolLeerbericht = false;
                break;
            default:
                strAusgabe = "<h1>" + strBerichtname + "</h1>" + strTable;
                boolLeerbericht = false;
        }

        String strContextPath = this.getServletContext().getRealPath("/");
        String strCSSPath1 = strContextPath + File.separator + "css" + File.separator + "pdfSimpel.css";
        String strFontPath = strContextPath + File.separator + "res" + File.separator + "Cambria.ttf";

//        String strCSSPath1 = strContextPath.replace("build\\web", "web\\css\\pdfSimpel.css");
//        String strFontPath = strContextPath.replace("build\\web", "web\\res\\Cambria.ttf");
        System.out.println("PDFServlet.doPost: CSSPath:" + strCSSPath1);
        Rectangle rect;

        try
        {
            Document document;
//            document = new Document(PageSize.A4, 36, 36, 54, 54);
            if (liBerHochformat.contains(strBerichtname))
            {
                document = new Document(PageSize.A4, 36, 36, 100, 54);
                rect = new Rectangle(36, 54, 559, 788);
            } else
            {
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

            //hier das (falls benötigt) CSS File einbinden für die .pdf Datei
            cssResolver.addCssFile(strCSSPath1, true);
            if (!boolLeerbericht)
            {
                cssResolver.addCssFile(strCSSPath1.replace("Simpel", "StandartBericht"), true);
            } else
            {
                cssResolver.addCssFile(strCSSPath1.replace("Simpel", "Leerbericht"), true);
            }
            Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(htmlContext, new PdfWriterPipeline(document, writer)));

            XMLWorker worker = new XMLWorker(pipeline, true);
            XMLParser p = new XMLParser(worker);
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
        } catch (DocumentException de)
        {
            throw new IOException(de.getMessage());
        } catch (CssResolverException ex)
        {
            Logger.getLogger(PDFServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String generiereStundenauswertungJeMitgliedJeInstanz(String strTemp)
    {
        String ausgabe = "";
        strTemp = strTemp.replace("<table class=\"tablesorter ui celled table\">", "");
        strTemp = strTemp.replaceAll("style=\"display:none\"", "");
//        System.out.println(strTemp);
        int intIndex1 = strTemp.indexOf("<thead>");
        int intIndex2 = strTemp.indexOf("</thead>") + 8;
        String strThead = strTemp.substring(intIndex1, intIndex2);
        intIndex1 = strTemp.indexOf("<tbody>") + 7;
        intIndex2 = strTemp.lastIndexOf("</tbody>");
        String strTRs = strTemp.substring(intIndex1, intIndex2);
        int intIndex = -1;
        //<tr></tr>
        //<tr> <table>
        //<thead>
        //<tr></tr>
        //</thead><tbody>
        //<tr></tr>
        //<tr></tr>
        //<tr></tr>
        //</tbody></tr>
        try
        {
            while (true)
            {

                intIndex = strTRs.indexOf("</tr>") + 1;
                intIndex = strTRs.indexOf("</tr>", intIndex) + 1;
                intIndex = strTRs.indexOf("</tr>", intIndex) + 1;
                intIndex = strTRs.indexOf("</tr>", intIndex) + 1;
                intIndex = strTRs.indexOf("</tr>", intIndex) + 1;
                intIndex = strTRs.indexOf("</tr>", intIndex) + 5;
                String strAktRows = strTRs.substring(0, intIndex);
                String strFirstRow = strAktRows.substring(0, strAktRows.indexOf("</tr>") + 5);
                String strSecondRow = strAktRows.substring(strAktRows.indexOf("<table"), strAktRows.indexOf("</table>") + 8);
                System.out.println(strAktRows);
                ausgabe += "<p>&nbsp;</p>"
                        + "<table>" + strThead + "<tbody>" + strFirstRow + "</tbody></table>"
                        + "<p>&nbsp;</p>"
                        + "<div>"
                        + strSecondRow
                        + "</div>";
                strTRs = strTRs.replace(strAktRows, "");
            }
        } catch (Exception ex)
        {

        }
        return ausgabe;
    }

    /**
     * Generiert einen Tätigkeitsbericht mit Hilfe des übergebenen Strings
     * strTable ist der HTML String der Zeilen des Tables
     * (<tr>...</tr><tr>...</tr><tr>...</tr>....)
     *
     * @param strTable
     * @return
     */
    public String generiereAusgabeTaetigkeitsberichtLeer(String strTableMitglieder, String strTableFahrzeuge)
    {
        System.out.println("generiereAusgabeTaetigkeitsberichtLeer: " + strTableMitglieder);
        String strHTMLOutput = strTaetigkeitsbericht;
        strHTMLOutput = strHTMLOutput.replace("##FahrzeugData##", strTableFahrzeuge);
        strHTMLOutput = strHTMLOutput.replace("##MitgliedData##", strTableMitglieder);
        System.out.println("strHTMLOutput: " + strHTMLOutput);
        return strHTMLOutput;
    }

    /**
     * Generiert einen Uebungsbericht mit Hilfe des übergebenen Strings strTable
     * ist der HTML String der Zeilen des Tables
     * (<tr>...</tr><tr>...</tr><tr>...</tr>....)
     *
     * @param strTable
     * @return
     */
    public String generiereAusgabeUebungsberichtLeer(String strTableMitglieder, String strTableFahrzeuge)
    {
        String strHTMLOutput = strUebungsbericht;
        strHTMLOutput = strHTMLOutput.replace("##FahrzeugData##", strTableFahrzeuge);
        strHTMLOutput = strHTMLOutput.replace("##MitgliedData##", strTableMitglieder);

        return strHTMLOutput;
    }

    /**
     * Generiert einen Einsatzbericht mit Hilfe des übergebenen Strings strTable
     * ist der HTML String der Zeilen des Tables
     * (<tr>...</tr><tr>...</tr><tr>...</tr>....)
     *
     * @param strTable
     * @return
     */
    public String generiereAusgabeEinsatzberichtLeer(String strTableMitglieder, String strTableFahrzeuge)
    {
        String strHTMLOutput = strEinsatzbericht;
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
        liBerHochformat.add("Einsatzbericht leer");
        liBerHochformat.add("Einfache Mitgliederliste");
        liBerHochformat.add("Dienstzeitliste");
        liBerHochformat.add("Geburtstagsliste");
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
