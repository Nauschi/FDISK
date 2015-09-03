/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PDF;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kinco_000
 */
public class PDFCreator {

    /**
     * Creates a PDF with a specified name and with the content of an HTML String.
     * The format can be specified with either "hoch" or "quer". A .css file can 
     * also be included for an improved user interface. 
     * @param strDateiNamePDF
     * @param strHTMLInhalt
     * @param strFormat
     * @throws IOException
     * @throws DocumentException
     * @throws CssResolverException 
     */
    public void createPdf(String strDateiNamePDF, String strHTMLInhalt, String strFormat) throws IOException, DocumentException, CssResolverException {
        Document docPDF;

        if (strFormat.equals("quer")) {
            docPDF = new Document(PageSize.A4.rotate());
        } else {
            docPDF = new Document(PageSize.A4);
        }
        PdfWriter writer = PdfWriter.getInstance(docPDF, new FileOutputStream(strDateiNamePDF));
        
        docPDF.addAuthor("HTL Kaindorf - Yvonne Hartner, Corinna Kindlhofer, Philipp Nauschnegg, Marcel Schmidt, Christoph Schöllauf");
        docPDF.addCreationDate();
        docPDF.open();

        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(false);
        
        //hier das (falls benötigt) CSS File einbinden für die .pdf Datei
        cssResolver.addCssFile(System.getProperty("user.home") + "/Desktop/styles.css", true);

        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(htmlContext, new PdfWriterPipeline(docPDF, writer)));

        XMLWorker worker = new XMLWorker(pipeline, true);
        XMLParser p = new XMLParser(worker);
        p.parse(new StringReader(strHTMLInhalt));

        if (docPDF.isOpen()) {
            docPDF.close();
        }

    }

    //nur zum Testen!
    public static void main(String[] args) throws IOException, DocumentException {
        String pdf = System.getProperty("user.home") + "/Desktop/test.pdf";
        try {
            new PDFCreator().createPdf(pdf, "<html><h1>Lorem ipsum</h1></html>", "quer");
        } catch (CssResolverException ex) {
            Logger.getLogger(PDFCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
