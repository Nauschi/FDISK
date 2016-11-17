/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PDF;

import Servlet.PDFServlet;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.parser.XMLParser;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcel Schmidt
 */
public class PDF_KopfFußzeile extends PdfPageEventHelper {

    int pagenumber;
    Font fontCambria;
    Image img;
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    PdfWriter pdfWriter;
    String strThead;
    
    public String getStrThead() {
        return strThead;
    }

    public void setStrThead(String strTHead) {
        this.strThead = strTHead;
    }

    public PDF_KopfFußzeile(String strFontPath, PdfWriter pdfWriter) {
        try {
            this.pdfWriter = pdfWriter;
            img = Image.getInstance(strFontPath.replace("Cambria.ttf", "logo_oben.png"));
            fontCambria = new Font(BaseFont.createFont(strFontPath, "", BaseFont.NOT_EMBEDDED));
            fontCambria.setSize(10);
        } catch (DocumentException ex) {
            Logger.getLogger(PDF_KopfFußzeile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PDF_KopfFußzeile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        pagenumber++;
        if (pagenumber != 1) {
            
        }

    }

    /**
     * Wird zum erstellen der Kopf- und Fußzeile verwendet
     *
     * @param writer
     * @param document
     */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        if (writer == null) {
            writer = pdfWriter;
        }

        Rectangle rect = writer.getBoxSize("pageRect");
        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase(String.format("Seite %d", pagenumber), fontCambria),
                (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 20, 0);

        Date date = new Date();
        String strTime = sdf.format(date);
        Phrase phraseDate = new Phrase(String.format("erstellt am %s", strTime), fontCambria);
        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_RIGHT, phraseDate, rect.getRight(), rect.getBottom() - 20, 0);

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_LEFT, new Phrase("Landesfeuerwehrverband Steiermark", fontCambria), rect.getLeft(), rect.getBottom() - 20, 0);

        img.scaleAbsolute(120, 40);
        img.setAbsolutePosition(rect.getRight() - 120, rect.getTop() - 25);
        try {
            writer.getDirectContent().addImage(img);
        } catch (DocumentException ex) {
            Logger.getLogger(PDF_KopfFußzeile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
